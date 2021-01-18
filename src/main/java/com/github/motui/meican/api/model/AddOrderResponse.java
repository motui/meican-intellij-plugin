package com.github.motui.meican.api.model;

/**
 * @author motui
 * @date 2021-01-18
 */
public class AddOrderResponse {

  private String status;
  private String message;
  private Order order;

  public boolean isSuccess() {
    return "SUCCESSFUL".equals(status);
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public static class Order {
    private String uniqueId;

    public String getUniqueId() {
      return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
      this.uniqueId = uniqueId;
    }
  }

}
