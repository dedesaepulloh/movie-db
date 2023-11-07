package com.dedesaepulloh.moviedb.presentation.view.review

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dedesaepulloh.moviedb.R
import com.dedesaepulloh.moviedb.data.remote.model.response.ResultsItem
import com.dedesaepulloh.moviedb.databinding.ActivityReviewBinding
import com.dedesaepulloh.moviedb.presentation.viewmodel.MovieViewModel
import com.dedesaepulloh.moviedb.utils.Constants.MOVIE_ITEM
import com.dedesaepulloh.moviedb.utils.Constants.URL_ID
import com.dedesaepulloh.moviedb.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding

    private val viewModel: MovieViewModel by viewModels()
    private var movieItem: ResultsItem? = null
    private var page = 1
    private val reviewAdapter: ReviewAdapter by lazy {
        ReviewAdapter {
            val intent = Intent(this@ReviewActivity, WebviewActivity::class.java)
            intent.putExtra(URL_ID, it.url)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initIntent()
        initObserver()
        initProcess()
        initUi()
    }

    private fun initIntent() {
        movieItem = intent.getParcelableExtra(MOVIE_ITEM)
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.reviews.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        if (result.data.isNotEmpty()) {
                            reviewAdapter.setReview(result.data)
                        }
                        isEmptyReview(reviewAdapter.itemCount == 0 && result.data.isEmpty())
                    }

                    is Resource.Error -> {
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun initProcess() {
        movieItem?.let {
            viewModel.loadReviews(it.id, page)
        }
    }

    private fun initUi() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${getString(R.string.review)} - ${movieItem?.title}"
        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            setHasFixedSize(true)
            adapter = reviewAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    if (!binding.progressBar.isVisible && visibleItemCount + firstVisibleItem >= totalItemCount) {
                        loadMore()
                    }
                }
            })
        }
    }

    private fun loadMore() {
        binding.progressBar.visibility = View.VISIBLE
        page++
        initProcess()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
                rvReview.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                rvReview.visibility = View.VISIBLE
            }
        }
    }

    private fun isEmptyReview(state: Boolean) {
        binding.apply {
            if (state) {
                reviewEmpty.apply {
                    imgEmpty.visibility = View.VISIBLE
                    tvEmpty.text = getString(R.string.no_reviews)
                    tvEmpty.visibility = View.VISIBLE
                    parentEmpty.visibility = View.VISIBLE
                }
                rvReview.visibility = View.GONE
            } else {
                reviewEmpty.apply {
                    imgEmpty.visibility = View.GONE
                    tvEmpty.visibility = View.GONE
                    parentEmpty.visibility = View.GONE
                }
                rvReview.visibility = View.VISIBLE
            }
        }

    }

}