package cn.motui.meican.order;

import cn.motui.meican.Constants;
import cn.motui.meican.model.ui.DateData;
import cn.motui.meican.notification.NotificationFactory;
import cn.motui.meican.service.ServiceFactory;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author it.motui
 * @date 2021-01-23
 */
public class OrderPanel {
  private JPanel rootPanel;
  private JTabbedPane tabbedPane;
  private JButton preButton;
  private JButton nextButton;
  private JLabel label;

  private LocalDateTime targetDateTime = LocalDateTime.now();
  private int currentSelectedIndex = 0;

  public OrderPanel() {
    this.label.setText(targetDateTime.format(Constants.FORMATTER_RECORD));
    this.preButton.addActionListener(this::preButtonAction);
    this.nextButton.addActionListener(this::nextButtonAction);
    this.renderUi();
  }

  public void refreshUi() {
    this.currentSelectedIndex = this.tabbedPane.getSelectedIndex();
    this.renderUi();
  }

  private void renderUi() {
    SwingUtilities.invokeLater(() -> {
      this.tabbedPane.removeAll();
      List<DateData> dateDataList = ServiceFactory.dataService().getDateData(targetDateTime);
      if (CollectionUtils.isNotEmpty(dateDataList)) {
        dateDataList.forEach(dateData -> {
          JPanel content = null;
          LocalDateTime targetTime = dateData.getTargetTime();
          switch (dateData.getStatus()) {
            case ORDER:
              content = new OrderDetailPanel(targetTime, dateData.getOrderUniqueId()).getRoot();
              break;
            case AVAILABLE:
              content = new DateOrderPanel(this, targetTime, dateData.getUserTabUniqueId(),
                  dateData.getCorpNamespace()).getRoot();
              break;
            default:
              content = new EmptyPanel(dateData.getReason()).getRoot();
              break;
          }
          this.tabbedPane.addTab(dateData.getTitle(), content);
        });
      }
      this.tabbedPane.setSelectedIndex(this.currentSelectedIndex);
    });
  }

  private void preButtonAction(ActionEvent event) {
    SwingUtilities.invokeLater(() -> {
      this.targetDateTime = this.targetDateTime.plusDays(-1);
      if (this.checkTargetDateTime()) {
        this.label.setText(this.targetDateTime.format(Constants.FORMATTER_RECORD));
        this.renderUi();
      }
    });
  }

  private void nextButtonAction(ActionEvent event) {
    SwingUtilities.invokeLater(() -> {
      this.targetDateTime = this.targetDateTime.plusDays(1);
      if (this.checkTargetDateTime()) {
        this.label.setText(this.targetDateTime.format(Constants.FORMATTER_RECORD));
        this.renderUi();
      }
    });
  }

  private boolean checkTargetDateTime() {
    long days = Duration.between(this.targetDateTime, LocalDateTime.now()).toDays();
    boolean result = Math.abs(days) > 6;
    if (result) {
      NotificationFactory.showErrorNotification("时间不能超出两周");
    }
    return !result;
  }

  public JPanel getRoot() {
    return rootPanel;
  }

}
