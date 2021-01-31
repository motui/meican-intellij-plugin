/**
 * Constants
 *
 * @author zhihaoguo
 * @date 2021-01-31
 */
@file:Suppress("SpellCheckingInspection")

package cn.motui.meican

import java.time.format.DateTimeFormatter

const val STORAGE_NAME = "itmotui.meican.xml"

const val USER_AGENT =
    "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

const val MEI_CAN_BASE_URL = "https://meican.com"
const val MEI_CAN_CLIENT_ID = "Xqr8w0Uk4ciodqfPwjhav5rdxTaYepD"
const val MEI_CAN_CLIENT_SECRET = "vD11O6xI9bG3kqYRu9OyPAHkRGxLh4E"
const val MEI_CAN_URL_LOGIN = "$MEI_CAN_BASE_URL/preference/preorder/api/v2.0/oauth/token"
const val MEI_CAN_URL_ACCOUNT = "$MEI_CAN_BASE_URL/preorder/api/v2.1/accounts/show"
const val MEI_CAN_URL_CALENDAR = "$MEI_CAN_BASE_URL/preorder/api/v2.1/calendaritems/list"
const val MEI_CAN_URL_RESTAURANTS = "$MEI_CAN_BASE_URL/preorder/api/v2.1/restaurants/list"
const val MEI_CAN_URL_RESTAURANTS_SHOW = "$MEI_CAN_BASE_URL/preorder/api/v2.1/restaurants/show"
const val MEI_CAN_URL_ADD_ORDER = "$MEI_CAN_BASE_URL/preorder/api/v2.1/orders/add"
const val MEI_CAN_URL_ORDER_SHOW = "$MEI_CAN_BASE_URL/preorder/api/v2.1/orders/show"
const val MEI_CAN_URL_ADDRESS = "$MEI_CAN_BASE_URL/preorder/api/v2.1/corpaddresses/getmulticorpaddress"


const val HTML_DESCRIPTION_SETTINGS = "#SETTINGS"

val FORMATTER_RECORD_MEI_CAN: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
val FORMATTER_TIME_MEI_CAN: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm")

const val NOTIFICATIONS_ID: String = "mei can"



