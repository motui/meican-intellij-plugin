package cn.motui.meican.model.api;

/**
 * 下单地址信息
 *
 * @author it.motui
 * @date 2021-01-23
 */
public class Address {
  private FinalValue finalValue;
  private String name;

  public Address() {
  }

  public Address(FinalValue finalValue, String name) {
    this.finalValue = finalValue;
    this.name = name;
  }

  public FinalValue getFinalValue() {
    return finalValue;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
