package cn.motui.meican.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 设置持久化
 *
 * @author it.motui
 * @date 2021-01-16
 */
@State(name = "美餐", storages = {@Storage(value = "meican.xml")})
public class SettingService implements PersistentStateComponent<Setting> {
  private final Setting setting = new Setting();

  @Override
  public @Nullable Setting getState() {
    return setting;
  }

  @Override
  public void loadState(@NotNull Setting setting) {
    XmlSerializerUtil.copyBean(setting, this.setting);
  }

}
