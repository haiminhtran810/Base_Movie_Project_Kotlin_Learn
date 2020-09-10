package learn.htm.projectlearn.data.remote.repository.movie

import io.reactivex.Completable
import io.reactivex.Single
import learn.htm.projectlearn.data.local.dao.MovieDao
import learn.htm.projectlearn.data.remote.api.MovieAPI
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.model.Movie

class MovieRepositoryImpl(private val movieAPI: MovieAPI, private val movieDao: MovieDao) :
    MovieRepository {
    override fun getMovieListPopular(page: Int): Single<List<Movie>> {
        return movieAPI.getMovieListPopular(page).map {
            it.results
        }
    }

    override fun updateMovieLocal(movie: Movie): Completable {
        return Completable.create {
            movieDao.updateMovies(movie)
        }
    }

    override fun insertMovieLocal(movie: Movie): Completable {
        return Completable.create {
            movieDao.insertMovie(movie)
        }
    }

    override fun getMoviesLocal(): Single<List<Movie>> {
        return movieDao.getAllMovies()
    }
}