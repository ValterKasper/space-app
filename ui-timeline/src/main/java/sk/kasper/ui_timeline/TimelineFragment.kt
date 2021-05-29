package sk.kasper.ui_timeline

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.utils.createSlideAnimNavOptions
import sk.kasper.ui_common.utils.viewModels
import sk.kasper.ui_timeline.ui.FilterDrawer
import sk.kasper.ui_timeline.ui.Timeline
import timber.log.Timber


class TimelineFragment : BaseFragment() {

    private val timelineViewModel: TimelineViewModel by viewModels()

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModels()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.filterComposeView).setContent {
            FilterDrawer(timelineViewModel)
        }

        view.findViewById<ComposeView>(R.id.timelineComposeView).setContent {
            Timeline(timelineViewModel)
        }

        drawerLayout = view.findViewById(R.id.drawerLayout)

        // todo do something
        // (activity as MainActivity).setIdle(!it)
    }

    private fun observeViewModels() {
        lifecycleScope.launchWhenStarted {
            timelineViewModel.sideEffects.collect {
                Timber.d("$it")
                when (it) {
                    SideEffect.ConnectionError -> {
                        Snackbar.make(
                            drawerLayout,
                            "Connection error occurred",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    SideEffect.ShowFilter -> {
                        drawerLayout.openDrawer(GravityCompat.END)
                    }
                    is SideEffect.NavigateTo -> {
                        findNavController().navigate(
                            Uri.parse(it.uriString),
                            createSlideAnimNavOptions()
                        )
                    }
                }
            }
        }
    }

    // todo move to state, when is used compose
    override fun onBackPress(): Boolean =
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
            true
        } else {
            false
        }
}
