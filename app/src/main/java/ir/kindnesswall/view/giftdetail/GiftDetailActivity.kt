package ir.kindnesswall.view.giftdetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.R
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.ActivityGiftDetailBinding
import ir.kindnesswall.utils.extentions.runOrStartAuth
import ir.kindnesswall.utils.shareString
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import ir.kindnesswall.view.gallery.GalleryActivity
import ir.kindnesswall.view.main.addproduct.SubmitGiftActivity
import ir.kindnesswall.view.main.conversation.chat.ChatActivity
import org.koin.android.viewmodel.ext.android.viewModel

class GiftDetailActivity : BaseActivity(), GiftViewListener {

    lateinit var binding: ActivityGiftDetailBinding

    val viewModel: GiftDetailViewModel by viewModel()

    companion object {
        const val GIFT_DETAIL_REQUEST_CODE = 185
        fun start(context: Context, giftModel: GiftModel) {
            context.startActivity(Intent(context, GiftDetailActivity::class.java).apply {
                putExtra("giftModel", giftModel)
            })
        }

        fun startActivityForResult(activity: AppCompatActivity, giftModel: GiftModel) {
            val intent = Intent(activity, GiftDetailActivity::class.java)
            intent.putExtra("giftModel", giftModel)
            activity.startActivityForResult(intent, GIFT_DETAIL_REQUEST_CODE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_gift_detail)

        viewModel.giftModel = intent?.getParcelableExtra("giftModel") as GiftModel
        if (viewModel.giftModel == null) {
            finish()
        }

        viewModel.isMyGift = viewModel.giftModel?.userId == UserInfoPref.userId
        viewModel.isDonatedToSomeone =
            viewModel.giftModel!!.donatedToUserId != null && viewModel.giftModel!!.donatedToUserId!! > 0

        configureViews(savedInstanceState)
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        viewModel.giftViewListener = this
        binding.item = viewModel.giftModel
        binding.viewModel = viewModel

        if (UserInfoPref.isAdmin && !viewModel.giftModel!!.isReviewed) {
            binding.reviewGiftsContainer.visibility = View.VISIBLE
            binding.requestButton.visibility = View.GONE
        }

        setupPhotoSlider()

        if (viewModel.isMyGift || UserInfoPref.isAdmin) {
            binding.situationTextView.visibility = View.VISIBLE
            binding.situationText.visibility = View.VISIBLE
            binding.secondDivider.visibility = View.VISIBLE

            setSituationText()
            setSituationTextIfIsAdmin()
        } else {
            binding.situationTextView.visibility = View.GONE
            binding.situationText.visibility = View.GONE
            binding.secondDivider.visibility = View.GONE
        }

        if (!viewModel.isMyGift &&
            viewModel.giftModel!!.donatedToUserId != null &&
            viewModel.giftModel!!.donatedToUserId!! > 0 &&
            viewModel.giftModel!!.donatedToUserId != UserInfoPref.userId
        ) {
            binding.requestButton.visibility = View.GONE
        }

        if (viewModel.giftModel!!.donatedToUserId == UserInfoPref.userId) {
            viewModel.isReceivedGift = true
            binding.requestButton.text = getString(R.string.talk_with_donator)
        } else if (viewModel.giftModel!!.donatedToUserId != null && viewModel.giftModel!!.donatedToUserId!! > 0) {
            viewModel.isReceivedGift = true
            binding.requestButton.text = getString(R.string.talk_with_receiver)
        } else if (viewModel.isMyGift) {
            binding.requestButton.text = getString(R.string.edit)
        } else {
            binding.requestButton.text = getString(R.string.request)
        }
    }

    private fun setSituationTextIfIsAdmin() {
        viewModel.giftModel?.let {
            if (it.isDeleted && UserInfoPref.isAdmin) {
                binding.situationTextView.text = getString(R.string.deleted)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.rejectTextColor)
                )
            } else if (it.donatedToUserId == UserInfoPref.userId) {
                binding.situationTextView.visibility = View.VISIBLE
                binding.situationText.visibility = View.VISIBLE
                binding.secondDivider.visibility = View.VISIBLE

                binding.situationTextView.text = getString(R.string.gift_received)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
                )
            }
        }
    }

    private fun setupPhotoSlider() {
        binding.photoSlider
            .initialize(
                zoomable = false,
                showIndicator = true,
                clickListener = {
                    gotoGalleryActivity()
                },
                pageChangeCallback = { selectedImageIndex ->
                    viewModel.selectedImageIndex = selectedImageIndex
                })
            .show(viewModel.giftModel?.giftImages)

    }

    private fun setSituationText() {
        binding.situationTextView.text = ""
        viewModel.giftModel?.let {
            if (!it.isReviewed) {
                binding.situationTextView.text = getString(R.string.situation_in_review_queue)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.situation_yellow_color)
                )

                return
            }

            if (it.isReviewed && !it.isRejected && it.donatedToUserId != null && it.donatedToUserId!! > 0) {
                binding.situationTextView.text = getString(R.string.filter_profile_donate)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
                )

                return
            }

            if (it.isReviewed && it.isRejected) {
                binding.situationTextView.text =
                    getString(R.string.rejected_place_holder, it.rejectReason.toString())
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.rejectTextColor)
                )

                return
            }

            if (!it.isRejected) {
                binding.situationTextView.text = getString(R.string.situation_confirm)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
                )
            }
        }
    }

    private fun gotoGalleryActivity() {
        GalleryActivity.start(
            this,
            viewModel.giftModel?.giftImages as ArrayList<String>,
            viewModel.selectedImageIndex
        )
    }

    override fun onBackButtonClicked() {
        onBackPressed()
    }

    override fun onEditButtonClicked() {
        SubmitGiftActivity.start(this, viewModel.giftModel)
        finish()
    }

    override fun onRequestClicked() {
        runOrStartAuth {
            if (viewModel.giftModel!!.donatedToUserId != null && viewModel.giftModel!!.donatedToUserId!! > 0) {
                viewModel.getRequestStatus().observe(this) {
                    if (it.status == CustomResult.Status.SUCCESS && it.data != null) {
                        ChatActivity.start(
                            this,
                            it.data.chat,
                            it.data.chat.contactProfile?.isCharity ?: false
                        )
                    } else if (it.status == CustomResult.Status.ERROR) {
                        if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                            NoInternetDialogFragment().display(supportFragmentManager) {
                                onRequestClicked()
                            }
                        } else {
                            showToastMessage(getString(R.string.please_try_again))
                        }
                    }
                }
                return
            }

            if (viewModel.isMyGift) {
                return
            }

            if (!UserInfoPref.isCharity && !UserInfoPref.isAdmin) {
                showPromptDialog(
                    messageToShow = getString(R.string.request_for_users_error_message),
                    positiveButtonText = getString(R.string.ok),
                    showNegativeButton = false
                )

                return
            }

            if (viewModel.isReceivedGift) {
                viewModel.getRequestStatus().observe(this) {
                    if (it.status == CustomResult.Status.SUCCESS && it.data != null) {
                        ChatActivity.start(
                            this,
                            it.data.chat,
                            it.data.chat.contactProfile?.isCharity ?: false
                        )
                    } else if (it.status == CustomResult.Status.ERROR) {
                        if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                            NoInternetDialogFragment().display(supportFragmentManager) {
                                onRequestClicked()
                            }
                        } else {
                            showToastMessage(getString(R.string.please_try_again))
                        }
                    }
                }
            } else {
                viewModel.requestGift().observe(this) {
                    when (it.status) {
                        CustomResult.Status.SUCCESS -> {
                            it.data?.let { data ->
                                ChatActivity.start(this, data, false)
                            }
                        }
                        CustomResult.Status.ERROR -> {
                            if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                                NoInternetDialogFragment().display(supportFragmentManager) {
                                    onRequestClicked()
                                }
                            } else {
                                showToastMessage(getString(R.string.please_try_again))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onShareClicked() {
        shareString(this, "Share kindnessWall message", "subject")
    }

    override fun onBookmarkClicked() {

    }

    override fun onAcceptGiftClicked() {
        viewModel.acceptGift(viewModel.giftModel!!.id)
            .observe(this) { result ->
                if (result.status == CustomResult.Status.SUCCESS) {
                    viewModel.giftModel!!.isReviewed = true
                    viewModel.giftModel!!.isRejected = false

                    binding.reviewGiftsContainer.visibility = View.GONE
                    binding.requestButton.visibility = View.VISIBLE

                    setSituationText()
                    setSituationTextIfIsAdmin()
                    returnResult(true, "isReviews")
                } else if (result.status == CustomResult.Status.ERROR) {
                    if (result.errorMessage?.message!!.contains("Unable to resolve host")) {
                        NoInternetDialogFragment().display(supportFragmentManager) {
                            onAcceptGiftClicked()
                        }
                    } else {
                        showToastMessage(getString(R.string.please_try_again))
                    }
                }
            }
    }

    override fun onRejectGiftClicked() {
        showGetInputDialog(Bundle().apply {
            putString("title", getString(R.string.Please_write_reason))
            putString("hint", getString(R.string.reason_of_reject))
            putString("accept_btn", getString(R.string.reject_gift))
        }, approveListener = {
            viewModel.rejectGift(viewModel.giftModel!!.id, it)
                .observe(this) { result ->
                    if (result.status == CustomResult.Status.SUCCESS) {
                        viewModel.giftModel!!.isReviewed = true
                        viewModel.giftModel!!.isRejected = true

                        binding.reviewGiftsContainer.visibility = View.GONE
                        binding.requestButton.visibility = View.VISIBLE

                        setSituationText()
                        setSituationTextIfIsAdmin()

                        returnResult(true, "isReviews")
                    } else if (result.status == CustomResult.Status.ERROR) {
                        if (result.errorMessage?.message!!.contains("Unable to resolve host")) {
                            NoInternetDialogFragment().display(supportFragmentManager) {
                                onRejectGiftClicked()
                            }
                        } else {
                            showToastMessage(getString(R.string.please_try_again))
                        }
                    }
                }
        })
    }

    override fun onDeleteButtonClicked() {
        showPromptDialog(
            title = getString(R.string.delete_gift),
            messageToShow = getString(R.string.sure_to_remove_gift),
            positiveButtonText = getString(R.string.delete_gift),
            negativeButtonText = getString(R.string.no),
            onPositiveClickCallback = {
                viewModel.deleteGift().observe(this) { result ->
                    when (result.status) {
                        CustomResult.Status.LOADING -> {
                            showProgressDialog { }
                        }

                        CustomResult.Status.ERROR -> {
                            dismissProgressDialog()
                            if (result.errorMessage?.message!!.contains("Unable to resolve host")) {
                                NoInternetDialogFragment().display(supportFragmentManager) {
                                    onRejectGiftClicked()
                                }
                            } else {
                                showToastMessage(getString(R.string.please_try_again))
                            }
                        }

                        CustomResult.Status.SUCCESS -> {
                            dismissProgressDialog()
                            KindnessApplication.instance.deletedGifts.add(viewModel.giftModel!!)
                            finish()
                        }
                    }
                }
            })
    }

    private fun returnResult(result: Boolean, task: String) {
        val returnIntent = Intent()
        returnIntent.putExtra(task, result)

        if (result) {
            setResult(Activity.RESULT_OK, returnIntent)
        } else {
            setResult(Activity.RESULT_CANCELED, null)
        }

        finish()
    }
}
