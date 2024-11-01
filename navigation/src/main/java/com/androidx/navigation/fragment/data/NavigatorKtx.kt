package com.androidx.navigation.fragment.data

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

fun sharedElementAnim(vararg params: Pair<View, String>): FragmentNavigator.Extras =
    FragmentNavigatorExtras(*params)

@MainThread
fun Fragment.navWithAction(
    @IdRes resId: Int,
    navOptions: NavOptions? = null
) = findNavController().navWithAction(resId, navOptions)

@MainThread
fun NavController.navWithAction(
    @IdRes resId: Int,
    navOptions: NavOptions? = null
) = NavigatorParams(this, resId, navOptions)


/**
 * FragmentA->FragmentB ，返回A时，调用此方法，将数据设置给A观察的livedata
 * 之后调用返回，回到A，A就可以观察到传回的数据。
 * ```
 * setResult(ResultCode.FAILED,
 *  "data" to "funsk"
 * )
 * findNavController().popBackStack()
 * ```
 *
 */
fun Fragment.setResult(resultCode: Int = ResultCode.SUCCESS, vararg params: Pair<String, Any?>) {
    arguments?.run {
        val requestCode = getString(NavigatorParams.Request_Code)
            ?: throw IllegalArgumentException("requestCode为null")
        val targetFragmentId = getInt(NavigatorParams.Result_Receiver_Fragment_ID)
        val bundleParams = Bundle()
            .putExtras(*params)
            .putExtras(
                NavigatorParams.Request_Code to requestCode,
                NavigatorParams.Result_Code to resultCode,
                NavigatorParams.Result_Receiver_Fragment_ID to targetFragmentId,
            )
        findNavController().getBackStackEntry(targetFragmentId).savedStateHandle
            .getLiveData<Bundle>(requestCode)
            .postValue(bundleParams)
    }
}

class ResultCode {
    companion object {
        const val SUCCESS = 1
        const val FAILED = -1
    }
}

/**
 * * 用法
 * ```
 * navController.navWithId(R.id.action_coachFragment_to_lessonsFragment)
 *  .addAnim(page.search to Params.searchView) //添加共享元素动画
 *  .withOptions {
 *     this.setEnterAnim(R.anim.fade_in)
 *  }
 *  //添加多种类型参数，支持Parcelable或者Serializable
 *  .withDatas(//添加bundles参数
 *      AFragment.Params.tag1 to "1",
 *      AFragment.Params.tag2 to "2",
 *      AFragment.Params.tag3 to "3",
 *      "ParcelableData" to data1,
 *      "SerializableData" to data2,
 *  )
 * .observeResult { re->
 *      re.observe(this) { bundle ->
 *      Log.d(
 *      "tty1-NavigatorParams",
 *      "得到回传参数：${bundle.resultCode},${bundle.getString("data")}"
 *      )
 *      re.removeObservers(this) //使用完传回数据后，取消观察
 *          }
 * }
 * .done()
 * ```
 */
class NavigatorParams(
    var controller: NavController?,
    @IdRes var resId: Int,
    var navOptions: NavOptions? = null,
) {
    var requestCode: String = "${resId}_${UUID.randomUUID()}"
    var bundleParams: Bundle? = null
    var navigatorExtras: FragmentNavigator.Extras? = null



    /**
     * 不在[navWithAction]方法中传入options，可以在此处动态构建
     *
     * 如果在此处动态构建，会替换掉在[navWithAction]方法中指定的options
     */
    fun withOptions(block: NavOptions.Builder.() -> NavOptions.Builder): NavigatorParams {
        this.navOptions = NavOptions.Builder().block().build()
        return this
    }

    fun addAnim(vararg params: Pair<View, String>): NavigatorParams {
        this.navigatorExtras = FragmentNavigatorExtras(*params)
        return this
    }

    /**
     *  //添加多种类型参数，支持Parcelable或者Serializable
     *  .withDatas(//添加bundles参数
     *      AFragment.Params.tag1 to "1",
     *      AFragment.Params.tag2 to "2",
     *      AFragment.Params.tag3 to "3",
     *      "ParcelableData" to data1,
     *      "SerializableData" to data2,
     *  )
     */
    fun withDatas(vararg params: Pair<String, Any?>): NavigatorParams {
        if (this.bundleParams == null) {
            bundleParams = Bundle()
        }
        this.bundleParams?.putExtras(*params)
        return this
    }

    /**
     * 对于 Fragment_A->Fragment_B, A里面调用此方法后，
     * A将观察自身返回栈里的livedata，B里面调用setResult将要返回的数据放进A返回栈的livedata里
     * @param resultReceiverId A->B 则传A的id，A->B->C 若希望c调用[setResult]后,由A接收数据，则传A的id。
     * 若是什么都不传，默认就是当前的fragment的id
     * @param requestTag 传入requestTag以覆盖默认的值
     * @param block 观察传回的数据
     *
     * ```
     * navController.navWithId(R.id.action_coachFragment_to_fitnessPersonFragment)
     * .addAnim(page.search to Params.Anim.searchView) //共享元素动画
     * .withDatas("data" to SomeData)//如果用observeResult观察返回数据，则withDatas方法必须在observeResult之前调用
     * .observeResult { re->
     *      re.observe(this) { bundle ->
     *      Log.d(
     *      "tty1-NavigatorParams",
     *      "得到回传参数：${bundle.resultCode},${bundle.getString("data")}"
     *      )
     *      re.removeObservers(this) //使用完传回数据后，取消观察
     *          }
     * }
     * .done()
     *```
     */
    fun observeResult(
        resultReceiverId: Int? = null,
        requestTag: String? = null,
        block: (data: LiveData<Bundle>) -> Unit
    ) {
        if (requestTag != null) {
            this.requestCode = requestTag
        }

        //观察数据变更
        controller?.let {
            //将RequestCode和导航目的地的id也放进bundle里
            val currentId = resultReceiverId ?: it.currentDestination?.id ?: let {
                throw Exception("找不到当前的目的地id")
            }
            if (bundleParams == null) {
                bundleParams = Bundle()
            }
            bundleParams?.putExtras(
                Result_Receiver_Fragment_ID to currentId,
                Request_Code to requestCode
            )
            it.currentBackStackEntry?.let { backStackEntry ->
                val data: MutableLiveData<Bundle> =
                    backStackEntry.savedStateHandle.getLiveData<Bundle>(requestCode)
                block(data)
            } ?: let {
                Log.e(TAG, "没有找到返回栈")
            }
        }
    }

    /**
     * 作用同[observeResult]
     */
    fun collectResult(
        resultReceiverId: Int? = null,
        requestTag: String? = null,
        block: (data: StateFlow<Bundle>) -> Unit
    ) {
        if (requestTag != null) {
            this.requestCode = requestTag
        }

        //观察数据变更
        controller?.let {
            //将RequestCode和导航目的地的id也放进bundle里
            val currentId = resultReceiverId ?: it.currentDestination?.id ?: let {
                throw Exception("找不到当前的目的地id")
            }
            if (bundleParams == null) {
                bundleParams = Bundle()
            }
            bundleParams?.putExtras(
                Result_Receiver_Fragment_ID to currentId,
                Request_Code to requestCode
            )
            it.currentBackStackEntry?.let { backStackEntry ->
                val data: StateFlow<Bundle> =
                    backStackEntry.savedStateHandle.getStateFlow<Bundle>(requestCode, bundleParams!!)
                block(data)
            } ?: let {
                Log.e(TAG, "没有找到返回栈")
            }
        }
    }

    fun done() {
        controller?.navigate(resId, bundleParams, navOptions, navigatorExtras)
        this.clear()
    }

    fun clear() {
        controller = null
        bundleParams = null
        navOptions = null
        navigatorExtras = null
    }

    companion object {
        const val TAG = "tty1-NavigatorParams"
        const val Result_Receiver_Fragment_ID = "resultArgsRecipientId"
        const val Request_Code = "ResultArgsRequestCode"
        const val Result_Code = "ResultArgsResultCode"
        const val data = "ResultArgsBundle"

        val Bundle.requestTag: String
            get() {
                return this.getString(Request_Code)!!
            }

        val Bundle.resultCode: Int
            get() {
                return this.getInt(Result_Code)
            }
    }
}