package cn.motui.meican.ui;

import cn.motui.meican.ui.settings.Automatic;
import cn.motui.meican.ui.settings.Cycle;
import cn.motui.meican.ui.settings.NoticeTime;
import cn.motui.meican.ui.settings.Tab;
import cn.motui.meican.ui.settings.TabShow;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;

/**
 * @author it.motui
 * @date 2021-01-16
 */
public class SettingForm {
  public JPanel root;
  protected JPanel accountPanel;
  protected JPanel noticePanel;

  protected JTextField usernameField;
  protected JPasswordField passwordField;
  protected JLabel testResultLabel;
  protected JButton testButton;
  protected JBLabel usernameLabel;
  protected JBLabel passwordLabel;

  protected JBLabel noticeCycleLabel;
  protected JBLabel noticeTimeLabel;
  protected ComboBox<NoticeTime> timeComboBox;
  protected ComboBox<Cycle> cycleComboBox;
  protected JPanel automaticPanel;
  protected JBLabel automaticLabel;
  protected ComboBox<Automatic> automaticComboBox;
  protected JPanel otherPanel;
  protected JBLabel tabShowLabel;
  protected ComboBox<TabShow> tabShowComboBox;
  protected JBLabel orderCycleLabel;
  protected ComboBox<Cycle> orderCycleComboBox;
  protected JBLabel noticeTabLabel;
  protected ComboBox<Tab> noticeTabComboBox;

}
