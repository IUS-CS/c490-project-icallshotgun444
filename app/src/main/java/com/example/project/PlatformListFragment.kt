package com.example.project

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.util.*

private const val TAG = "PlatformListFragment"

class PlatformListFragment : Fragment() {

    private lateinit var repository: UserRepository
    private lateinit var currentUser: User


    companion object {
        fun newInstance() = PlatformListFragment()
    }

    private val viewModel: PlatformListViewModel by lazy {
        ViewModelProviders.of(this).get(PlatformListViewModel::class.java)
    }

    private lateinit var platformRecyclerView: RecyclerView
    private var adapter: PlatformAdapter? = PlatformAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.platform_list_fragment, container, false)
        repository = UserRepository.get()

        platformRecyclerView = view.findViewById(R.id.platform_recycler_view)
        platformRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner,
            Observer { user ->
                if (user == null) {
                    var user = User()
                    repository.insertUser(user)
                }
                currentUser = user
                updateUI()
            }
        )
    }

    private fun updateUI() {
        var  platforms: List<Platform> = emptyList()
        for((index,level) in currentUser.platformLevels.withIndex()){
            val platform = Platform()
            platform.index = index
            platform.lvl = level
            platforms += platform
        }
        adapter = PlatformAdapter(platforms);
        platformRecyclerView.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private inner class PlatformHolder(view: View) : ViewHolder(view), View.OnClickListener {
        override fun onClick(p0: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        }


        private lateinit var platform: Platform
        private val platImageView: ImageView = itemView.findViewById(R.id.platform_image)
        private val timerTextView: TextView = itemView.findViewById(R.id.timer)
        private val levelTextView: TextView = itemView.findViewById(R.id.platform_level)
        private val lvlUpButton: Button = itemView.findViewById(R.id.level_up)


        fun bind(platform: Platform) {
            this.platform = platform
            val draw = platform.name[platform.index]
            val img = getResources().getIdentifier("com.example.project:drawable/$draw",null,null)
            platImageView.setImageResource(img)
            timerTextView.text = platform.time.toString()
            levelTextView.text = "lvl ".plus(platform.lvl.toString())
            lvlUpButton.setOnClickListener{
                 /*   viewModel.user.observe(viewLifecycleOwner,
                        Observer { user ->
                            if(platform.lvl == 0 && user.platformLevels.size <9) {
                                user.platformLevels.add(0)
                            }
                            user.platformLevels[platform.index]++
                            repository.updateUser(user)
                        }
                    )*/
                currentUser.platformLevels[platform.index]++
                updateUI()
                Log.d(TAG, "hit")
            }

        }
    }
    private inner class PlatformAdapter(var platforms: List<Platform>) : RecyclerView.Adapter<PlatformHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatformHolder {
            val view = layoutInflater.inflate(R.layout.list_item_platform, parent, false)
            return PlatformHolder(view)
        }


        override fun getItemCount(): Int = platforms.size

        override fun onBindViewHolder(holder: PlatformHolder, position: Int) {
            holder.bind(platforms[position])
        }

    }//adapter class

    interface Callbacks {

    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onStop() {
        super.onStop()
            repository.updateUser(currentUser)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
}
