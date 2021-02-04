package cn.motui.meican

import cn.motui.meican.model.TabType
import cn.motui.meican.ui.settings.Automatic
import cn.motui.meican.ui.settings.Cycle
import cn.motui.meican.ui.settings.NoticeTime
import cn.motui.meican.ui.settings.Tab
import cn.motui.meican.ui.settings.TabShow
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

    /**
     * 订餐
     */
    var order: Order = Order()

    /**
     * 其他
     */
    var other: Other = Other()

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
     * 类型
     */
    var tab: Tab = Tab.NO,

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
     * Cron表达式
     */
    private fun cron(hour: Int): String {
        return beforeClosingTime.cron(hour) + " " + cycle.cron
    }

    fun cron(tabType: TabType): String {
        return if (TabType.AM == tabType) {
            cron(10)
        } else {
            cron(15)
        }
    }

    fun isNotice(tabType: TabType): Boolean {
        return when (tab) {
            Tab.NO -> false
            Tab.ALL -> true
            Tab.AM -> TabType.AM == tabType
            else -> TabType.PM == tabType
        }
    }
}

/**
 * 点餐
 */
class Order constructor(
    var automatic: Automatic = Automatic.NO,
    var cycle: Cycle = Cycle.MONDAY_TO_FRIDAY,
) {
    fun isOrderAutomatic(tabType: TabType): Boolean {
        return when (automatic) {
            Automatic.NO -> false
            Automatic.ALL -> true
            Automatic.AM -> TabType.AM == tabType
            else -> TabType.PM == tabType
        }
    }

    fun cron(tabType: TabType): String {
        return if (TabType.AM == tabType) {
            "0 55 9 " + cycle.cron
        } else {
            "0 55 14 " + cycle.cron
        }
    }
}

/**
 * 其他
 */
class Other constructor(
    var tabShow: TabShow = TabShow.ALL
) {

    fun isTabShow(tabType: TabType): Boolean {
        return when (tabShow) {
            TabShow.ALL -> true
            TabShow.AM -> TabType.AM == tabType
            else -> TabType.PM == tabType
        }
    }
}
