package com.techcareer.mobileapphackathon.chatapp.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.FragmentSignUpBinding
import com.techcareer.mobileapphackathon.common.base.BaseFragment
import com.techcareer.mobileapphackathon.common.util.exteinsion.showSnackBar
import com.techcareer.mobileapphackathon.common.view.LoadingView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_sign_up

    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = signUpViewModel

        lifecycleScope.launch {
            signUpViewModel.signUpState.collect {
                when (it) {
                    is SignUpState.Fail -> {
                        it.message?.let {
                            binding.root.showSnackBar(it)
                        }
                        hideLoading()
                    }
                    SignUpState.Success, SignUpState.AlreadyExistUser -> {
                        navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                        hideLoading()
                    }

                    SignUpState.LoadingState -> {
                       showLoading()
                    }
                    null -> {
                        hideLoading()
                    }
                }
            }
        }

    }
}