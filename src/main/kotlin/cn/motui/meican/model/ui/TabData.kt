package cn.motui.meican.model.ui

import cn.motui.meican.model.TabStatus
import java.time.LocalDateTime

/**
 * tab数据（午餐、晚餐）
 */
class TabData(
    val title: String,
    val tabStatus: TabStatus,
    val reason: String,
    val targetDateTime: LocalDateTime,

    val openingTime: OpeningTime,

    val userTabUniqueId: String,

    val corpUniqueId: String,
    val corpNamespace: String,
    val corpOrderUser: CorpOrderUser?
) {

    class CorpOrderUser constructor(
        val orderUniqueId: String,
        var readyToDelete: Boolean,
        var corpOrderStatus: String,
    ) {
        fun isRefresh(): Boolean {
            return readyToDelete && "ON_THE_WAY" != corpOrderStatus
        }
    }
}
