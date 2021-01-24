package cn.motui.meican.model.api.vo;


/**
 * 地址信息
 *
 * @author it.motui
 * @date 2021-01-17
 */

public class AddressVO {
  private String uniqueId;
  private String address;
  private String corpAddressCode;
  private String pickUpLocation;

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCorpAddressCode() {
    return corpAddressCode;
  }

  public void setCorpAddressCode(String corpAddressCode) {
    this.corpAddressCode = corpAddressCode;
  }

  public String getPickUpLocation() {
    return pickUpLocation;
  }

  public void setPickUpLocation(String pickUpLocation) {
    this.pickUpLocation = pickUpLocation;
  }

  @Override
  public String toString() {
    return pickUpLocation;
  }
}
