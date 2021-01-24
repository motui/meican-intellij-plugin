package cn.motui.meican.model.api.vo;

import cn.motui.meican.model.api.Dish;


import java.util.List;

/**
 * @author it.motui
 * @date 2021-01-17
 */

public class RestaurantDishVO {
  private Long restaurantId;
  private String uniqueId;
  private List<Dish> dishList;
  private Boolean showPrice;
  private String targetTime;
  private String tel;
  private Integer rating;
  private Long restaurantStatus;
  private String othersRegularDishIdListSource;
  private Boolean open;
  private Boolean onlinePayment;

  public Long getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(Long restaurantId) {
    this.restaurantId = restaurantId;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public List<Dish> getDishList() {
    return dishList;
  }

  public void setDishList(List<Dish> dishList) {
    this.dishList = dishList;
  }

  public Boolean getShowPrice() {
    return showPrice;
  }

  public void setShowPrice(Boolean showPrice) {
    this.showPrice = showPrice;
  }

  public String getTargetTime() {
    return targetTime;
  }

  public void setTargetTime(String targetTime) {
    this.targetTime = targetTime;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  public Long getRestaurantStatus() {
    return restaurantStatus;
  }

  public void setRestaurantStatus(Long restaurantStatus) {
    this.restaurantStatus = restaurantStatus;
  }

  public String getOthersRegularDishIdListSource() {
    return othersRegularDishIdListSource;
  }

  public void setOthersRegularDishIdListSource(String othersRegularDishIdListSource) {
    this.othersRegularDishIdListSource = othersRegularDishIdListSource;
  }

  public Boolean getOpen() {
    return open;
  }

  public void setOpen(Boolean open) {
    this.open = open;
  }

  public Boolean getOnlinePayment() {
    return onlinePayment;
  }

  public void setOnlinePayment(Boolean onlinePayment) {
    this.onlinePayment = onlinePayment;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Integer getAvailableDishCount() {
    return availableDishCount;
  }

  public void setAvailableDishCount(Integer availableDishCount) {
    this.availableDishCount = availableDishCount;
  }

  public Integer getDishLimit() {
    return dishLimit;
  }

  public void setDishLimit(Integer dishLimit) {
    this.dishLimit = dishLimit;
  }

  private String name;
  private Double longitude;
  private Double latitude;
  private Integer availableDishCount;
  private Integer dishLimit;
}
