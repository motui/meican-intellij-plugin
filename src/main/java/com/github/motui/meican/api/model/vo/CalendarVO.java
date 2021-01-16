package com.github.motui.meican.api.model.vo;

import com.github.motui.meican.api.model.OpeningTime;


import java.util.List;

/**
 * @author motui
 * @date 2021-01-17
 */

public class CalendarVO {
  private String startDate;
  private String endDate;
  private List<DateItem> dateList;

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public List<DateItem> getDateList() {
    return dateList;
  }

  public void setDateList(List<DateItem> dateList) {
    this.dateList = dateList;
  }


  public static class DateItem {
    private String date;
    private List<CalendarItem> calendarItemList;

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public List<CalendarItem> getCalendarItemList() {
      return calendarItemList;
    }

    public void setCalendarItemList(List<CalendarItem> calendarItemList) {
      this.calendarItemList = calendarItemList;
    }
  }


  public static class CalendarItem {
    private Long targetTime;
    private String title;
    private UserTabVO userTab;
    private OpeningTime openingTime;
    /**
     * CLOSED/AVAILABLE
     */
    private String status;
    private String reason;

    /**
     * 请求参数 withOrderDetail:true 返回该字段
     */
    private CorpOrderUserVO corpOrderUser;

    public Long getTargetTime() {
      return targetTime;
    }

    public void setTargetTime(Long targetTime) {
      this.targetTime = targetTime;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public UserTabVO getUserTab() {
      return userTab;
    }

    public void setUserTab(UserTabVO userTab) {
      this.userTab = userTab;
    }

    public OpeningTime getOpeningTime() {
      return openingTime;
    }

    public void setOpeningTime(OpeningTime openingTime) {
      this.openingTime = openingTime;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getReason() {
      return reason;
    }

    public void setReason(String reason) {
      this.reason = reason;
    }

    public CorpOrderUserVO getCorpOrderUser() {
      return corpOrderUser;
    }

    public void setCorpOrderUser(CorpOrderUserVO corpOrderUser) {
      this.corpOrderUser = corpOrderUser;
    }

    public boolean isOpen() {
      return "AVAILABLE".equals(status);
    }

  }
}
