package com.github.motui.meican.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author motui
 * @date 2021-01-17
 */

public class Corp {
  private String uniqueId;
  private Boolean useCloset;
  private String name;
  private String namespace;
  private Boolean priceVisible;
  private Boolean showPrice;
  private Double priceLimit;
  private Integer priceLimitInCent;
  private Boolean acceptCashPaymentToMeican;
  private Boolean alwaysOpen;
  private List<Address> addressList;
  @JsonProperty("isAdmin")
  private Boolean admin;

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public Boolean getUseCloset() {
    return useCloset;
  }

  public void setUseCloset(Boolean useCloset) {
    this.useCloset = useCloset;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public Boolean getPriceVisible() {
    return priceVisible;
  }

  public void setPriceVisible(Boolean priceVisible) {
    this.priceVisible = priceVisible;
  }

  public Boolean getShowPrice() {
    return showPrice;
  }

  public void setShowPrice(Boolean showPrice) {
    this.showPrice = showPrice;
  }

  public Double getPriceLimit() {
    return priceLimit;
  }

  public void setPriceLimit(Double priceLimit) {
    this.priceLimit = priceLimit;
  }

  public Integer getPriceLimitInCent() {
    return priceLimitInCent;
  }

  public void setPriceLimitInCent(Integer priceLimitInCent) {
    this.priceLimitInCent = priceLimitInCent;
  }

  public Boolean getAcceptCashPaymentToMeican() {
    return acceptCashPaymentToMeican;
  }

  public void setAcceptCashPaymentToMeican(Boolean acceptCashPaymentToMeican) {
    this.acceptCashPaymentToMeican = acceptCashPaymentToMeican;
  }

  public Boolean getAlwaysOpen() {
    return alwaysOpen;
  }

  public void setAlwaysOpen(Boolean alwaysOpen) {
    this.alwaysOpen = alwaysOpen;
  }

  public List<Address> getAddressList() {
    return addressList;
  }

  public void setAddressList(List<Address> addressList) {
    this.addressList = addressList;
  }

  public Boolean getAdmin() {
    return admin;
  }

  public void setAdmin(Boolean admin) {
    this.admin = admin;
  }
}
