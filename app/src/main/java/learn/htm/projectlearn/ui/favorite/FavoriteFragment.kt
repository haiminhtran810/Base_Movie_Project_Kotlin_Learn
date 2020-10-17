package learn.htm.projectlearn.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.databinding.FragmentFavoriteBinding
import learn.htm.projectlearn.ui.ShareViewModel
import learn.htm.projectlearn.ui.home.HomeFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>() {
    private val shareViewModel: ShareViewModel by sharedViewModel()

    private var movieAdapter: FavoriteAdapter? = null

    override val viewModel: FavoriteViewModel by viewModel()

    override val layoutId: Int
        get() = R.layout.fragment_favorite


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel.apply {
            getMoviesLocal()
        }
    }

    private fun initAdapter() {
        movieAdapter = FavoriteAdapter {
            getHomeFragment()?.navigationMovieDetailFragment(it)
        }
        viewBinding.recyclerMovies.adapter = movieAdapter
    }

    override fun observeEvent() {
        super.observeEvent()
        viewModel.apply {
            movies.observe(viewLifecycleOwner, Observer {
                movieAdapter?.submitList(it)
            })
        }

        shareViewModel.apply {
            refreshMovieFavorite.observe(viewLifecycleOwner, Observer {
                viewModel.getMoviesLocal()
            })
        }
    }

    private fun getHomeFragment(): HomeFragment? {
        for (fragment in parentFragmentManager.fragments) {
            if (fragment is HomeFragment) {
                return fragment
            }
        }
        return null
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }

}