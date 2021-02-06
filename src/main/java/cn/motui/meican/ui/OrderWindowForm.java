package cn.motui.meican.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.components.labels.LinkLabel;

import javax.swing.*;

/**
 * OrderWindowForm
 *
 * @author it.motui
 * @date 2021-02-06
 */
public class OrderWindowForm extends SimpleToolWindowPanel {
  protected JPanel root;
  protected LinkLabel<String> openSettingLinkLabel;
  protected JButton preButton;
  protected JButton nextButton;
  protected JBLabel timeLabel;
  protected JBTabbedPane tabbedPane;
  protected JPanel topPanel;

  public OrderWindowForm() {
    super(true, true);
    setContent(root);
  }

  private void createUIComponents() {
    openSettingLinkLabel = new LinkLabel<>("", AllIcons.General.Settings);
  }
}
