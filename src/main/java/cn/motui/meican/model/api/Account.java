package cn.motui.meican.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户信息
 *
 * @author it.motui
 * @date 2021-01-23
 */
public class Account {
  private String uniqueId;
  @JsonProperty("nameForEcard")
  private String username;
  @JsonProperty("nameForShow")
  private String email;
  private String userType;

  public Account() {
  }

  public Account(String uniqueId, String username, String email, String userType) {
    this.uniqueId = uniqueId;
    this.username = username;
    this.email = email;
    this.userType = userType;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getUserType() {
    return userType;
  }
}
