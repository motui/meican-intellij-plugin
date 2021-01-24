package cn.motui.meican;

import cn.motui.meican.model.api.Dish;
import cn.motui.meican.model.api.vo.CalendarVO;
import cn.motui.meican.model.api.vo.RestaurantDishVO;
import cn.motui.meican.exception.HttpRequestException;
import cn.motui.meican.exception.MeiCanAddOrderException;
import cn.motui.meican.exception.MeiCanLoginException;
import cn.motui.meican.model.api.Account;
import cn.motui.meican.model.api.vo.AddOrderResponseVO;
import cn.motui.meican.model.api.vo.AddressVO;
import cn.motui.meican.model.api.vo.CorpAddressVO;
import cn.motui.meican.model.api.vo.LoginVO;
import cn.motui.meican.model.api.vo.OrderDetailVO;
import cn.motui.meican.model.api.vo.RestaurantVO;
import cn.motui.meican.model.api.vo.UserTabVO;
import cn.motui.meican.setting.Setting;
import cn.motui.meican.setting.SettingService;
import cn.motui.meican.util.JsonUtil;
import com.intellij.openapi.components.ServiceManager;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 美餐客户端
 *
 * @author it.motui
 * @date 2021-01-16
 */
public class MeiCanClient {
  private String username;
  private String password;
  private final HttpClient httpClient;

  private static final MeiCanClient INSTANCE;

  static {
    SettingService service = ServiceManager.getService(SettingService.class);
    Setting state = service.getState();
    if (Objects.isNull(state)) {
      INSTANCE = new MeiCanClient(null, null);
    } else {
      INSTANCE = new MeiCanClient(state.getUsername(), state.getPassword());
    }
  }

  public static MeiCanClient instance() {
    return INSTANCE;
  }

  private MeiCanClient(String username, String password) {
    this.username = username;
    this.password = password;
    CookieStore cookie = new BasicCookieStore();
    this.httpClient = HttpClients.custom().setDefaultCookieStore(cookie).build();
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      return;
    }
    this.login();
  }

  public void refresh(String username, String password) {
    if (!this.username.equals(username) || !this.password.equals(password)) {
      this.username = username;
      this.password = password;
      this.login();
    }
  }

  public void login() {
    List<BasicNameValuePair> parameters = new ArrayList<>();
    parameters.add(new BasicNameValuePair("username", username));
    parameters.add(new BasicNameValuePair("username_type", "username"));
    parameters.add(new BasicNameValuePair("password", password));
    parameters.add(new BasicNameValuePair("meican_credential_type", "password"));
    parameters.add(new BasicNameValuePair("grant_type", "password"));
    Map<String, Object> urlParameter = this.createUrlParameter();
    urlParameter.put("remember", true);
    try {
      String responseStr = this.post(Constants.URL_LOGIN, urlParameter, parameters);
      LoginVO loginVO = JsonUtil.from(responseStr, LoginVO.class);
      if (!loginVO.isSuccess()) {
        throw new MeiCanLoginException("login error");
      }
    } catch (IOException e) {
      throw new MeiCanLoginException("login error", e);
    }
  }

  /**
   * 用户信息
   *
   * @return Account
   */
  public Account account() {
    Map<String, Object> urlParameter = this.createUrlParameter();
    try {
      String response = this.get(Constants.URL_ACCOUNT, urlParameter);
      return JsonUtil.from(response, Account.class);
    } catch (IOException e) {
      throw new HttpRequestException(Constants.URL_ACCOUNT + " request error.");
    }
  }

  public CorpAddressVO address(String corpNamespace) {
    Map<String, Object> urlParameter = this.createUrlParameter();
    urlParameter.put("namespace", corpNamespace);
    try {
      String response = this.get(Constants.URL_ADDRESS, urlParameter);
      CorpAddressVO corpAddress = JsonUtil.from(response, CorpAddressVO.class);
      if (corpAddress.isSuccess()) {
        return corpAddress;
      }
      throw new HttpRequestException(Constants.URL_ADDRESS + " request error." + corpAddress.getResultDescription());
    } catch (IOException e) {
      throw new HttpRequestException(Constants.URL_ADDRESS + " request error.");
    }
  }

  /**
   * 可点菜单
   *
   * @param startDate 开始时间 yyyy-MM-dd
   * @param endDate   结束时间 yyyy-MM-dd, 最大值startDate + 7
   * @return Calendar
   */
  public CalendarVO calendar(String startDate, String endDate) {
    Map<String, Object> urlParameter = this.createUrlParameter();
    urlParameter.put("beginDate", startDate);
    urlParameter.put("endDate", endDate);
    urlParameter.put("withOrderDetail", false);
    try {
      String response = this.get(Constants.URL_CALENDAR, urlParameter);
      return JsonUtil.from(response, CalendarVO.class);
    } catch (IOException e) {
      throw new HttpRequestException(Constants.URL_CALENDAR + " request error.");
    }
  }

  /**
   * 餐厅列表
   *
   * @param userTableUniqueId {@link UserTabVO#getUniqueId()}
   * @param targetTime        {@link CalendarVO.CalendarItem#getTargetTime()}
   * @return RestaurantVO
   */
  public RestaurantVO restaurants(String userTableUniqueId, LocalDateTime targetTime) {
    Map<String, Object> urlParameter = this.createUrlParameter();
    urlParameter.put("tabUniqueId", userTableUniqueId);
    String formatTime = targetTime.format(Constants.FORMATTER_TIME_MEI_CAN);
    urlParameter.put("targetTime", formatTime);
    try {
      String response = this.get(Constants.URL_RESTAURANTS, urlParameter);
      return JsonUtil.from(response, RestaurantVO.class);
    } catch (IOException e) {
      throw new HttpRequestException(Constants.URL_RESTAURANTS + " request error.");
    }
  }

  /**
   * 菜单
   *
   * @param userTableUniqueId  {@link UserTabVO#getUniqueId()}
   * @param targetTime         {@link CalendarVO.CalendarItem#getTargetTime()}
   * @param restaurantUniqueId {@link RestaurantDishVO#getUniqueId()}
   * @return RestaurantDishVO
   */
  public RestaurantDishVO restaurantDish(String userTableUniqueId, LocalDateTime targetTime, String restaurantUniqueId) {
    Map<String, Object> urlParameter = this.createUrlParameter();
    urlParameter.put("tabUniqueId", userTableUniqueId);
    urlParameter.put("restaurantUniqueId", restaurantUniqueId);
    String formatTime = targetTime.format(Constants.FORMATTER_TIME_MEI_CAN);
    urlParameter.put("targetTime", formatTime);
    try {
      String response = this.get(Constants.URL_RESTAURANTS_SHOW, urlParameter);
      return JsonUtil.from(response, RestaurantDishVO.class);
    } catch (IOException e) {
      throw new HttpRequestException(Constants.URL_RESTAURANTS_SHOW + " request error.");
    }
  }

  /**
   * 订单详情
   *
   * @param orderUniqueId 订单编号
   * @return OrderDetailVO
   */
  public OrderDetailVO orderDetail(String orderUniqueId) {
    Map<String, Object> urlParameter = this.createUrlParameter();
    urlParameter.put("uniqueId", orderUniqueId);
    urlParameter.put("type", "CORP_ORDER");
    urlParameter.put("progressMarkdownSupport", true);
    urlParameter.put("x", System.currentTimeMillis());
    try {
      String response = this.get(Constants.URL_ORDER_SHOW, urlParameter);
      return JsonUtil.from(response, OrderDetailVO.class);
    } catch (IOException e) {
      throw new HttpRequestException(Constants.URL_ORDER_SHOW + " request error.");
    }
  }


  /**
   * 下单
   *
   * @param userTabUniqueId {@link UserTabVO#getUniqueId()}
   * @param addressUniqueId   {@link AddressVO#getUniqueId()}
   * @param targetTime        {@link CalendarVO.CalendarItem#getTargetTime()}
   * @param dishId            {@link Dish#getId()}
   */
  public void order(String userTabUniqueId, String addressUniqueId, LocalDateTime targetTime, Long dishId) {
    List<BasicNameValuePair> parameters = new ArrayList<>();
    parameters.add(new BasicNameValuePair("corpAddressRemark", ""));
    parameters.add(new BasicNameValuePair("corpAddressUniqueId", addressUniqueId));
    parameters.add(new BasicNameValuePair("order", "[{\"count\":1,\"dishId\":" + dishId + "}]"));
    parameters.add(new BasicNameValuePair("remarks", "[{\"dishId\":\"" + dishId + "\",\"remark\":\"\"}]"));
    parameters.add(new BasicNameValuePair("tabUniqueId", userTabUniqueId));
    String formatTime = targetTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    parameters.add(new BasicNameValuePair("targetTime", formatTime));
    parameters.add(new BasicNameValuePair("userAddressUniqueId", addressUniqueId));
    Map<String, Object> urlParameter = this.createUrlParameter();
    try {
      String responseStr = this.post(Constants.URL_ADD_ORDER, urlParameter, parameters);
      AddOrderResponseVO addOrderResponseVO = JsonUtil.from(responseStr, AddOrderResponseVO.class);
      if (!addOrderResponseVO.isSuccess()) {
        throw new MeiCanAddOrderException("order error." + responseStr);
      }
    } catch (IOException e) {
      throw new HttpRequestException(Constants.URL_ADD_ORDER + " request error", e);
    }
  }


  private void setHeader(HttpRequestBase httpRequest) {
    httpRequest.addHeader("User-Agent", Constants.USER_AGENT);
    httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
  }

  private Map<String, Object> createUrlParameter() {
    Map<String, Object> map = new HashMap<>();
    map.put("client_id", Constants.CLIENT_ID);
    map.put("client_secret", Constants.CLIENT_SECRET);
    return map;
  }

  private String createUrl(String url, Map<String, Object> urlParameter) {
    if (MapUtils.isNotEmpty(urlParameter)) {
      List<String> parameter = new ArrayList<>();
      urlParameter.forEach((k, v) -> parameter.add(String.format("%s=%s", k, v)));
      url = String.format("%s?%s", url, StringUtils.join(parameter, "&"));
    }
    return url;
  }

  private String post(String url, Map<String, Object> urlParameter, List<BasicNameValuePair> parameters)
      throws IOException {
    HttpPost httpPost = new HttpPost(this.createUrl(url, urlParameter));
    this.setHeader(httpPost);
    httpPost.setEntity(new UrlEncodedFormEntity(parameters));
    HttpResponse response = httpClient.execute(httpPost);
    if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
      throw new HttpRequestException("request error. httpStatusCode:" + response.getStatusLine().getStatusCode());
    }
    return EntityUtils.toString(response.getEntity());
  }

  private String get(String url, Map<String, Object> urlParameter) throws IOException {
    HttpGet httpGet = new HttpGet(this.createUrl(url, urlParameter));
    this.setHeader(httpGet);
    HttpResponse response = httpClient.execute(httpGet);
    if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
      throw new HttpRequestException("request error. httpStatusCode:" + response.getStatusLine().getStatusCode());
    }
    return EntityUtils.toString(response.getEntity());
  }

}
