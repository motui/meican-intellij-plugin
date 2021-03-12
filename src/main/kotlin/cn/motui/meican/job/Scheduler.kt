package cn.motui.meican.job

import cn.motui.meican.Notice
import cn.motui.meican.Order
import cn.motui.meican.model.TabType
import cn.motui.meican.ui.settings.Automatic
import cn.motui.meican.ui.settings.Tab
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
    jobType: TabType,
    jobDetail: JobDetail
) {
    val scheduler = schedulerFactory.scheduler
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

private fun update(triggerKey: TriggerKey, trigger: CronTrigger, cron: String, jobType: TabType) {
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

/**
 * 通知调度器
 */
class NotificationScheduler {
    companion object {
        private const val amJobName: String = "AM_JOB"
        private const val pmJobName: String = "PM_JOB"

        private fun scheduler(cron: String, jobType: TabType) {
            val jobName = if (TabType.AM == jobType) amJobName else pmJobName
            val triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName)
            val trigger = schedulerFactory.scheduler.getTrigger(triggerKey)
            val jobDetail = JobBuilder.newJob(NotificationJob::class.java)
                .withIdentity(jobName, triggerGroupName)
                .build()
            trigger?.let {
                update(triggerKey, trigger as CronTrigger, cron, jobType)
            } ?: create(triggerKey, cron, jobType, jobDetail)
        }

        private fun deleteSchedule(jobKey: JobKey) {
            schedulerFactory.scheduler.deleteJob(jobKey)
        }

        fun schedule(notice: Notice) {
            when (notice.tab) {
                Tab.NO -> {
                    deleteSchedule(JobKey.jobKey(amJobName, triggerGroupName))
                    deleteSchedule(JobKey.jobKey(pmJobName, triggerGroupName))
                }
                Tab.ALL -> {
                    scheduler(notice.cron(TabType.AM), TabType.AM)
                    scheduler(notice.cron(TabType.PM), TabType.PM)
                }
                Tab.AM -> {
                    scheduler(notice.cron(TabType.AM), TabType.AM)
                    deleteSchedule(JobKey.jobKey(pmJobName, triggerGroupName))
                }
                Tab.PM -> {
                    deleteSchedule(JobKey.jobKey(amJobName, triggerGroupName))
                    scheduler(notice.cron(TabType.PM), TabType.PM)
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
        private const val amJobName: String = "AM_ORDER_AUTOMATIC_JOB"
        private const val pmJobName: String = "PM_ORDER_AUTOMATIC_JOB"

        private fun scheduler(cron: String, jobType: TabType) {
            val jobName = if (TabType.AM == jobType) amJobName else pmJobName
            val triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName)
            val trigger = schedulerFactory.scheduler.getTrigger(triggerKey)
            val jobDetail = JobBuilder.newJob(OrderAutomaticJob::class.java)
                .withIdentity(jobName, triggerGroupName)
                .build()
            trigger?.let {
                update(triggerKey, trigger as CronTrigger, cron, jobType)
            } ?: create(triggerKey, cron, jobType, jobDetail)
        }

        private fun deleteSchedule(jobKey: JobKey) {
            schedulerFactory.scheduler.deleteJob(jobKey)
        }

        fun schedule(order: Order) {
            when (order.automatic) {
                Automatic.NO -> {
                    deleteSchedule(JobKey.jobKey(amJobName, triggerGroupName))
                    deleteSchedule(JobKey.jobKey(pmJobName, triggerGroupName))
                }
                Automatic.ALL -> {
                    scheduler(order.cron(TabType.AM), TabType.AM)
                    scheduler(order.cron(TabType.PM), TabType.PM)
                }
                Automatic.AM -> {
                    scheduler(order.cron(TabType.AM), TabType.AM)
                    deleteSchedule(JobKey.jobKey(pmJobName, triggerGroupName))
                }
                Automatic.PM -> {
                    deleteSchedule(JobKey.jobKey(amJobName, triggerGroupName))
                    scheduler(order.cron(TabType.PM), TabType.PM)
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
