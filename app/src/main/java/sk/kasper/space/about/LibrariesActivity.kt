package sk.kasper.space.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sk.kasper.space.R
import sk.kasper.space.view.TopToolbar


class LibrariesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libraries)

        findViewById<TopToolbar>(R.id.toolbar).setNavigationOnClickListener {
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
