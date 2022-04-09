package cn.motui.meican.util

import cn.motui.meican.MeiCanClient
import cn.motui.meican.Settings
import cn.motui.meican.service.DataService
import com.intellij.notification.Notification
import com.intellij.notification.Notifications
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
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
inline val dataService: DataService get() = application.getService(DataService::class.java)

/**
 * Asserts whether the method is being called from the event dispatch thread.
 */
fun assertIsDispatchThread() = application.assertIsDispatchThread()

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