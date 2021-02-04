package cn.motui.meican.model.ui

import cn.motui.meican.model.api.Dish
import cn.motui.meican.model.api.Postbox
import cn.motui.meican.model.api.vo.Progress

/**
 * 订单详情
 */
class OrderDetail constructor(
    val corpOrderId: String,
    val pickUpMessage: String,
    val postbox: Postbox,
    val progressList: List<Progress>,
    val restaurantUniqueId: String,
    val restaurantName: String,
    val dish: Dish,
    val dishCount: Int
)
