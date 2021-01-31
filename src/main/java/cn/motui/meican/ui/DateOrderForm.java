package cn.motui.meican.ui;

import cn.motui.meican.model.api.Address;
import cn.motui.meican.model.api.Dish;
import cn.motui.meican.model.ui.RestaurantData;
import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;

/**
 * 时间对应菜品选择面板
 *
 * @author it.motui
 * @date 2021-01-17
 */
public class DateOrderForm {
  public JPanel rootPanel;
  public JList<RestaurantData> restaurantList;
  public JList<Dish> dishList;
  public ComboBox<Address> addressComboBox;
  public JSplitPane splitPane;
  public JButton okButton;
}
