package cn.motui.meican.ui

import com.intellij.ui.JBColor
import java.awt.Color

/**
 * UI
 */
object UI {
    @JvmStatic
    fun transparentColor(): JBColor {
        return JBColor(Color(0, 0, 0, 0), Color(0, 0, 0, 0))
    }
}