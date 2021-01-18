package com.github.motui.meican.api;

import com.github.motui.meican.Constants;
import com.github.motui.meican.api.model.AddOrderResponse;
import com.github.motui.meican.api.model.Address;
import com.github.motui.meican.api.model.Dish;
import com.github.motui.meican.api.model.MeiCanPanelData;
import com.github.motui.meican.api.model.vo.CalendarRestaurantVO;
import com.github.motui.meican.api.model.vo.CalendarVO;
import com.github.motui.meican.api.model.vo.LoginVO;
import com.github.motui.meican.api.model.vo.RestaurantDishVO;
import com.github.motui.meican.api.model.vo.RestaurantVO;
import com.github.motui.meican.api.model.vo.UserTabVO;
import com.github.motui.meican.exception.HttpRequestException;
import com.github.motui.meican.exception.MeiCanAddOrderException;
import com.github.motui.meican.exception.MeiCanLoginException;
import com.github.motui.meican.setting.Setting;
import com.github.motui.meican.setting.SettingService;
import com.github.motui.meican.util.JsonUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.intellij.openapi.components.ServiceManager;
import org.apache.commons.collections.CollectionUtils;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 美餐客户端
 *
 * @author motui
 * @date 2021-01-16
 */
public class MeiCanClient {
  private String username;
  private String password;
  private final HttpClient httpClient;

  private static final MeiCanClient INSTANCE;
  private static final Cache<String, MeiCanPanelData> CACHE;

  static {
    SettingService service = ServiceManager.getService(SettingService.class);
    Setting state = service.getState();
    if (Objects.isNull(state)) {
      INSTANCE = new MeiCanClient(null, null);
    } else {
      INSTANCE = new MeiCanClient(state.getUsername(), state.getPassword());
    }
    CACHE = CacheBuilder.newBuilder()
        // 默认1小时过期
        .expireAfterWrite(1, TimeUnit.HOURS)
        .maximumSize(20)
        .build();
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
  public RestaurantVO restaurants(String userTableUniqueId, Long targetTime) {
    Map<String, Object> urlParameter = this.createUrlParameter();
    urlParameter.put("tabUniqueId", userTableUniqueId);
    String formatTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(targetTime), ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm"));
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
  public RestaurantDishVO restaurantDish(String userTableUniqueId, Long targetTime, String restaurantUniqueId) {
    Map<String, Object> urlParameter = this.createUrlParameter();
    urlParameter.put("tabUniqueId", userTableUniqueId);
    urlParameter.put("restaurantUniqueId", restaurantUniqueId);
    String formatTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(targetTime), ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm"));
    urlParameter.put("targetTime", formatTime);
    try {
      String response = this.get(Constants.URL_RESTAURANTS_SHOW, urlParameter);
      return JsonUtil.from(response, RestaurantDishVO.class);
    } catch (IOException e) {
      throw new HttpRequestException(Constants.URL_RESTAURANTS_SHOW + " request error.");
    }
  }

  /**
   * 下单
   *
   * @param userTableUniqueId {@link UserTabVO#getUniqueId()}
   * @param addressUniqueId   {@link Address#getUniqueId()}
   * @param targetTime        {@link CalendarVO.CalendarItem#getTargetTime()}
   * @param dishId            {@link Dish#getId()}
   */
  public void order(String userTableUniqueId, String addressUniqueId, Long targetTime, Long dishId) {
    List<BasicNameValuePair> parameters = new ArrayList<>();
    parameters.add(new BasicNameValuePair("corpAddressRemark", ""));
    parameters.add(new BasicNameValuePair("corpAddressUniqueId", addressUniqueId));
    List<HashMap<String, Object>> order = Collections.singletonList(new HashMap<String, Object>(2) {
      {
        put("count", 1);
        put("dishId", dishId);
      }
    });
    parameters.add(new BasicNameValuePair("order", "[{\"count\":1,\"dishId\":" + dishId + "}]"));
    List<HashMap<String, Object>> remarks = Collections.singletonList(new HashMap<String, Object>(2) {
      {
        put("dishId", dishId);
        put("remark", "1");
      }
    });
    parameters.add(new BasicNameValuePair("remarks", "[{\"dishId\":\"" + dishId + "\",\"remark\":\"\"}]"));
    parameters.add(new BasicNameValuePair("tabUniqueId", userTableUniqueId));
    String formatTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(targetTime), ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    parameters.add(new BasicNameValuePair("targetTime", formatTime));
    parameters.add(new BasicNameValuePair("userAddressUniqueId", addressUniqueId));
    Map<String, Object> urlParameter = this.createUrlParameter();
    try {
      String responseStr = this.post(Constants.URL_ADD_ORDER, urlParameter, parameters);
      AddOrderResponse addOrderResponse = JsonUtil.from(responseStr, AddOrderResponse.class);
      if (!addOrderResponse.isSuccess()) {
        throw new MeiCanAddOrderException("order error." + responseStr);
      }
    } catch (IOException e) {
      throw new HttpRequestException(Constants.URL_ADD_ORDER + " request error", e);
    }
  }

  public MeiCanPanelData getMeiCanPanelData(LocalDateTime targetDateTime) {
    // 可点菜单
    String time = targetDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    MeiCanPanelData meiCanPanelData = CACHE.getIfPresent(time);
    if (Objects.nonNull(meiCanPanelData)) {
      return meiCanPanelData;
    }
    CalendarVO calendar = this.calendar(time, time);
    CalendarVO.DateItem dateItem = calendar.getDateList().get(0);
    List<MeiCanPanelData.DateData> dateDataList = dateItem.getCalendarItemList().stream().map(calendarItem -> {
      MeiCanPanelData.DateData dateData = new MeiCanPanelData.DateData();
      UserTabVO userTab = calendarItem.getUserTab();
      Long targetTime = calendarItem.getTargetTime();

      dateData.setTitle(calendarItem.getTitle());
      dateData.setOpen(calendarItem.isOpen());
      dateData.setUserTab(userTab);
      dateData.setTargetTime(targetTime);
      // 地址数据
      List<Address> addressList = userTab.getCorp().getAddressList();
      dateData.setAddressList(addressList);

      // 状态和理由
      dateData.setStatus(calendarItem.getStatus());
      dateData.setReason(calendarItem.getReason());

      // 已下单信息
      if (Objects.nonNull(calendarItem.getCorpOrderUser()) &&
          CollectionUtils.isNotEmpty(calendarItem.getCorpOrderUser().getRestaurantItemList())) {
        CalendarRestaurantVO calendarRestaurantVO = calendarItem.getCorpOrderUser().getRestaurantItemList().get(0);
        if (CollectionUtils.isNotEmpty(calendarRestaurantVO.getDishItemList())) {
          dateData.setDish(calendarRestaurantVO.getDishItemList().get(0).getDish());
        }
      }

      List<MeiCanPanelData.RestaurantData> restaurantDataList = new ArrayList<>();
      if (calendarItem.isOpen()) {
        // 餐厅数据
        RestaurantVO restaurants = this.restaurants(userTab.getUniqueId(), targetTime);
        restaurantDataList = restaurants.getRestaurantList().stream()
            .map(restaurant -> {
              MeiCanPanelData.RestaurantData restaurantData = new MeiCanPanelData.RestaurantData();
              restaurantData.setName(restaurant.getName());
              restaurantData.setOpen(restaurant.getOpen());
              restaurantData.setUniqueId(restaurant.getUniqueId());
              // dish
              RestaurantDishVO restaurantDish = this.restaurantDish(userTab.getUniqueId(), targetTime,
                  restaurant.getUniqueId());
              Set<Dish> dishSet = restaurantDish.getDishList().stream().filter(dish -> !dish.getSection())
                  .collect(Collectors.toSet());
              restaurantData.setDishList(new ArrayList<>(dishSet));
              return restaurantData;
            }).collect(Collectors.toList());
      }
      dateData.setRestaurants(restaurantDataList);
      return dateData;
    }).collect(Collectors.toList());
    MeiCanPanelData data = new MeiCanPanelData(dateDataList);
    CACHE.put(time, data);
    return data;
  }


  private void setHeader(HttpRequestBase httpRequest) {
    httpRequest.addHeader("User-Agent", Constants.USER_AGENT);
    httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
  }

  private Map<String, Object> createUrlParameter() {
    Map<String, Object> map = new HashMap<>(16);
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
