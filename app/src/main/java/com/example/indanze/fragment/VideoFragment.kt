package com.example.indanze.fragment
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indanze.R
import com.example.indanze.activity.AddVideoActivity
import com.example.indanze.adapter.VideoAdapter
import com.example.indanze.data.Video
import com.example.indanze.databinding.FragmentVideoBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VideoFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var adapter: VideoAdapter
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var binding : FragmentVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_fragment_video)

        binding = FragmentVideoBinding.inflate(inflater, container, false)
        var view =  binding.root
        setHasOptionsMenu(true)

        databaseReference = FirebaseDatabase
            .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Video")
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

        fun newInstance(): VideoFragment = VideoFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoRecyclerView = binding.videoList
        floatingActionButton = binding.floatingbtnAddVideo

        val options = FirebaseRecyclerOptions.Builder<Video>()
        options.setQuery(databaseReference , Video::class.java)
        options.setLifecycleOwner(this)

        adapter  = VideoAdapter(requireActivity() , options.build())
        videoRecyclerView.layoutManager = LinearLayoutManager(view.context)
        videoRecyclerView.itemAnimator = null

        videoRecyclerView.adapter = adapter

        addVideo()
    }

    private fun searchData(string : String) {
        var query= databaseReference.orderByChild("name").startAt(string).endAt(string+"~")

        val options = FirebaseRecyclerOptions.Builder<Video>()
        options.setQuery(query ,Video::class.java)
        options.setLifecycleOwner(this)

        adapter  = VideoAdapter(requireActivity() , options.build())

        videoRecyclerView.adapter = adapter

    }

    private fun addVideo(){

        floatingActionButton.setOnClickListener{

            val intent = Intent(activity, AddVideoActivity::class.java)
            startActivity(intent)

        }

    }
}


