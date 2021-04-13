package cn.motui.meican.job

import cn.motui.meican.MeiCanBundle
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.model.TabStatus
import cn.motui.meican.model.ui.TabData
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.dataService
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDateTime
import java.util.function.Function
import java.util.stream.Collectors

/**
 * 订餐通知JOB
 */
class NotificationJob : Job {
    override fun execute(context: JobExecutionContext?) {
        val title = context?.mergedJobDataMap?.get(jobKey)
        var content: String? = null
        try {
            val tabDataList = dataService.getTabData(LocalDateTime.now())
            val tabData = tabDataList.stream()
                .collect(Collectors.toMap(TabData::title, Function.identity()))[title]
            tabData?.let {
                content = when (it.tabStatus) {
                    TabStatus.AVAILABLE -> MeiCanBundle.message("order.notification").format(it.title)
                    TabStatus.ORDER -> {
                        if (it.corpOrderUser == null) {
                            MeiCanBundle.message("order.notification").format(it.title)
                        } else {
                            val orderDetail = dataService.getOrderDetail(it.corpOrderUser.orderUniqueId)
                            MeiCanBundle.message("order.notification.order.detail").format(
                                orderDetail.restaurantName,
                                orderDetail.dish.name,
                                orderDetail.pickUpMessage
                            )
                        }
                    }
                    else -> MeiCanBundle.message("order.notification.restaurant.close")
                }
            }
        } catch (e: Exception) {
            content = MeiCanBundle.message("order.notification").format("")
        } finally {
            content?.let {
                Notifications.showInfoNotification(
                    NOTIFICATIONS_ID,
                    MeiCanBundle.message("order.notification.title"),
                    it
                )
            }
        }
    }
}
