package com.example.moviesapp.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviesapp.model.entities.favourite.FavouriteMovie
import com.example.moviesapp.model.entities.movies.credits.ResponseCredits
import com.example.moviesapp.model.entities.movies.details.ResponseMovieDetail
import com.example.moviesapp.model.entities.movies.popular.results.Result
import com.example.moviesapp.model.entities.movies.popular.results.ResultNowPlayong
import com.example.moviesapp.model.entities.movies.popular.results.ResultTopRated
import com.example.moviesapp.model.entities.movies.popular.results.ResultUpComing

@Database(
    entities = [Result::class, ResultTopRated::class, ResultNowPlayong::class,
        ResultUpComing::class, ResponseMovieDetail::class, ResponseCredits::class, FavouriteMovie::class],
    version = 1
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract val moviesDaoPopular: MoviesDaoPopular
    abstract val moviesDaoTopRated: MoviesDaoTopRated
    abstract val moviesDaoNowPlayong: MoviesDaoNowPlayong
    abstract val moviesDaoUpComing: MoviesDaoUpComing
    abstract val moviesDaoDetail: MoviesDaoDetail
    abstract val moviesDaoCast: MoviesDaoCast
    abstract val moviesDaoFavourite : MoviesDaoFavourite


    companion object {
        private var db: MoviesDatabase? = null

        fun getInstance(context: Context): MoviesDatabase {
            synchronized(MoviesDatabase::class.java) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context,
                        MoviesDatabase::class.java,
                        MoviesContact.DATABASE_NAME
                    ).build()
                db = instance
                return instance
            }
        }
    }
}