package learn.htm.projectlearn.ui.favorite

import androidx.hilt.lifecycle.ViewModelInject
import learn.htm.projectlearn.base.BaseViewModel
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie
import learn.htm.projectlearn.utils.RxUtils
import learn.htm.projectlearn.utils.SingleLiveData

class FavoriteViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) :
    BaseViewModel() {
    val movies = SingleLiveData<List<Movie>>()

    fun getMoviesLocal() {
        addDisposable(
            movieRepository.getMoviesLocal().compose(RxUtils.applySingleScheduler())
                .subscribe({
                    movies.value = it
                }, {
                    onError(it)
                })
        )
    }
}