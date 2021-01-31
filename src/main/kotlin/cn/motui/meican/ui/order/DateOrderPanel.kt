package cn.motui.meican.ui.order

import cn.motui.meican.MeiCanBundle.message
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.exception.MeiCanAddOrderException
import cn.motui.meican.model.api.Address
import cn.motui.meican.model.api.Dish
import cn.motui.meican.model.ui.RestaurantData
import cn.motui.meican.ui.DateOrderForm
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.dataService
import java.awt.event.ActionEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * 时间对应店铺和菜品选择面板
 */
class DateOrderPanel constructor(
    private val orderPanel: OrderPanel,
    private val targetDateTime: LocalDateTime,
    private val userTabUniqueId: String,
    private val corpNamespace: String
) {
    private val form = DateOrderForm()

    init {
        form.splitPane.dividerSize = 2
        form.splitPane.dividerLocation = 200
        form.okButton.addActionListener { okListener(it) }
        renderUi()
    }

    private fun renderUi() {
        renderRestaurantUi()
        renderAddressUi()
    }

    private fun okListener(event: ActionEvent) {
        val restaurant = form.restaurantList.selectedValue
        val dish = form.dishList.selectedValue
        val address = form.addressComboBox.selectedItem as Address
        if (Objects.isNull(dish)) {
            JOptionPane.showMessageDialog(
                null, message("order.tool.window.date.order.check.dish.message"),
                message("order.tool.window.date.order.check.title"), JOptionPane.ERROR_MESSAGE, null
            )
            return
        }
        if (Objects.isNull(address)) {
            JOptionPane.showMessageDialog(
                null, message("order.tool.window.date.order.check.address.message"),
                message("order.tool.window.date.order.check.title"), JOptionPane.ERROR_MESSAGE, null
            )
            return
        }
        form.okButton.isEnabled = false
        val operate = JOptionPane.showConfirmDialog(
            null,
            orderConfirmation(restaurant, dish, address),
            message("order.tool.window.date.order.confirmation"),
            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE
        )
        if (JOptionPane.YES_OPTION == operate) {
            SwingUtilities.invokeLater {
                try {
                    dataService.order(
                        userTabUniqueId, address.finalValue.uniqueId,
                        targetDateTime, dish.id
                    )
                    Notifications.showInfoNotification(
                        NOTIFICATIONS_ID,
                        message("order.tool.window.date.order.notification.title"),
                        message("order.tool.window.date.order.success").format(
                            restaurant.name,
                            dish.name,
                            address.finalValue.pickUpLocation,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                        )
                    )
                    orderPanel.refreshUi()
                } catch (e: MeiCanAddOrderException) {
                    form.okButton.isEnabled = true
                    Notifications.showErrorNotification(
                        NOTIFICATIONS_ID,
                        message("order.tool.window.date.order.notification.title"),
                        message("order.tool.window.date.order.fail") + e.message
                    )
                } catch (e: Exception) {
                    form.okButton.isEnabled = true
                    Notifications.showErrorNotification(
                        NOTIFICATIONS_ID,
                        message("order.tool.window.date.order.notification.title"),
                        message("order.tool.window.date.order.exception")
                    )
                }
            }
        }
    }


    private fun orderConfirmation(restaurant: RestaurantData, dish: Dish, address: Address): String {
        return message("order.tool.window.date.order.restaurant") + restaurant.name + ":\n" +
                message("order.tool.window.date.order.dish") + dish.name + ":\n" +
                message("order.tool.window.date.order.address") + address.finalValue.pickUpLocation
    }

    private fun renderRestaurantUi() {
        SwingUtilities.invokeLater {
            val restaurantData = dataService.getRestaurantData(userTabUniqueId, targetDateTime)
            form.restaurantList.setListData(restaurantData.toTypedArray())
            form.restaurantList.addListSelectionListener {
                if (!it.valueIsAdjusting) {
                    renderDishUi(form.restaurantList.selectedValue)
                }
            }
            form.restaurantList.selectedIndex = 0
        }
    }

    private fun renderAddressUi() {
        SwingUtilities.invokeLater {
            val addressList = dataService.getAddress(corpNamespace)
            addressList.forEach {
                form.addressComboBox.addItem(it)
            }
            form.addressComboBox.selectedIndex = 0
        }
    }

    private fun renderDishUi(restaurantData: RestaurantData) {
        form.dishList.removeAll()
        form.dishList.setListData(restaurantData.dishes.toTypedArray())
    }

    fun root(): JPanel {
        return form.rootPanel;
    }
}