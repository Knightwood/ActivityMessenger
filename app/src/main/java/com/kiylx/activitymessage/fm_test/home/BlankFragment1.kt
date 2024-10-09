package com.kiylx.activitymessage.fm_test.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentContainerView
import com.kelp.android.fms.NavControllerHelper
import com.kelp.android.fms.fragmentController2
import com.kiylx.activitymessage.R
import com.kiylx.activitymessage.fm_test.T1Activity
import com.kiylx.libx.activitymessenger.fms.controller.buildNavOptions
import com.kiylx.libx.activitymessenger.fms.fragmentController
import com.kiylx.libx.activitymessenger.fms.impl.SealFragmentController
import com.kiylx.libx.activitymessenger.fms.impl.launchFragment
import com.kiylx.libx.activitymessenger.fms.impl.setParentNode
import com.kiylx.libx.activitymessenger.fms.mode.FragmentNode

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
    lateinit var controller: SealFragmentController
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
        val container = view.findViewById<FragmentContainerView>(R.id.nav_host_fragment)
        val c = fragmentController2(container)
        c.launchFragment<BlankFragment3> {

        }

        if (!this::controller.isInitialized) {
            controller = fragmentController(container)
            controller.buildNavGraph {
                plus<BlankFragment1> {
                    //指定fragment生成函数
                    newInstanceFactory = {
                        BlankFragment1()
                    }
                    plus<BlankFragment2> {//添加平级节点
                        //写在里面和外面效果一样
                        //plus<BlankFragment3>()
                    }
                    plus<BlankFragment3>()//添加平级节点
                }
            }.setParentNode<T1Activity>()
        }

//        view.findViewById<TextView>(R.id.tv).setText("BlankFragment1" + UUID.randomUUID().toString())
        view.findViewById<Button>(R.id.btn_1).setOnClickListener {
            controller.launchFragment<BlankFragment2>() {
                attachTo = controller.parent
            }
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