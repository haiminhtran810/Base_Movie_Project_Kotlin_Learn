package learn.htm.projectlearn.ui.popular

import android.os.Bundle
import androidx.lifecycle.Observer
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentPopularBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularFragment : BaseFragment<FragmentPopularBinding, PopularViewModel>() {

    override val viewModel: PopularViewModel by viewModel()

    override val layoutId: Int
        get() = R.layout.fragment_popular

    private var movieAdapter: MovieAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieAdapter = MovieAdapter {
            //TODO: Handler navigation
        }
        viewDataBinding.apply {
            recyclerMovie.adapter = movieAdapter
            swipeRefreshLayout.setOnRefreshListener {
                viewModel?.apply {
                    resetLoadMore()
                    refreshData()
                }
            }
        }
        viewModel.apply {
            if (listItem.value.isNullOrEmpty()) {
                firstLoad()
            }
        }
    }

    override fun observeEvent() {
        viewModel.apply {
            listItem.observe(viewLifecycleOwner, Observer {
                movieAdapter?.submitList(it)
            })
        }
    }

    companion object {
        fun newInstance() = PopularFragment()
    }
}