package cn.motui.meican.job

import cn.motui.meican.MeiCanBundle
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.model.TabStatus
import cn.motui.meican.model.ui.TabData
import cn.motui.meican.ui.order.OrderView
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.dataService
import org.apache.commons.lang3.RandomUtils
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDateTime
import java.util.function.Function
import java.util.stream.Collectors

/**
 * 自动点餐JOB
 */
class OrderAutomaticJob : Job {
    override fun execute(context: JobExecutionContext?) {
        val title = context?.mergedJobDataMap?.get(jobKey)
        var content: String? = null
        try {
            val tabDataList = dataService.getTabData(LocalDateTime.now())
            val tabData = tabDataList.stream()
                .collect(Collectors.toMap(TabData::title, Function.identity()))[title]
            tabData?.let {
                if (it.tabStatus == TabStatus.AVAILABLE) {
                    content = orderAutomatic(it)
                }
            }
        } catch (e: Exception) {
            content = MeiCanBundle.message("order.notification").format("")
        } finally {
            content?.let {
                Notifications.showInfoNotification(
                    NOTIFICATIONS_ID,
                    MeiCanBundle.message("order.automatic.notification.title"),
                    it
                )
            }
        }
    }

    private fun orderAutomatic(tabData: TabData): String {
        val restaurantList = dataService.getRestaurantData(tabData.userTabUniqueId, tabData.targetDateTime)
        // 采用随机策略
        val restaurant = restaurantList[RandomUtils.nextInt(0, restaurantList.size)]
        val dish = restaurant.dishes[RandomUtils.nextInt(0, restaurant.dishes.size)]
        val addressList = dataService.getAddress(tabData.corpNamespace)
        // 地址默认选取第一个
        val address = addressList[0]
        val orderId =
            dataService.order(tabData.userTabUniqueId, address.finalValue.uniqueId, tabData.targetDateTime, dish.id)
        val orderDetail = dataService.getOrderDetail(orderId)
        // 刷新订餐面板数据
        OrderView.instance.refreshUi()
        return MeiCanBundle.message("order.automatic.notification.order.detail").format(
            orderDetail.restaurantName,
            orderDetail.dish.title,
            address.finalValue.pickUpLocation
        )
    }
}
