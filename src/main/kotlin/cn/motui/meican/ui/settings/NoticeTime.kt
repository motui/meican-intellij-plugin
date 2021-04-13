package cn.motui.meican.ui.settings

import cn.motui.meican.MeiCanBundle.message

enum class NoticeTime(
    val time: Int,
    private val displayName: String
) {
    M5(5, message("setting.time.m5")),
    M10(10, message("setting.time.m10")),
    M15(15, message("setting.time.m15")),
    M20(20, message("setting.time.m20")),
    M30(30, message("setting.time.m30")),
    H1(1, message("setting.time.h1")),
    H2(2, message("setting.time.h2")), ;

    override fun toString(): String {
        return displayName
    }

    fun cron(hour: Int): String {
        if (H1 == this || H2 == this) {
            return "0 %d %d".format(0, hour - this.time)
        }
        return "0 %d %d".format(60 - this.time, hour - 1)
    }

    fun cron(hour: Int, minute: Int): String {
        return if (H1 == this || H2 == this) {
            "0 %d %d".format(minute, hour - this.time)
        } else {
            if (minute < this.time) {
                "0 %d %d".format(60 + minute - this.time, hour - 1)
            } else {
                "0 %d %d".format(minute - this.time, hour)
            }
        }
    }
}
