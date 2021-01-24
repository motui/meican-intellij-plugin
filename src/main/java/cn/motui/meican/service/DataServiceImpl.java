package cn.motui.meican.service;

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
 * 数据服务
 *
 * @author it.motui
 * @date 2021-01-30
 */
public class DataServiceImpl implements DataService {

  @Override
  public List<DateData> getDateData(LocalDateTime targetDateTime) {
    CalendarVO calendar = MeiCanClient.instance().calendar(targetDateTime, targetDateTime);
    List<DateData> dateDataList = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(calendar.getDateList())) {
      List<CalendarVO.CalendarItem> calendarItemList = calendar.getDateList().get(0).getCalendarItemList();
      if (CollectionUtils.isNotEmpty(calendarItemList)) {
        dateDataList = calendarItemList.stream()
            .map(CalendarVO.CalendarItem::toDateData)
            .collect(Collectors.toList());
      }
    }
    return dateDataList;
  }

  @Override
  public List<RestaurantData> getRestaurantData(String userTabUniqueId, LocalDateTime targetDateTime) {
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

  @Override
  public List<Address> getAddress(String corpNamespace) {
    CorpAddressVO corpAddress = MeiCanClient.instance().address(corpNamespace);
    List<Address> addressList = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(corpAddress.getData().getAddressList())) {
      addressList = corpAddress.getData().getAddressList();
    }
    return addressList;
  }

  @Override
  public Account getAccount() {
    return MeiCanClient.instance().account();
  }

  @Override
  public OrderDetail getOrderDetail(String orderUniqueId) {
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

  @Override
  public String order(String userTabUniqueId, String addressUniqueId, LocalDateTime targetDateTime, Long dishId) {
    return MeiCanClient.instance().order(userTabUniqueId, addressUniqueId, targetDateTime, dishId);
  }
}
