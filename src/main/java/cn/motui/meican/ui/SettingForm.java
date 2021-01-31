package cn.motui.meican.ui;

import cn.motui.meican.ui.settings.NoticeCycle;
import cn.motui.meican.ui.settings.NoticeTime;
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
  protected JBCheckBox noticeAmCheckBox;
  protected JBCheckBox noticePmCheckBox;
  protected ComboBox<NoticeTime> timeComboBox;
  protected ComboBox<NoticeCycle> cycleComboBox;

}
