package cn.motui.meican

import cn.motui.meican.ui.settings.NoticeTime
import org.junit.Assert.assertEquals
import org.junit.Test


class NoticeTest {
    @Test
    fun cron() {
        val notice = Notice()
        assertEquals("0 30 14 ? * *", notice.cron(15))
        notice.beforeClosingTime = NoticeTime.H2
        assertEquals("0 0 13 ? * *", notice.cron(15))
    }
}