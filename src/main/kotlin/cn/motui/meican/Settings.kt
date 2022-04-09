package cn.motui.meican

import cn.motui.meican.model.ui.OpeningTime
import cn.motui.meican.ui.settings.Cycle
import cn.motui.meican.ui.settings.NoticeTime
import cn.motui.meican.util.PasswordSafeDelegate
import cn.motui.meican.util.application
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Transient
import java.util.function.Function
import java.util.stream.Collectors

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
     * Tab
     */
    var tabs: Tabs = Tabs()

    /**
     * 通知选项
     */
    var notice: Notice = Notice()

    /**
     * 订餐
     */
    var order: Order = Order()

    override fun getState(): Settings = this

    override fun loadState(state: Settings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: Settings
            get() = application.getService(Settings::class.java)
    }
}

@Suppress("SpellCheckingInspection")
private const val MEICAN_SERVICE_NAME = "ITMOTUI.MEI_CAN"

/**
 * Account
 */
class Account constructor(
    var username: String? = null,
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
        return username?.isNotBlank() == true and getPassword().isNotBlank() and verification
    }
}

/**
 * Notice
 */
class Notice constructor(
    /**
     * 周期
     */
    var cycle: Cycle = Cycle.MONDAY_TO_FRIDAY,

    /**
     * 截止前{time}通知
     */
    var beforeClosingTime: NoticeTime = NoticeTime.M30
) {

    /**
     * cron 表达式
     * @param closeTime 关闭时间 HH:mm
     */
    fun corn(closeTime: String): String {
        val split = closeTime.split(":")
        return this.beforeClosingTime.cron(split[0].toInt(), split[1].toInt()) + " " + cycle.cron
    }
}

/**
 * 点餐
 */
class Order constructor(
    var cycle: Cycle = Cycle.MONDAY_TO_FRIDAY,
    /**
     * 截止前{time}通知
     */
    var beforeClosingTime: NoticeTime = NoticeTime.M30
) {

    /**
     * cron 表达式
     * @param closeTime 关闭时间 HH:mm
     */
    fun corn(closeTime: String): String {
        val split = closeTime.split(":")
        return this.beforeClosingTime.cron(split[0].toInt(), split[1].toInt()) + " " + cycle.cron
    }
}

/**
 * Tab数据
 */
class Tab constructor(
    var title: String = "",
    var openingTime: OpeningTime = OpeningTime(),
    var show: Boolean = true,
    var notice: Boolean = false,
    var automatic: Boolean = false
)

/**
 * Tab数据对象
 */
class Tabs constructor(
    var tabSet: Set<Tab> = mutableSetOf()
) {
    private fun toMap(): Map<String, Tab> {
        return tabSet.stream().collect(Collectors.toMap(Tab::title, Function.identity()))
    }

    fun get(title: String): Tab? {
        return this.toMap()[title]
    }

    fun contains(title: String): Boolean {
        return toMap().containsKey(title)
    }
}