package cn.motui.meican.ui.settings

import cn.motui.meican.MeiCanBundle
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.Settings
import cn.motui.meican.Tab
import cn.motui.meican.Tabs
import cn.motui.meican.exception.MeiCanLoginException
import cn.motui.meican.job.NotificationScheduler
import cn.motui.meican.job.OrderAutomaticScheduler
import cn.motui.meican.model.ui.TabData
import cn.motui.meican.ui.SettingForm
import cn.motui.meican.ui.order.OrderView
import cn.motui.meican.ui.selected
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.application
import cn.motui.meican.util.dataService
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.intellij.uiDesigner.core.GridLayoutManager
import java.time.LocalDateTime

/**
 * 设置面板
 */
class SettingsPanel(val settings: Settings) : SettingForm() {
    /**
     * 用于记录tab数据
     */
    private var tabList = mutableListOf<TabItem>()

    init {
        this.accountInit()
        this.tabInit()
        this.noticeInit()
        this.automaticInit()
        this.tabRender()
    }

    private fun accountInit() {
        accountPanel.border = IdeBorderFactory.createTitledBorder(MeiCanBundle.message("setting.options.account"))
        this.usernameLabel.text = MeiCanBundle.message("setting.options.account.label.username")
        this.passwordLabel.text = MeiCanBundle.message("setting.options.account.label.password")
        this.testButton.text = MeiCanBundle.message("setting.option.account.button")

        testButton.addActionListener {
            application.invokeLater {
                val password = String(passwordField.password)
                if (usernameField.text.isNotBlank() and password.isNotBlank()) {
                    testButton.isEnabled = false
                    try {
                        dataService.refresh(usernameField.text, password)
                        val account = dataService.getAccount()
                        testResultLabel.text = account.email
                        // 渲染tab数据
                        this.tabRender()
                    } catch (e: MeiCanLoginException) {
                        Notifications.showErrorNotification(
                            NOTIFICATIONS_ID,
                            MeiCanBundle.message("setting.option.account.button.test.notification.title"),
                            MeiCanBundle.message("setting.option.account.button.test.notification.error") + e.message
                        )
                    } finally {
                        testButton.isEnabled = true
                    }
                }
            }
        }
    }

    private fun tabInit() {
        tabPanel.border = IdeBorderFactory.createTitledBorder(MeiCanBundle.message("setting.options.tab"))
        this.tabRender()
    }

    private fun noticeInit() {
        // 通知
        noticePanel.border = IdeBorderFactory.createTitledBorder(MeiCanBundle.message("setting.options.notice"))
        noticeTimeLabel.text = MeiCanBundle.message("setting.options.notice.label.time")
        noticeTimeLabel.toolTipText = MeiCanBundle.message("setting.options.notice.label.time.message")
        NoticeTime.values().forEach { noticeTimeComboBox.addItem(it) }
        noticeCycleLabel.text = MeiCanBundle.message("setting.options.notice.label.cycle")
        Cycle.values().forEach { noticeCycleComboBox.addItem(it) }
    }

    private fun automaticInit() {
        automaticPanel.border = IdeBorderFactory.createTitledBorder(MeiCanBundle.message("setting.options.order"))
        orderTimeLabel.text = MeiCanBundle.message("setting.order.automatic.label.time")
        orderTimeLabel.toolTipText = MeiCanBundle.message("setting.order.automatic.label.time.message")
        orderCycleLabel.text = MeiCanBundle.message("setting.options.order.label.cycle")
        NoticeTime.values().forEach { orderTimeComboBox.addItem(it) }
        Cycle.values().forEach { orderCycleComboBox.addItem(it) }
    }

    /**
     * tab数据渲染
     */
    private fun tabRender() {
        if (!dataService.isCanRequest()) {
            val gridLayoutManager = GridLayoutManager(1, 1)
            this.tabPanel.layout = gridLayoutManager
            return
        }
        application.invokeAndWait {
            val tabDataList = dataService.getTabData(LocalDateTime.now())
            if (tabDataList.isNotEmpty()) {
                tabList = mutableListOf()
            }
            val gridLayoutManager = GridLayoutManager(6, tabDataList.size + 1)
            this.tabPanel.layout = gridLayoutManager
            this.tabPanel.add(JBLabel(MeiCanBundle.message("setting.options.tab.tabs")), buildGridConstraints(0, 0))
            this.tabPanel.add(JBLabel(MeiCanBundle.message("setting.options.tab.open")), buildGridConstraints(0, 1))
            this.tabPanel.add(JBLabel(MeiCanBundle.message("setting.options.tab.close")), buildGridConstraints(0, 2))
            this.tabPanel.add(JBLabel(MeiCanBundle.message("setting.options.tab.show")), buildGridConstraints(0, 3))
            this.tabPanel.add(JBLabel(MeiCanBundle.message("setting.options.tab.notice")), buildGridConstraints(0, 4))
            this.tabPanel.add(
                JBLabel(MeiCanBundle.message("setting.options.tab.automatic")),
                buildGridConstraints(0, 5)
            )
            for (index in tabDataList.indices) {
                val tabData = tabDataList[index]
                val tab = settings.tabs.get(tabData.title)
                val tabItem = TabItem(
                    tabData,
                    JBCheckBox(null, tab?.show ?: true),
                    JBCheckBox(null, tab?.notice ?: false),
                    JBCheckBox(null, tab?.automatic ?: false)
                )
                tabList.add(tabItem)
                val column = index + 1
                this.tabPanel.add(JBLabel(tabData.title), buildGridConstraints(column, 0))
                this.tabPanel.add(JBLabel(tabData.openingTime.openTime), buildGridConstraints(column, 1))
                this.tabPanel.add(JBLabel(tabData.openingTime.closeTime), buildGridConstraints(column, 2))
                this.tabPanel.add(tabItem.show, buildGridConstraints(column, 3))
                this.tabPanel.add(tabItem.notice, buildGridConstraints(column, 4))
                this.tabPanel.add(tabItem.automatic, buildGridConstraints(column, 5))
            }
        }
    }

    private fun buildGridConstraints(column: Int, row: Int): GridConstraints {
        val gridConstraints = GridConstraints()
        gridConstraints.column = column
        gridConstraints.row = row
        gridConstraints.anchor = ANCHOR_WEST
        return gridConstraints
    }

    val isModified: Boolean
        get() {
            val settings = settings
            var tabResult = true
            tabList.forEach { tabItem ->
                val tab = settings.tabs.get(tabItem.tabData.title)
                tab?.let {
                    tabResult = tabResult || tabItem.isModified(it)
                }
            }
            return settings.account.username != usernameField.text ||
                settings.account.getPassword() != String(passwordField.password) ||
                tabResult ||
                settings.notice.beforeClosingTime != noticeTimeComboBox.selectedItem ||
                settings.notice.cycle != noticeCycleComboBox.selectedItem ||
                settings.order.cycle != orderCycleComboBox.selectedItem ||
                settings.order.beforeClosingTime != orderTimeComboBox.selectedItem
        }

    fun apply() {
        val password = String(passwordField.password)
        settings.account.username = usernameField.text
        settings.account.setPassword(password)
        settings.tabs = Tabs(
            tabList.map { tabItem -> tabItem.toTab() }.toSet()
        )
        settings.notice.beforeClosingTime = noticeTimeComboBox.selected ?: NoticeTime.M30
        settings.notice.cycle = noticeCycleComboBox.selected ?: Cycle.MONDAY_TO_FRIDAY
        settings.order.cycle = orderCycleComboBox.selected ?: Cycle.MONDAY_TO_FRIDAY
        settings.order.beforeClosingTime = orderTimeComboBox.selected ?: NoticeTime.M5
        try {
            // 刷新账户信息
            dataService.refresh(settings.account.username, password)
            settings.account.verification = true
        } catch (ignore: Exception) {
            settings.account.verification = false
        }
        // 创建/更新/删除job
        NotificationScheduler.schedule(settings.notice, settings.tabs)
        OrderAutomaticScheduler.schedule(settings.order, settings.tabs)
        OrderView.instance.refreshUi()
    }

    @Suppress("Duplicates")
    fun reset() {
        usernameField.text = settings.account.username
        passwordField.text = settings.account.getPassword()
        // 重新渲染tab数据
        this.tabRender()
        noticeTimeComboBox.selected = settings.notice.beforeClosingTime
        noticeCycleComboBox.selected = settings.notice.cycle
        orderCycleComboBox.selected = settings.order.cycle
        orderTimeComboBox.selected = settings.order.beforeClosingTime
    }

    private class TabItem constructor(
        val tabData: TabData,
        val show: JBCheckBox,
        val notice: JBCheckBox,
        val automatic: JBCheckBox,
    ) {

        fun isModified(tab: Tab): Boolean {
            return this.show.isSelected != tab.show ||
                this.notice.isSelected != tab.notice ||
                this.automatic.isSelected != tab.automatic
        }

        fun toTab(): Tab {
            return Tab(
                this.tabData.title,
                this.tabData.openingTime,
                this.show.isSelected,
                this.notice.isSelected,
                this.automatic.isSelected
            )
        }
    }
}
