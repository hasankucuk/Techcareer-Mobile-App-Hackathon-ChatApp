package com.techcareer.mobileapphackathon.chatapp.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.FragmentLoginBinding
import com.techcareer.mobileapphackathon.common.base.BaseFragment
import com.techcareer.mobileapphackathon.common.util.exteinsion.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author: Hasan Küçük on 10.10.2021
 */

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_login

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = loginViewModel

        lifecycleScope.launch {
            loginViewModel.loginState.collect {
                when (it) {
                    LoginState.DontHaveAnyUser -> {
                        navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
                    }
                    LoginState.Success -> {
                        navigate(LoginFragmentDirections.actionLoginFragmentToChatRoomListFragment())
                    }
                    is LoginState.Fail -> {
                        binding.root.showSnackBar(it.message)
                    }
                    null -> {
                    }
                }
            }
        }
    }
}