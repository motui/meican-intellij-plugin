package cn.motui.meican.model.ui

import cn.motui.meican.model.api.Dish

/**
 * 餐厅信息
 */
class RestaurantData constructor(
    val uniqueId: String,
    val name: String,
    val open: Boolean,
    val dishes: List<Dish>
) {

    override fun toString(): String {
        return name
    }
}