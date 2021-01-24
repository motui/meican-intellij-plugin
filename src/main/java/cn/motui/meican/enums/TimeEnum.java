package cn.motui.meican.enums;

/**
 * @author it.motui
 * @date 2021-01-24
 */
public enum TimeEnum {
  M5(5, "5 minute"),
  M10(10, "10 minute"),
  M15(15, "15 minute"),
  M20(20, "20 minute"),
  M25(25, "25 minute"),
  M30(30, "30 minute"),
  ;

  private final int minute;
  private final String displayName;

  TimeEnum(int minute, String displayName) {
    this.minute = minute;
    this.displayName = displayName;
  }

  public int getMinute() {
    return minute;
  }

  public String getDisplayName() {
    return displayName;
  }


  @Override
  public String toString() {
    return displayName;
  }
}
