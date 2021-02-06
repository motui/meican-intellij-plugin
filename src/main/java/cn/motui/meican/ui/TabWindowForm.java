package cn.motui.meican.ui;

import cn.motui.meican.model.api.Address;
import cn.motui.meican.model.api.Dish;
import cn.motui.meican.model.api.Postbox;
import cn.motui.meican.model.ui.RestaurantData;
import cn.motui.meican.model.ui.TabData;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;

import javax.swing.*;

/**
 * TabWindowForm
 *
 * @author it.motui
 * @date 2021-02-06
 */
public class TabWindowForm {
  protected JPanel root;
  protected JBLabel label;

  protected JSplitPane splitPane;
  protected JBList<RestaurantData> restaurantList;
  protected JBList<Dish> dishList;
  protected ComboBox<Address> addressComboBox;
  protected JButton orderButton;

  protected JBLabel dishNameLabel;
  protected JBLabel dishRemarkLabel;
  protected JBLabel dishCountLabel;
  protected JBLabel tipsLabel;
  protected JBList<String> scheduleList;
  protected JButton cancelOrderButton;
}
