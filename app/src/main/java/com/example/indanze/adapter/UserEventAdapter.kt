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
import com.example.indanze.activity.EventDetailsActivity
import com.example.indanze.activity.UpdateEventActivity
import com.example.indanze.data.Event
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class UserEventAdapter (private val context: Context,
options: FirebaseRecyclerOptions<Event>
) : FirebaseRecyclerAdapter<Event, UserEventAdapter.ViewHolder>(options){

    private lateinit var view: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_event_item, parent, false)
        )
        view = parent
        return v
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Event) {

        val key = getRef(position).key

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

        holder.updateEvent.setOnClickListener{

            val intent = Intent(context, UpdateEventActivity::class.java)

            intent.putExtra("firebase_eventUserID", model.userID)
            intent.putExtra("firebase_eventPosition", key)
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

        holder.deleteEvent.setOnClickListener{
            var builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            builder.setTitle("Delete this event?").setMessage("Deleted event cannot be restored.")
            builder.setPositiveButton("Delete") { dialog, _ ->
                FirebaseDatabase
                    .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Event")
                    .child(key!!).removeValue()
            }
            builder.setNegativeButton("Cancel") {dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()

        }
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val eventImage : ImageView = itemView.findViewById(R.id.userEventImage)
        val eventName : TextView = itemView.findViewById(R.id.userEventName)
        val eventDate : TextView = itemView.findViewById(R.id.userEventDate)
        val eventTime : TextView = itemView.findViewById(R.id.userEventTime)
        val eventLocation : TextView = itemView.findViewById(R.id.userEventLocation)
        val updateEvent : Button = itemView.findViewById(R.id.btn_updateEvent)
        val deleteEvent : Button = itemView.findViewById(R.id.btn_deleteEvent)
    }

}