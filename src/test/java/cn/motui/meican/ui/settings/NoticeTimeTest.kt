package cn.motui.meican.ui.settings

import org.junit.Test
import kotlin.test.assertEquals

internal class NoticeTimeTest {

    @Test
    fun cron() {
        assertEquals("0 10 9", NoticeTime.M20.cron(9, 30))
        assertEquals("0 0 9", NoticeTime.M30.cron(9, 30))
        assertEquals("0 50 8", NoticeTime.M30.cron(9, 20))
        assertEquals("0 20 8", NoticeTime.H1.cron(9, 20))
    }

    @Test
    fun closeTime() {
        var split = "09:30".split(":")
        assertEquals(9, split[0].toInt())
        assertEquals(30, split[1].toInt())

        split = "10:00".split(":")
        assertEquals(10, split[0].toInt())
        assertEquals(0, split[1].toInt())
    }
}
