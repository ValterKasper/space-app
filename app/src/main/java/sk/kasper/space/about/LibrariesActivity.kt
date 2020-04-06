package sk.kasper.space.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_libraries.*
import sk.kasper.space.R


class LibrariesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libraries)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.librariesRecyclerView)
        recyclerView.adapter = LibrariesAdapter()
        val itemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_right, R.anim.exit_right)
    }

}
