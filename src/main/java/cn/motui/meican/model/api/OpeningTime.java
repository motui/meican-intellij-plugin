package cn.motui.meican.model.api;



/**
 * @author it.motui
 * @date 2021-01-17
 */

public class OpeningTime {
  private String uniqueId;
  private String name;
  /**
   * 可订餐餐时间
   */
  private String openTime;
  /**
   * 关闭订单时间
   */
  private String closeTime;
  /**
   * 默认提醒时间
   */
  private String defaultAlarmTime;
  /**
   * 邮箱提醒时间
   */
  private String postboxOpenTime;

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

  public String getOpenTime() {
    return openTime;
  }

  public void setOpenTime(String openTime) {
    this.openTime = openTime;
  }

  public String getCloseTime() {
    return closeTime;
  }

  public void setCloseTime(String closeTime) {
    this.closeTime = closeTime;
  }

  public String getDefaultAlarmTime() {
    return defaultAlarmTime;
  }

  public void setDefaultAlarmTime(String defaultAlarmTime) {
    this.defaultAlarmTime = defaultAlarmTime;
  }

  public String getPostboxOpenTime() {
    return postboxOpenTime;
  }

  public void setPostboxOpenTime(String postboxOpenTime) {
    this.postboxOpenTime = postboxOpenTime;
  }
}
