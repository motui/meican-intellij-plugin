package com.github.motui.meican.order;

import com.github.motui.meican.api.MeiCanClient;
import com.github.motui.meican.api.model.vo.AddOrderVO;
import com.github.motui.meican.exception.MeiCanAddOrderException;
import com.github.motui.meican.exception.MeiCanException;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class OrderDialog extends JDialog {
  private JPanel contentPane;
  private JButton buttonOk;
  private JButton buttonCancel;
  private JTable orderTable;
  private AddOrderVO addOrder;

  public OrderDialog(String timeName, String restaurant, String dish, String address, AddOrderVO addOrder) {
    this.addOrder = addOrder;
    orderTable.setMinimumSize(new Dimension(400, 100));
    setContentPane(contentPane);
    this.orderTable.getModel().setValueAt(timeName, 0, 1);
    this.orderTable.getModel().setValueAt(restaurant, 1, 1);
    this.orderTable.getModel().setValueAt(dish, 2, 1);
    this.orderTable.getModel().setValueAt(address, 3, 1);
    setModal(true);
    getRootPane().setDefaultButton(buttonOk);

    buttonOk.addActionListener(e -> onOk());

    buttonCancel.addActionListener(e -> onCancel());

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onOk() {
    if (Objects.isNull(addOrder)) {
      throw new MeiCanException("addOrder is null");
    }
    try {
      MeiCanClient.instance().order(addOrder.getUserTableUniqueId(), addOrder.getAddressUniqueId(),
          addOrder.getTargetTime(), addOrder.getDishId());
      Messages.showMessageDialog(this.getParent(), "下单成功", "提示", null);

    } catch (MeiCanAddOrderException e) {
      Messages.showMessageDialog(this.getParent(), e.getMessage(), "下单警告", null);
    } catch (Exception e) {
      Messages.showMessageDialog(this.getParent(), "请求异常，请稍后再试", "下单警告", null);
    }
    dispose();
  }

  private void onCancel() {
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
