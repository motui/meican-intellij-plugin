package cn.motui.meican

import cn.motui.meican.ui.settings.NoticeCycle
import cn.motui.meican.ui.settings.NoticeTime
import cn.motui.meican.util.PasswordSafeDelegate
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Transient

/**
 * Settings
 */
@State(name = "MeiCanSettings", storages = [(Storage(STORAGE_NAME))])
class Settings : PersistentStateComponent<Settings> {

    /**
     * 账户选项
     */
    var account: Account = Account()

    /**
     * 通知选项
     */
    var notice: Notice = Notice()

    override fun getState(): Settings = this

    override fun loadState(state: Settings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: Settings
            get() = ServiceManager.getService(Settings::class.java)
    }
}

@Suppress("SpellCheckingInspection")
private const val MEICAN_SERVICE_NAME = "ITMOTUI.MEI_CAN"

/**
 * Account
 */
class Account constructor(
    var username: String = "",
    var verification: Boolean = false
) {
    private var _password: String? by PasswordSafeDelegate(MEICAN_SERVICE_NAME, username)
        @Transient get
        @Transient set

    @Transient
    fun getPassword(): String = _password?.trim() ?: ""

    @Transient
    fun setPassword(value: String?) {
        _password = if (value.isNullOrBlank()) null else value
    }

    /**
     * 是否已验证
     */
    @Transient
    fun isVerified(): Boolean {
        return username.isNotBlank() and getPassword().isNotBlank() and verification
    }
}

/**
 * Notice
 */
class Notice constructor(
    /**
     * 午餐
     */
    var am: Boolean = false,

    /**
     * 晚餐
     */
    var pm: Boolean = false,

    /**
     * 周期
     */
    var cycle: NoticeCycle = NoticeCycle.EVERYDAY,

    /**
     * 截止前{time}通知
     */
    var beforeClosingTime: NoticeTime = NoticeTime.M30
) {

    /**
     * Cron表达式
     */
    fun cron(hour: Int): String {
        return beforeClosingTime.cron(hour) + " " + cycle.cron
    }

    fun amCron(): String {
        return cron(10)
    }

    fun pmCron(): String {
        return cron(15)
    }
}