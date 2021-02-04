package cn.motui.meican.job

import cn.motui.meican.MeiCanBundle
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.model.TabStatus
import cn.motui.meican.model.TabType
import cn.motui.meican.model.ui.TabData
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.dataService
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDateTime

/**
 * 订餐通知JOB
 */
class NotificationJob : Job {
    override fun execute(context: JobExecutionContext?) {
        val jobTypeStr = context?.mergedJobDataMap?.get(jobKey)
        val jobType = TabType.valueOf(jobTypeStr as String)
        var content: String? = null
        try {
            val dateData = dataService.getTabData(LocalDateTime.now())
            val tabData: TabData = if (TabType.AM == jobType) dateData[0] else dateData[1]
            content = when (tabData.tabStatus) {
                TabStatus.AVAILABLE ->
                    if (TabType.AM == jobType)
                        MeiCanBundle.message("order.notification.am") else MeiCanBundle.message("order.notification.pm")
                TabStatus.ORDER -> {
                    if (tabData.corpOrderUser == null) {
                        if (TabType.AM == jobType) MeiCanBundle.message("order.notification.am")
                        else MeiCanBundle.message("order.notification.pm")
                    } else {
                        val orderDetail = dataService.getOrderDetail(tabData.corpOrderUser.orderUniqueId)
                        MeiCanBundle.message("order.notification.order.detail").format(
                            orderDetail.restaurantName,
                            orderDetail.dish.name,
                            orderDetail.pickUpMessage
                        )
                    }
                }
                else -> MeiCanBundle.message("order.notification.restaurant.close")
            }
        } catch (e: Exception) {
            content = if (TabType.AM == jobType) MeiCanBundle.message("order.notification.am")
            else MeiCanBundle.message("order.notification.pm")
        } finally {
            content?.let {
                Notifications.showInfoNotification(
                    NOTIFICATIONS_ID,
                    MeiCanBundle.message("order.notification.title"),
                    content
                )
            }
        }
    }
}
