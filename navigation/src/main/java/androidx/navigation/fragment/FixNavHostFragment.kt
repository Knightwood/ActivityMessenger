package androidx.navigation.fragment

import android.view.View
import androidx.navigation.NavHostController


public open class FixNavHostFragment : NavHostFragment() {
    override fun onCreateNavHostController(navHostController: NavHostController) {
        super.onCreateNavHostController(navHostController)
        navHostController.navigatorProvider.addNavigator(createFragmentNavigator2())
    }

    private fun createFragmentNavigator2(): FixFragmentNavigator {
        return FixFragmentNavigator(requireContext(), childFragmentManager, containerId)
    }

    private val containerId: Int
        get() {
            val id = id
            return if (id != 0 && id != View.NO_ID) {
                id
            } else R.id.nav_host_fragment_container
            // Fallback to using our own ID if this Fragment wasn't added via
            // add(containerViewId, Fragment)
        }
}