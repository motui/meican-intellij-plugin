package com.github.motui.meican;

/**
 * 常量池
 *
 * @author motui
 * @date 2021-01-16
 */
public class Constants {
  public static final String MEI_CAN_BASE_URL = "https://meican.com";
  public static final String CLIENT_ID = "Xqr8w0Uk4ciodqfPwjhav5rdxTaYepD";
  public static final String CLIENT_SECRET = "vD11O6xI9bG3kqYRu9OyPAHkRGxLh4E";
  public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";


  public static final String URL_LOGIN = MEI_CAN_BASE_URL + "/preference/preorder/api/v2.0/oauth/token";
  public static final String URL_CALENDAR = MEI_CAN_BASE_URL + "/preorder/api/v2.1/calendaritems/list";
  public static final String URL_RESTAURANTS = MEI_CAN_BASE_URL + "/preorder/api/v2.1/restaurants/list";
  public static final String URL_RESTAURANTS_SHOW = MEI_CAN_BASE_URL + "/preorder/api/v2.1/restaurants/show";
  public static final String URL_ADD_ORDER = MEI_CAN_BASE_URL + "/preorder/api/v2.1/orders/add";
}
