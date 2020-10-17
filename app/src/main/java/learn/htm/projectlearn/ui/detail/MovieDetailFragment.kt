package learn.htm.projectlearn.ui.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackPreparer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import dagger.hilt.android.AndroidEntryPoint
import learn.htm.projectlearn.BuildConfig
import learn.htm.projectlearn.R
import learn.htm.projectlearn.base.BaseFragment
import learn.htm.projectlearn.binding.setOnSingleClickListener
import learn.htm.projectlearn.databinding.FragmentMovieDetailBinding
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.model.Videos
import learn.htm.projectlearn.ui.ShareViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
* UI: https://hayko.tv/zelpro.id/CCV_uptp9ON
* */

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding, MovieDetailViewModel>(),
    PlaybackPreparer, StyledPlayerControlView.VisibilityListener {

    private var idMovie = ""
    private var player: SimpleExoPlayer? = null
    private var movie: Movie? = null

    private val shareViewModel: ShareViewModel by sharedViewModel()
    override val viewModel: MovieDetailViewModel by viewModel()
    override val layoutId: Int
        get() = R.layout.fragment_movie_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = MovieDetailFragmentArgs.fromBundle(it).movie
            idMovie = movie?.id?.toString() ?: ""
            viewModel.getMovieDetail(idMovie)
            //if (movie?.video == true) {
            viewModel.getVideo(idMovie)
            //}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            imageViewBack.setOnSingleClickListener(View.OnClickListener {
                requireActivity().onBackPressed()
            })
        }
    }

    private fun initExoPlayer(videos: Videos) {
        //Creating the player
        player = SimpleExoPlayer.Builder(requireContext()).build()
        viewBinding.apply {
            styledPlayerView.setControllerVisibilityListener(this@MovieDetailFragment)
            styledPlayerView.player = player
            val videoUrl = BuildConfig.VIDEO_URL + videos.results.getOrNull(0)?.key
            val mediaItem =
                MediaItem.fromUri(videoUrl)
            player?.setMediaItem(mediaItem)
            player?.prepare()
            player?.play()
        }
    }

    private fun releasePlayer() {
        player?.apply {
            pause()
            release()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    override fun observeEvent() {
        super.observeEvent()
        viewModel.apply {
            insertSuccess.observe(viewLifecycleOwner, Observer {
                shareViewModel.refreshMovieFavorite.call()
                requireActivity().onBackPressed()
            })
            movieVideos.observe(viewLifecycleOwner, Observer {
                initExoPlayer(it)
            })
        }
    }

    override fun preparePlayback() {

    }

    override fun onVisibilityChange(visibility: Int) {

    }
}