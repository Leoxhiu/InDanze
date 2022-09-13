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
import com.example.indanze.activity.EventDetailsActivity
import com.example.indanze.data.Event
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class EventAdapter ( private val context: Context,
                     options: FirebaseRecyclerOptions<Event>
) : FirebaseRecyclerAdapter<Event, EventAdapter.ViewHolder>(options) {

    private lateinit var view: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item, parent, false))
        view = parent
        return v
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model : Event) {

        Glide.with(context).load(model.image).into(holder.eventImage)

        holder.eventName.text = model.name
        holder.eventDate.text = model.date
        holder.eventTime.text = model.time
        holder.eventLocation.text = model.location

        holder.itemView.setOnClickListener{

            val intent = Intent(context, EventDetailsActivity::class.java)

            intent.putExtra("firebase_eventUserID", model.userID)
            intent.putExtra("firebase_eventImage", model.image)
            intent.putExtra("firebase_eventName", model.name)
            intent.putExtra("firebase_eventDate", model.date)
            intent.putExtra("firebase_eventTime", model.time)
            intent.putExtra("firebase_eventDescription", model.description)
            intent.putExtra("firebase_eventLocation", model.location)
            intent.putExtra("firebase_eventLatitude", model.latitude)
            intent.putExtra("firebase_eventLongitude", model.longitude)

            view?.context?.startActivity(intent)
        }

    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val eventImage : ImageView = itemView.findViewById(R.id.eventImage)
        val eventName : TextView = itemView.findViewById(R.id.eventName)
        val eventDate : TextView = itemView.findViewById(R.id.eventDate)
        val eventTime : TextView = itemView.findViewById(R.id.eventTime)
        val eventLocation : TextView = itemView.findViewById(R.id.eventLocation)
    }

}