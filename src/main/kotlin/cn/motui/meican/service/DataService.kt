package cn.motui.meican.service

import cn.motui.meican.model.api.Account
import cn.motui.meican.model.api.Address
import cn.motui.meican.model.ui.OrderDetail
import cn.motui.meican.model.ui.RestaurantData
import cn.motui.meican.model.ui.TabData
import java.time.LocalDateTime

/**
 * 数据服务
 */
interface DataService {
    /**
     * 时间数据
     *
     * @param targetDateTime 目标时间
     * @return TabData
     */
    fun getDateData(targetDateTime: LocalDateTime): List<TabData>

    /**
     * 餐厅数据
     *
     * @param userTabUniqueId userTabUniqueId
     * @param targetDateTime  目标时间
     * @return RestaurantData
     */
    fun getRestaurantData(userTabUniqueId: String, targetDateTime: LocalDateTime): List<RestaurantData>

    /**
     * 地址信息
     *
     * @param corpNamespace corpNamespace
     * @return Address
     */
    fun getAddress(corpNamespace: String): List<Address>

    /**
     * 刷新账户信息
     */
    fun refresh(username: String?, password: String?)

    /**
     * 当前用户信息
     *
     * @return Account
     */
    fun getAccount(): Account

    /**
     * 订单信息
     *
     * @param orderUniqueId orderUniqueId
     * @return OrderDetail
     */
    fun getOrderDetail(orderUniqueId: String): OrderDetail

    /**
     * 下单
     *
     * @param userTabUniqueId userTabUniqueId
     * @param addressUniqueId addressUniqueId
     * @param targetDateTime  目标时间
     * @param dishId          菜品ID
     * @return 订单ID
     */
    fun order(
        userTabUniqueId: String,
        addressUniqueId: String,
        targetDateTime: LocalDateTime,
        dishId: Long
    ): String

    /**
     * 取消订单
     * @param orderUniqueId 订单ID
     */
    fun cancelOrder(orderUniqueId: String)
}
