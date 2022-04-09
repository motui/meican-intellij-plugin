package cn.motui.meican.ui.order

import cn.motui.meican.MeiCanBundle
import cn.motui.meican.util.application
import cn.motui.meican.util.assertIsDispatchThread
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ex.ToolWindowEx

/**
 * OrderView
 */
class OrderView {
    private var toolWindow: ToolWindow? = null
    private val orderPanel: OrderPanel = OrderPanel()

    fun setup(project: Project, toolWindow: ToolWindow) {
        project.basePath
        assertIsDispatchThread()
        this.toolWindow = toolWindow
        (toolWindow as ToolWindowEx).apply {
            setTitleActions(listOf(RefreshAction()))
        }
        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(orderPanel, null, false)
        contentManager.addContent(content)
    }

    fun refreshUi() {
        orderPanel.refreshUi()
    }

    inner class RefreshAction :
        DumbAwareAction(MeiCanBundle.message("order.tool.window.refresh"), null, AllIcons.Actions.Refresh) {
        override fun actionPerformed(e: AnActionEvent) {
            orderPanel.refreshUi()
        }
    }

    companion object {
        val instance: OrderView
            get() = application.getService(OrderView::class.java)
    }
}
