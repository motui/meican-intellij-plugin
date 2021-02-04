package cn.motui.meican.job

import cn.motui.meican.MeiCanBundle
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.model.TabStatus
import cn.motui.meican.model.TabType
import cn.motui.meican.model.ui.TabData
import cn.motui.meican.ui.order.OrderView
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.dataService
import org.apache.commons.lang3.RandomUtils
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDateTime

/**
 * 自动点餐JOB
 */
class OrderAutomaticJob : Job {
    override fun execute(context: JobExecutionContext?) {
        val jobTypeStr = context?.mergedJobDataMap?.get(jobKey)
        val jobType = TabType.valueOf(jobTypeStr as String)
        var content: String? = null
        try {
            val dateData = dataService.getTabData(LocalDateTime.now())
            val tabData: TabData = if (TabType.AM == jobType) dateData[0] else dateData[1]
            if (tabData.tabStatus == TabStatus.AVAILABLE) {
                content = orderAutomatic(tabData)
            }
        } catch (e: Exception) {
            content = if (TabType.AM == jobType) MeiCanBundle.message("order.notification.am")
            else MeiCanBundle.message("order.notification.pm")
        } finally {
            content?.let {
                Notifications.showInfoNotification(
                    NOTIFICATIONS_ID,
                    MeiCanBundle.message("order.automatic.notification.title"),
                    content
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
