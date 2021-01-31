package cn.motui.meican.ui.settings

import cn.motui.meican.MeiCanBundle.message
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.Settings
import cn.motui.meican.exception.MeiCanLoginException
import cn.motui.meican.job.NotificationScheduler
import cn.motui.meican.ui.SettingForm
import cn.motui.meican.ui.selected
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.dataService
import com.intellij.ui.IdeBorderFactory
import javax.swing.SwingUtilities

/**
 * 设置面板
 */
class SettingsPanel(val settings: Settings) : SettingForm() {
    init {
        accountPanel.border = IdeBorderFactory.createTitledBorder(message("setting.options.account"))
        usernameLabel.text = message("setting.options.account.label.username")
        passwordLabel.text = message("setting.options.account.label.password")
        testButton.text = message("setting.option.account.button")
        noticePanel.border = IdeBorderFactory.createTitledBorder(message("setting.options.notice"))
        noticeAmCheckBox.text = message("setting.options.notice.am")
        noticePmCheckBox.text = message("setting.options.notice.pm")
        noticeTimeLabel.text = message("setting.options.notice.label.time")
        NoticeTime.values().forEach { timeComboBox.addItem(it) }
        noticeCycleLabel.text = message("setting.options.notice.label.cycle")
        NoticeCycle.values().forEach { cycleComboBox.addItem(it) }
        testButton.addActionListener {
            SwingUtilities.invokeLater {
                val password = String(passwordField.password)
                if (usernameField.text.isNotBlank() and password.isNotBlank()) {
                    testButton.isEnabled = false
                    try {
                        dataService.refresh(usernameField.text, password)
                        val account = dataService.getAccount()
                        testResultLabel.text = account.email
                    } catch (e: MeiCanLoginException) {
                        Notifications.showErrorNotification(
                            NOTIFICATIONS_ID,
                            message("setting.option.account.button.test.notification.title"),
                            message("setting.option.account.button.test.notification.error") + e.message
                        )
                    } finally {
                        testButton.isEnabled = true
                    }
                }
            }
        }
    }

    val isModified: Boolean
        get() {
            val settings = settings
            return settings.account.username != usernameField.text
                    || settings.account.getPassword() != String(passwordField.password)
                    || settings.notice.am != noticeAmCheckBox.isSelected
                    || settings.notice.pm != noticePmCheckBox.isSelected
                    || settings.notice.beforeClosingTime != timeComboBox.selectedItem
                    || settings.notice.cycle != cycleComboBox.selectedItem
        }

    fun apply() {
        @Suppress("Duplicates")
        with(settings) {
            val password = String(passwordField.password)
            settings.account.username = usernameField.text
            settings.account.setPassword(password)
            settings.notice.am = noticeAmCheckBox.isSelected
            settings.notice.pm = noticePmCheckBox.isSelected
            settings.notice.beforeClosingTime = timeComboBox.selected ?: NoticeTime.M30
            settings.notice.cycle = cycleComboBox.selected ?: NoticeCycle.EVERYDAY
            try {
                // 刷新账户信息
                dataService.refresh(settings.account.username, password)
                settings.account.verification = true
            } catch (ignore: Exception) {
                settings.account.verification = false
            }
            // 创建/更新job
            val beforeClosing = settings.notice.beforeClosingTime != timeComboBox.selected ?: NoticeTime.M30
            val am =
                (settings.notice.am != noticeAmCheckBox.isSelected or beforeClosing) and noticeAmCheckBox.isSelected
            val pm =
                (settings.notice.pm != noticePmCheckBox.isSelected or beforeClosing) and noticePmCheckBox.isSelected
            if (am) {
                NotificationScheduler.scheduler(notice.amCron(), NotificationScheduler.JobType.AM)
            }
            if (pm) {
                NotificationScheduler.scheduler(notice.pmCron(), NotificationScheduler.JobType.PM)
            }
        }
    }

    @Suppress("Duplicates")
    fun reset() {
        usernameField.text = settings.account.username
        passwordField.text = settings.account.getPassword()
        noticeAmCheckBox.isSelected = settings.notice.am
        noticePmCheckBox.isSelected = settings.notice.pm
        timeComboBox.selected = settings.notice.beforeClosingTime
        cycleComboBox.selected = settings.notice.cycle
    }

}