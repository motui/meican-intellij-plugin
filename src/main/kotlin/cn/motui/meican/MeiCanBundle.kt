package cn.motui.meican

import com.intellij.AbstractBundle
import org.jetbrains.annotations.PropertyKey


/**
 * message
 */
const val BUNDLE = "messages.MeiCanBundle"

object MeiCanBundle : AbstractBundle(BUNDLE) {

    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
        return MeiCanBundle.getMessage(key, *params);
    }
}