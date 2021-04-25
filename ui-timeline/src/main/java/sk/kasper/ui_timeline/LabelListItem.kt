package sk.kasper.ui_timeline

sealed class LabelListItem(/** higher priority higher in list */
                           private val priority: Int = 0) : TimelineListItem, Comparable<LabelListItem> {
    override fun getType() = TimelineListItem.LABEL_TYPE

    object Today : LabelListItem(100)
    object Tomorrow : LabelListItem(99)
    object ThisWeek : LabelListItem(98)
    data class Month(val month: Int) : LabelListItem(12 - month)

    override fun compareTo(other: LabelListItem): Int {
        return priority.compareTo(other.priority) * -1
    }
}