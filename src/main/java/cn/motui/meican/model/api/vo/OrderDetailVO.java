package cn.motui.meican.model.api.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import cn.motui.meican.model.api.Postbox;

import java.util.List;

/**
 * @author it.motui
 * @date 2021-01-23
 */
public class OrderDetailVO {
  private String corpOrderId;
  private String pickUpMessage;
  private Postbox postbox;
  private List<Progress> progressList;
  @JsonProperty("restaurantItemList")
  private List<CalendarRestaurantVO> calendarRestaurants;
  private Boolean readyToDelete;
  private String uniqueId;

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public Boolean getReadyToDelete() {
    return readyToDelete;
  }

  public void setReadyToDelete(Boolean readyToDelete) {
    this.readyToDelete = readyToDelete;
  }

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
