package com.github.motui.meican.api.model;

import java.util.List;

/**
 * @author motui
 * @date 2021-01-17
 */
public class MeiCanPanelData {
  private List<DateData> dateDataList;

  public MeiCanPanelData(List<DateData> dateDataList) {
    this.dateDataList = dateDataList;
  }

  public List<DateData> getDateDataList() {
    return dateDataList;
  }

  public void setDateDataList(List<DateData> dateDataList) {
    this.dateDataList = dateDataList;
  }

  public static class DateData {
    private String title;
    private Boolean open;
    private List<RestaurantData> restaurants;
    private List<Address> addressList;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public Boolean getOpen() {
      return open;
    }

    public void setOpen(Boolean open) {
      this.open = open;
    }

    public List<RestaurantData> getRestaurants() {
      return restaurants;
    }

    public void setRestaurants(List<RestaurantData> restaurants) {
      this.restaurants = restaurants;
    }

    public List<Address> getAddressList() {
      return addressList;
    }

    public void setAddressList(List<Address> addressList) {
      this.addressList = addressList;
    }
  }


  public static class RestaurantData {
    private String uniqueId;
    private String name;
    private Boolean open;
    private List<Dish> dishList;

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

    public Boolean getOpen() {
      return open;
    }

    public void setOpen(Boolean open) {
      this.open = open;
    }

    public List<Dish> getDishList() {
      return dishList;
    }

    public void setDishList(List<Dish> dishList) {
      this.dishList = dishList;
    }

    @Override
    public String toString() {
      return name;
    }
  }
}
