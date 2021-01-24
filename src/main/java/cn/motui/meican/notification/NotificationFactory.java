package cn.motui.meican.notification;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * 通知
 *
 * @author it.motui
 * @date 2021-01-24
 */
public class NotificationFactory {
  private final static NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("Mei Can",
      NotificationDisplayType.STICKY_BALLOON, true);

  public static void showInfoNotification(String content) {
    Notifications.Bus.notify(NOTIFICATION_GROUP.createNotification(content, NotificationType.INFORMATION));
  }

  public static void showWarningNotification(String content) {
    Notifications.Bus.notify(NOTIFICATION_GROUP.createNotification(content, NotificationType.WARNING));
  }

  public static void showErrorNotification(String content) {
    Notifications.Bus.notify(NOTIFICATION_GROUP.createNotification(content, NotificationType.ERROR));
  }


}
