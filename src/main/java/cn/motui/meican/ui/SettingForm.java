package cn.motui.meican.ui;

import cn.motui.meican.ui.settings.Cycle;
import cn.motui.meican.ui.settings.NoticeTime;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;

/**
 * @author it.motui
 * @date 2021-04-12
 */
public class SettingForm {
  public JPanel root;
  /**
   * 账户
   */
  protected JPanel accountPanel;
  protected JTextField usernameField;
  protected JPasswordField passwordField;
  protected JButton testButton;
  protected JBLabel testResultLabel;
  protected JBLabel usernameLabel;
  protected JBLabel passwordLabel;
  /**
   * tab
   */
  protected JPanel tabPanel;
  /**
   * 通知
   */
  protected JPanel noticePanel;
  protected JBLabel noticeCycleLabel;
  protected JBLabel noticeTimeLabel;
  protected ComboBox<NoticeTime> noticeTimeComboBox;
  protected ComboBox<Cycle> noticeCycleComboBox;

  /**
   * 自动点餐
   */
  protected JPanel automaticPanel;
  protected JBLabel orderCycleLabel;
  protected JBLabel orderTimeLabel;
  protected ComboBox<NoticeTime> orderTimeComboBox;
  protected ComboBox<Cycle> orderCycleComboBox;

}
