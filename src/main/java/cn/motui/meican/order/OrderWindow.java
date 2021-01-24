package cn.motui.meican.order;

import cn.motui.meican.setting.Setting;
import cn.motui.meican.setting.SettingService;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

/**
 * @author it.motui
 * @date 2021-01-16
 */
public class OrderWindow {
  private final ToolWindow toolWindow;

  public OrderWindow(ToolWindow toolWindow) {
    this.toolWindow = toolWindow;
    ToolWindowEx toolWindowEx = (ToolWindowEx) toolWindow;
    if (!ApplicationManager.getApplication().isUnitTestMode()) {
      toolWindowEx.setTitleActions(new RefreshAction());
    }
  }

  public class RefreshAction extends DumbAwareAction {
    public RefreshAction() {
      super("刷新", null, AllIcons.Actions.Refresh);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
      renderUi();
    }
  }

  public void renderUi() {
    ContentManager contentManager = this.toolWindow.getContentManager();
    contentManager.removeAllContents(true);
    Setting setting = ServiceManager.getService(SettingService.class).getState();
    JPanel contentPanel = null;
    if (Objects.nonNull(setting) && !setting.isRequest()) {
      contentPanel = new EmptyPanel("请设置用户名密码").getRoot();
    } else {
      contentPanel = new OrderPanel().getRoot();
    }
    Content content = contentManager.getFactory().createContent(contentPanel, null, false);
    contentManager.addContent(content);
  }
}
