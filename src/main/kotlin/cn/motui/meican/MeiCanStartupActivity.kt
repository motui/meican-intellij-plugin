package cn.motui.meican

import cn.motui.meican.job.NotificationScheduler
import cn.motui.meican.job.OrderAutomaticScheduler
import cn.motui.meican.model.TabType
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
        val notice = settings.notice
        // 创建/更新job
        if (notice.isNotice(TabType.AM)) {
            NotificationScheduler.scheduler(notice.cron(TabType.AM), TabType.AM)
        }
        if (notice.isNotice(TabType.PM)) {
            NotificationScheduler.scheduler(notice.cron(TabType.PM), TabType.PM)
        }
        val order = settings.order
        if (settings.order.isOrderAutomatic(TabType.AM)) {
            OrderAutomaticScheduler.scheduler(order.cron(TabType.AM), TabType.AM)
        }
        if (settings.order.isOrderAutomatic(TabType.PM)) {
            OrderAutomaticScheduler.scheduler(order.cron(TabType.PM), TabType.PM)
        }
    }
}
