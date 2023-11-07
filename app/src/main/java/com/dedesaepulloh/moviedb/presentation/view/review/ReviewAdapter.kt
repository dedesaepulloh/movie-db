package com.dedesaepulloh.moviedb.presentation.view.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dedesaepulloh.moviedb.R
import com.dedesaepulloh.moviedb.data.remote.model.response.ReviewsItem
import com.dedesaepulloh.moviedb.databinding.ItemsReviewBinding
import com.dedesaepulloh.moviedb.utils.Constants.BASE_IMAGE_URL
import java.text.SimpleDateFormat

class ReviewAdapter(private val onReviewClick: (ReviewsItem) -> Unit) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var reviews: MutableList<ReviewsItem> = mutableListOf()

    fun setReview(review: List<ReviewsItem>) {
        reviews.addAll(review)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReviewAdapter.ReviewViewHolder {
        val binding = ItemsReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    inner class ReviewViewHolder(private val binding: ItemsReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewsItem) {
            review.apply {
                binding.apply {
                    tvName.text = author
                    tvRate.text = authorDetails.rating.toString()
                    val parser = SimpleDateFormat("yyyy-MM-dd")
                    val formatter = SimpleDateFormat("dd MMMM yyyy")
                    val output: String = if (updatedAt.isNullOrEmpty()) {
                        "${R.string.strip}"
                    } else {
                        formatter.format(parser.parse(updatedAt))
                    }
                    tvUpdated.text = output
                    tvContent.text = content
                    Glide.with(itemView)
                        .load("$BASE_IMAGE_URL${authorDetails.avatarPath}")
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.ic_image)
                        .into(imgUser)
                    itemView.setOnClickListener {
                        onReviewClick.invoke(review)
                    }
                }
            }
        }
    }

}