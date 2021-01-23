package cn.motui.meican.model.api.vo;

/**
 * @author motui
 * @date 2021-01-23
 */
public class Progress {
  private Long timestamp;
  private String activity;

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }
}
