package cn.motui.meican.model.api.vo;

/**
 * @author motui
 * @date 2021-01-18
 */
public class AddOrderVO {
  private String userTableUniqueId;
  private String addressUniqueId;
  private Long targetTime;
  private Long dishId;

  public AddOrderVO(String userTableUniqueId, String addressUniqueId, Long targetTime, Long dishId) {
    this.userTableUniqueId = userTableUniqueId;
    this.addressUniqueId = addressUniqueId;
    this.targetTime = targetTime;
    this.dishId = dishId;
  }

  public String getUserTableUniqueId() {
    return userTableUniqueId;
  }

  public void setUserTableUniqueId(String userTableUniqueId) {
    this.userTableUniqueId = userTableUniqueId;
  }

  public String getAddressUniqueId() {
    return addressUniqueId;
  }

  public void setAddressUniqueId(String addressUniqueId) {
    this.addressUniqueId = addressUniqueId;
  }

  public Long getTargetTime() {
    return targetTime;
  }

  public void setTargetTime(Long targetTime) {
    this.targetTime = targetTime;
  }

  public Long getDishId() {
    return dishId;
  }

  public void setDishId(Long dishId) {
    this.dishId = dishId;
  }
}
