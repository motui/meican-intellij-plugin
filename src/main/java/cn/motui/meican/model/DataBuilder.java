package cn.motui.meican.model;

import cn.motui.meican.Constants;
import cn.motui.meican.MeiCanClient;
import cn.motui.meican.model.api.Account;
import cn.motui.meican.model.api.Address;
import cn.motui.meican.model.api.Dish;
import cn.motui.meican.model.api.vo.CalendarRestaurantVO;
import cn.motui.meican.model.api.vo.CalendarVO;
import cn.motui.meican.model.api.vo.CorpAddressVO;
import cn.motui.meican.model.api.vo.OrderDetailVO;
import cn.motui.meican.model.api.vo.RestaurantDishVO;
import cn.motui.meican.model.api.vo.RestaurantVO;
import cn.motui.meican.model.ui.DateData;
import cn.motui.meican.model.ui.OrderDetail;
import cn.motui.meican.model.ui.RestaurantData;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author it.motui
 * @date 2021-01-23
 */
public class DataBuilder {

  /**
   * 时间数据
   *
   * @param targetDateTime 目标时间
   * @return DateData
   */
  public static List<DateData> getDateData(LocalDateTime targetDateTime) {
    String time = targetDateTime.format(Constants.FORMATTER_RECORD_MEI_CAN);
    CalendarVO calendar = MeiCanClient.instance().calendar(time, time);
    List<DateData> dateDataList = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(calendar.getDateList())) {
      List<CalendarVO.CalendarItem> calendarItemList = calendar.getDateList().get(0).getCalendarItemList();
      if (CollectionUtils.isNotEmpty(calendarItemList)) {
        dateDataList = calendarItemList.stream().map(CalendarVO.CalendarItem::toDateData).collect(Collectors.toList());
      }
    }
    return dateDataList;
  }

  /**
   * 餐厅数据
   *
   * @param userTabUniqueId 时间TabId
   * @param targetDateTime  目标时间
   * @return RestaurantData
   */
  public static List<RestaurantData> getRestaurantData(String userTabUniqueId, LocalDateTime targetDateTime) {
    // 餐厅数据
    RestaurantVO restaurants = MeiCanClient.instance().restaurants(userTabUniqueId, targetDateTime);
    return restaurants.getRestaurantList().stream()
        .map(restaurant -> {
          // dish
          RestaurantDishVO restaurantDish = MeiCanClient.instance().restaurantDish(userTabUniqueId, targetDateTime,
              restaurant.getUniqueId());
          Set<Dish> dishSet = restaurantDish.getDishList().stream().filter(dish -> !dish.getSection())
              .collect(Collectors.toSet());
          return new RestaurantData(restaurant.getUniqueId(),
              restaurant.getName(),
              restaurant.getOpen(),
              Lists.newArrayList(dishSet)
          );
        }).collect(Collectors.toList());
  }

  /**
   * 地址
   *
   * @param corpNamespace corpNamespace
   * @return Address
   */
  public static List<Address> getAddress(String corpNamespace) {
    CorpAddressVO corpAddress = MeiCanClient.instance().address(corpNamespace);
    List<Address> addressList = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(corpAddress.getData().getAddressList())) {
      addressList = corpAddress.getData().getAddressList();
    }
    return addressList;
  }

  public static Account getAccount() {
    return MeiCanClient.instance().account();
  }

  public static OrderDetail getOrderDetail(String orderUniqueId) {
    OrderDetailVO orderDetailVO = MeiCanClient.instance().orderDetail(orderUniqueId);
    CalendarRestaurantVO calendarRestaurantVO = orderDetailVO.getCalendarRestaurants().get(0);
    CalendarRestaurantVO.DishItem dishItem = calendarRestaurantVO.getDishItemList().get(0);
    return new OrderDetail(
        orderDetailVO.getCorpOrderId(),
        orderDetailVO.getPickUpMessage(),
        orderDetailVO.getPostbox(),
        orderDetailVO.getProgressList(),
        calendarRestaurantVO.getUniqueId(),
        calendarRestaurantVO.getRestaurantName(),
        dishItem.getDish().toDish(),
        dishItem.getCount()
    );
  }
}
