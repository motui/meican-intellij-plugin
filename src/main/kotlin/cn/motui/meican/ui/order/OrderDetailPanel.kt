package cn.motui.meican.ui.order

import cn.motui.meican.MeiCanBundle.message
import cn.motui.meican.ui.OrderDetailForm
import cn.motui.meican.ui.UI
import cn.motui.meican.util.dataService
import cn.motui.meican.util.toLocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * 订单详情
 */
class OrderDetailPanel constructor(
    private val orderUniqueId: String
) {

    private val form = OrderDetailForm()

    init {
        renderUi()
    }

    fun root(): JPanel {
        return form.rootPanel
    }

    private fun renderUi() {
        SwingUtilities.invokeLater {
            val orderDetail = dataService.getOrderDetail(orderUniqueId)
            form.count.text = message("order.tool.window.order.detail.count").format(orderDetail.dishCount)
            form.title.text = orderDetail.dish.title
            form.remark.text = orderDetail.dish.remark
            form.tips.text = "[" + orderDetail.postbox.postboxCode + "] " + orderDetail.pickUpMessage
            form.scheduleList.setListData(
                orderDetail.progressList.map {
                    "[" + it.timestamp.toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + it.activity
                }
                    .toTypedArray()
            )
        }
    }

}