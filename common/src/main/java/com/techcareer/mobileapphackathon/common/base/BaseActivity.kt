package com.techcareer.mobileapphackathon.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author: Hasan Küçük on 9.10.2021
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: VB

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
    }


}