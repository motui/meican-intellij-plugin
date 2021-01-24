package cn.motui.meican;

import com.intellij.ui.JBColor;

import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * 常量池
 *
 * @author it.motui
 * @date 2021-01-16
 */
public class Constants {
  public static final String MEI_CAN_BASE_URL = "https://meican.com";
  public static final String CLIENT_ID = "Xqr8w0Uk4ciodqfPwjhav5rdxTaYepD";
  public static final String CLIENT_SECRET = "vD11O6xI9bG3kqYRu9OyPAHkRGxLh4E";
  public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

  public static final DateTimeFormatter FORMATTER_RECORD = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  public static final DateTimeFormatter FORMATTER_RECORD_MEI_CAN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public static final DateTimeFormatter FORMATTER_TIME_MEI_CAN = DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm");


  public static final String URL_LOGIN = MEI_CAN_BASE_URL + "/preference/preorder/api/v2.0/oauth/token";
  public static final String URL_ACCOUNT = MEI_CAN_BASE_URL + "/preorder/api/v2.1/accounts/show";
  public static final String URL_CALENDAR = MEI_CAN_BASE_URL + "/preorder/api/v2.1/calendaritems/list";
  public static final String URL_RESTAURANTS = MEI_CAN_BASE_URL + "/preorder/api/v2.1/restaurants/list";
  public static final String URL_RESTAURANTS_SHOW = MEI_CAN_BASE_URL + "/preorder/api/v2.1/restaurants/show";
  public static final String URL_ADD_ORDER = MEI_CAN_BASE_URL + "/preorder/api/v2.1/orders/add";
  public static final String URL_ORDER_SHOW = MEI_CAN_BASE_URL + "/preorder/api/v2.1/orders/show";
  public static final String URL_ADDRESS = MEI_CAN_BASE_URL + "/preorder/api/v2.1/corpaddresses/getmulticorpaddress";

  public static final JBColor TRANSPARENT = new JBColor(new Color(0, 0, 0, 0), new Color(0, 0, 0, 0));
}
