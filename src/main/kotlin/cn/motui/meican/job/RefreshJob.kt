package cn.motui.meican.job

import cn.motui.meican.ui.order.OrderView
import cn.motui.meican.util.dataService
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDateTime

/**
 * 定时刷新数据
 * 1. 刷新订餐状态
 * 2. 保持token不失效
 */
class RefreshJob : Job {
    override fun execute(context: JobExecutionContext?) {
        val now = LocalDateTime.now()
        try {
            if (!dataService.isCanRequest()) {
                return
            }
            val tabDataList = dataService.getTabData(now)
            tabDataList.forEach { tabData ->
                tabData.corpOrderUser?.let {
                    if (it.isRefresh()) {
                        OrderView.instance.refreshUi()
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    companion object {
        /**
         * 十分钟执行一次，美餐客户端是每分钟执行一次
         */
        const val cron = "0 0/10 * * * ? "
    }
}
