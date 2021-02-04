@file:Suppress("unused")

package cn.motui.meican.ui

import com.intellij.openapi.ui.ComboBox

/**
 * 当前选中项
 */
inline var <reified E> ComboBox<E>.selected: E?
    get() = selectedItem as? E
    set(value) {
        selectedItem = value
    }
