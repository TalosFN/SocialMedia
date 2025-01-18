package com.example.socialmediaapp

import Post
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostAdapter(private val postsList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsList[position]
        holder.contentTextView.text = post.content
        // Загрузка изображения, если требуется
        if (post.imageResId != 0) { // Проверяем, что идентификатор ресурса не равен 0
            Glide.with(holder.itemView.context)
                .load(post.imageResId)
                .into(holder.imageView)
        }

    }

    override fun getItemCount(): Int = postsList.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
