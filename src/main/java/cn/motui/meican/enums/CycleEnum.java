package cn.motui.meican.enums;

/**
 * @author it.motui
 * @date 2021-01-24
 */
public enum CycleEnum {
  MONDAY_TO_FRIDAY("周一~周五", "? * 2-6"),
  MONDAY_TO_SATURDAY("周一~周六", "? * 2-7"),
  EVERYDAY("每天", "? * *");

  private final String displayName;
  private final String cron;

  CycleEnum(String displayName, String cron) {
    this.displayName = displayName;
    this.cron = cron;
  }

  public String getCron() {
    return cron;
  }

  public String getDisplayName() {
    return displayName;
  }


  @Override
  public String toString() {
    return displayName;
  }
}
