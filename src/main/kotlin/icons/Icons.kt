package icons

import com.intellij.ui.IconManager
import javax.swing.Icon

object Icons {
    @JvmField
    val MeiCan: Icon = load("/icons/meican.svg")

    @JvmStatic
    private fun load(path: String): Icon {
        return IconManager.getInstance().getIcon(path, Icons::class.java)
    }
}
