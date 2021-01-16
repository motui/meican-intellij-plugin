package com.github.motui.meican.setting;

import com.github.motui.meican.api.MeiCanClient;
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
 * @author motui
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
    return "美餐";
  }

  @Override
  public @Nullable String getHelpTopic() {
    return "美餐配置";
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
        || setting.getCycle() != form.getCycle()
        || setting.getTipsHour() != form.getTipsHour()
        || setting.getTipsMinute() != form.getTipsMinute();
  }

  @Override
  public void apply() throws ConfigurationException {
    if (Objects.isNull(form)) {
      return;
    }
    setting.setUsername(form.getUsername());
    setting.setPassword(form.getPassword());
    setting.setCycle(form.getCycle());
    setting.setTipsHour(form.getTipsHour());
    setting.setTipsMinute(form.getTipsMinute());
    //刷新MeiCanClient中的用户名密码
    MeiCanClient.instance().refresh(setting.getUsername(), setting.getPassword());
  }

  @Override
  public void reset() {
    form.setUsername(setting.getUsername());
    form.setPassword(setting.getPassword());
    form.setCycle(setting.getCycle());
    form.setTipsHour(setting.getTipsHour());
    form.setTipsMinute(setting.getTipsMinute());
  }

  @Override
  public void disposeUIResources() {
    form = null;
  }
}
