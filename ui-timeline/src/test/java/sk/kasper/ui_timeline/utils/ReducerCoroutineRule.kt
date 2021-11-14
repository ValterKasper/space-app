package sk.kasper.ui_timeline.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import sk.kasper.ui_common.viewmodel.ReducerViewModel

@ExperimentalCoroutinesApi
class ReducerCoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    override fun starting(description: Description) {
        super.starting(description)
        ReducerViewModel.setDispatcher(TestCoroutineDispatcher())
    }

    override fun finished(description: Description) {
        super.finished(description)
        ReducerViewModel.resetDispatcher()
    }

}