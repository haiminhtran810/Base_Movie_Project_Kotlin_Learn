package learn.htm.projectlearn.ui.popular

import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.PagingData
import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.utils.RxUtils
import learn.htm.projectlearn.utils.SingleLiveData

class PopularViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) :
    BaseViewModel() {
    val movie = SingleLiveData<PagingData<Movie>>()
    fun getMovies() {
        addDisposable(
            movieRepository.getMovieListPopular().compose(RxUtils.applyFlowableScheduler())
                .subscribe({
                    movie.value = it
                }, {
                    //TODO: get error to adapter UI
                    //onError(it)
                })
        )
    }
}