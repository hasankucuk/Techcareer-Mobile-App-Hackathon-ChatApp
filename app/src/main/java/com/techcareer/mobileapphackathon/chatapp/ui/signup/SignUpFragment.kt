package com.techcareer.mobileapphackathon.chatapp.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.FragmentSignUpBinding
import com.techcareer.mobileapphackathon.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_sign_up

    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = signUpViewModel

        binding.signUpBtn.setOnClickListener {
            lifecycleScope.launch {
                Log.i("TWO_WAY", "test -> ${signUpViewModel.userName.value}")
            }
        }

    }
}