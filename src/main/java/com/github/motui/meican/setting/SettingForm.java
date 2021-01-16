package com.github.motui.meican.setting;

import com.github.motui.meican.api.MeiCanClient;
import com.github.motui.meican.exception.HttpRequestException;
import com.github.motui.meican.exception.MeiCanLoginException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author motui
 * @date 2021-01-16
 */
public class SettingForm {
  private JPanel rootPanel;
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JButton testButton;
  private JComboBox<String> cycleComboBox;
  private JComboBox<Integer> tipsHourComboBox;
  private JComboBox<Integer> tipsMinuteComboBox;
  private JLabel testResultText;

  public SettingForm() {
    this.cycleComboBox.setModel(new DefaultComboBoxModel<String>(new String[]{"永不", "每天", "周一~周五", "周一~周六"}));
    this.tipsHourComboBox.setModel(new DefaultComboBoxModel<Integer>(
        new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23}));
    this.tipsMinuteComboBox.setModel(new DefaultComboBoxModel<Integer>(
        new Integer[]{0, 10, 20, 30, 40, 50}
    ));
    this.testButton.addActionListener(event -> {
      this.testResultText.setVisible(true);
      try {
        MeiCanClient.instance().refresh(this.getUsername(), this.getPassword());
        this.testResultText.setText("成功");
      } catch (MeiCanLoginException e) {
        this.testResultText.setText("用户名或密码错误");
      } catch (HttpRequestException e) {
        this.testResultText.setText("请求异常");
      }
    });
    this.testResultText.setVisible(false);
  }

  private static class TestButtonActionListener implements ActionListener {
    private final JLabel testResultText;

    public TestButtonActionListener(JLabel testResultText) {
      this.testResultText = testResultText;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      // TODO: 2021/1/16 测试按钮监听
      System.out.println("点击了监听");
      testResultText.setText("成功 or 失败");
      testResultText.setVisible(true);
    }
  }


  public JPanel getRoot() {
    return rootPanel;
  }

  public String getUsername() {
    return this.usernameField.getText();
  }

  public void setUsername(String username) {
    this.usernameField.setText(username);
  }

  public String getPassword() {
    return String.valueOf(passwordField.getPassword());
  }

  public void setPassword(String password) {
    this.passwordField.setText(password);
  }

  public int getCycle() {
    return cycleComboBox.getSelectedIndex();
  }

  public void setCycle(int cycle) {
    this.cycleComboBox.setSelectedIndex(cycle);
  }

  public int getTipsHour() {
    return tipsHourComboBox.getSelectedIndex();
  }

  public void setTipsHour(int tipsHour) {
    this.tipsHourComboBox.setSelectedIndex(tipsHour);
  }

  public int getTipsMinute() {
    return tipsMinuteComboBox.getSelectedIndex();
  }

  public void setTipsMinute(int tipsMinute) {
    this.tipsMinuteComboBox.setSelectedIndex(tipsMinute);
  }
}
