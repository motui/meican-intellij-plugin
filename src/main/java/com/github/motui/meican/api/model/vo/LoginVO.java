package com.github.motui.meican.api.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.StringUtils;

/**
 * @author motui
 * @date 2021-01-16
 */
public class LoginVO {
  @JsonProperty("error_description")
  private String errorDescription;
  private String error;
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("expires_in")
  private Integer expiresIn;
  @JsonProperty("need_reset_password")
  private Boolean needResetPassword;

  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
  }

  public Boolean getNeedResetPassword() {
    return needResetPassword;
  }

  public void setNeedResetPassword(Boolean needResetPassword) {
    this.needResetPassword = needResetPassword;
  }

  public boolean isSuccess() {
    return StringUtils.isEmpty(error) && StringUtils.isEmpty(errorDescription);
  }

}
