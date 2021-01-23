package cn.motui.meican.model.api;

/**
 * @author motui
 * @date 2021-01-23
 */
public class Postbox {
  private String postboxCode;
  private Long postboxOpenTime;

  public String getPostboxCode() {
    return postboxCode;
  }

  public void setPostboxCode(String postboxCode) {
    this.postboxCode = postboxCode;
  }

  public Long getPostboxOpenTime() {
    return postboxOpenTime;
  }

  public void setPostboxOpenTime(Long postboxOpenTime) {
    this.postboxOpenTime = postboxOpenTime;
  }
}
