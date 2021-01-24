package cn.motui.meican.model.ui;

import cn.motui.meican.model.StatusEnum;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 时间对应的数据(午餐、晚餐)
 *
 * @author it.motui
 * @date 2021-01-23
 */
public class DateData {
  private final String title;
  private final StatusEnum status;
  private final String reason;
  private final String userTabUniqueId;
  private final LocalDateTime targetTime;
  private final String corpUniqueId;
  private final String corpNamespace;

  /**
   * 如果已经下单，存在该值
   */
  private final String orderUniqueId;

  public DateData(String title, String status, String reason, Long targetTime, String userTabUniqueId,
                  String corpUniqueId, String corpNamespace, String orderUniqueId) {
    this.title = title;
    this.status = StatusEnum.valueOf(status);
    this.reason = reason;
    this.targetTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(targetTime), ZoneId.systemDefault());
    this.userTabUniqueId = userTabUniqueId;
    this.corpUniqueId = corpUniqueId;
    this.corpNamespace = corpNamespace;
    this.orderUniqueId = orderUniqueId;
  }


  public String getTitle() {
    return title;
  }

  public StatusEnum getStatus() {
    return status;
  }

  public String getReason() {
    return reason;
  }

  public LocalDateTime getTargetTime() {
    return targetTime;
  }

  public String getUserTabUniqueId() {
    return userTabUniqueId;
  }

  public String getCorpUniqueId() {
    return corpUniqueId;
  }

  public String getCorpNamespace() {
    return corpNamespace;
  }

  public String getOrderUniqueId() {
    return orderUniqueId;
  }
}

