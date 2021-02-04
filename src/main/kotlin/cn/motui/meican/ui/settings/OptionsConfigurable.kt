package cn.motui.meican.ui.settings

import cn.motui.meican.MeiCanBundle.message
import cn.motui.meican.util.settings
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * 选项配置
 */
class OptionsConfigurable : SearchableConfigurable, Disposable {
    private var settingsPanel: SettingsPanel? = null

    @Suppress("SpellCheckingInspection")
    override fun getId(): String = "itmotui.plugin.meican"

    override fun getDisplayName(): String = message("setting.page.name")

    override fun createComponent(): JComponent = SettingsPanel(settings).let {
        settingsPanel = it
        it.root
    }

    override fun reset() {
        settingsPanel?.reset()
    }

    override fun isModified(): Boolean {
        return settingsPanel?.isModified ?: false
    }

    override fun apply() {
        settingsPanel?.apply()
    }

    override fun dispose() {
        settingsPanel = null
    }

    companion object {
        fun showSettingsDialog(project: Project? = null) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, OptionsConfigurable::class.java)
        }
    }
}
