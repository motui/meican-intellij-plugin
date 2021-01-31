package cn.motui.meican.ui

import cn.motui.meican.ui.order.OrderToolWindow
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

/**
 * 工具窗口工厂
 */
class OrderToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val orderToolWindow = OrderToolWindow(toolWindow)
        orderToolWindow.renderUi()
    }
}