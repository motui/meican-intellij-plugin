package cn.motui.meican.order;

import cn.motui.meican.Constants;

import javax.swing.*;

/**
 * 空面板
 *
 * @author it.motui
 * @date 2021-01-23
 */
public class EmptyPanel {
  private JPanel rootPanel;
  private JTextArea textArea;

  public EmptyPanel(String content) {
    this.textArea.setBackground(Constants.TRANSPARENT);
    this.textArea.setText(content);
  }

  public JPanel getRoot() {
    return rootPanel;
  }
}
