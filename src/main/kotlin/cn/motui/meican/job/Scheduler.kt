package cn.motui.meican.job

import cn.motui.meican.model.TabType
import org.quartz.CronScheduleBuilder
import org.quartz.CronTrigger
import org.quartz.JobBuilder
import org.quartz.JobDetail
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

private fun update(triggerKey: TriggerKey, trigger: CronTrigger, cron: String, jobKey: String, jobType: TabType) {
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

        fun scheduler(cron: String, jobType: TabType) {
            val jobName = if (TabType.AM == jobType) amJobName else pmJobName
            val triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName)
            val trigger = schedulerFactory.scheduler.getTrigger(triggerKey)
            val jobDetail = JobBuilder.newJob(NotificationJob::class.java)
                .withIdentity(jobName, triggerGroupName)
                .build()
            trigger?.let {
                update(triggerKey, trigger as CronTrigger, cron, jobKey, jobType)
            } ?: create(triggerKey, cron, jobType, jobDetail)
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

        fun scheduler(cron: String, jobType: TabType) {
            println(cron + "\t" + jobType.name)
            val jobName = if (TabType.AM == jobType) amJobName else pmJobName
            val triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName)
            val trigger = schedulerFactory.scheduler.getTrigger(triggerKey)
            val jobDetail = JobBuilder.newJob(OrderAutomaticJob::class.java)
                .withIdentity(jobName, triggerGroupName)
                .build()
            trigger?.let {
                update(triggerKey, trigger as CronTrigger, cron, jobKey, jobType)
            } ?: create(triggerKey, cron, jobType, jobDetail)
        }
    }
}
