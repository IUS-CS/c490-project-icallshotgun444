package com.example.project

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.CountDownTimer
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
import kotlinx.android.synthetic.main.list_item_platform.*

private const val TAG = "PlatformListFragment"

class PlatformListFragment : Fragment() {

    private lateinit var repository: UserRepository
    private lateinit var currentUser: User
    private var platforms: List<Platform> = emptyList()


    private val viewModel: PlatformListViewModel by lazy {
        ViewModelProviders.of(this).get(PlatformListViewModel::class.java)
    }

    private lateinit var platformRecyclerView: RecyclerView
    private lateinit var sellOutButton: Button
    private lateinit var likesText: TextView
    private lateinit var upgradeButton: Button
    private var adapter: PlatformAdapter? = PlatformAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.platform_list_fragment, container, false)

        platformRecyclerView = view.findViewById(R.id.platform_recycler_view)
        platformRecyclerView.layoutManager = LinearLayoutManager(context)
        likesText = view.findViewById(R.id.like_count)
        sellOutButton = view.findViewById(R.id.sell_out_button)
        sellOutButton.setOnClickListener{
            for(platform in platforms){
                platform.timer.cancel()
            }
            currentUser.followers = currentUser.likes/50
            currentUser.likes = 0
            currentUser.platformLevels = mutableListOf(1,0)
            platforms = emptyList()
           // platforms+=setPlatform(0,1)
           // platforms+=setPlatform(1,0)
            repository.updateUser(currentUser)
            updateUI()
        }
        upgradeButton = view.findViewById(R.id.upgrades_button)
        upgradeButton.setOnClickListener{
        callbacks?.onUpgrade()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun updateUI() {
        likesText.text = currentUser.likes.toString()
       // var  platforms: List<Platform> = emptyList()
        adapter = PlatformAdapter(platforms)
        platformRecyclerView.adapter = adapter
    }

    private fun setPlatform(index: Int, level: Int): Platform{
        val platform = Platform()
        platform.index = index
        platform.lvl = level
        if(platform.index == 0){
            platform.time = ((platform.index+1)*5000).toLong()
            platform.generation = (((platform.index+1)*(platform.index+1))*platform.lvl)+currentUser.facebookLikeUpgrade
        }
        else  platform.generation = (((platform.index+1)*(platform.index+1))*platform.lvl)
        if (platform.lvl>0) {
            platform.time = ((platform.index+1)*5000).toLong()
            var setTimer = object : CountDownTimer(platform.time, 1000) {
                override fun onFinish() {
                    currentUser.likes += platform.generation
                    //likesText.text = currentUser.likes.toString()
                    platform.timer.start()
                }

                override fun onTick(p0: Long) {
                    platform.timeRemaining = (p0 / 1000).toInt()
                    updateUI()
                }
            }
            platform.timer = setTimer

            platform.timer.start()
        }
        //active platform
        return platform
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        repository = UserRepository.get()
        val getuser = repository.getUser()
        getuser.observe(viewLifecycleOwner,
            Observer { user ->
                if (user == null) {
                    var newuser = User()
                    repository.insertUser(newuser)
                    currentUser = newuser
                }
                else {
                    currentUser = user
                }
                if(platforms.isEmpty()) {
                    for ((index, level) in currentUser.platformLevels.withIndex()) {
                        platforms += setPlatform(index, level)
                    }
                    Log.d(TAG, "set platforms from user")
                }
                updateUI()
            }
        )
    }



    private inner class PlatformHolder(view: View) : ViewHolder(view), View.OnClickListener {
        override fun onClick(p0: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        }

        private val platImageView: ImageView = itemView.findViewById(R.id.platform_image)
        private val timerTextView: TextView = itemView.findViewById(R.id.timer)
        private val levelTextView: TextView = itemView.findViewById(R.id.platform_level)
        private val lvlUpButton: Button = itemView.findViewById(R.id.level_up)


        fun bind(platform: Platform) {

            val draw = platform.name[platform.index]
            val img = getResources().getIdentifier("com.example.project:drawable/$draw",null,null)
            platImageView.setImageResource(img)
            levelTextView.text = "lvl ".plus(platform.lvl.toString())
            timerTextView.text = platform.timeRemaining.toString()
            lvlUpButton.setOnClickListener{
                if(currentUser.likes>= ((platform.index+1)*10)+(platform.lvl*5)) {
                    currentUser.likes -= ((platform.index+1)*10)+(platform.lvl*5)
                    if (platform.lvl == 0) {
                        if (currentUser.platformLevels.size < platform.name.size) {
                            currentUser.platformLevels.add(0)
                            platforms += setPlatform(platforms.size, 0)
                        }
                        platform.time = ((platform.index + 1) * 5000).toLong()
                        var setTimer = object : CountDownTimer(platform.time, 1000) {
                            override fun onFinish() {
                                currentUser.likes += platform.generation
                                platform.timer.start()
                            }

                            override fun onTick(p0: Long) {
                                platform.timeRemaining = (p0 / 1000).toInt()
                                updateUI()
                            }
                        }
                        platform.timer = setTimer
                        platform.timer.start()
                    }
                    currentUser.platformLevels[platform.index]++
                    platform.lvl++
                    levelTextView.text = "lvl ".plus(platform.lvl.toString())
                    platform.generation =
                        ((platform.index + 1) * (platform.index + 1)) * platform.lvl
                    repository.updateUser(currentUser)
                    updateUI()
                    Log.d(TAG, "level up")
                }//likes sufficient
                else{
                    var cost = ((platform.index+1)*10)+(platform.lvl*5)
                    Toast.makeText(context, "Cost to level up is $cost likes",Toast.LENGTH_LONG).show()
                }
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
    fun onUpgrade()
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onStop() {
        repository.updateUser(currentUser)
        super.onStop()

    }

    override fun onDetach() {
        repository.updateUser(currentUser)
        super.onDetach()
        callbacks = null
    }

    override fun onDestroy() {
        super.onDestroy()
        repository.updateUser(currentUser)
    }
}
