package cn.motui.meican.ui.order

import cn.motui.meican.MeiCanBundle.message
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.model.TabStatus
import cn.motui.meican.model.ui.TabData
import cn.motui.meican.ui.EmptyForm
import cn.motui.meican.ui.OrderForm
import cn.motui.meican.ui.UI.transparentColor
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.dataService
import java.awt.event.ActionEvent
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Consumer
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.math.abs

/**
 * 主面板
 */
class OrderPanel {
    private val form = OrderForm()
    private var targetDateTime = LocalDateTime.now()
    private var currentTabSelectedIndex = 0

    init {
        form.preButton.addActionListener { preButtonAction(it) }
        form.nextButton.addActionListener { nextButtonAction(it) }
        form.label.text = labelText()
        renderUi()
    }

    fun refreshUi() {
        currentTabSelectedIndex = form.tabbedPane.selectedIndex
        renderUi()
    }

    fun root(): JPanel {
        return form.rootPanel
    }

    private fun renderUi() {
        SwingUtilities.invokeLater {
            form.tabbedPane.removeAll()
            val tabDataList: List<TabData> = dataService.getDateData(targetDateTime)
            tabDataList.forEach(Consumer { tabData: TabData ->
                var content: JPanel? = null
                val targetTime = tabData.targetTime
                content = when (tabData.tabStatus) {
                    TabStatus.ORDER -> tabData.orderUniqueId?.let {
                        OrderDetailPanel(it).root()
                    }
                    TabStatus.AVAILABLE -> DateOrderPanel(
                        this, targetTime, tabData.userTabUniqueId,
                        tabData.corpNamespace
                    ).root()
                    else -> EmptyForm(tabData.reason).root
                }
                form.tabbedPane.addTab(tabData.title, content)
            })
            form.tabbedPane.selectedIndex = currentTabSelectedIndex
        }
    }

    private fun preButtonAction(event: ActionEvent) {
        SwingUtilities.invokeLater {
            if (checkTargetTime(targetDateTime.plusDays(-1))) {
                targetDateTime = targetDateTime.plusDays(-1)
                form.label.text = labelText()
                renderUi()
            }
        }
    }

    private fun nextButtonAction(event: ActionEvent) {
        SwingUtilities.invokeLater {
            if (checkTargetTime(targetDateTime.plusDays(1))) {
                targetDateTime = targetDateTime.plusDays(1)
                form.label.text = labelText()
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
                message("order.tool.window.check.target.time.notification.title"),
                message("order.tool.window.check.target.time.notification.error")
            )
        }
        return !result
    }

    private fun labelText(): String {
        var format = targetDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + " "
        format += when (targetDateTime.dayOfWeek) {
            DayOfWeek.MONDAY -> message("order.tool.window.monday")
            DayOfWeek.TUESDAY -> message("order.tool.window.tuesday")
            DayOfWeek.WEDNESDAY -> message("order.tool.window.wednesday")
            DayOfWeek.THURSDAY -> message("order.tool.window.thursday")
            DayOfWeek.FRIDAY -> message("order.tool.window.friday")
            DayOfWeek.SATURDAY -> message("order.tool.window.saturday")
            else -> message("order.tool.window.sunday")
        }
        return format
    }
}