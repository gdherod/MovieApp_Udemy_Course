package com.example.movieapp.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.example.movieapp.R
import com.example.movieapp.core.Resource
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.remote.MovieDataSource
import com.example.movieapp.databinding.FragmentMovieBinding
import com.example.movieapp.presentation.MovieViewModel
import com.example.movieapp.presentation.MovieViewModelFactory
import com.example.movieapp.repository.MovieRepositoryImpl
import com.example.movieapp.repository.RetrofitClient
import com.example.movieapp.ui.movie.adapters.MovieAdapter
import com.example.movieapp.ui.movie.adapters.concat.PopularConcatAdapter
import com.example.movieapp.ui.movie.adapters.concat.TopRatedConcatAdapter
import com.example.movieapp.ui.movie.adapters.concat.UpcomingConcatAdapter

class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentMovieBinding
    private val viewModel by viewModels<MovieViewModel> {
        MovieViewModelFactory(
            MovieRepositoryImpl(
                MovieDataSource(RetrofitClient.webService)
            )
        )
    }

    private lateinit var concatAdapter: ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieBinding.bind(view)

        concatAdapter = ConcatAdapter()

        viewModel.fetchMainScreenMovies().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    concatAdapter.apply {
                        addAdapter(
                            0,
                            UpcomingConcatAdapter(
                                MovieAdapter(
                                    result.data.first.results,
                                    this@MovieFragment
                                )
                            )
                        )
                        addAdapter(
                            1,
                            TopRatedConcatAdapter(
                                MovieAdapter(
                                    result.data.second.results,
                                    this@MovieFragment
                                )
                            )
                        )
                        addAdapter(
                            2,
                            PopularConcatAdapter(
                                MovieAdapter(
                                    result.data.third.results,
                                    this@MovieFragment
                                )
                            )
                        )
                    }
                    binding.rvMovies.adapter = concatAdapter
                }

                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("LiveData", "${result.exception}")
                }
            }
        })
    }

    override fun onMovieClick(movie: Movie) {
        val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
            movie.posterPath,
            movie.backdropPath,
            movie.voteAverage.toFloat(),
            movie.voteCount,
            movie.overview,
            movie.title,
            movie.originalLanguage,
            movie.releaseDate
        )
        findNavController().navigate(action)
    }
}