package cn.motui.meican.model.ui;

import cn.motui.meican.model.api.Dish;

import java.util.List;

/**
 * 餐厅信息
 *
 * @author it.motui
 * @date 2021-01-23
 */
public class RestaurantData {
  private final String uniqueId;
  private final String name;
  private final Boolean open;
  private final List<Dish> dishes;

  public RestaurantData(String uniqueId, String name, Boolean open, List<Dish> dishes) {
    this.uniqueId = uniqueId;
    this.name = name;
    this.open = open;
    this.dishes = dishes;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public String getName() {
    return name;
  }

  public Boolean getOpen() {
    return open;
  }

  public List<Dish> getDishes() {
    return dishes;
  }

  @Override
  public String toString() {
    return name;
  }
}
