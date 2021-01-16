package com.github.motui.meican.api.model.vo;



import java.util.List;

/**
 * @author motui
 * @date 2021-01-17
 */

public class CalendarRestaurantVO {
  private String uniqueId;
  private List<DishItem> dishItemList;

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public List<DishItem> getDishItemList() {
    return dishItemList;
  }

  public void setDishItemList(List<DishItem> dishItemList) {
    this.dishItemList = dishItemList;
  }


  public static class DishItem {
    private CalendarDishVO dish;
    private Integer count;

    public CalendarDishVO getDish() {
      return dish;
    }

    public void setDish(CalendarDishVO dish) {
      this.dish = dish;
    }

    public Integer getCount() {
      return count;
    }

    public void setCount(Integer count) {
      this.count = count;
    }
  }
}
