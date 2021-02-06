package cn.motui.meican.service

import cn.motui.meican.model.TabStatus
import cn.motui.meican.model.api.Account
import cn.motui.meican.model.api.Address
import cn.motui.meican.model.api.Dish
import cn.motui.meican.model.ui.OrderDetail
import cn.motui.meican.model.ui.RestaurantData
import cn.motui.meican.model.ui.TabData
import cn.motui.meican.util.meiCanClient
import cn.motui.meican.util.toLocalDateTime
import com.intellij.openapi.components.Service
import java.time.LocalDateTime

/**
 * 数据服务
 */
@Service
class DataServiceImpl : DataService {
    override fun getDateData(targetDateTime: LocalDateTime): MutableList<TabData> {
        val calendar = meiCanClient.calendar(targetDateTime, targetDateTime)
        val tabDataList: MutableList<TabData> = mutableListOf()
        calendar.dateList?.get(0)?.calendarItemList?.forEach {
            tabDataList.add(
                TabData(
                    it.title,
                    TabStatus.valueOf(it.status),
                    it.reason,
                    it.userTab.uniqueId,
                    it.userTab.corp.uniqueId,
                    it.userTab.corp.namespace,
                    it.corpOrderUser?.uniqueId,
                    it.targetTime.toLocalDateTime()
                )
            )
        }
        return tabDataList
    }

    override fun getRestaurantData(
        userTabUniqueId: String,
        targetDateTime: LocalDateTime
    ): MutableList<RestaurantData> {
        val restaurantDataList: MutableList<RestaurantData> = mutableListOf()
        val restaurant = meiCanClient.restaurants(userTabUniqueId, targetDateTime)
        restaurant.restaurantList?.forEach {
            val restaurantDish = meiCanClient.restaurantDish(userTabUniqueId, targetDateTime, it.uniqueId)
            val dishes: MutableList<Dish> = mutableListOf()
            restaurantDish?.dishList?.filter { item -> !item.section }
                ?.let { item -> dishes.addAll(item) }
            restaurantDataList.add(
                RestaurantData(
                    it.uniqueId,
                    it.name,
                    it.open,
                    dishes
                )
            )
        }
        return restaurantDataList
    }

    override fun getAddress(corpNamespace: String): List<Address> {
        val address = meiCanClient.address(corpNamespace)
        val addressList: MutableList<Address> = mutableListOf()
        address?.data?.addressList?.let { addressList.addAll(it) }
        return addressList
    }

    override fun refresh(username: String?, password: String?) {
        meiCanClient.refresh(username, password)
    }

    override fun getAccount(): Account {
        return meiCanClient.account()
    }

    override fun getOrderDetail(orderUniqueId: String): OrderDetail {
        val orderDetail = meiCanClient.orderDetail(orderUniqueId)
        val calendarRestaurant = orderDetail.calendarRestaurants[0]
        val dishItem = calendarRestaurant.dishItemList[0]
        return OrderDetail(
            orderDetail.uniqueId,
            orderDetail.corpOrderId,
            orderDetail.pickUpMessage,
            orderDetail.postbox,
            orderDetail.progressList,
            calendarRestaurant.uniqueId,
            calendarRestaurant.restaurantName,
            dishItem.dish.toDish(),
            dishItem.count,
            orderDetail.readyToDelete
        )
    }

    override fun order(
        userTabUniqueId: String,
        addressUniqueId: String,
        targetDateTime: LocalDateTime,
        dishId: Long
    ): String {
        return meiCanClient.order(userTabUniqueId, addressUniqueId, targetDateTime, dishId)
    }

    override fun cancelOrder(orderUniqueId: String) {
        meiCanClient.cancelOrder(orderUniqueId)
    }
}
