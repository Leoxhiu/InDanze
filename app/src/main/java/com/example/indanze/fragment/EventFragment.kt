package com.example.indanze.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indanze.R
import com.example.indanze.activity.AddEventActivity
import com.example.indanze.adapter.EventAdapter
import com.example.indanze.data.Event
import com.example.indanze.databinding.FragmentEventBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class EventFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var binding : FragmentEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_fragment_event)

        binding = FragmentEventBinding.inflate(inflater, container, false)
        var view =  binding.root
        setHasOptionsMenu(true)

        databaseReference = FirebaseDatabase
            .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Event")
        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_bar, menu)
        val item = menu?.findItem(R.id.searchAction)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                searchData(query!!)
                return true
            }

            override fun onQueryTextChange(filterString: String?): Boolean {

                searchData(filterString!!)
                return true
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {

        fun newInstance(): EventFragment = EventFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventRecyclerView = binding.eventList
        floatingActionButton = binding.floatingbtnAddEvent

        val options = FirebaseRecyclerOptions.Builder<Event>()
        options.setQuery(databaseReference ,Event::class.java)
        options.setLifecycleOwner(this)

        adapter  = EventAdapter(requireActivity() , options.build())
        eventRecyclerView.layoutManager = LinearLayoutManager(view.context)
        eventRecyclerView.itemAnimator = null

        eventRecyclerView.adapter = adapter

        addEvent()
    }

    private fun searchData(string : String) {
        var query= databaseReference.orderByChild("name").startAt(string).endAt(string+"~")

        val options = FirebaseRecyclerOptions.Builder<Event>()
        options.setQuery(query ,Event::class.java)
        options.setLifecycleOwner(this)

        adapter  = EventAdapter(requireActivity() , options.build())

        eventRecyclerView.adapter = adapter

    }

    private fun addEvent(){

        floatingActionButton.setOnClickListener{

            val intent = Intent(activity, AddEventActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}