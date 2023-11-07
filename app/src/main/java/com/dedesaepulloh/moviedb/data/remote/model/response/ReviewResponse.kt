package com.dedesaepulloh.moviedb.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ReviewResponse(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("results")
	val results: List<ReviewsItem>,

	@field:SerializedName("total_results")
	val totalResults: Int
)

@Parcelize
data class AuthorDetails(

	@field:SerializedName("avatar_path")
	val avatarPath: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("username")
	val username: String
) : Parcelable

@Parcelize
data class ReviewsItem(

	@field:SerializedName("author_details")
	val authorDetails: AuthorDetails,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("url")
	val url: String
) :Parcelable
