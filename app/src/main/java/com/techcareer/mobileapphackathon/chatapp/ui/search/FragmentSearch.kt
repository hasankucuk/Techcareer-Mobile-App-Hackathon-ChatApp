package com.techcareer.mobileapphackathon.chatapp.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.FragmentSearchBinding
import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import com.techcareer.mobileapphackathon.common.base.BaseFragment
import com.techcareer.mobileapphackathon.common.util.exteinsion.gone
import com.techcareer.mobileapphackathon.common.util.exteinsion.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

/**
 * @author: Hasan Küçük on 10.10.2021
 */

@AndroidEntryPoint
class FragmentSearch : BaseFragment<FragmentSearchBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_search

    override fun getMenuId(): Int = R.menu.menu_home

    lateinit var searchItem: MenuItem

    private val searchViewModel: SearchViewModel by activityViewModels()
    private var searchAdapter = SearchAdapter(emptyList(), object : SearchListener {
        override fun onUserClicked(user: UserModel) {
            Log.i("SIGN_","tıkladın ${user.uid}")
            user.uid?.let {
                onSearchViewClose()
                searchViewModel.getChatRoom(user)
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.searchRecyclerView.adapter = searchAdapter
        binding.viewModel = searchViewModel

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //super.onCreateOptionsMenu(menu, inflater)
         searchItem = menu.findItem(R.id.search) ?: return
        val searchView = (searchItem.actionView as SearchView)
        searchView.setOnQueryTextFocusChangeListener { view, focus ->
            if (focus) {
                binding.root.visible()
            } else {
                onSearchViewClose()
            }
        }
    }
    private fun onSearchViewClose(){
        if (this::searchItem.isInitialized) {
            binding.root.gone()
            searchItem.collapseActionView()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        searchItem = menu.findItem(R.id.search) ?: return
//        searchView = searchItem.actionView as SearchView
//        setOnQueryTextFocusChangeListener()
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    private fun setOnQueryTextFocusChangeListener() {
//        searchView.setOnQueryTextFocusChangeListener { view, b ->
//            if (b.not()) onSearchViewClose()
//            else {
//                binding.root.visibility = View.VISIBLE
//                binding.searchView.visibility = View.GONE
//            }
//        }
//    }
}