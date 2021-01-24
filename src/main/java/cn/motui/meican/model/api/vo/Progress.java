package cn.motui.meican.model.api.vo;

/**
 * @author it.motui
 * @date 2021-01-23
 */
public class Progress {
  private Long timestamp;
  private String activity;

  public Progress() {
  }

  public Progress(Long timestamp, String activity) {
    this.timestamp = timestamp;
    this.activity = activity;
  }

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
