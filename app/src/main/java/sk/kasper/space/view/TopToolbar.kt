package sk.kasper.space.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import com.google.android.material.appbar.MaterialToolbar
import sk.kasper.space.R

private const val NAVI_ICON_NONE = 0
private const val NAVI_ICON_BACK = 1

class TopToolbar: MaterialToolbar {

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let {
            context.withStyledAttributes(attrs, R.styleable.TopToolbar, defStyleAttr, R.style.MyToolbarStyle) {
                when (getInteger(R.styleable.TopToolbar_naviIcon, NAVI_ICON_NONE)) {
                    NAVI_ICON_BACK -> setNavigationIcon(R.drawable.ic_arrow_back)
                    NAVI_ICON_NONE -> navigationIcon = null
                }
            }
        }
    }

}