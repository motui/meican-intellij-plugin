package cn.motui.meican.ui.order

import cn.motui.meican.MeiCanBundle
import cn.motui.meican.MeiCanBundle.message
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.exception.MeiCanAddOrderException
import cn.motui.meican.model.TabStatus
import cn.motui.meican.model.TabType
import cn.motui.meican.model.api.Address
import cn.motui.meican.model.api.Dish
import cn.motui.meican.model.ui.OrderDetail
import cn.motui.meican.model.ui.RestaurantData
import cn.motui.meican.model.ui.TabData
import cn.motui.meican.ui.TabWindowForm
import cn.motui.meican.ui.UI
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.application
import cn.motui.meican.util.dataService
import cn.motui.meican.util.toLocalDateTime
import java.awt.CardLayout
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.JOptionPane
import javax.swing.JPanel

/**
 * TabPanel
 */
class TabPanel constructor(
    private val tabType: TabType,
    private val targetDateTime: LocalDateTime
) : TabWindowForm() {

    init {
        // 订单
        splitPane.dividerSize = 2
        splitPane.dividerLocation = 200
        // 订单详情
        cancelOrderButton.isVisible = false
        scheduleList.background = UI.transparentColor()
        renderUi()
    }

    private fun renderUi() {
        application.invokeLater {
            val tabDataList: List<TabData> = dataService.getTabData(targetDateTime)
            val tabData = tabDataList.first { tabData -> tabData.type() == tabType }
            when (tabData.tabStatus) {
                TabStatus.AVAILABLE -> renderOrderUi(tabData)
                TabStatus.ORDER -> renderOrderDetailUi(tabData)
                else -> renderEmptyUi(tabData)
            }
        }
    }

    private fun renderOrderUi(tabData: TabData) {
        application.invokeLater {
            orderButton.text = message("tab.button.order")
            orderButton.addActionListener { buttonOrderListener(tabData) }
            renderRestaurantUi(tabData)
            renderAddressUi(tabData)
            showOrderPane()
        }
    }

    private fun renderOrderDetailUi(tabData: TabData) {
        application.invokeLater {
            cancelOrderButton.text = message("tab.button.order.cancel")
            tabData.corpOrderUser?.let {
                val orderDetail = dataService.getOrderDetail(it.orderUniqueId)
                cancelOrderButton.addActionListener { buttonCancelOrderListener(orderDetail) }
                if (orderDetail.readyToDelete) {
                    cancelOrderButton.isVisible = true
                }
                dishCountLabel.text =
                    MeiCanBundle.message("tab.order.detail.count").format(orderDetail.dishCount)
                dishNameLabel.text = orderDetail.dish.title
                dishRemarkLabel.text = orderDetail.dish.remark
                tipsLabel.text = "[" + orderDetail.postbox.postboxCode + "] " + orderDetail.pickUpMessage
                scheduleList.setListData(
                    orderDetail.progressList.map { progress ->
                        "[" + progress.timestamp.toLocalDateTime()
                            .format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + progress.activity
                    }
                        .toTypedArray()
                )
                showOrderDetailPane()
            }
        }
    }

    private fun renderEmptyUi(tabData: TabData) {
        application.invokeLater {
            label.text = tabData.reason
            showMessagePane()
        }
    }

    private fun buttonCancelOrderListener(orderDetail: OrderDetail) {
        val message = "%s:%s\n%s:%s".format(
            message("tab.restaurant"),
            orderDetail.restaurantName,
            message("tab.dish"),
            orderDetail.dish.title
        )
        val operate = JOptionPane.showConfirmDialog(
            null,
            message,
            message("tab.order.cancel.confirmation.title"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.PLAIN_MESSAGE
        )
        if (JOptionPane.YES_OPTION == operate) {
            application.invokeLater {
                cancelOrderButton.isEnabled = false
                try {
                    dataService.cancelOrder(orderDetail.uniqueId)
                    Notifications.showInfoNotification(
                        NOTIFICATIONS_ID,
                        MeiCanBundle.message("tab.order.notification.title"),
                        MeiCanBundle.message("tab.order.cancel.success.notification.message").format(
                            orderDetail.restaurantName,
                            orderDetail.dish.name,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                        )
                    )
                    // 重新渲染页面
                    renderUi()
                } catch (e: Exception) {
                    Notifications.showErrorNotification(
                        NOTIFICATIONS_ID,
                        message("tab.order.fail.notification.message"),
                        message("http.exception")
                    )
                } finally {
                    orderButton.isEnabled = true
                }
            }
        }
    }

    private fun buttonOrderListener(tabData: TabData) {
        val restaurant = restaurantList.selectedValue
        val dish = dishList.selectedValue
        val address = addressComboBox.selectedItem as Address
        if (dish == null) {
            JOptionPane.showMessageDialog(
                null,
                MeiCanBundle.message("tab.order.check.title"),
                MeiCanBundle.message("tab.order.check.dish.message"),
                JOptionPane.ERROR_MESSAGE,
                null
            )
            return
        }
        val operate = JOptionPane.showConfirmDialog(
            null,
            orderConfirmation(restaurant, dish, address),
            MeiCanBundle.message("tab.order.confirmation.title"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.PLAIN_MESSAGE
        )
        if (JOptionPane.YES_OPTION == operate) {
            application.invokeLater {
                orderButton.isEnabled = false
                try {
                    dataService.order(
                        tabData.userTabUniqueId,
                        address.finalValue.uniqueId,
                        tabData.targetDateTime,
                        dish.id
                    )
                    Notifications.showInfoNotification(
                        NOTIFICATIONS_ID,
                        MeiCanBundle.message("tab.order.notification.title"),
                        MeiCanBundle.message("tab.order.success.notification.message").format(
                            restaurant.name,
                            dish.name,
                            address.finalValue.pickUpLocation,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                        )
                    )
                    // 重新渲染页面
                    renderUi()
                } catch (e: MeiCanAddOrderException) {
                    Notifications.showErrorNotification(
                        NOTIFICATIONS_ID,
                        MeiCanBundle.message("tab.order.notification.title"),
                        MeiCanBundle.message("tab.order.fail.notification.message") + e.message
                    )
                } catch (e: Exception) {
                    Notifications.showErrorNotification(
                        NOTIFICATIONS_ID,
                        MeiCanBundle.message("tab.order.notification.title"),
                        message("http.exception")
                    )
                } finally {
                    orderButton.isEnabled = true
                }
            }
        }
    }

    private fun orderConfirmation(restaurant: RestaurantData, dish: Dish, address: Address): String {
        return "%s:%s\n%s:%s\n%s:%s".format(
            MeiCanBundle.message("tab.restaurant"),
            restaurant.name,
            MeiCanBundle.message("tab.dish"),
            dish.name,
            MeiCanBundle.message("tab.address"),
            address.finalValue.pickUpLocation
        )
    }

    private fun renderRestaurantUi(tabData: TabData) {
        application.invokeLater {
            val restaurantData = dataService.getRestaurantData(tabData.userTabUniqueId, tabData.targetDateTime)
            restaurantList.setListData(restaurantData.toTypedArray())
            restaurantList.addListSelectionListener {
                if (!it.valueIsAdjusting) {
                    renderDishUi(restaurantList.selectedValue)
                }
            }
            restaurantList.selectedIndex = 0
        }
    }

    private fun renderAddressUi(tabData: TabData) {
        application.invokeLater {
            val addressList = dataService.getAddress(tabData.corpNamespace)
            addressList.forEach {
                addressComboBox.addItem(it)
            }
            addressComboBox.selectedIndex = 0
        }
    }

    private fun renderDishUi(restaurantData: RestaurantData) {
        dishList.removeAll()
        dishList.setListData(restaurantData.dishes.toTypedArray())
    }

    private fun showMessagePane() {
        (root.layout as CardLayout).show(root, CARD_MESSAGE)
    }

    private fun showOrderPane() {
        (root.layout as CardLayout).show(root, CARD_ORDER)
    }

    private fun showOrderDetailPane() {
        (root.layout as CardLayout).show(root, CARD_ORDER_DETAIL)
    }

    fun root(): JPanel {
        return root
    }

    companion object {
        private const val CARD_MESSAGE = "CARD_MESSAGE"
        private const val CARD_ORDER = "CARD_ORDER"
        private const val CARD_ORDER_DETAIL = "CARD_ORDER_DETAIL"
    }
}
