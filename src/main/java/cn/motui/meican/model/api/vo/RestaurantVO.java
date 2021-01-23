package cn.motui.meican.model.api.vo;

import cn.motui.meican.model.api.Restaurant;


import java.util.List;

/**
 * @author motui
 * @date 2021-01-17
 */

public class RestaurantVO {
  private Boolean noMore;
  private String targetTime;
  private List<Restaurant> restaurantList;

  public Boolean getNoMore() {
    return noMore;
  }

  public void setNoMore(Boolean noMore) {
    this.noMore = noMore;
  }

  public String getTargetTime() {
    return targetTime;
  }

  public void setTargetTime(String targetTime) {
    this.targetTime = targetTime;
  }

  public List<Restaurant> getRestaurantList() {
    return restaurantList;
  }

  public void setRestaurantList(List<Restaurant> restaurantList) {
    this.restaurantList = restaurantList;
  }
}
