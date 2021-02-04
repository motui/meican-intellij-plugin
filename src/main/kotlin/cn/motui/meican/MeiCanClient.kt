package cn.motui.meican

import cn.motui.meican.exception.HttpRequestException
import cn.motui.meican.exception.MeiCanAddOrderException
import cn.motui.meican.exception.MeiCanLoginException
import cn.motui.meican.model.api.Account
import cn.motui.meican.model.api.vo.AddOrderResponseVO
import cn.motui.meican.model.api.vo.CalendarVO
import cn.motui.meican.model.api.vo.CorpAddressVO
import cn.motui.meican.model.api.vo.LoginVO
import cn.motui.meican.model.api.vo.OrderDetailVO
import cn.motui.meican.model.api.vo.RestaurantDishVO
import cn.motui.meican.model.api.vo.RestaurantVO
import cn.motui.meican.util.JsonUtil
import cn.motui.meican.util.settings
import org.apache.commons.collections.MapUtils
import org.apache.commons.httpclient.HttpStatus
import org.apache.commons.lang3.StringUtils
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.jetbrains.annotations.NotNull
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 美餐客户端
 */
class MeiCanClient {
    private var username: String? = null
    private var password: String? = null
    private val httpClient: HttpClient = HttpClients.custom().setDefaultCookieStore(BasicCookieStore()).build()

    init {
        this.username = settings.account.username
        this.password = settings.account.getPassword()
        login()
    }

    /**
     * 刷新账户信息
     */
    fun refresh(username: String?, password: String?) {
        if (username?.isNotBlank() == true and (password?.isNotBlank() == true)) {
            this.username = username
            this.password = password
            login()
        }
    }

    /**
     * 登录
     */
    private fun login() {
        if (username?.isNotBlank() == true and (password?.isNotBlank() == true)) {
            val parameters: MutableList<BasicNameValuePair> = ArrayList()
            parameters.add(BasicNameValuePair("username", username))
            parameters.add(BasicNameValuePair("username_type", "username"))
            parameters.add(BasicNameValuePair("password", password))
            parameters.add(BasicNameValuePair("meican_credential_type", "password"))
            parameters.add(BasicNameValuePair("grant_type", "password"))
            val urlParameter: MutableMap<String, Any> = urlParameter()
            urlParameter["remember"] = true
            try {
                val responseStr = post(MEI_CAN_URL_LOGIN, urlParameter, parameters)
                val loginVO = JsonUtil.from(responseStr, LoginVO::class.java)
                if (!loginVO.isSuccess) {
                    throw MeiCanLoginException(loginVO.errorDescription)
                }
            } catch (e: IOException) {
                throw MeiCanLoginException(e)
            }
        }
    }

    /**
     * 用户信息
     *
     * @return Account
     */
    fun account(): Account {
        val urlParameter: Map<String, Any> = this.urlParameter()
        return try {
            val response = this[MEI_CAN_URL_ACCOUNT, urlParameter]
            JsonUtil.from(response, Account::class.java)
        } catch (e: IOException) {
            throw HttpRequestException("$MEI_CAN_URL_ACCOUNT request error.")
        }
    }

    fun address(corpNamespace: String): CorpAddressVO? {
        val urlParameter: MutableMap<String, Any> = this.urlParameter()
        urlParameter["namespace"] = corpNamespace
        try {
            val response = this[MEI_CAN_URL_ADDRESS, urlParameter]
            val corpAddress = JsonUtil.from(response, CorpAddressVO::class.java)
            if (corpAddress.isSuccess) {
                return corpAddress
            }
            throw HttpRequestException(MEI_CAN_URL_ADDRESS + " request error." + corpAddress.resultDescription)
        } catch (e: IOException) {
            throw HttpRequestException("$MEI_CAN_URL_ADDRESS request error.")
        }
    }

    /**
     * 可点菜单
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return Calendar
     */
    fun calendar(startDateTime: LocalDateTime, endDateTime: LocalDateTime): CalendarVO {
        val urlParameter: MutableMap<String, Any> = this.urlParameter()
        urlParameter["beginDate"] = startDateTime.format(FORMATTER_RECORD_MEI_CAN)
        urlParameter["endDate"] = endDateTime.format(FORMATTER_RECORD_MEI_CAN)
        urlParameter["withOrderDetail"] = false
        return try {
            val response = this[MEI_CAN_URL_CALENDAR, urlParameter]
            JsonUtil.from(response, CalendarVO::class.java)
        } catch (e: IOException) {
            throw HttpRequestException("$MEI_CAN_URL_CALENDAR request error.")
        }
    }

    /**
     * 餐厅列表
     *
     * @param userTableUniqueId [UserTabVO.getUniqueId]
     * @param targetTime        [CalendarVO.CalendarItem.getTargetTime]
     * @return RestaurantVO
     */
    fun restaurants(userTableUniqueId: String, targetTime: LocalDateTime): RestaurantVO {
        val urlParameter: MutableMap<String, Any> = this.urlParameter()
        urlParameter["tabUniqueId"] = userTableUniqueId
        val formatTime = targetTime.format(FORMATTER_TIME_MEI_CAN)
        urlParameter["targetTime"] = formatTime
        return try {
            val response = this[MEI_CAN_URL_RESTAURANTS, urlParameter]
            JsonUtil.from(response, RestaurantVO::class.java)
        } catch (e: IOException) {
            throw HttpRequestException("$MEI_CAN_URL_RESTAURANTS request error.")
        }
    }

    /**
     * 菜单
     *
     * @param userTableUniqueId  [UserTabVO.getUniqueId]
     * @param targetTime         [CalendarVO.CalendarItem.getTargetTime]
     * @param restaurantUniqueId [RestaurantDishVO.getUniqueId]
     * @return RestaurantDishVO
     */
    fun restaurantDish(
        userTableUniqueId: String,
        targetTime: LocalDateTime,
        restaurantUniqueId: String
    ): RestaurantDishVO? {
        val urlParameter: MutableMap<String, Any> = this.urlParameter()
        urlParameter["tabUniqueId"] = userTableUniqueId
        urlParameter["restaurantUniqueId"] = restaurantUniqueId
        val formatTime = targetTime.format(FORMATTER_TIME_MEI_CAN)
        urlParameter["targetTime"] = formatTime
        return try {
            val response = this[MEI_CAN_URL_RESTAURANTS_SHOW, urlParameter]
            JsonUtil.from(response, RestaurantDishVO::class.java)
        } catch (e: IOException) {
            throw HttpRequestException("$MEI_CAN_URL_RESTAURANTS_SHOW request error.")
        }
    }

    /**
     * 订单详情
     *
     * @param orderUniqueId 订单编号
     * @return OrderDetailVO
     */
    fun orderDetail(orderUniqueId: String): OrderDetailVO {
        val urlParameter: MutableMap<String, Any> = this.urlParameter()
        urlParameter["uniqueId"] = orderUniqueId
        urlParameter["type"] = "CORP_ORDER"
        urlParameter["progressMarkdownSupport"] = true
        urlParameter["x"] = System.currentTimeMillis()
        return try {
            val response = this[MEI_CAN_URL_ORDER_SHOW, urlParameter]
            JsonUtil.from(response, OrderDetailVO::class.java)
        } catch (e: IOException) {
            throw HttpRequestException("$MEI_CAN_URL_ORDER_SHOW request error.")
        }
    }

    /**
     * 下单
     *
     * @param userTabUniqueId [UserTabVO.getUniqueId]
     * @param addressUniqueId [AddressVO.getUniqueId]
     * @param targetTime      [CalendarVO.CalendarItem.getTargetTime]
     * @param dishId          [Dish.getId]
     * @return 订单ID
     */
    fun order(
        userTabUniqueId: String,
        addressUniqueId: String,
        targetTime: LocalDateTime,
        dishId: Long
    ): String {
        val parameters: MutableList<BasicNameValuePair> = ArrayList()
        parameters.add(BasicNameValuePair("corpAddressRemark", ""))
        parameters.add(BasicNameValuePair("corpAddressUniqueId", addressUniqueId))
        parameters.add(BasicNameValuePair("order", "[{\"count\":1,\"dishId\":$dishId}]"))
        parameters.add(BasicNameValuePair("remarks", "[{\"dishId\":\"$dishId\",\"remark\":\"\"}]"))
        parameters.add(BasicNameValuePair("tabUniqueId", userTabUniqueId))
        val formatTime = targetTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        parameters.add(BasicNameValuePair("targetTime", formatTime))
        parameters.add(BasicNameValuePair("userAddressUniqueId", addressUniqueId))
        val urlParameter: Map<String, Any> = urlParameter()
        return try {
            val responseStr = post(MEI_CAN_URL_ADD_ORDER, urlParameter, parameters)
            val addOrderResponseVO = JsonUtil.from(
                responseStr,
                AddOrderResponseVO::class.java
            )
            if (!addOrderResponseVO.isSuccess) {
                throw MeiCanAddOrderException("order error.$responseStr")
            }
            addOrderResponseVO.order.uniqueId
        } catch (e: IOException) {
            throw HttpRequestException("$MEI_CAN_URL_ADD_ORDER request error", e)
        }
    }

    private fun setHeader(httpRequest: HttpRequestBase) {
        httpRequest.addHeader("User-Agent", USER_AGENT)
        httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded")
    }

    private fun createUrl(@NotNull url: String, urlParameter: Map<String, Any>): String {
        var finalUrl: String = url
        if (MapUtils.isNotEmpty(urlParameter)) {
            val parameter: MutableList<String> = ArrayList()
            urlParameter.forEach { (k: String, v: Any?) ->
                parameter.add(
                    String.format("%s=%s", k, v)
                )
            }
            finalUrl = String.format("%s?%s", url, StringUtils.join(parameter, "&"))
        }
        return finalUrl
    }

    private fun urlParameter(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["client_id"] = MEI_CAN_CLIENT_ID
        map["client_secret"] = MEI_CAN_CLIENT_SECRET
        return map
    }

    @Throws(IOException::class)
    private fun post(url: String, urlParameter: Map<String, Any>, parameters: List<BasicNameValuePair>): String? {
        val httpPost = HttpPost(createUrl(url, urlParameter))
        this.setHeader(httpPost)
        httpPost.entity = UrlEncodedFormEntity(parameters)
        val response = httpClient.execute(httpPost)
        if (HttpStatus.SC_OK != response.statusLine.statusCode) {
            throw HttpRequestException("request error. httpStatusCode:" + response.statusLine.statusCode)
        }
        return EntityUtils.toString(response.entity)
    }

    @Throws(IOException::class)
    private operator fun get(url: String, urlParameter: Map<String, Any>): String? {
        val httpGet = HttpGet(this.createUrl(url, urlParameter))
        this.setHeader(httpGet)
        val response = httpClient.execute(httpGet)
        if (HttpStatus.SC_OK != response.statusLine.statusCode) {
            throw HttpRequestException("request error. httpStatusCode:" + response.statusLine.statusCode)
        }
        return EntityUtils.toString(response.entity)
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MeiCanClient()
        }
    }
}
