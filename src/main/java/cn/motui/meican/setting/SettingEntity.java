package cn.motui.meican.setting;

import cn.motui.meican.MeiCanClient;
import cn.motui.meican.enums.TimeEnum;
import cn.motui.meican.job.NotificationScheduler;
import cn.motui.meican.model.api.Account;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * 设置配置
 *
 * @author it.motui
 * @date 2021-01-16
 */
public class SettingEntity implements Configurable {
  private SettingForm form = null;
  private final Setting setting;

  public SettingEntity() {
    SettingService settingService = ServiceManager.getService(SettingService.class);
    setting = settingService.getState();
  }

  @Override
  public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
    return "Mei Can";
  }

  @Override
  public @Nullable String getHelpTopic() {
    return null;
  }

  @Override
  public @Nullable JComponent createComponent() {
    form = new SettingForm();
    reset();
    return form.getRoot();
  }

  @Override
  public boolean isModified() {
    if (Objects.isNull(form) || Objects.isNull(setting)) {
      return false;
    }
    return !setting.getUsername().equals(form.getUsername())
        || !setting.getPassword().equals(form.getPassword())
        || !setting.getAmNotice().equals(form.getAmNotice())
        || !setting.getPmNotice().equals(form.getPmNotice())
        || !setting.getCycle().equals(form.getCycle())
        || !setting.getNotifyBeforeClosing().equals(form.getNotifyBeforeClosing())
        ;
  }

  @Override
  public void apply() throws ConfigurationException {
    if (Objects.isNull(form)) {
      return;
    }
    boolean notifyBeforeClosing = !setting.getNotifyBeforeClosing().equals(form.getNotifyBeforeClosing());
    boolean am = (!setting.getAmNotice().equals(form.getAmNotice()) || notifyBeforeClosing) && form.getAmNotice();
    boolean pm = (!setting.getPmNotice().equals(form.getPmNotice()) || notifyBeforeClosing) && form.getPmNotice();
    setting.setUsername(form.getUsername());
    setting.setPassword(form.getPassword());
    setting.setAmNotice(form.getAmNotice());
    setting.setPmNotice(form.getPmNotice());
    setting.setCycle(form.getCycle());
    setting.setNotifyBeforeClosing(form.getNotifyBeforeClosing());
    if (am) {
      NotificationScheduler.amScheduler(setting.getCycle(), setting.getNotifyBeforeClosing());
    }
    if (pm) {
      NotificationScheduler.pmScheduler(setting.getCycle(), setting.getNotifyBeforeClosing());
    }
    try {
      //刷新MeiCanClient中的用户名密码
      MeiCanClient.instance().refresh(setting.getUsername(), setting.getPassword());
      Account account = MeiCanClient.instance().account();
      if (Objects.nonNull(account)) {
        setting.setVerification(true);
      }
    } catch (Exception ignore) {
    }
  }

  @Override
  public void reset() {
    form.setUsername(setting.getUsername());
    form.setPassword(setting.getPassword());
    form.setAmNotice(setting.getAmNotice());
    form.setPmNotice(setting.getPmNotice());
    form.setCycle(setting.getCycle());
    form.setNotifyBeforeClosing(setting.getNotifyBeforeClosing());
  }

  @Override
  public void disposeUIResources() {
    form = null;
  }
}
