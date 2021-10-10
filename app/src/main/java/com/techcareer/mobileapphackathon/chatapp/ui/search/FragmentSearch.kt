package com.techcareer.mobileapphackathon.chatapp.ui.search

import android.os.Bundle
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

/**
 * @author: Hasan Küçük on 10.10.2021
 */

@AndroidEntryPoint
class FragmentSearch : BaseFragment<FragmentSearchBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_search

    override fun getMenuId(): Int = R.menu.menu_home

    private val searchViewModel: SearchViewModel by activityViewModels()
    private var searchAdapter = SearchAdapter(emptyList(), object : SearchListener {
        override fun onUserClicked(user: UserModel) {

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
        val searchItem = menu.findItem(R.id.search) ?: return
        val searchView = (searchItem.actionView as SearchView)
        searchView.setOnQueryTextFocusChangeListener { view, focus ->
            if (focus) {
                binding.root.visible()
            } else {
                searchItem.collapseActionView()
                binding.root.gone()
            }
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