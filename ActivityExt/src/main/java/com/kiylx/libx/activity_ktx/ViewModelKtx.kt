package com.kiylx.libx.activity_ktx

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import java.lang.reflect.Constructor
import java.util.*

//<editor-fold desc="SavedStateHandler给viewmodel传参">

/**
 * Fragment 1.2.0 或者 Activity 1.1.0 起, 可以使用 SavedStateHandle 作为 ViewModel 的参数。
 * SavedStateHandle 可以帮助 ViewModel 实现数据持久化，同时可以传递 Fragment 的 arguments 给 ViewModel。
 *
 * ViewModel 的初次数据加载推荐放到 init{} 中进行，这样可以保证 ViewModelScope 中只加载一次
 * 如果在 ViewModel 构造函数中请求数据，当需要参数时该如何传入呢？ 比如我们需要传入一个 TaskId。
 *
 *
 * 使用：
 * * 在fragment中创建viewmodel
 * ```
 *
 * class DetailTaskFragment : Fragment(R.layout.fragment_detailed_task){
 *
 * private val viewModel by viewModelByFactory(arguments) //初始化viewmodel，传入bundle参数
 *
 * override fun onViewCreated(view: View, savedInstanceState:Bundle?) {
 *      super.onViewCreated(view, savedInstanceState)
 *      //...
 *    }
 * }
 *
 * 除了 SavedStateHandler 以外如果还希望增加更多参数，还可以自定义 CreateViewModel
 *
 * ```
 *
 * * viewmodel的实现，以及在init块中初始化
 *
 * ```
 * class TaskViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
 *  //如果 ViewModel 在 fragment的 onViewCreated 中请求数据，当 View 因为横竖屏等原因重建时会再次请求，
 *  //而我们知道 ViewModel 的生命周期长于 View，数据可以跨越 View 的生命周期存在，所以没有必要随着 View 的重建反复请求。
 *  //ViewModel 的初次数据加载推荐放到 init{} 中进行，这样可以保证 ViewModelScope 中只加载一次
 *
 *  //...
 *  init {
 *       viewModelScope.launch {
 *           _tasks.value = TasksRepository.fetchTask(
 *               savedStateHandle.get<Int>(TASK_ID)// 获取传进来的bundle参数
 *           )
 *       }
 *   }
 *}
 *
 * ```
 */
typealias CreateViewModel = (handle: SavedStateHandle) -> ViewModel

inline fun <reified VM : ViewModel> Fragment.viewModelByFactory(
    defaultArgs: Bundle? = null,
    noinline create: CreateViewModel = {
        val constructor =
            findMatchingConstructor(VM::class.java, arrayOf(SavedStateHandle::class.java))
        constructor!!.newInstance(it)
    }
): Lazy<VM> {
    return viewModels {
        createViewModelFactoryFactory(this, defaultArgs, create)
    }
}

inline fun <reified VM : ViewModel> Fragment.activityViewModelByFactory(
    defaultArgs: Bundle? = null,
    noinline create: CreateViewModel
): Lazy<VM> {
    return activityViewModels {
        createViewModelFactoryFactory(this, defaultArgs, create)
    }
}

fun createViewModelFactoryFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle?,
    create: CreateViewModel
): ViewModelProvider.Factory {
    return object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
        override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            @Suppress("UNCHECKED_CAST")
            return create(handle) as? T
                ?: throw IllegalArgumentException("Unknown viewmodel class!")
        }
    }
}

@PublishedApi
internal fun <T> findMatchingConstructor(
    modelClass: Class<T>,
    signature: Array<Class<*>>
): Constructor<T>? {
    for (constructor in modelClass.constructors) {
        val parameterTypes = constructor.parameterTypes
        if (Arrays.equals(signature, parameterTypes)) {
            return constructor as Constructor<T>
        }
    }
    return null
}
//</editor-fold>
