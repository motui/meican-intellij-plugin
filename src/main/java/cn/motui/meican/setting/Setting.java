package cn.motui.meican.setting;

import cn.motui.meican.enums.CycleEnum;
import cn.motui.meican.enums.TimeEnum;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.util.xmlb.annotations.Transient;
import org.apache.commons.lang3.StringUtils;

/**
 * 设置项
 *
 * @author it.motui
 * @date 2021-01-16
 */
public class Setting {
  private String username = "";
  private Boolean amNotice = false;
  private Boolean pmNotice = false;
  /**
   * 关闭之前多久通知 {@link TimeEnum}
   */
  private String notifyBeforeClosing = TimeEnum.M30.name();
  private String cycle = CycleEnum.MONDAY_TO_FRIDAY.name();
  private Boolean verification = false;

  @Transient
  public boolean isRequest() {
    return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(this.getPassword());
  }

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

  public Boolean getAmNotice() {
    return amNotice;
  }

  public void setAmNotice(Boolean amNotice) {
    this.amNotice = amNotice;
  }

  public Boolean getPmNotice() {
    return pmNotice;
  }

  public void setPmNotice(Boolean pmNotice) {
    this.pmNotice = pmNotice;
  }

  public TimeEnum getNotifyBeforeClosing() {
    return TimeEnum.valueOf(this.notifyBeforeClosing);
  }

  public void setNotifyBeforeClosing(TimeEnum noticeTimeCloseBefore) {
    this.notifyBeforeClosing = noticeTimeCloseBefore.name();
  }

  public void setNotifyBeforeClosing(String notifyBeforeClosing) {
    this.notifyBeforeClosing = notifyBeforeClosing;
  }

  public CycleEnum getCycle() {
    return CycleEnum.valueOf(cycle);
  }

  public void setCycle(CycleEnum cycle) {
    this.cycle = cycle.name();
  }

  public Boolean getVerification() {
    return verification;
  }

  public void setVerification(Boolean verification) {
    this.verification = verification;
  }

  private CredentialAttributes createCredentialAttributes(String key) {
    return new CredentialAttributes(CredentialAttributesKt.generateServiceName("meican-intellij-plugin", key));
  }
}
