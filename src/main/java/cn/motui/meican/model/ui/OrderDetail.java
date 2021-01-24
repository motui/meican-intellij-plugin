package cn.motui.meican.model.ui;

import cn.motui.meican.model.api.Dish;
import cn.motui.meican.model.api.vo.Progress;
import cn.motui.meican.model.api.Postbox;

import java.util.List;

/**
 * 订单详情
 *
 * @author it.motui
 * @date 2021-01-23
 */
public class OrderDetail {
  private final String corpOrderId;
  private final String pickUpMessage;
  private final Postbox postbox;
  private final List<Progress> progressList;
  private final String restaurantUniqueId;
  private final String restaurantName;
  private final Dish dish;
  /**
   * dish 份数
   */
  private final Integer dishCount;

  public OrderDetail(String corpOrderId, String pickUpMessage, Postbox postbox, List<Progress> progressList,
                     String restaurantUniqueId, String restaurantName, Dish dish, Integer dishCount) {
    this.corpOrderId = corpOrderId;
    this.pickUpMessage = pickUpMessage;
    this.postbox = postbox;
    this.progressList = progressList;
    this.restaurantUniqueId = restaurantUniqueId;
    this.restaurantName = restaurantName;
    this.dish = dish;
    this.dishCount = dishCount;
  }

  public String getCorpOrderId() {
    return corpOrderId;
  }

  public String getPickUpMessage() {
    return pickUpMessage;
  }

  public Postbox getPostbox() {
    return postbox;
  }

  public List<Progress> getProgressList() {
    return progressList;
  }

  public String getRestaurantUniqueId() {
    return restaurantUniqueId;
  }

  public String getRestaurantName() {
    return restaurantName;
  }

  public Dish getDish() {
    return dish;
  }

  public Integer getDishCount() {
    return dishCount;
  }
}
