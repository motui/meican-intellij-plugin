package cn.motui.meican.ui;

import javax.swing.*;

/**
 * 空面板
 *
 * @author it.motui
 * @date 2021-01-23
 */
public class EmptyForm {
  private JPanel rootPanel;
  private JTextPane textPane;

  public EmptyForm(String content) {
    this.textPane.setBackground(UI.transparentColor());
    this.textPane.setText(content);
  }

  public JPanel getRoot() {
    return rootPanel;
  }
}
