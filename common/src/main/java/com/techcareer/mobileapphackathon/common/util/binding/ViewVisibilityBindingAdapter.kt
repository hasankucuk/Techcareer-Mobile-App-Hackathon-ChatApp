package com.techcareer.mobileapphackathon.common.util.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.techcareer.mobileapphackathon.common.util.exteinsion.gone
import com.techcareer.mobileapphackathon.common.util.exteinsion.visible


/**
 * @author: Hasan Küçük on 11.10.2021
 */

@BindingAdapter("ifNotEmptyVisibleElseGone")
fun ifNotEmptyVisibleElseGone(view: View, charSequence: CharSequence?) {
    if (charSequence.isNullOrEmpty()) view.gone() else view.visible()
}

@BindingAdapter("visibleElseGone")
fun visibleElseGone(view: View, visible: Boolean?) {
    if (visible != null && visible) view.visible() else view.gone()
}
