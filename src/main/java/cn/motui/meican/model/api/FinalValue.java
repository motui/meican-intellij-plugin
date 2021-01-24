package cn.motui.meican.model.api;

/**
 * @author it.motui
 * @date 2021-01-23
 */
public class FinalValue {
  private String uniqueId;
  private String pickUpLocation;

  public FinalValue() {
  }

  public FinalValue(String uniqueId, String pickUpLocation) {
    this.uniqueId = uniqueId;
    this.pickUpLocation = pickUpLocation;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public String getPickUpLocation() {
    return pickUpLocation;
  }
}
