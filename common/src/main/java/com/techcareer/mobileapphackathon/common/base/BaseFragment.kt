package com.techcareer.mobileapphackathon.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * @author: Hasan Küçük on 9.10.2021
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    lateinit var binding: VB

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        }
        return binding.root
    }

}