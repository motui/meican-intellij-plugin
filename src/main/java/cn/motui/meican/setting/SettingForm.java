package cn.motui.meican.setting;

import cn.motui.meican.MeiCanClient;
import cn.motui.meican.enums.CycleEnum;
import cn.motui.meican.enums.TimeEnum;
import cn.motui.meican.exception.HttpRequestException;
import cn.motui.meican.exception.MeiCanLoginException;
import cn.motui.meican.model.api.Account;
import cn.motui.meican.service.ServiceFactory;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBCheckBox;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author it.motui
 * @date 2021-01-16
 */
public class SettingForm {
  private JPanel rootPanel;
  private JPanel accountPanel;
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JButton testButton;
  private JLabel testResultText;

  private JPanel noticePanel;
  private JBCheckBox amNoticeCheckBox;
  private JBCheckBox pmNoticeCheckBox;
  private JComboBox<TimeEnum> timeComboBox;
  private JComboBox<CycleEnum> cycleComBox;

  public SettingForm() {
    this.accountPanel.setBorder(IdeBorderFactory.createTitledBorder("Account"));
    this.noticePanel.setBorder(IdeBorderFactory.createTitledBorder("Notification"));
    this.testButton.addActionListener(this::testButtonActionListener);
    this.testResultText.setVisible(false);
    for (TimeEnum timeEnum : TimeEnum.values()) {
      this.timeComboBox.addItem(timeEnum);
    }
    for (CycleEnum cycleEnum : CycleEnum.values()) {
      this.cycleComBox.addItem(cycleEnum);
    }
  }

  private void testButtonActionListener(ActionEvent event) {
    SwingUtilities.invokeLater(() -> {
      this.testButton.setEnabled(false);
      this.testResultText.setText(null);
      try {
        MeiCanClient.instance().refresh(this.getUsername(), this.getPassword());
        Account account = ServiceFactory.dataService().getAccount();
        this.testResultText.setText("成功, 账户: " + account.getEmail());
      } catch (MeiCanLoginException e) {
        this.testResultText.setText("用户名或密码错误");
      } catch (HttpRequestException e) {
        this.testResultText.setText("请求异常");
      } finally {
        this.testButton.setEnabled(true);
        this.testResultText.setVisible(true);
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

  public void setNotifyBeforeClosing(TimeEnum timeEnum) {
    this.timeComboBox.setSelectedItem(timeEnum);
  }

  public TimeEnum getNotifyBeforeClosing() {
    return (TimeEnum) this.timeComboBox.getSelectedItem();
  }

  public void setAmNotice(Boolean notice) {
    this.amNoticeCheckBox.setSelected(notice);
  }

  public Boolean getAmNotice() {
    return this.amNoticeCheckBox.isSelected();
  }

  public void setPmNotice(Boolean notice) {
    this.pmNoticeCheckBox.setSelected(notice);
  }

  public Boolean getPmNotice() {
    return this.pmNoticeCheckBox.isSelected();
  }

  public CycleEnum getCycle() {
    return (CycleEnum) this.cycleComBox.getSelectedItem();
  }

  public void setCycle(CycleEnum cycleEnum) {
    this.cycleComBox.setSelectedItem(cycleEnum);
  }
}
