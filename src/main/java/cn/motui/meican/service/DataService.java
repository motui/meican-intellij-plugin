package cn.motui.meican.service;

import cn.motui.meican.model.api.Account;
import cn.motui.meican.model.api.Address;
import cn.motui.meican.model.ui.DateData;
import cn.motui.meican.model.ui.OrderDetail;
import cn.motui.meican.model.ui.RestaurantData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据服务
 *
 * @author it.motui
 * @date 2021-01-30
 */
public interface DataService {

  /**
   * 时间数据
   *
   * @param targetDateTime 目标时间
   * @return DateData
   */
  List<DateData> getDateData(LocalDateTime targetDateTime);

  /**
   * 餐厅数据
   *
   * @param userTabUniqueId userTabUniqueId
   * @param targetDateTime  目标时间
   * @return RestaurantData
   */
  List<RestaurantData> getRestaurantData(String userTabUniqueId, LocalDateTime targetDateTime);

  /**
   * 地址信息
   *
   * @param corpNamespace corpNamespace
   * @return Address
   */
  List<Address> getAddress(String corpNamespace);

  /**
   * 当前用户信息
   *
   * @return Account
   */
  Account getAccount();

  /**
   * 订单信息
   *
   * @param orderUniqueId orderUniqueId
   * @return OrderDetail
   */
  OrderDetail getOrderDetail(String orderUniqueId);

  /**
   * 下单
   *
   * @param userTabUniqueId userTabUniqueId
   * @param addressUniqueId addressUniqueId
   * @param targetDateTime  目标时间
   * @param dishId          菜品ID
   * @return 订单ID
   */
  String order(String userTabUniqueId, String addressUniqueId, LocalDateTime targetDateTime, Long dishId);

}
