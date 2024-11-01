package com.kiylx.activitymessage.fm_test.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.kiylx.activitymessage.R
import com.kiylx.libx.activitymessenger.fragments.launchFragment
import com.kiylx.libx.activitymessenger.fragments.popUp
import com.kiylx.libx.activitymessenger.fragments.removeSelf

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass. Use the [BlankFragment1.newInstance]
 * factory method to create an instance of this fragment.
 */
class BlankFragment1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_1).setOnClickListener {
            //使用上一级（T1Activity）的FragmentManager，和T1Activity中的容器id，添加新的Fragment到T1Activity
            parentFragmentManager.launchFragment<BlankFragment2>(R.id.fragment_container_view) {
                params("key1" to "value1")
                primaryNavigationFragment = this@BlankFragment1
            }
        }
        view.findViewById<Button>(R.id.btn_2).setOnClickListener {
            //使用上一级（T1Activity）的FragmentManager，和T1Activity中的容器id，添加新的Fragment到T1Activity
            childFragmentManager.launchFragment<BlankFragment3>(R.id.nav_host_fragment) {
                params("key1" to "value1")
            }
        }
        view.findViewById<Button>(R.id.finish).setOnClickListener {
            popUp()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment using
         * the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}