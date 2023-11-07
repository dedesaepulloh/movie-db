package com.dedesaepulloh.moviedb.presentation.view.genre

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.dedesaepulloh.moviedb.R
import com.dedesaepulloh.moviedb.databinding.ActivityGenreBinding
import com.dedesaepulloh.moviedb.presentation.view.movie.MovieActivity
import com.dedesaepulloh.moviedb.presentation.viewmodel.MovieViewModel
import com.dedesaepulloh.moviedb.utils.CommonUtils.showToast
import com.dedesaepulloh.moviedb.utils.Constants.GENRE_ID
import com.dedesaepulloh.moviedb.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GenreActivity : AppCompatActivity() {

    private var activityGenreBinding: ActivityGenreBinding? = null
    private val binding get() = activityGenreBinding

    private val viewModel: MovieViewModel by viewModels()
    private val genreAdapter: GenreAdapter by lazy {
        GenreAdapter {
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra(GENRE_ID, it.id)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGenreBinding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initObserver()
        initProcess()
        initUi()

    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.genres.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        if (result.data.isNotEmpty()) {
                            genreAdapter.setGenres(result.data)
                        } else {
                            showToast(this@GenreActivity, getString(R.string.label_data_not_found))
                        }
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showToast(this@GenreActivity, result.exception.message.toString())
                    }
                }
            }
        }
    }

    private fun initProcess() {
        viewModel.loadGenre()
    }

    private fun initUi() {
        binding?.rvGenre?.apply {
            layoutManager = GridLayoutManager(this@GenreActivity, 2)
            setHasFixedSize(true)
            adapter = genreAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding?.apply {
                rvGenre.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        } else {
            binding?.apply {
                rvGenre.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

}

