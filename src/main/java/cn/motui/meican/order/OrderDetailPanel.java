package cn.motui.meican.order;

import cn.motui.meican.Constants;
import cn.motui.meican.model.api.vo.Progress;
import cn.motui.meican.model.ui.OrderDetail;
import cn.motui.meican.service.ServiceFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 订单详情
 *
 * @author it.motui
 * @date 2021-01-23
 */
public class OrderDetailPanel {
  private JPanel rootPanel;
  private JList<String> scheduleList;
  private JLabel tips;
  private JLabel count;
  private JLabel title;
  private JLabel remark;

  private final LocalDateTime targetDateTime;
  private final String orderUniqueId;

  public OrderDetailPanel(LocalDateTime targetDateTime, String orderUniqueId) {
    this.targetDateTime = targetDateTime;
    this.orderUniqueId = orderUniqueId;
    this.scheduleList.setBackground(Constants.TRANSPARENT);
    this.renderUi();
  }

  private void renderUi() {
    this.scheduleList.setBackground(null);
    SwingUtilities.invokeLater(() -> {
      OrderDetail orderDetail = ServiceFactory.dataService().getOrderDetail(this.orderUniqueId);
      this.count.setText(orderDetail.getDishCount() + "份  ");
      this.title.setText(orderDetail.getDish().getTitle());
      this.remark.setText(orderDetail.getDish().getRemark());
      this.tips.setText("[" + orderDetail.getPostbox().getPostboxCode() + "] " + orderDetail.getPickUpMessage());
      List<Progress> progressList = orderDetail.getProgressList();
      if (CollectionUtils.isNotEmpty(progressList)) {
        String[] progressArray = new String[progressList.size()];
        for (int i = 0; i < progressList.size(); i++) {
          Progress progress = progressList.get(i);
          progressArray[i] = "[" + DateFormatUtils.format(new Date(progress.getTimestamp()), "HH:mm:ss") + "] "
              + progress.getActivity();
        }
        this.scheduleList.setListData(progressArray);
      }
    });
  }


  public JPanel getRoot() {
    return rootPanel;
  }
}
