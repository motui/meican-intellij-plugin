package com.github.motui.meican.order;

import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OrderDialog extends JDialog {
  private JPanel contentPane;
  private JButton buttonOk;
  private JButton buttonCancel;
  private JTable orderTable;

  public OrderDialog(String time, String restaurant, String dish, String address) {
    orderTable.setMinimumSize(new Dimension(400, 100));
    setContentPane(contentPane);
    this.orderTable.getModel().setValueAt(time, 0, 1);
    this.orderTable.getModel().setValueAt(restaurant, 1, 1);
    this.orderTable.getModel().setValueAt(dish, 2, 1);
    this.orderTable.getModel().setValueAt(address, 3, 1);
    setModal(true);
    getRootPane().setDefaultButton(buttonOk);

    buttonOk.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onOk();
      }
    });

    buttonCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    });

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onOk() {
    // add your code here
    System.out.println("下单");
    dispose();
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }

  private void createUIComponents() {
    DefaultTableModel defaultTableModel = new DefaultTableModel(
        new String[][]{
            {"时间", ""},
            {"店铺", ""},
            {"菜品", ""},
            {"地址", ""},
        },
        new String[]{"属性", "值"}
    );
    this.orderTable = new JBTable(defaultTableModel);
    this.orderTable.getColumnModel().getColumn(0).setWidth(3);
  }
}
