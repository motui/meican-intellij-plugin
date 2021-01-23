package cn.motui.meican.setting;

import cn.motui.meican.MeiCanClient;
import cn.motui.meican.exception.HttpRequestException;
import cn.motui.meican.exception.MeiCanLoginException;
import cn.motui.meican.model.DataBuilder;
import cn.motui.meican.model.api.Account;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author motui
 * @date 2021-01-16
 */
public class SettingForm {
  private JPanel rootPanel;
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JButton testButton;
  private JLabel testResultText;

  public SettingForm() {
    this.testButton.addActionListener(this::testButtonActionListener);
    this.testResultText.setVisible(false);
  }

  private void testButtonActionListener(ActionEvent event) {
    SwingUtilities.invokeLater(() -> {
      this.testResultText.setVisible(true);
      try {
        MeiCanClient.instance().refresh(this.getUsername(), this.getPassword());
        Account account = DataBuilder.getAccount();
        this.testResultText.setText("成功," + account.getEmail());
      } catch (MeiCanLoginException e) {
        this.testResultText.setText("用户名或密码错误");
      } catch (HttpRequestException e) {
        this.testResultText.setText("请求异常");
      }
    });
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

}
