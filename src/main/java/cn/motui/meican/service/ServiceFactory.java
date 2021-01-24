package cn.motui.meican.service;

import java.util.Objects;

/**
 * @author it.motui
 * @date 2021-01-30
 */
public class ServiceFactory {
  private static volatile DataService dataService;

  public static DataService dataService() {
    if (Objects.isNull(dataService)) {
      synchronized (ServiceFactory.class) {
        if (Objects.isNull(dataService)) {
          dataService = new DataServiceImpl();
        }
      }
    }
    return dataService;
  }
}
