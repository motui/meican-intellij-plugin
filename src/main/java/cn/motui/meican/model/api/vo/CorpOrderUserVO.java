package cn.motui.meican.model.api.vo;



import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author motui
 * @date 2021-01-17
 */

public class CorpOrderUserVO {
  @JsonProperty("isLegacyPay")
  private Boolean legacyPay;
  private String status;
  private String uniqueId;
  private List<CalendarRestaurantVO> restaurantItemList;

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public Boolean getLegacyPay() {
    return legacyPay;
  }

  public void setLegacyPay(Boolean legacyPay) {
    this.legacyPay = legacyPay;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<CalendarRestaurantVO> getRestaurantItemList() {
    return restaurantItemList;
  }

  public void setRestaurantItemList(List<CalendarRestaurantVO> restaurantItemList) {
    this.restaurantItemList = restaurantItemList;
  }
}
