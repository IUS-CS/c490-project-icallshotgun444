package com.example.project

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TAG = "UpgradeFragment"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CrimeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CrimeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpgradeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var followerCount: TextView
    private lateinit var facebookPurchaseButton: Button
    private lateinit var repository: UserRepository

    private lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = UserRepository.get()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.upgrades, container, false)

        Log.d(TAG, "onCreateView")

        followerCount = view.findViewById(R.id.follower_count)
        facebookPurchaseButton = view.findViewById(R.id.facebook_like_button)


            repository.getUser().observe(viewLifecycleOwner,
                Observer { user ->
                        this.user = user
                        updateUI()
                })


        facebookPurchaseButton.setOnClickListener{
            if(user.followers>=5000){
                user.facebookLikeUpgrade =5
                user.followers -= 5000
                repository.updateUser(user)
            }
            else{
                Toast.makeText(this.context,"Not enough followers",Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    fun updateUI() {
        followerCount.text = user.followers.toString()
    }

    override fun onStart() {
        super.onStart()

        Log.d(TAG, "onStart")

    }

    override fun onStop() {
        super.onStop()
            repository.updateUser(user)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        Log.d(TAG, "onDatach")
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CrimeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            UpgradeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
