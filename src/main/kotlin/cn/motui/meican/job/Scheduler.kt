package cn.motui.meican.job

import cn.motui.meican.Notice
import cn.motui.meican.Order
import cn.motui.meican.Tabs
import org.quartz.CronScheduleBuilder
import org.quartz.CronTrigger
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.TriggerBuilder
import org.quartz.TriggerKey
import org.quartz.impl.StdSchedulerFactory

private val schedulerFactory: StdSchedulerFactory = StdSchedulerFactory()
private const val triggerGroupName: String = "TRIGGER_GROUP_NAME"
const val jobKey: String = "JOB_KEY"

private fun create(
    triggerKey: TriggerKey,
    cron: String,
    title: String,
    jobDetail: JobDetail
) {
    val scheduler = schedulerFactory.scheduler
    val trigger = TriggerBuilder
        .newTrigger()
        .withIdentity(triggerKey)
        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
        .usingJobData(jobKey, title)
        .build()
    scheduler.scheduleJob(jobDetail, trigger)
    if (!scheduler.isStarted) {
        scheduler.start()
    }
}

private fun update(triggerKey: TriggerKey, trigger: CronTrigger, cron: String, title: String) {
    if (trigger.cronExpression != cron) {
        schedulerFactory.scheduler
            .rescheduleJob(
                triggerKey,
                trigger.triggerBuilder
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .usingJobData(jobKey, title)
                    .build()
            )
    }
}

/**
 * 通知调度器
 */
class NotificationScheduler {
    companion object {
        private fun scheduler(cron: String, id: String, title: String) {
            val triggerKey = TriggerKey.triggerKey(id, triggerGroupName)
            val trigger = schedulerFactory.scheduler.getTrigger(triggerKey)
            val jobDetail = JobBuilder.newJob(NotificationJob::class.java)
                .withIdentity(id, triggerGroupName)
                .build()
            trigger?.let {
                update(triggerKey, trigger as CronTrigger, cron, title)
            } ?: create(triggerKey, cron, title, jobDetail)
        }

        private fun deleteSchedule(jobKey: JobKey) {
            schedulerFactory.scheduler.deleteJob(jobKey)
        }

        fun schedule(notice: Notice, tabs: Tabs) {
            tabs.tabSet.forEach { tab ->
                if (tab.notice) {
                    val id = "notification_" + tab.openingTime.uniqueId
                    deleteSchedule(JobKey.jobKey(id, triggerGroupName))
                    scheduler(notice.corn(tab.openingTime.closeTime), id, tab.title)
                }
            }
        }
    }
}

/**
 * 自动订餐调度器
 */
class OrderAutomaticScheduler {
    companion object {
        private fun scheduler(cron: String, id: String, title: String) {
            val triggerKey = TriggerKey.triggerKey(id, triggerGroupName)
            val trigger = schedulerFactory.scheduler.getTrigger(triggerKey)
            val jobDetail = JobBuilder.newJob(OrderAutomaticJob::class.java)
                .withIdentity(id, triggerGroupName)
                .build()
            trigger?.let {
                update(triggerKey, trigger as CronTrigger, cron, title)
            } ?: create(triggerKey, cron, title, jobDetail)
        }

        private fun deleteSchedule(jobKey: JobKey) {
            schedulerFactory.scheduler.deleteJob(jobKey)
        }

        fun schedule(order: Order, tabs: Tabs) {
            tabs.tabSet.forEach { tab ->
                if (tab.automatic) {
                    val id = "order_" + tab.openingTime.uniqueId
                    deleteSchedule(JobKey.jobKey(id, triggerGroupName))
                    scheduler(order.corn(tab.openingTime.closeTime), id, tab.title)
                }
            }
        }
    }
}

/**
 * 刷新任务
 */
class RefreshScheduler {
    companion object {
        fun scheduler(cron: String) {
            val jobName = "REFRESH_JOB"
            val triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName)
            val trigger = schedulerFactory.scheduler.getTrigger(triggerKey)
            if (trigger == null) {
                val jobDetail = JobBuilder.newJob(RefreshJob::class.java)
                    .withIdentity(jobName, triggerGroupName)
                    .build()
                val scheduler = schedulerFactory.scheduler
                scheduler.scheduleJob(
                    jobDetail,
                    TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .build()
                )
                if (!scheduler.isStarted) {
                    scheduler.start()
                }
            }
        }
    }
}
