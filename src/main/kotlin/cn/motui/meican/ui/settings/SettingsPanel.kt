package cn.motui.meican.ui.settings

import cn.motui.meican.MeiCanBundle.message
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.Settings
import cn.motui.meican.exception.MeiCanLoginException
import cn.motui.meican.job.NotificationScheduler
import cn.motui.meican.job.OrderAutomaticScheduler
import cn.motui.meican.model.TabType
import cn.motui.meican.ui.SettingForm
import cn.motui.meican.ui.order.OrderView
import cn.motui.meican.ui.selected
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.application
import cn.motui.meican.util.dataService
import com.intellij.ui.IdeBorderFactory

/**
 * 设置面板
 */
class SettingsPanel(val settings: Settings) : SettingForm() {
    init {
        accountPanel.border = IdeBorderFactory.createTitledBorder(message("setting.options.account"))
        usernameLabel.text = message("setting.options.account.label.username")
        passwordLabel.text = message("setting.options.account.label.password")
        testButton.text = message("setting.option.account.button")
        // 通知
        noticePanel.border = IdeBorderFactory.createTitledBorder(message("setting.options.notice"))
        noticeTabLabel.text = message("setting.options.notice.tab.label")
        Tab.values().forEach { noticeTabComboBox.addItem(it) }
        noticeTimeLabel.text = message("setting.options.notice.label.time")
        NoticeTime.values().forEach { timeComboBox.addItem(it) }
        noticeCycleLabel.text = message("setting.options.notice.label.cycle")
        Cycle.values().forEach { cycleComboBox.addItem(it) }
        // 自动点餐
        automaticPanel.border = IdeBorderFactory.createTitledBorder(message("setting.options.order"))
        automaticLabel.text = message("setting.order.automatic.label")
        Automatic.values().forEach { automaticComboBox.addItem(it) }
        orderCycleLabel.text = message("setting.options.order.label.cycle")
        Cycle.values().forEach { orderCycleComboBox.addItem(it) }
        // 其他设置
        otherPanel.border = IdeBorderFactory.createTitledBorder(message("setting.options.other"))
        tabShowLabel.text = message("setting.other.tab.show.label")
        TabShow.values().forEach { tabShowComboBox.addItem(it) }
        testButton.addActionListener {
            application.invokeLater {
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
            return settings.account.username != usernameField.text ||
                settings.account.getPassword() != String(passwordField.password) ||
                settings.notice.tab != noticeTabComboBox.selectedItem ||
                settings.notice.beforeClosingTime != timeComboBox.selectedItem ||
                settings.notice.cycle != cycleComboBox.selectedItem ||
                settings.order.automatic != automaticComboBox.selectedItem ||
                settings.order.cycle != orderCycleComboBox.selectedItem ||
                settings.other.tabShow != tabShowComboBox.selectedItem
        }

    fun apply() {
        @Suppress("Duplicates")
        with(settings) {
            val password = String(passwordField.password)
            settings.account.username = usernameField.text
            settings.account.setPassword(password)
            settings.notice.tab = noticeTabComboBox.selected ?: Tab.NO
            settings.notice.beforeClosingTime = timeComboBox.selected ?: NoticeTime.M30
            settings.notice.cycle = cycleComboBox.selected ?: Cycle.MONDAY_TO_FRIDAY
            settings.order.automatic = automaticComboBox.selected ?: Automatic.NO
            settings.order.cycle = orderCycleComboBox.selected ?: Cycle.MONDAY_TO_FRIDAY
            settings.other.tabShow = tabShowComboBox.selected ?: TabShow.ALL
            try {
                // 刷新账户信息
                dataService.refresh(settings.account.username, password)
                settings.account.verification = true
            } catch (ignore: Exception) {
                settings.account.verification = false
            }
            // 创建/更新job
            if (settings.notice.isNotice(TabType.AM)) {
                NotificationScheduler.scheduler(notice.cron(TabType.AM), TabType.AM)
            }
            if (settings.notice.isNotice(TabType.PM)) {
                NotificationScheduler.scheduler(notice.cron(TabType.PM), TabType.PM)
            }
            if (settings.order.isOrderAutomatic(TabType.AM)) {
                OrderAutomaticScheduler.scheduler(order.cron(TabType.AM), TabType.AM)
            }
            if (settings.order.isOrderAutomatic(TabType.PM)) {
                OrderAutomaticScheduler.scheduler(order.cron(TabType.PM), TabType.PM)
            }
            OrderView.instance.refreshUi()
        }
    }

    @Suppress("Duplicates")
    fun reset() {
        usernameField.text = settings.account.username
        passwordField.text = settings.account.getPassword()
        noticeTabComboBox.selected = settings.notice.tab
        timeComboBox.selected = settings.notice.beforeClosingTime
        cycleComboBox.selected = settings.notice.cycle
        automaticComboBox.selected = settings.order.automatic
        orderCycleComboBox.selected = settings.order.cycle
        tabShowComboBox.selected = settings.other.tabShow
    }
}
