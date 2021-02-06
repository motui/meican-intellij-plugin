package cn.motui.meican.model.ui

import cn.motui.meican.model.TabStatus
import cn.motui.meican.model.TabType
import java.time.LocalDateTime

/**
 * tab数据（午餐、晚餐）
 */
class TabData(
    val title: String,
    val tabStatus: TabStatus,
    val reason: String,
    val userTabUniqueId: String,
    val corpUniqueId: String,
    val corpNamespace: String,
    val orderUniqueId: String?,
    val targetDateTime: LocalDateTime
) {

    fun type(): TabType {
        if (targetDateTime.hour <= 12) {
            return TabType.AM
        }
        return TabType.PM
    }
}
