package com.github.motui.meican.api.model;



/**
 * @author motui
 * @date 2021-01-17
 */

public class Restaurant {
  private String uniqueId;
  private String name;
  private String tel;
  private String rating;
  private Integer minimumOrder;
  private Double latitude;
  private Double longitude;
  private String warning;
  private String openingTime;
  private Boolean onlinePayment;
  private Boolean open;
  private Integer availableDishCount;
  private Integer dishLimit;
  private Integer restaurantStatus;
  private Boolean remarkEnabled;

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public Integer getMinimumOrder() {
    return minimumOrder;
  }

  public void setMinimumOrder(Integer minimumOrder) {
    this.minimumOrder = minimumOrder;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public String getWarning() {
    return warning;
  }

  public void setWarning(String warning) {
    this.warning = warning;
  }

  public String getOpeningTime() {
    return openingTime;
  }

  public void setOpeningTime(String openingTime) {
    this.openingTime = openingTime;
  }

  public Boolean getOnlinePayment() {
    return onlinePayment;
  }

  public void setOnlinePayment(Boolean onlinePayment) {
    this.onlinePayment = onlinePayment;
  }

  public Boolean getOpen() {
    return open;
  }

  public void setOpen(Boolean open) {
    this.open = open;
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

  public Integer getRestaurantStatus() {
    return restaurantStatus;
  }

  public void setRestaurantStatus(Integer restaurantStatus) {
    this.restaurantStatus = restaurantStatus;
  }

  public Boolean getRemarkEnabled() {
    return remarkEnabled;
  }

  public void setRemarkEnabled(Boolean remarkEnabled) {
    this.remarkEnabled = remarkEnabled;
  }
}
