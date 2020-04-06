package sk.kasper.space.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.MaterialToolbar
import sk.kasper.space.R

private const val NAVI_ICON_NONE = 0
private const val NAVI_ICON_BACK = 1

class TopToolbar: MaterialToolbar {

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        // todo cez defaultny styl
        attrs?.let {
            val typedArray = resources.obtainAttributes(attrs, R.styleable.TopToolbar)
            naviIcon = typedArray.getInteger(R.styleable.TopToolbar_naviIcon, NAVI_ICON_NONE)
            typedArray.recycle()
        }
    }

    // todo inlinuj
    private var naviIcon: Int = NAVI_ICON_NONE
            set(value) {
                field = value
                when (field) {
                    NAVI_ICON_BACK -> setNavigationIcon(R.drawable.ic_arrow_back)
                    NAVI_ICON_NONE -> {
                        navigationIcon = null
                    }
                }
            }

}