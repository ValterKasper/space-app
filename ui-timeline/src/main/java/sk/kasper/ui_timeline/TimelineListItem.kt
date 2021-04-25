package sk.kasper.ui_timeline

interface TimelineListItem {

    companion object {
        const val LABEL_TYPE = 0
        const val LAUNCH_TYPE = 1
        const val NEXT_LAUNCH_TYPE = 2
    }

    fun getType(): Int

}