package com.github.motui.meican.api.model;



/**
 * @author motui
 * @date 2021-01-17
 */

public class OpeningTime {
  private String uniqueId;
  private String name;
  /**
   * 00:00
   */
  private String openTime;
  /**
   * 10:00
   */
  private String closeTime;
  /**
   * 09:30
   */
  private String defaultAlarmTime;
  /**
   * 11:30
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
