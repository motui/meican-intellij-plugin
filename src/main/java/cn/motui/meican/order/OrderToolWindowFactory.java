package cn.motui.meican.order;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author motui
 * @date 2021-01-16
 */
public class OrderToolWindowFactory implements ToolWindowFactory, DumbAware {
  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    OrderWindow orderWindow = new OrderWindow(toolWindow);
    orderWindow.renderUi();
  }
}
