package com.kiylx.libx.activity_ktx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 *一些快捷方法，可以直接在activity或是fragment中直接调用，以观察livedata或是收集stateflow
 */

//<editor-fold desc="flow订阅">

//<editor-fold desc="activity-flow订阅">
inline fun <reified T> FragmentActivity.collectStateFlow(
    stateFlow: StateFlow<T>,
    collector: FlowCollector<T>
) {
    this.lifecycleOwnerStateFlowScope {
        stateFlow.collect(collector)
    }
}

fun FragmentActivity.collectStateFlowScope(
    block: suspend CoroutineScope.() -> Unit,
) {
    this.lifecycleOwnerStateFlowScope(block)
}
//</editor-fold>

//<editor-fold desc="fragment-flow订阅">
inline fun <reified T> Fragment.collectStateFlow(
    stateFlow: StateFlow<T>,
    collector: FlowCollector<T>
) {
    this.viewLifecycleOwner.lifecycleOwnerStateFlowScope {
        stateFlow.collect(collector)
    }
}

//lifecycleScope :fragment的生命周期
//viewLifecycleOwner和lifecycleScope是不同的生命周期
//viewLifecycleOwner是fragment的view的生命周期
fun Fragment.collectStateFlowScope(
    block: suspend CoroutineScope.() -> Unit,
) {
    viewLifecycleOwner.lifecycleOwnerStateFlowScope(block)
}
//</editor-fold>

//<editor-fold desc="viewmodel-flow订阅">

inline fun <reified T> ViewModel.collectStateFlowScope(
    stateFlow: StateFlow<T>,
    collector: FlowCollector<T>
) {
    this.viewModelScope.launch {
        stateFlow.collect(collector)
    }
}

//</editor-fold>

/**
 * 从这个scope中安全的观察stateflow，具有生命周期
 * 当生命周期处于 STARTED 时安全地执行block
 * 当生命周期进入 STOPPED 时停止
 */
fun LifecycleOwner.lifecycleOwnerStateFlowScope(
    block: suspend CoroutineScope.() -> Unit,
) {
    this.lifecycleScope.launch {
        // repeatOnLifecycle 每当生命周期处于 STARTED 或以后的状态时会在新的协程中
        // 启动执行代码块，并在生命周期进入 STOPPED 时取消协程。
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}

/**
 * 收集flow
 */
inline fun <reified T> LifecycleOwner.lifecycleOwnerCollectStateFlow(
    stateFlow: StateFlow<T>,
    collector: FlowCollector<T>
) = this.lifecycleScope.launch {
    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        stateFlow.collect(collector)
    }
}

//</editor-fold>

//<editor-fold desc="livedata观察">

inline fun <reified T> Fragment.observeLivedata(
    liveData: LiveData<T>,
    observer:Observer<T>
) = viewLifecycleOwner.lifecycleObserveLivedata(liveData, observer)

inline fun <reified T> FragmentActivity.observeLivedata(
    liveData: LiveData<T>,
    observer:Observer<T>
) = this.lifecycleObserveLivedata(liveData, observer)

inline fun <reified T> LifecycleOwner.lifecycleObserveLivedata(
    liveData: LiveData<T>,
    observer:Observer<T>
) = liveData.observe(this, observer)

//</editor-fold>

//<editor-fold desc="lifecycle 中缀函数">

/**
 * ```
 * 示例：
 *
 *在某个activity中：
 *  this observeWith mainViewModel.signResult then {//逻辑}
 *等价于：
 *  mainViewModel.signResult.observe(this){//逻辑}
 *
 * ```
 */
infix fun <T> FragmentActivity.observeWith(data: LiveData<T>): Pair<LifecycleOwner, LiveData<T>> {
    return this to data
}

infix fun <T> Fragment.observeWith(data: LiveData<T>): Pair<LifecycleOwner, LiveData<T>> {
    return this.viewLifecycleOwner to data
}

infix fun <T> Pair<LifecycleOwner, LiveData<T>>.then(observer: Observer<T>) {
    second.observe(first, observer)
}

//</editor-fold>
