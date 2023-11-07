package com.dedesaepulloh.moviedb.presentation.view.movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dedesaepulloh.moviedb.R
import com.dedesaepulloh.moviedb.databinding.ActivityMovieBinding
import com.dedesaepulloh.moviedb.presentation.view.detail.DetailActivity
import com.dedesaepulloh.moviedb.presentation.viewmodel.MovieViewModel
import com.dedesaepulloh.moviedb.utils.CommonUtils.showToast
import com.dedesaepulloh.moviedb.utils.Constants.GENRE_ID
import com.dedesaepulloh.moviedb.utils.Constants.MOVIE_ITEM
import com.dedesaepulloh.moviedb.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {

    private var activityMovieBinding: ActivityMovieBinding? = null
    private val binding get() = activityMovieBinding

    private val viewModel: MovieViewModel by viewModels()

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(MOVIE_ITEM, it)
            startActivity(intent)
        }
    }

    private var page = 1
    private var genreId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMovieBinding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initIntent()
        initObserver()
        initProcess()
        initUi()

    }

    private fun initIntent() {
        genreId = intent.getIntExtra(GENRE_ID, 0)
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.movies.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        if (result.data.isNotEmpty()) {
                            movieAdapter.setMovies(result.data)
                        } else {
                            showToast(this@MovieActivity, getString(R.string.label_data_not_found))
                        }
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showToast(
                            this@MovieActivity,
                            result.exception.message.toString()
                        )
                    }
                }
            }
        }
    }

    private fun initProcess() {
        viewModel.loadMovies(
            genreIds = genreId,
            page = page
        )
    }

    private fun initUi() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.list_movie)
        binding?.rvMovie?.apply {
            layoutManager = GridLayoutManager(this@MovieActivity, 3)
            setHasFixedSize(true)
            adapter = movieAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    if (binding?.progressBar?.isVisible == false && visibleItemCount + firstVisibleItem >= totalItemCount) {
                        loadMore()
                    }
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadMore() {
        binding?.progressBar?.visibility = View.VISIBLE
        page++
        initProcess()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding?.apply {
                rvMovie.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        } else {
            binding?.apply {
                rvMovie.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

}