package ir.kindnesswall.view.main.charity.charitydetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.ActivityCharityDetailBinding
import ir.kindnesswall.utils.StaticContentViewer
import ir.kindnesswall.utils.extentions.runOrStartAuth
import ir.kindnesswall.utils.shareString
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import ir.kindnesswall.view.main.charity.Rating.RatingActivity
import ir.kindnesswall.view.report.ReportDialog
import org.koin.android.viewmodel.ext.android.viewModel

class CharityDetailActivity : BaseActivity(), CharityViewListener {

    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var binding: ActivityCharityDetailBinding

    private val viewModel: CharityViewModel by viewModel()

    companion object {
        @JvmStatic
        fun start(context: Context, charityModel: CharityModel) {
            context.startActivity(Intent(context, CharityDetailActivity::class.java).apply {
                putExtra("charityModel", charityModel)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_charity_detail)

        viewModel.charityModel = intent?.getSerializableExtra("charityModel") as CharityModel

        if (viewModel.charityModel == null) {
            finish()
        }

        configureViews(savedInstanceState)

        getUserInformation()
        binding.reportButton.setOnClickListener {
            val charityId = viewModel.charityModel?.id ?: return@setOnClickListener

            runOrStartAuth {
                ReportDialog
                    .newInstance(ReportDialog.ReportType.Charity(id = charityId))
                    .show(supportFragmentManager, null)
            }
        }
    }

    private fun getUserInformation() {
        viewModel.getUserInformation().observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                binding.otherUser = it.data
            } else if (it.status == CustomResult.Status.ERROR) {
                if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                    NoInternetDialogFragment().display(supportFragmentManager) {
                        getUserInformation()
                    }
                } else {
                    showToastMessage(getString(R.string.please_try_again))
                }
            }
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.item = viewModel.charityModel

        viewModel.charityViewListener = this
        viewModel.charityModel?.apply {
            if (listOfNotNull(telephoneNumber, telegram, instagram, website).count() == 0)
                binding.informationBottomSheet.charityContentBottomSheet.visibility = View.GONE
        }
        initBottomSheet()
    }

    private fun initBottomSheet() {
        binding.informationBottomSheet.item = viewModel.charityModel
        binding.informationBottomSheet.viewModel = viewModel

        sheetBehavior =
            BottomSheetBehavior.from(binding.informationBottomSheet.charityContentBottomSheet)

        binding.informationBottomSheet.toolbar.setOnClickListener {
            if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onBackPressed() {
        if (sheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    override fun onBackButtonClicked() {
        onBackPressed()
    }

    override fun onShareClicked() {
        shareString(this, "Share kindnessWall message", "subject")
    }

    override fun onBookmarkClicked() {
    }

    override fun onRatingClick() {
        val intent = Intent(this, RatingActivity::class.java)
        startActivity(intent)
    }

    override fun onCallClicked() {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", viewModel.charityModel?.telephoneNumber ?: "", null)
        )

        startActivity(intent)
    }

    override fun onTelegramClicked() {
        StaticContentViewer.show(this, viewModel.charityModel?.telegram)
    }

    override fun onInstagramClicked() {
        StaticContentViewer.show(this, viewModel.charityModel?.instagram)
    }

    override fun onEmailClicked() {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", viewModel.charityModel?.email, null)
        )

        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    override fun onWebsiteClicked() {
        StaticContentViewer.show(this, viewModel.charityModel?.website)
    }
}
