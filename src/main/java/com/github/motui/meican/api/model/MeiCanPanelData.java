package com.github.motui.meican.api.model;

import com.github.motui.meican.api.model.vo.CalendarDishVO;
import com.github.motui.meican.api.model.vo.UserTabVO;

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
    private String status;
    private String reason;
    private List<RestaurantData> restaurants;
    private List<Address> addressList;
    private Long targetTime;
    private UserTabVO userTab;

    /**
     * 已下单信息
     */
    private CalendarDishVO dish;

    public boolean isOrder() {
      return "AVAILABLE".equals(status) || "ORDER".equals(status);
    }

    public CalendarDishVO getDish() {
      return dish;
    }

    public void setDish(CalendarDishVO dish) {
      this.dish = dish;
    }

    public Long getTargetTime() {
      return targetTime;
    }

    public void setTargetTime(Long targetTime) {
      this.targetTime = targetTime;
    }

    public UserTabVO getUserTab() {
      return userTab;
    }

    public void setUserTab(UserTabVO userTab) {
      this.userTab = userTab;
    }

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

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getReason() {
      return reason;
    }

    public void setReason(String reason) {
      this.reason = reason;
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
