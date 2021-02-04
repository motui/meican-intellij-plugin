package cn.motui.meican.notification

import cn.motui.meican.job.NotificationScheduler
import cn.motui.meican.util.settings
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

/**
 * 服务启动提交通知服务
 */
class NotificationStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        if (project.isDisposed) {
            return
        }
        val notice = settings.notice
        if (notice.am) {
            NotificationScheduler.scheduler(notice.amCron(), NotificationScheduler.JobType.AM)
        }
        if (notice.pm) {
            NotificationScheduler.scheduler(notice.pmCron(), NotificationScheduler.JobType.PM)
        }
    }
}
