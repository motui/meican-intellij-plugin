package cn.motui.meican.notification;

import cn.motui.meican.enums.CycleEnum;
import cn.motui.meican.enums.TimeEnum;
import cn.motui.meican.job.NotificationScheduler;
import cn.motui.meican.setting.Setting;
import cn.motui.meican.setting.SettingService;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author it.motui
 * @date 2021-01-24
 */
public class NotificationStartupActivity implements StartupActivity {
  @Override
  public void runActivity(@NotNull Project project) {
    if (ApplicationManager.getApplication().isUnitTestMode() || project.isDisposed()) {
      return;
    }
    SettingService settingService = ServiceManager.getService(SettingService.class);
    Setting state = settingService.getState();
    if (Objects.nonNull(state) && state.getVerification()) {
      if (state.getAmNotice()) {
        CycleEnum cycle = state.getCycle();
        TimeEnum notifyBeforeClosing = state.getNotifyBeforeClosing();
        NotificationScheduler.amScheduler(cycle, notifyBeforeClosing);
      }
      if (state.getPmNotice()) {
        CycleEnum cycle = state.getCycle();
        TimeEnum notifyBeforeClosing = state.getNotifyBeforeClosing();
        NotificationScheduler.pmScheduler(cycle, notifyBeforeClosing);
      }
    }
  }
}
