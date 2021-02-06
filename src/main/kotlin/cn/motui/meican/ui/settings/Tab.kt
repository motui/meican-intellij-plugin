package cn.motui.meican.ui.settings

import cn.motui.meican.MeiCanBundle.message

/**
 * 通知Tab
 */
enum class Tab(
    private val displayName: String,
) {
    ALL(message("setting.options.notice.tab.all")),
    AM(message("setting.options.notice.tab.am")),
    PM(message("setting.options.notice.tab.pm")),
    NO(message("setting.options.notice.tab.no"))
    ;

    override fun toString(): String {
        return displayName
    }
}
