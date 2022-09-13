package com.example.indanze.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.indanze.R
import com.example.indanze.activity.VideoDetailsActivity
import com.example.indanze.data.Video
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class VideoAdapter (private val context: Context,
options: FirebaseRecyclerOptions<Video>
) : FirebaseRecyclerAdapter<Video, VideoAdapter.ViewHolder>(options) {

    private lateinit var view: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.video_item, parent, false)
        )
        view = parent
        return v
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Video) {

        Glide.with(context).load(model.image).into(holder.videoImage)
        holder.videoName.text = model.name

        holder.itemView.setOnClickListener{

            val intent = Intent(context, VideoDetailsActivity::class.java)

            intent.putExtra("firebase_videoUserID", model.userID)
            intent.putExtra("firebase_videoImage", model.image)
            intent.putExtra("firebase_videoName", model.name)
            intent.putExtra("firebase_videoDescription", model.description)
            intent.putExtra("firebase_videoVideo", model.video)

            view?.context?.startActivity(intent)
        }

    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val videoImage : ImageView = itemView.findViewById(R.id.videoImage)
        val videoName : TextView = itemView.findViewById(R.id.videoName)

    }
}