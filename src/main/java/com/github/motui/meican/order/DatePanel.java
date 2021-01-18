package com.github.motui.meican.order;

import com.github.motui.meican.api.model.Address;
import com.github.motui.meican.api.model.Dish;
import com.github.motui.meican.api.model.MeiCanPanelData;
import com.github.motui.meican.api.model.vo.AddOrderVO;
import com.github.motui.meican.api.model.vo.CalendarDishVO;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

/**
 * @author motui
 * @date 2021-01-17
 */
public class DatePanel {
  private JPanel rootPanel;
  private JLabel tips;
  private JPanel tipsPanel;
  private JPanel corePanel;
  private JList<MeiCanPanelData.RestaurantData> restaurantList;
  private JList<Dish> dishList;
  private JComboBox<Address> addressComboBox;
  private JSplitPane splitPane;
  private JButton okButton;

  public DatePanel(MeiCanPanelData.DateData dateData) {
    this.corePanel.setVisible(false);
    this.tipsPanel.setVisible(false);
    Boolean open = dateData.getOpen();
    if (open) {
      this.renderOpenUi(dateData);
    } else {
      this.renderCloseUi(dateData);
    }
  }

  private void renderOpenUi(MeiCanPanelData.DateData dateData) {
    this.corePanel.setVisible(true);
    splitPane.setDividerSize(2);
    splitPane.setDividerLocation(200);
    this.restaurantUi(dateData.getRestaurants());
    this.addressUi(dateData.getAddressList());
    this.okButton.addActionListener(event -> {
      MeiCanPanelData.RestaurantData restaurantData = this.restaurantList.getSelectedValue();
      Dish dish = this.dishList.getSelectedValue();
      Address address = (Address) addressComboBox.getSelectedItem();
      if (Objects.isNull(restaurantData)) {
        Messages.showMessageDialog(this.rootPanel.getParent(), "商家未选择", "下单警告", null);
        return;
      }
      if (Objects.isNull(dish)) {
        Messages.showMessageDialog(this.rootPanel.getParent(), "菜品未选择", "下单警告", null);
        return;
      }
      if (Objects.isNull(address)) {
        Messages.showMessageDialog(this.rootPanel.getParent(), "地址未选择", "下单警告", null);
        return;
      }
      AddOrderVO addOrderVO = new AddOrderVO(dateData.getUserTab().getUniqueId(),
          address.getUniqueId(), dateData.getTargetTime(), dish.getId());
      // 二次确认
      OrderDialog orderDialog = new OrderDialog(dateData.getTitle(), restaurantData.getName(),
          dish.getName(), address.getPickUpLocation(), addOrderVO);
      orderDialog.setLocationRelativeTo(this.rootPanel.getParent());
      orderDialog.pack();
      orderDialog.setVisible(true);
    });
  }

  private void renderCloseUi(MeiCanPanelData.DateData dateData) {
    if (dateData.isOrder()) {
      // 已下单
      CalendarDishVO dish = dateData.getDish();
      this.tips.setText("订单:\n"
          + dish.getName()
      );
      return;
    }
    this.tips.setText("订餐时间已过，已关闭");
    this.tipsPanel.setVisible(true);
  }

  private void restaurantUi(List<MeiCanPanelData.RestaurantData> restaurants) {
    if (CollectionUtils.isEmpty(restaurants)) {
      return;
    }
    MeiCanPanelData.RestaurantData[] restaurantDataArray = new MeiCanPanelData.RestaurantData[restaurants.size()];
    restaurants.toArray(restaurantDataArray);
    restaurantList.setListData(restaurantDataArray);
    // 默认值
    restaurantList.setSelectedIndex(0);
    // 渲染菜单
    this.dishUi(restaurants.get(0));
    restaurantList.addListSelectionListener(event -> {
      if (!event.getValueIsAdjusting()) {
        MeiCanPanelData.RestaurantData selectedValue = restaurantList.getSelectedValue();
        this.dishUi(selectedValue);
      }
    });
  }

  private void dishUi(MeiCanPanelData.RestaurantData restaurantData) {
    Dish[] dishes = new Dish[restaurantData.getDishList().size()];
    restaurantData.getDishList().toArray(dishes);
    dishList.setListData(dishes);
  }

  private void addressUi(List<Address> addressList) {
    if (CollectionUtils.isEmpty(addressList)) {
      return;
    }
    addressList.forEach(address -> addressComboBox.addItem(address));
    // 默认第一个
    addressComboBox.setSelectedIndex(0);
  }

  public JPanel getRoot() {
    return rootPanel;
  }

}
