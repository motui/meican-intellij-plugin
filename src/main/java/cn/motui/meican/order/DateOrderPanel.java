package cn.motui.meican.order;

import cn.motui.meican.Constants;
import cn.motui.meican.MeiCanClient;
import cn.motui.meican.exception.MeiCanAddOrderException;
import cn.motui.meican.model.DataBuilder;
import cn.motui.meican.model.api.Address;
import cn.motui.meican.model.api.Dish;
import cn.motui.meican.model.ui.RestaurantData;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 时间对应菜品选择面板
 *
 * @author it.motui
 * @date 2021-01-17
 */
public class DateOrderPanel {
  private JPanel rootPanel;
  private JList<RestaurantData> restaurantList;
  private JList<Dish> dishList;
  private JComboBox<Address> addressComboBox;
  private JSplitPane splitPane;
  private JButton okButton;

  private final LocalDateTime targetDateTime;
  private final String userTabUniqueId;
  private final String corpNamespace;
  private final OrderPanel orderPanel;

  public DateOrderPanel(OrderPanel orderPanel, LocalDateTime targetDateTime,
                        String userTabUniqueId, String corpNamespace) {
    this.orderPanel = orderPanel;
    this.targetDateTime = targetDateTime;
    this.userTabUniqueId = userTabUniqueId;
    this.corpNamespace = corpNamespace;

    // splitPane 设置
    this.splitPane.setBackground(Constants.TRANSPARENT);
    this.splitPane.setDividerSize(2);
    this.splitPane.setDividerLocation(200);
    this.okButton.addActionListener(this::okListener);
    this.renderUi();
  }

  private void renderUi() {
    this.renderRestaurantUi();
    this.renderAddressUi();
  }

  private void okListener(ActionEvent event) {
    RestaurantData restaurantData = this.restaurantList.getSelectedValue();
    Dish dish = this.dishList.getSelectedValue();
    Address address = (Address) addressComboBox.getSelectedItem();
    if (Objects.isNull(dish)) {
      JOptionPane.showMessageDialog(null, "请选择菜品", "下单参数校验", JOptionPane.ERROR_MESSAGE, null);
      return;
    }
    if (Objects.isNull(address)) {
      JOptionPane.showMessageDialog(null, "请选择地址", "下单参数校验", JOptionPane.ERROR_MESSAGE, null);
      return;
    }
    this.okButton.setEnabled(false);
    // 订单确认信息
    String message = "店铺: " + restaurantData.getName() +
        "\n菜品: " + dish.getName() +
        "\n地址: " + address.getFinalValue().getPickUpLocation();
    System.out.println(message);
    int operate = JOptionPane.showConfirmDialog(null, message, "订单确认",
        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (JOptionPane.YES_OPTION == operate) {
      SwingUtilities.invokeLater(() -> {
        try {
          MeiCanClient.instance().order(userTabUniqueId, address.getFinalValue().getUniqueId(),
              targetDateTime, dish.getId());
          orderPanel.refreshUi();
        } catch (MeiCanAddOrderException e) {
          this.okButton.setEnabled(true);
          JOptionPane.showMessageDialog(null, e.getMessage(), "下单提示",
              JOptionPane.ERROR_MESSAGE, null);
        } catch (Exception e) {
          this.okButton.setEnabled(true);
          JOptionPane.showMessageDialog(null, "请求异常，请稍后再试", "下单提示",
              JOptionPane.ERROR_MESSAGE, null);
        }
      });
    } else {
      this.okButton.setEnabled(true);
    }
  }

  private void renderRestaurantUi() {
    SwingUtilities.invokeLater(() -> {
      List<RestaurantData> restaurantDataList = DataBuilder.getRestaurantData(this.userTabUniqueId, targetDateTime);
      RestaurantData[] restaurantDataArray = new RestaurantData[restaurantDataList.size()];
      restaurantDataList.toArray(restaurantDataArray);
      restaurantList.setListData(restaurantDataArray);
      // 默认值
      restaurantList.setSelectedIndex(0);
      // 渲染菜单
      this.renderDishUi(restaurantDataList.get(0));
      restaurantList.addListSelectionListener(event -> {
        if (!event.getValueIsAdjusting()) {
          RestaurantData selectedValue = restaurantList.getSelectedValue();
          this.renderDishUi(selectedValue);
        }
      });
    });
  }

  private void renderDishUi(RestaurantData restaurantData) {
    Dish[] dishes = new Dish[restaurantData.getDishes().size()];
    restaurantData.getDishes().toArray(dishes);
    dishList.setListData(dishes);
  }

  private void renderAddressUi() {
    SwingUtilities.invokeLater(() -> {
      List<Address> addressList = DataBuilder.getAddress(this.corpNamespace);
      if (CollectionUtils.isNotEmpty(addressList)) {
        addressList.forEach(address -> addressComboBox.addItem(address));
        // 默认第一个
        addressComboBox.setSelectedIndex(0);
      }
    });
  }

  public JPanel getRoot() {
    return rootPanel;
  }
}
