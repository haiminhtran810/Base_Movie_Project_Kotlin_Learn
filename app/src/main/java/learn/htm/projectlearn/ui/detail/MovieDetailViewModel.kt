package learn.htm.projectlearn.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.model.Videos
import learn.htm.projectlearn.utils.RxUtils
import learn.htm.projectlearn.utils.SingleLiveData

class MovieDetailViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) :
    BaseViewModel() {
    val movie = SingleLiveData<Movie>()
    val insertSuccess = SingleLiveData<Unit>()
    val movieVideos = SingleLiveData<Videos>()

    fun getMovieDetail(idMovie: String) {
        addDisposable(
            movieRepository.getMovieDetail(idMovie)
                .compose(RxUtils.applySingleScheduler())
                .subscribe({ movieDetail ->
                    movie.value = movieDetail
                }, { error ->
                    onError(error)
                })
        )
    }

    fun getVideo(idMovie: String) {
        addDisposable(
            movieRepository.getVideos(idMovie)
                .compose(RxUtils.applySingleScheduler())
                .subscribe({ videos ->
                    movieVideos.value = videos
                }, { error ->
                    onError(error)
                })
        )
    }

    fun insertMovie() {
        movie.value?.let {
            addDisposable(
                movieRepository.insertMovieLocal(it).compose(RxUtils.applyCompletableScheduler())
                    .subscribe({
                        insertSuccess.call()
                    }, { error ->
                        onError(error)
                    })
            )
        }
    }

    fun removeMovieInDatabase() {
        movie.value?.let {

        }
    }
}