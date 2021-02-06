package cn.motui.meican.ui.settings

import cn.motui.meican.MeiCanBundle.message

/**
 * 面板显示
 */
enum class TabShow(
    private val displayName: String,
) {
    ALL(message("setting.other.tab.show.all")),
    AM(message("setting.other.tab.show.am")),
    PM(message("setting.other.tab.show.pm"))
    ;

    override fun toString(): String {
        return displayName
    }
}
