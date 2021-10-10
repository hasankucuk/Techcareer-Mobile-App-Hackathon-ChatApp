package com.techcareer.mobileapphackathon.common.util.binding

import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.techcareer.mobileapphackathon.common.base.BaseAdapter
import com.techcareer.mobileapphackathon.common.base.ListAdapterItem


/**
 * @author: Hasan Küçük on 10.10.2021
 */

@BindingAdapter("listAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: BaseAdapter<ViewDataBinding, ListAdapterItem>?
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("listItem")
fun submitList(recyclerView: RecyclerView, list: List<ListAdapterItem>?) {
    val adapter = recyclerView.adapter as BaseAdapter<ViewDataBinding, ListAdapterItem>?
    adapter?.updateData(list ?: listOf())
}