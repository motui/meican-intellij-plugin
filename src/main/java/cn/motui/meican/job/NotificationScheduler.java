package cn.motui.meican.job;

import cn.motui.meican.enums.CycleEnum;
import cn.motui.meican.enums.TimeEnum;
import cn.motui.meican.notification.NotificationFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Objects;

/**
 * @author it.motui
 * @date 2021-01-24
 */
public class NotificationScheduler {
  private final static StdSchedulerFactory INSTANCE = new StdSchedulerFactory();
  private final static String JOB_GROUP_NAME = "NOTIFICATION_GROUP_NAME";
  private final static String TRIGGER_GROUP_NAME = "TRIGGER_GROUP_NAME";

  public static final String AM_JOB_NAME = "AM_JOB";
  public static final String PM_JOB_NAME = "PM_JOB";

  public static void scheduler(String jobName, String cron) throws SchedulerException {
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
    Scheduler scheduler = INSTANCE.getScheduler();
    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
    if (Objects.isNull(trigger)) {
      JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class)
          .withIdentity(jobName, TRIGGER_GROUP_NAME)
          .build();
      CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
      trigger = TriggerBuilder
          .newTrigger()
          .withIdentity(jobName, TRIGGER_GROUP_NAME)
          .withSchedule(scheduleBuilder)
          .build();
      scheduler.scheduleJob(jobDetail, trigger);
    } else {
      String cronExpression = trigger.getCronExpression();
      if (!cronExpression.equalsIgnoreCase(cron)) {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
      }
    }
    if (!scheduler.isStarted()) {
      scheduler.start();
    }
  }

  public static void amScheduler(CycleEnum cycle, TimeEnum notifyBeforeClosing) {
    String amCron = String.format("* %s 9 ", 60 - notifyBeforeClosing.getMinute()) + cycle.getCron();
    try {
      NotificationScheduler.scheduler(NotificationScheduler.AM_JOB_NAME, amCron);
    } catch (SchedulerException e) {
      NotificationFactory.showWarningNotification("am scheduler error. cron:" + amCron);
    }
  }

  public static void pmScheduler(CycleEnum cycle, TimeEnum notifyBeforeClosing) {
    String pmCron = String.format("* %s 14 ", 60 - notifyBeforeClosing.getMinute()) + cycle.getCron();
    try {
      NotificationScheduler.scheduler(NotificationScheduler.PM_JOB_NAME, pmCron);
    } catch (SchedulerException e) {
      NotificationFactory.showWarningNotification("pm scheduler error. cron: " + pmCron);
    }
  }
}
