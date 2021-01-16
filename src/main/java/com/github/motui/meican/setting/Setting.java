package com.github.motui.meican.setting;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.util.xmlb.annotations.Transient;
import org.apache.commons.lang3.StringUtils;

/**
 * 设置项
 *
 * @author motui
 * @date 2021-01-16
 */
public class Setting {
  private String username = "";
  private Integer cycle = 2;
  private Integer tipsHour = 13;
  private Integer tipsMinute = 0;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Transient
  public String getPassword() {
    if (StringUtils.isEmpty(this.username)) {
      return "";
    }
    CredentialAttributes credentialAttributes = createCredentialAttributes(this.username);
    return PasswordSafe.getInstance().getPassword(credentialAttributes);
  }

  public void setPassword(String password) {
    CredentialAttributes credentialAttributes = createCredentialAttributes(this.username);
    Credentials credentials = new Credentials(this.username, password);
    PasswordSafe.getInstance().set(credentialAttributes, credentials);
  }

  public Integer getCycle() {
    return cycle;
  }

  public void setCycle(Integer cycle) {
    this.cycle = cycle;
  }

  public Integer getTipsHour() {
    return tipsHour;
  }

  public void setTipsHour(Integer tipsHour) {
    this.tipsHour = tipsHour;
  }

  public Integer getTipsMinute() {
    return tipsMinute;
  }

  public void setTipsMinute(Integer tipsMinute) {
    this.tipsMinute = tipsMinute;
  }

  private CredentialAttributes createCredentialAttributes(String key) {
    return new CredentialAttributes(CredentialAttributesKt.generateServiceName("meican-intellij-plugin", key));
  }
}
