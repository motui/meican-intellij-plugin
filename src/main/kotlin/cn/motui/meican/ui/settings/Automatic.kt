package cn.motui.meican.ui.settings

import cn.motui.meican.MeiCanBundle.message

/**
 * 自动
 */
enum class Automatic(
    private val displayName: String,
) {

    ALL(message("setting.order.automatic.all")),
    AM(message("setting.order.automatic.am")),
    PM(message("setting.order.automatic.pm")),
    NO(message("setting.order.automatic.no"))
    ;

    override fun toString(): String {
        return displayName
    }
}
