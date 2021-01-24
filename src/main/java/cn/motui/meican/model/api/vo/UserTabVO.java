package cn.motui.meican.model.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import cn.motui.meican.model.api.Corp;


/**
 * @author it.motui
 * @date 2021-01-17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTabVO {
  private Corp corp;
  private String name;
  private String uniqueId;
  private Long lastUsedTime;
  private String latitude;
  private String longitude;

  public Corp getCorp() {
    return corp;
  }

  public void setCorp(Corp corp) {
    this.corp = corp;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public Long getLastUsedTime() {
    return lastUsedTime;
  }

  public void setLastUsedTime(Long lastUsedTime) {
    this.lastUsedTime = lastUsedTime;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }
}
