package cn.motui.meican.ui.order

import cn.motui.meican.MeiCanBundle.message
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.model.ui.TabData
import cn.motui.meican.ui.OrderWindowForm
import cn.motui.meican.ui.settings.OptionsConfigurable
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.application
import cn.motui.meican.util.dataService
import cn.motui.meican.util.settings
import com.intellij.ui.components.labels.LinkLabel
import com.intellij.ui.components.labels.LinkListener
import java.awt.CardLayout
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Consumer
import kotlin.math.abs

/**
 * OrderPanel
 */
class OrderPanel : OrderWindowForm() {
    private var targetDateTime: LocalDateTime = LocalDateTime.now()

    init {
        openSettingLinkLabel.text = message("order.tool.view.setting")
        timeLabel.text = timeText()
        preButton.text = message("order.tool.view.pre.button.text")
        preButton.addActionListener { buttonAction(-1) }
        nextButton.text = message("order.tool.view.next.button.text")
        nextButton.addActionListener { buttonAction(1) }
        openSettingLinkLabel.setListener(OpenSettingListener(), null)
        refreshUi()
    }

    fun refreshUi() {
        renderUi()
    }

    private fun renderUi() {
        if (settings.account.isVerified()) {
            renderTabbedUi()
        } else {
            showMessagePane()
        }
    }

    private fun renderTabbedUi() {
        application.invokeLater() {
            val tabDataList: List<TabData> = dataService.getTabData(targetDateTime)
            tabbedPane.removeAll()
            tabDataList.forEach(
                Consumer { tabData: TabData ->
                    if (settings.other.isTabShow(tabData.type())) {
                        tabbedPane.addTab(tabData.title, TabPanel(tabData.type(), targetDateTime).root())
                    }
                }
            )
            showTabbedPane()
        }
    }

    private fun showMessagePane() {
        (root.layout as CardLayout).show(root, CARD_MESSAGE)
    }

    private fun showTabbedPane() {
        (root.layout as CardLayout).show(root, CARD_TABBED)
    }

    private fun buttonAction(day: Long) {
        application.invokeLater {
            if (checkTargetTime(targetDateTime.plusDays(day))) {
                targetDateTime = targetDateTime.plusDays(day)
                timeLabel.text = timeText()
                renderUi()
            }
        }
    }

    private fun checkTargetTime(targetTime: LocalDateTime): Boolean {
        val days = Duration.between(targetTime, LocalDateTime.now()).toDays()
        val result = abs(days) > 6
        if (result) {
            Notifications.showErrorNotification(
                NOTIFICATIONS_ID,
                message("order.tool.view.check.target.time.notification.title"),
                message("order.tool.view.check.target.time.notification.error")
            )
        }
        return !result
    }

    private fun timeText(): String {
        var format = targetDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + " "
        format += when (targetDateTime.dayOfWeek) {
            DayOfWeek.MONDAY -> message("order.tool.view.monday")
            DayOfWeek.TUESDAY -> message("order.tool.view.tuesday")
            DayOfWeek.WEDNESDAY -> message("order.tool.view.wednesday")
            DayOfWeek.THURSDAY -> message("order.tool.view.thursday")
            DayOfWeek.FRIDAY -> message("order.tool.view.friday")
            DayOfWeek.SATURDAY -> message("order.tool.view.saturday")
            else -> message("order.tool.view.sunday")
        }
        return format
    }

    inner class OpenSettingListener : LinkListener<String> {
        override fun linkSelected(aSource: LinkLabel<*>?, aLinkData: String?) {
            OptionsConfigurable.showSettingsDialog(null)
        }
    }

    companion object {
        private const val CARD_MESSAGE = "CARD_MESSAGE"
        private const val CARD_TABBED = "CARD_TABBED"
    }
}
