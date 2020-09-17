package ir.kindnesswall.view.main.more.hami

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.hami.HamiModel
import kotlinx.android.synthetic.main.activity_about_us.backImageView
import kotlinx.android.synthetic.main.hami_activity.*
import org.koin.android.viewmodel.ext.android.viewModel

class HamiActivity : BaseActivity() {

    private lateinit var hamiAdapter: HamiAdapter
    private val hamiViewModel: HamiViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hami_activity)
        configureViews(savedInstanceState)

        initialData()

        srl.setOnRefreshListener {
            initialData()
        }

    }

    private fun initialData() {
        hamiViewModel.getHamiLiveData().observe(this, Observer {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                    if (!srl.isRefreshing)
                        progressBar.visibility = View.VISIBLE
                }
                CustomResult.Status.ERROR -> {
                    srl.isRefreshing = false
                    progressBar.visibility = View.GONE
                    if (it.errorMessage != null)
                        Toast.makeText(this, it.errorMessage.message, Toast.LENGTH_SHORT).show()
                }
                CustomResult.Status.SUCCESS -> {
                    srl.isRefreshing = false
                    progressBar.visibility = View.GONE
                    hamiAdapter = HamiAdapter(it.data as MutableList<HamiModel>?)
                    rv.adapter = hamiAdapter

                }
            }
        })
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        super.configureViews(savedInstanceState)
        backImageView.setOnClickListener { onBackPressed() }
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, HamiActivity::class.java))
        }
    }
}