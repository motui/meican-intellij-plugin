package com.github.motui.meican.order;

import com.github.motui.meican.api.MeiCanClient;
import com.github.motui.meican.api.model.MeiCanPanelData;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author motui
 * @date 2021-01-16
 */
public class OrderWindow {
  private JPanel rootPanel;
  private JTabbedPane tabbedPanel;
  private JButton preButton;
  private JButton nextButton;
  private JLabel label;

  private LocalDateTime currentDate = LocalDateTime.now();

  private final MeiCanClient meiCanClient;

  public OrderWindow() {
    meiCanClient = MeiCanClient.instance();
    this.label.setText(currentDate.format(DateTimeFormatter.ofPattern("MM/dd")));
    this.preButton.addActionListener(new PreButtonListener(this));
    this.nextButton.addActionListener(new NextButtonListener(this));
    tabbedPanel.setMinimumSize(new Dimension(-1, 300));
    this.refreshUi();
  }

  private void refreshUi() {
    this.tabbedPanel.removeAll();
    MeiCanPanelData meiCanPanelData = meiCanClient.getMeiCanPanelData(currentDate);
    List<MeiCanPanelData.DateData> dateDataList = meiCanPanelData.getDateDataList();
    if (CollectionUtils.isNotEmpty(dateDataList)) {
      dateDataList.forEach(dateData -> {
        JPanel tabbedPaneItem = this.createTabbedPaneItem(dateData);
        this.tabbedPanel.addTab(dateData.getTitle(), tabbedPaneItem);
      });
    }
  }

  private JPanel createTabbedPaneItem(MeiCanPanelData.DateData dateData) {
    DatePanel datePanel = new DatePanel(dateData);
    return datePanel.getRoot();
  }


  private static class PreButtonListener implements ActionListener {
    private final OrderWindow orderWindow;

    public PreButtonListener(OrderWindow orderWindow) {
      this.orderWindow = orderWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      orderWindow.currentDate = orderWindow.currentDate.plusDays(-1);
      orderWindow.label.setText(orderWindow.currentDate.format(DateTimeFormatter.ofPattern("MM/dd")));
      orderWindow.refreshUi();
    }
  }

  private static class NextButtonListener implements ActionListener {
    private final OrderWindow orderWindow;

    public NextButtonListener(OrderWindow orderWindow) {
      this.orderWindow = orderWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      orderWindow.currentDate = orderWindow.currentDate.plusDays(1);
      orderWindow.label.setText(orderWindow.currentDate.format(DateTimeFormatter.ofPattern("MM/dd")));
      orderWindow.refreshUi();
    }
  }

  public JPanel getContent() {
    return rootPanel;
  }


}
