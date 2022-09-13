package com.example.indanze.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.indanze.R
import com.example.indanze.activity.UpdateVideoActivity
import com.example.indanze.activity.VideoDetailsActivity
import com.example.indanze.data.Video
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class UserVideoAdapter(private val context: Context,
                       options: FirebaseRecyclerOptions<Video>
) : FirebaseRecyclerAdapter<Video, UserVideoAdapter.ViewHolder>(options){

    private lateinit var view: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_video_item, parent, false)
        )
        view = parent
        return v
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Video) {

        val key = getRef(position).key

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

        holder.updateVideo.setOnClickListener{

            val intent = Intent(context, UpdateVideoActivity::class.java)

            intent.putExtra("firebase_videoUserID", model.userID)
            intent.putExtra("firebase_videoPosition", key)
            intent.putExtra("firebase_videoImage", model.image)
            intent.putExtra("firebase_videoName", model.name)
            intent.putExtra("firebase_videoDescription", model.description)
            intent.putExtra("firebase_videoVideo", model.video)

            view?.context?.startActivity(intent)
        }

        holder.deleteVideo.setOnClickListener{
            var builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            builder.setTitle("Delete this video?").setMessage("Deleted video cannot be restored.")
            builder.setPositiveButton("Delete") { dialog, _ ->
                FirebaseDatabase
                    .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Video")
                    .child(key!!).removeValue()
            }
            builder.setNegativeButton("Cancel") {dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()

        }
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val videoImage : ImageView = itemView.findViewById(R.id.userVideoImage)
        val videoName : TextView = itemView.findViewById(R.id.userVideoName)
        val updateVideo : Button = itemView.findViewById(R.id.btn_updateVideo)
        val deleteVideo : Button = itemView.findViewById(R.id.btn_deleteVideo)
    }
}