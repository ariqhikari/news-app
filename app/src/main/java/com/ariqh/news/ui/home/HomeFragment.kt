package com.ariqh.news.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ariqh.news.R
import com.ariqh.news.databinding.CustomToolbarBinding
import com.ariqh.news.databinding.FragmentHomeBinding
import com.ariqh.news.source.news.ArticleModel
import com.ariqh.news.source.news.CategoryModel
import com.ariqh.news.ui.detail.DetailActivity
import com.ariqh.news.ui.news.CategoryAdapter
import com.ariqh.news.ui.news.NewsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import timber.log.Timber

val homeModule = module {
    factory { HomeFragment() }
}

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var bindingToolbar: CustomToolbarBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bindingToolbar = binding.toolbar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        bindingToolbar.title = viewModel.title
        bindingToolbar.container.inflateMenu(R.menu.menu_search)
        val menu = binding.toolbar.container.menu
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                firstLoad()
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.query = it }
                return true
            }
        })

        binding.listCategory.adapter = categoryAdapter
        viewModel.category.observe(viewLifecycleOwner, {
            NewsAdapter.VIEW_TYPES = if(it!!.isEmpty()) 1 else 2
            firstLoad()
        })

        binding.listNews.adapter = newsAdapter
        viewModel.news.observe(viewLifecycleOwner, {
            Timber.e(it.articles.toString())
            if(viewModel.page == 1) newsAdapter.clear()
            newsAdapter.add(it.articles)
        })

        viewModel.message.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
        
        binding.scroll.setOnScrollChangeListener {
                v: NestedScrollView, _, scrollY, _, _ ->
            if(scrollY == v?.getChildAt(0)!!.measuredHeight - v?.measuredHeight) {
                if(viewModel.page <= viewModel.total && viewModel.loadMore.value == false) viewModel.fetch()
            }
        }
    }

    private fun firstLoad() {
        binding.scroll.scrollTo(0,0)
        viewModel.page = 1
        viewModel.total = 1
        viewModel.fetch()
    }

    private val newsAdapter by lazy {
        NewsAdapter(arrayListOf(), object : NewsAdapter.OnAdapterListener {
            override fun onClick(articleModel: ArticleModel) {
                startActivity(Intent(requireContext(), DetailActivity::class.java)
                    .putExtra("intent_detail", articleModel)
                )
            }
        })
    }

    private val categoryAdapter by lazy {
        CategoryAdapter(viewModel.categories, object : CategoryAdapter.OnAdapterListener {
            override fun onClick(category: CategoryModel) {
                viewModel.category.postValue(category.id)
            }
        })
    }
}