package cn.motui.meican.model.api.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import cn.motui.meican.model.api.Postbox;

import java.util.List;

/**
 * @author motui
 * @date 2021-01-23
 */
public class OrderDetailVO {
  private String corpOrderId;
  private String pickUpMessage;
  private Postbox postbox;
  private List<Progress> progressList;
  @JsonProperty("restaurantItemList")
  private List<CalendarRestaurantVO> calendarRestaurants;

  public String getCorpOrderId() {
    return corpOrderId;
  }

  public void setCorpOrderId(String corpOrderId) {
    this.corpOrderId = corpOrderId;
  }

  public String getPickUpMessage() {
    return pickUpMessage;
  }

  public void setPickUpMessage(String pickUpMessage) {
    this.pickUpMessage = pickUpMessage;
  }

  public Postbox getPostbox() {
    return postbox;
  }

  public void setPostbox(Postbox postbox) {
    this.postbox = postbox;
  }

  public List<Progress> getProgressList() {
    return progressList;
  }

  public void setProgressList(List<Progress> progressList) {
    this.progressList = progressList;
  }

  public List<CalendarRestaurantVO> getCalendarRestaurants() {
    return calendarRestaurants;
  }

  public void setCalendarRestaurants(List<CalendarRestaurantVO> calendarRestaurants) {
    this.calendarRestaurants = calendarRestaurants;
  }
}
