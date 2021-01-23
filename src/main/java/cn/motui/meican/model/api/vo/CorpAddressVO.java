package cn.motui.meican.model.api.vo;

import cn.motui.meican.model.api.Address;

import java.util.List;

/**
 * 地址VO
 *
 * @author motui
 * @date 2021-01-23
 */
public class CorpAddressVO {
  private String resultCode;
  private String resultDescription;
  private Data data;

  public CorpAddressVO() {
  }

  public CorpAddressVO(String resultCode, String resultDescription, Data data) {
    this.resultCode = resultCode;
    this.resultDescription = resultDescription;
    this.data = data;
  }

  public boolean isSuccess() {
    return "OK".equals(resultCode);
  }

  public static class Data {
    private List<Address> addressList;

    public Data() {
    }

    public Data(List<Address> addressList) {
      this.addressList = addressList;
    }

    public List<Address> getAddressList() {
      return addressList;
    }

    public void setAddressList(List<Address> addressList) {
      this.addressList = addressList;
    }
  }

  public String getResultCode() {
    return resultCode;
  }

  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }

  public String getResultDescription() {
    return resultDescription;
  }

  public void setResultDescription(String resultDescription) {
    this.resultDescription = resultDescription;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }
}
