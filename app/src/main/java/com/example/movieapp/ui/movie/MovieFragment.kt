package com.example.movieapp.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.movieapp.R
import com.example.movieapp.core.Resource
import com.example.movieapp.data.remote.MovieDataSource
import com.example.movieapp.databinding.FragmentMovieBinding
import com.example.movieapp.presentation.MovieViewModel
import com.example.movieapp.presentation.MovieViewModelFactory
import com.example.movieapp.repository.MovieRepositoryImpl
import com.example.movieapp.repository.RetrofitClient

class MovieFragment : Fragment(R.layout.fragment_movie) {

    private lateinit var binding: FragmentMovieBinding
    private val viewModel by viewModels<MovieViewModel> {
        MovieViewModelFactory(
            MovieRepositoryImpl(
                MovieDataSource(RetrofitClient.webService)
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieBinding.bind(view)

        viewModel.fetchMainScreenMovies().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("LiveData", "Loading... ")
                }

                is Resource.Success -> {
                    Log.d(
                        "LiveData",
                        "Upcoming: ${result.data.first} \n \n " +
                                "TopRated: ${result.data.second} \n \n " +
                                "Popular: ${result.data.third}"
                    )
                }

                is Resource.Failure -> {
                    Log.d("LiveData", "${result.exception}")
                }
            }
        })
    }
}