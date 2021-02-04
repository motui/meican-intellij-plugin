package cn.motui.meican.job

import cn.motui.meican.MeiCanBundle.message
import cn.motui.meican.NOTIFICATIONS_ID
import cn.motui.meican.model.TabStatus
import cn.motui.meican.model.ui.TabData
import cn.motui.meican.util.Notifications
import cn.motui.meican.util.dataService
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory
import java.time.LocalDateTime

/**
 * 通知调度器
 */
class NotificationScheduler {

    enum class JobType {
        AM,
        PM
    }

    companion object {
        private val schedulerFactory: StdSchedulerFactory = StdSchedulerFactory()
        private const val triggerGroupName: String = "TRIGGER_GROUP_NAME"
        private const val amJobName: String = "AM_JOB"
        private const val pmJobName: String = "PM_JOB"
        const val jobKey: String = "JOB_KEY"

        fun scheduler(cron: String, jobType: JobType) {
            val jobName = if (JobType.AM == jobType) amJobName else pmJobName
            val triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName)
            val trigger = schedulerFactory.scheduler.getTrigger(triggerKey)
            trigger?.let {
                update(triggerKey, trigger as CronTrigger, cron, jobType)
            } ?: create(triggerKey, jobName, cron, jobType)
        }

        private fun create(triggerKey: TriggerKey, jobName: String, cron: String, jobType: JobType) {
            val scheduler = schedulerFactory.scheduler
            val jobDetail = JobBuilder.newJob(NotificationJob::class.java)
                .withIdentity(jobName, triggerGroupName)
                .build()
            val trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .usingJobData(jobKey, jobType.name)
                .build()
            scheduler.scheduleJob(jobDetail, trigger)
            if (!scheduler.isStarted) {
                scheduler.start()
            }
        }

        private fun update(triggerKey: TriggerKey, trigger: CronTrigger, cron: String, jobType: JobType) {
            if (trigger.cronExpression != cron) {
                schedulerFactory.scheduler
                    .rescheduleJob(
                        triggerKey,
                        trigger.triggerBuilder
                            .withIdentity(triggerKey)
                            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                            .usingJobData(jobKey, jobType.name)
                            .build()
                    )
            }
        }
    }
}

/**
 * 通知JOB
 */
class NotificationJob : Job {
    override fun execute(context: JobExecutionContext?) {
        val jobTypeStr = context?.mergedJobDataMap?.get(NotificationScheduler.jobKey)
        val jobType = NotificationScheduler.JobType.valueOf(jobTypeStr as String)
        try {
            val dateData = dataService.getDateData(LocalDateTime.now().plusDays(1))
            val tabData: TabData = if (NotificationScheduler.JobType.AM == jobType) dateData[0] else dateData[1]
            val content = when (tabData.tabStatus) {
                TabStatus.AVAILABLE ->
                    if (NotificationScheduler.JobType.AM == jobType)
                        message("order.notification.am") else message("order.notification.pm")
                TabStatus.ORDER -> {
                    if (tabData.orderUniqueId.isNullOrBlank()) {
                        if (NotificationScheduler.JobType.AM == jobType) message("order.notification.am")
                        else message("order.notification.pm")
                    } else {
                        val orderDetail = dataService.getOrderDetail(tabData.orderUniqueId)
                        message("order.notification.order.detail").format(
                            orderDetail.restaurantName,
                            orderDetail.dish.title,
                            orderDetail.dish.remark
                        )
                    }
                }
                else -> message("order.notification.restaurant.close")
            }
            Notifications.showInfoNotification(NOTIFICATIONS_ID, message("order.notification.title"), content)
        } catch (e: Exception) {
            val content = if (NotificationScheduler.JobType.AM == jobType) message("order.notification.am")
            else message("order.notification.pm")
            Notifications.showInfoNotification(NOTIFICATIONS_ID, message("order.notification.title"), content)
        }
    }
}
