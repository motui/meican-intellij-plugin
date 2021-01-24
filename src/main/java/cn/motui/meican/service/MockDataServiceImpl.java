package cn.motui.meican.service;

import cn.motui.meican.model.api.Account;
import cn.motui.meican.model.api.Address;
import cn.motui.meican.model.api.Dish;
import cn.motui.meican.model.api.FinalValue;
import cn.motui.meican.model.api.Postbox;
import cn.motui.meican.model.api.vo.Progress;
import cn.motui.meican.model.ui.DateData;
import cn.motui.meican.model.ui.OrderDetail;
import cn.motui.meican.model.ui.RestaurantData;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * mock数据服务
 *
 * @author it.motui
 * @date 2021-01-30
 */
public class MockDataServiceImpl implements DataService {

  @Override
  public List<DateData> getDateData(LocalDateTime targetDateTime) {
    List<DateData> dateDataList = Lists.newArrayList();
    dateDataList.add(buildAm());
    dateDataList.add(buildPmOrder());
    dateDataList.add(buildPm());
    return dateDataList;
  }

  private DateData buildAm() {
    return new DateData(
        "午餐", "CLOSED", "超出订单时间", System.currentTimeMillis(),
        "user-000000", "crop-0000000", "corp-namespace", null
    );
  }

  private DateData buildPmOrder() {
    return new DateData(
        "晚餐-未定", "AVAILABLE", "", System.currentTimeMillis(),
        "user-000000", "crop-0000000", "corp-namespace", null
    );
  }

  private DateData buildPm() {
    return new DateData(
        "晚餐-已定", "ORDER", "", System.currentTimeMillis(),
        "user-000000", "crop-0000000", "corp-namespace",
        "order-000000"
    );
  }

  @Override
  public List<RestaurantData> getRestaurantData(String userTabUniqueId, LocalDateTime targetDateTime) {
    return Lists.newArrayList(
        this.buildRestaurantData(),
        this.buildRestaurantData(),
        this.buildRestaurantData(),
        this.buildRestaurantData(),
        this.buildRestaurantData()
    );
  }

  private RestaurantData buildRestaurantData() {
    long id = RandomUtils.nextLong(10000, 99999);
    return new RestaurantData("" + id, "餐厅-" + id, true,
        Arrays.asList(buildDish(), buildDish(), buildDish(), buildDish(), buildDish()));
  }

  private Dish buildDish() {
    Dish dish = new Dish();
    long id = RandomUtils.nextLong(10000, 99999);
    dish.setId(id);
    dish.setName("菜品(描述-" + id + ")");
    return dish;
  }


  @Override
  public List<Address> getAddress(String corpNamespace) {
    return Lists.newArrayList(
        new Address(new FinalValue("1", "1903"), "地址1"),
        new Address(new FinalValue("2", "1904"), "地址2"),
        new Address(new FinalValue("3", "1905"), "地址3"),
        new Address(new FinalValue("4", "1906"), "地址4")
    );
  }

  @Override
  public Account getAccount() {
    return new Account("", "test", "test@test.com", "");
  }

  @Override
  public OrderDetail getOrderDetail(String orderUniqueId) {
    return new OrderDetail(
        orderUniqueId,
        "" + RandomUtils.nextLong(10000, 99999),
        new Postbox("G112", System.currentTimeMillis()),
        Arrays.asList(
            new Progress(System.currentTimeMillis(), "下单下单下单"),
            new Progress(System.currentTimeMillis(), "收单收单收单收单收单"),
            new Progress(System.currentTimeMillis(), "送达送达送达送达送达送达")
        ),
        "" + RandomUtils.nextLong(10000, 99999),
        "餐厅-" + RandomUtils.nextLong(10000, 99999),
        buildDish(),
        1
    );
  }

  @Override
  public String order(String userTabUniqueId, String addressUniqueId, LocalDateTime targetDateTime, Long dishId) {
    return "" + RandomUtils.nextLong(10000, 99999);
  }
}
