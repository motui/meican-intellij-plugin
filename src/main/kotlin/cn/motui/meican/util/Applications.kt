package cn.motui.meican.util

import cn.motui.meican.MeiCanClient
import cn.motui.meican.Settings;
import cn.motui.meican.service.DataService
import com.intellij.notification.Notification
import com.intellij.notification.Notifications
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * ApplicationUtils
 */
inline val application: Application get() = ApplicationManager.getApplication()
inline val settings: Settings get() = Settings.instance
inline val meiCanClient: MeiCanClient get() = MeiCanClient.instance
inline val dataService: DataService get() = ServiceManager.getService(DataService::class.java)

/**
 * Shows the notification[Notification].
 */
fun Notification.show(project: Project? = null) {
    Notifications.Bus.notify(this, project)
}

/**
 * timeStamp to LocalDateTime[LocalDateTime]
 */
fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}