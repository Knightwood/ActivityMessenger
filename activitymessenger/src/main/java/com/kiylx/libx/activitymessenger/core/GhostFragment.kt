package com.kiylx.libx.activitymessenger.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.kiylx.libx.activitymessenger.patch.IntentActionDelegateHolder

//=================================================最终方法=======================================//

/**
 * @param starter
 * @param intent 传入intent
 * @param callback startActivityForResult之后执行block块
 */
fun finallyLaunchActivityForResult(
    starter: Fragment,
    intent: Intent,
    callback: ((resultCode: Int, result: Intent?) -> Unit)
) {
    val fm: FragmentManager = starter.childFragmentManager
    ActionHolder(intent,fm,callback).start()
}

/**
 * @param starter
 * @param intent 传入intent
 * @param callback startActivityForResult之后执行block块
 */
 fun finallyLaunchActivityForResult(
    starter: FragmentActivity,
    intent: Intent,
    callback: ((resultCode: Int, result: Intent?) -> Unit)
) {
    val fm = starter.supportFragmentManager
    ActionHolder(intent,fm,callback).start()
}

class ActionHolder(
    val intent: Intent,
    val fm: FragmentManager,
    val callback: (code: Int, intent: Intent?) -> Unit
) {
    private lateinit var fragment: GhostFragment

    fun start() {
        this.fragment = GhostFragment.newInstance(intent,this)
        fm.beginTransaction()
            .add(fragment, GhostFragment::class.java.simpleName)
            .commitAllowingStateLoss()
    }

    fun release() {
        //通知fm关联的Activity,一切已经处理完成
        fm.setFragmentResult(IntentActionDelegateHolder.REQUEST_KEY,
            Bundle().apply {
                putExtras(IntentActionDelegateHolder.RESULT_KEY to true)
            }
        )
        fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
}

/**
 * 真正执行startActivityForResult的地方
 *
 * 初始化此实例，调用[init]赋予callback,在attach到activity时自动执行startActivityForResult，
 * 获取结果后，通过callback传出去。
 */
class GhostFragment : Fragment() {
    private lateinit var intent: Intent
    private lateinit var actionHolder: ActionHolder

    private var register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            actionHolder.callback.invoke(activityResult.resultCode,activityResult.data)
            actionHolder.release()
        }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        register.launch(intent)
    }


    override fun onDetach() {
        super.onDetach()
        register.unregister()
    }

    companion object {
        fun newInstance(intent: Intent, actionHolder: ActionHolder): GhostFragment {
            return GhostFragment().apply {
                this.intent = intent
                this.actionHolder = actionHolder
            }
        }
    }
}