package cn.motui.meican.job;

import cn.motui.meican.model.ui.DateData;
import cn.motui.meican.model.ui.OrderDetail;
import cn.motui.meican.notification.NotificationFactory;
import cn.motui.meican.service.ServiceFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 通知JOB
 *
 * @author it.motui
 * @date 2021-01-24
 */
public class NotificationJob implements Job {

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    LocalDateTime now = LocalDateTime.now();
    List<DateData> dateDataList = ServiceFactory.dataService().getDateData(now);
    int hour = now.getHour();
    DateData dateData;
    if (hour < 12) {
      dateData = dateDataList.get(0);
    } else {
      dateData = dateDataList.get(1);
    }
    String content = null;
    switch (dateData.getStatus()) {
      case AVAILABLE:
        content = "<p>时间:" + now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
            + "</p><p><b>还未订餐</b>，记得订餐哦</p>";
        NotificationFactory.showInfoNotification(content);
        break;
      case ORDER:
        OrderDetail orderDetail = ServiceFactory.dataService().getOrderDetail(dateData.getOrderUniqueId());
        content = "<p>已订餐:</p>" +
            "<p>店铺: " + orderDetail.getRestaurantName() + "</p>" +
            "<p><b>" + orderDetail.getDish().getName() + "</b></p>" +
            "<p>" + orderDetail.getDish().getRemark() + "</p>";
        NotificationFactory.showInfoNotification(content);
        break;
      case CLOSED:
        NotificationFactory.showWarningNotification("[美餐]餐厅关闭,无法订餐");
        break;
      default:
        break;
    }
  }
}
