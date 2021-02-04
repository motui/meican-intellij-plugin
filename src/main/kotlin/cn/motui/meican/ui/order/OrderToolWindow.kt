package cn.motui.meican.ui.order

import cn.motui.meican.MeiCanBundle.message
import cn.motui.meican.ui.EmptyForm
import cn.motui.meican.util.settings
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ex.ToolWindowEx
import javax.swing.JPanel

/**
 * 订餐工具窗口
 */
class OrderToolWindow constructor(
    private val toolWindow: ToolWindow
) {
    init {
        (toolWindow as ToolWindowEx).setTitleActions(RefreshAction())
    }

    fun renderUi() {
        val contentManager = toolWindow.contentManager
        contentManager.removeAllContents(true)
        var contentPanel: JPanel? = null
        contentPanel = if (settings.account.isVerified()) {
            OrderPanel().root()
        } else {
            EmptyForm(emptyContent()).root
        }
        val content = contentManager.factory.createContent(contentPanel, null, false)
        contentManager.addContent(content)
    }

    private fun emptyContent(): String {
        return "<p>%s</p><p><b>Settings/Preferences</b> > <b>Other Settings</b> > <b>Mei Can</b> > <b>Account</b></p>"
            .format(message("order.tool.window.empty.content"))
    }

    inner class RefreshAction : DumbAwareAction(message("order.tool.window.refresh"), null, AllIcons.Actions.Refresh) {
        override fun actionPerformed(e: AnActionEvent) {
            renderUi()
        }
    }
}
