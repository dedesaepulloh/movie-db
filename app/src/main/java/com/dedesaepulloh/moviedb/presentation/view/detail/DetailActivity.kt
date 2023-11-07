package com.dedesaepulloh.moviedb.presentation.view.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dedesaepulloh.moviedb.R
import com.dedesaepulloh.moviedb.data.remote.model.response.ResultsItem
import com.dedesaepulloh.moviedb.data.remote.model.response.TrailersItem
import com.dedesaepulloh.moviedb.databinding.ActivityDetailBinding
import com.dedesaepulloh.moviedb.presentation.view.review.ReviewActivity
import com.dedesaepulloh.moviedb.presentation.viewmodel.MovieViewModel
import com.dedesaepulloh.moviedb.utils.CommonUtils.showToast
import com.dedesaepulloh.moviedb.utils.Constants.BASE_IMAGE_URL
import com.dedesaepulloh.moviedb.utils.Constants.MOVIE_ITEM
import com.dedesaepulloh.moviedb.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: MovieViewModel by viewModels()

    private var movieItem: ResultsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initIntent()
        initAction()
        initObserver()
        initProcess()
        initUi()

    }

    private fun initIntent() {
        movieItem = intent.getParcelableExtra(MOVIE_ITEM)
    }

    private fun initAction() {
        binding.apply {
            btnReview.setOnClickListener {
                val intent = Intent(this@DetailActivity, ReviewActivity::class.java)
                intent.putExtra(MOVIE_ITEM, movieItem)
                startActivity(intent)
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.trailers.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        if (result.data.isNotEmpty()) {
                            setVideoTrailer(result.data)
                        } else {
                            binding.itemDetail.apply {
                                labelTrailer.visibility = View.GONE
                                mainWebview.visibility = View.GONE
                            }
                        }
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showToast(
                            this@DetailActivity, result.exception.message.toString()
                        )
                    }
                }
            }
        }
    }

    private fun setVideoTrailer(data: List<TrailersItem>) {
        binding.itemDetail.mainWebview.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.apply {
                javaScriptCanOpenWindowsAutomatically = true
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                domStorageEnabled = true
                allowContentAccess = true
                allowFileAccess = true
            }

            val frame =
                "<head></head><body>" + "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + data[0].key + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>" + "</body>"

            loadData(frame, "text/html; charset=utf-8", null)
        }

    }

    private fun setDataView() {
        movieItem?.apply {
            binding.itemDetail.apply {
                val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

                val output = try {
                    val parsedDate = parser.parse(releaseDate)
                    formatter.format(parsedDate ?: Date())
                } catch (e: ParseException) {
                    getString(R.string.strip)
                }
                title = originalTitle
                tvTitle.text = originalTitle
                tvRelease.text = output
                tvPopularity.text = popularity.toString()
                tvRate.text = voteAverage.toString()
                tvOverview.text = overview
                tvVote.text = voteCount.toString()
                Glide.with(this@DetailActivity).load("$BASE_IMAGE_URL$posterPath")
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_image).into(riPoster)
                Glide.with(this@DetailActivity)
                    .load("$BASE_IMAGE_URL$backdropPath")
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_image).into(imgBackdrop)
            }
        }
    }


    private fun initProcess() {
        movieItem?.id?.let { viewModel.loadTrailers(it) }
    }

    private fun initUi() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.detail_movie)
        setDataView()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showLoading(state: Boolean) {
        binding.itemDetail.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
                labelRelease.visibility = View.GONE
                labelPopularity.visibility = View.GONE
                labelRate.visibility = View.GONE
                tvOverview.visibility = View.GONE
                tvTitle.visibility = View.GONE
                tvRelease.visibility = View.GONE
                tvPopularity.visibility = View.GONE
                tvRate.visibility = View.GONE
                labelOverview.visibility = View.GONE
                labelVote.visibility = View.GONE
                tvVote.visibility = View.GONE
                labelTrailer.visibility = View.GONE
                mainWebview.visibility = View.GONE
                riPoster.visibility = View.GONE
                imgBackdrop.visibility = View.GONE
                binding.btnReview.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                labelRelease.visibility = View.VISIBLE
                labelPopularity.visibility = View.VISIBLE
                labelRate.visibility = View.VISIBLE
                tvOverview.visibility = View.VISIBLE
                tvTitle.visibility = View.VISIBLE
                tvRelease.visibility = View.VISIBLE
                tvPopularity.visibility = View.VISIBLE
                tvRate.visibility = View.VISIBLE
                labelOverview.visibility = View.VISIBLE
                labelVote.visibility = View.VISIBLE
                tvVote.visibility = View.VISIBLE
                labelTrailer.visibility = View.VISIBLE
                mainWebview.visibility = View.VISIBLE
                riPoster.visibility = View.VISIBLE
                imgBackdrop.visibility = View.VISIBLE
                binding.btnReview.visibility = View.VISIBLE
            }
        }
    }


}