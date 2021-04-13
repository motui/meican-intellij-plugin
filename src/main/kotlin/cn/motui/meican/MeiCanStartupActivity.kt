package cn.motui.meican

import cn.motui.meican.job.NotificationScheduler
import cn.motui.meican.job.OrderAutomaticScheduler
import cn.motui.meican.job.RefreshJob
import cn.motui.meican.job.RefreshScheduler
import cn.motui.meican.util.settings
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

/**
 * 服务启动提交服务
 */
class MeiCanStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        if (project.isDisposed) {
            return
        }
        // 创建/更新/删除job
        NotificationScheduler.schedule(settings.notice, settings.tabs)
        OrderAutomaticScheduler.schedule(settings.order, settings.tabs)
        RefreshScheduler.scheduler(RefreshJob.cron)
    }
}
