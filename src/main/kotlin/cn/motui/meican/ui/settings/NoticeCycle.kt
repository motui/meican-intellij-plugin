package cn.motui.meican.ui.settings

import cn.motui.meican.MeiCanBundle.message


enum class NoticeCycle(
    private val displayName: String,
    val cron: String
) {
    MONDAY_TO_FRIDAY(message("setting.cycle.monday_to_friday"), "? * 2-6"),
    MONDAY_TO_SATURDAY(message("setting.cycle.monday_to_saturday"), "? * 2-7"),
    EVERYDAY(message("setting.cycle.everyday"), "? * *");

    override fun toString(): String {
        return displayName
    }
}