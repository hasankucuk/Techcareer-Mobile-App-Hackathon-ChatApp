package com.techcareer.mobileapphackathon.common.base

import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController

/**
 * @author: Hasan Küçük on 9.10.2021
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    lateinit var binding: VB

    @LayoutRes
    abstract fun getLayoutId(): Int

    @MenuRes
    open fun getMenuId() = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            binding.lifecycleOwner = viewLifecycleOwner
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    protected fun navigate(
        navDirections: NavDirections,
        navigatorExtras: Navigator.Extras? = null
    ) {
        // TODO: Duruma göre buraya animasyon ekleyebilirim.
        findNavController().navigate(
            navDirections.actionId,
            navDirections.arguments,
            null,
            navigatorExtras
        )
//        findNavController().navigateUp()
    }

    @CallSuper
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()

        if (getMenuId() != -1) {
            inflater.inflate(getMenuId(), menu)
        }
    }

}