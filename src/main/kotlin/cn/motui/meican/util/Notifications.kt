@file:Suppress("MemberVisibilityCanBePrivate")

package cn.motui.meican.util

import cn.motui.meican.HTML_DESCRIPTION_SETTINGS
import cn.motui.meican.ui.settings.OptionsConfigurable
import com.intellij.notification.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import javax.swing.event.HyperlinkEvent

object Notifications {

    fun showErrorNotification(
        project: Project?,
        displayId: String,
        title: String,
        message: String,
        vararg actions: AnAction
    ) {
        NotificationGroup(displayId, NotificationDisplayType.BALLOON, true)
            .createNotification(
                title,
                message,
                NotificationType.ERROR,
                object : NotificationListener.Adapter() {
                    override fun hyperlinkActivated(notification: Notification, event: HyperlinkEvent) {
                        notification.expire()
                        when (event.description) {
                            HTML_DESCRIPTION_SETTINGS -> OptionsConfigurable.showSettingsDialog(project)
                        }
                    }
                })
            .apply { for (action in actions) addAction(action) }
            .show(project)
    }

    fun showNotification(
        displayId: String,
        title: String,
        message: String,
        type: NotificationType,
        project: Project? = null
    ) {
        NotificationGroup(displayId, NotificationDisplayType.BALLOON, true)
            .createNotification(title, message, type, null)
            .show(project)
    }

    fun showInfoNotification(displayId: String, title: String, message: String, project: Project? = null) {
        showNotification(displayId, title, message, NotificationType.INFORMATION, project)
    }

    fun showWarningNotification(displayId: String, title: String, message: String, project: Project? = null) {
        showNotification(displayId, title, message, NotificationType.WARNING, project)
    }

    fun showErrorNotification(displayId: String, title: String, message: String, project: Project? = null) {
        showNotification(displayId, title, message, NotificationType.ERROR, project)
    }


}