package com.techcareer.mobileapphackathon.common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Hasan Küçük on 10.10.2021
 */

abstract class BaseAdapter<VB : ViewDataBinding, T : ListAdapterItem>(
    var data: List<T>
) : RecyclerView.Adapter<BaseViewHolder<VB>>() {

    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun bind(binding: VB, item: T)

    fun updateData(list: List<T>?) {
      list?.let {
          this.data = list
      }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val binder = DataBindingUtil.inflate<VB>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )

        return BaseViewHolder(binder)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        bind(holder.binder, data[position])
    }

    override fun getItemCount(): Int = data.size
}

class BaseViewHolder<VB : ViewDataBinding>(val binder: VB) :
    RecyclerView.ViewHolder(binder.root) {
}


interface ListAdapterItem {
    val id: Long
}