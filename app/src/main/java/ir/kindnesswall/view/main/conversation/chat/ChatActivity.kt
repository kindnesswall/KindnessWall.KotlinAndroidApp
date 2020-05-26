package ir.kindnesswall.view.main.conversation.chat

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.model.*
import ir.kindnesswall.databinding.ActivityChatBinding
import ir.kindnesswall.utils.helper.EndlessRecyclerViewScrollListener
import ir.kindnesswall.utils.imageloader.circleCropTransform
import ir.kindnesswall.utils.imageloader.loadImage
import ir.kindnesswall.view.main.MainActivity
import ir.kindnesswall.view.main.conversation.chat.todonategifts.ToDonateGiftsBottomSheet
import org.koin.android.viewmodel.ext.android.viewModel


class ChatActivity : BaseActivity() {

    lateinit var binding: ActivityChatBinding
    val viewModel: ChatViewModel by viewModel()

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    private lateinit var adapter: ChatAdapter

    private lateinit var chatBroadcastReceiver: ChatBroadcastReceiver

    companion object {
        fun start(
            context: Context,
            requestChatModel: ChatModel,
            isCharity: Boolean = false,
            isStartFromNotification: Boolean = false
        ) {
            context.startActivity(
                Intent(context, ChatActivity::class.java)
                    .putExtra("isStartFromNotification", isStartFromNotification)
                    .putExtra("requestChatModel", requestChatModel)
                    .putExtra("isCharity", isCharity)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

        fun start(
            context: Context,
            chatContactModel: ChatContactModel,
            isCharity: Boolean,
            isStartFromNotification: Boolean = false
        ) {
            context.startActivity(
                Intent(context, ChatActivity::class.java)
                    .putExtra("isStartFromNotification", isStartFromNotification)
                    .putExtra("chatContactModel", chatContactModel)
                    .putExtra("isCharity", isCharity)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        configureViews(savedInstanceState)

        viewModel.isStartFromNotification = intent.getBooleanExtra("isStartFromNotification", false)

        viewModel.chatContactModel =
            intent.getSerializableExtra("chatContactModel") as? ChatContactModel

        viewModel.requestChatModel =
            intent.getSerializableExtra("requestChatModel") as? ChatModel

        viewModel.isCharity = intent.getBooleanExtra("isCharity", false)

        if (viewModel.requestChatModel == null && viewModel.chatContactModel == null) {
            finish()
            return
        }

        viewModel.setSessionId()

        initBroadcastReceiver()

        if (viewModel.requestChatModel != null && viewModel.chatContactModel == null) {
            getUserProfile()
        } else {
            showUserData(
                viewModel.chatContactModel?.contactProfile?.image,
                viewModel.chatContactModel?.contactProfile?.name
            )
        }

        checkBlockState()
        getChats()
        getToDonateGifts()

        viewModel.refreshToDonateList.observe(this) {
            if (it) {
                getToDonateGifts()
            }
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.backImageView.setOnClickListener { onBackPressed() }
        binding.userImageView.setOnClickListener { }
        binding.userNameTextView.setOnClickListener { }

        binding.sendImageView.setOnClickListener {
            if (!viewModel.messageTextLiveData.value.isNullOrEmpty()) {
                viewModel.sendMessage().observe(this) { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            result.data?.let { data ->
                                binding.messageEditText.setText("")
                                viewModel.chatList?.add(0, data)
                                viewModel.chatList?.let { adapter.setData(it) }
                                checkEmptyPage()
                            }
                        }

                        CustomResult.Status.LOADING -> {

                        }

                        CustomResult.Status.ERROR -> {
                            showToastMessage(getString(R.string.error_sending_message))
                        }
                    }
                }
            }
        }

        binding.giftImageView.setOnClickListener { showGiftOptionsMenu() }
        binding.blockUserImageView.setOnClickListener { blockUser() }
        binding.unblockButton.setOnClickListener { unblockUser() }

        initRecyclerView()
    }

    private fun initBroadcastReceiver() {
        chatBroadcastReceiver = ChatBroadcastReceiver { message ->
            if (message.chatId != viewModel.chatId) {
                return@ChatBroadcastReceiver
            }

            viewModel.chatList?.add(0, message)
            viewModel.chatList?.let { adapter.setData(it) }
            viewModel.sendAckMessage(message.id, message.chatId)

            checkEmptyPage()
        }

        val filter = IntentFilter("CHAT")
        registerReceiver(chatBroadcastReceiver, filter)
    }

    private fun initRecyclerView() {
        binding.itemsListRecyclerView.adapter = getAdapter()

        val animator = binding.itemsListRecyclerView.itemAnimator

        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }

        (binding.itemsListRecyclerView.layoutManager as? LinearLayoutManager)?.reverseLayout = true
        (binding.itemsListRecyclerView.layoutManager as? LinearLayoutManager)?.stackFromEnd = false

        setRecyclerViewPagination(binding.itemsListRecyclerView.layoutManager as LinearLayoutManager)
    }

    private fun setRecyclerViewPagination(layoutManager: LinearLayoutManager) {
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore() {
                    endlessRecyclerViewScrollListener.isLoading = true
                    viewModel.gatherLastId()
                    getChats()
                }

                override fun onScrolled(position: Int) {
                }
            }

        endlessRecyclerViewScrollListener.PAGE_SIZE = 10
        endlessRecyclerViewScrollListener.VISIBLE_THRESHOLD = 5

        binding.itemsListRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun getAdapter(): ChatAdapter {
        if (!::adapter.isInitialized) {
            adapter = ChatAdapter(viewModel)
        }

        adapter.setHasStableIds(true)

        return adapter
    }

    private fun getToDonateGifts() {
        viewModel.getToDonateGifts().observe(this) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }
                CustomResult.Status.ERROR -> {
                }
                CustomResult.Status.SUCCESS -> {
                    if (it.data.isNullOrEmpty()) {
                        return@observe
                    }

                    viewModel.toDonateList.clear()
                    viewModel.toDonateList.addAll(it.data)
                }
            }
        }
    }

    private fun getUserProfile() {
        if (viewModel.isCharity) {
            viewModel.getCharityProfile().observe(this) {
                when (it.status) {
                    CustomResult.Status.SUCCESS -> {
                        showUserData(it.data?.imageUrl, it.data?.name)
                    }
                    CustomResult.Status.ERROR -> {
                        if (viewModel.triedToFetchCharityAndUserProfile) {
                            finish()
                            return@observe
                        }

                        viewModel.triedToFetchCharityAndUserProfile = true
                        viewModel.isCharity = false
                        getUserProfile()
                    }
                }
            }
        } else {
            viewModel.getUserProfile().observe(this) {
                when (it.status) {
                    CustomResult.Status.SUCCESS -> {
                        showUserData(it.data?.image, it.data?.name)
                    }
                    CustomResult.Status.ERROR -> {
                        if (viewModel.triedToFetchCharityAndUserProfile) {
                            finish()
                            return@observe
                        }

                        viewModel.triedToFetchCharityAndUserProfile = true
                        viewModel.isCharity = true
                        getUserProfile()
                    }
                }
            }
        }
    }

    private fun showUserData(imageUrl: String?, name: String?) {
        loadImage(
            imageUrl,
            binding.userImageView,
            placeHolderId = R.drawable.ic_profile_place_holder_white,
            options = circleCropTransform()
        )

        binding.userNameTextView.text = name
    }

    private fun getChats() {
        viewModel.getChats().observe(this) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }

                CustomResult.Status.SUCCESS -> {
                    endlessRecyclerViewScrollListener.isLoading = false
                    showList(it.data)
                }

                CustomResult.Status.ERROR -> {
                    endlessRecyclerViewScrollListener.isLoading = false
                }
            }
        }
    }

    private fun showList(chatMessageModel: ChatMessageModel?) {
        if (chatMessageModel == null) {
            return
        }

        if (chatMessageModel.textMessages.isNullOrEmpty()) {
            return
        }

        val items = arrayListOf<TextMessageBaseModel>()
        val textMessages: List<TextMessageModel>? = chatMessageModel.textMessages

        viewModel.chatList?.addAll(chatMessageModel.textMessages!!)
        viewModel.chatList?.let { adapter.setData(it) }

        checkEmptyPage()
    }

    private fun showGiftOptionsMenu() {
        if (viewModel.toDonateList.isNullOrEmpty()) {
            showToastMessage(getString(R.string.no_gift_to_donate))
            return
        }

        ToDonateGiftsBottomSheet.newInstance(viewModel.toDonateList, viewModel.receiverUserId)
            .apply {
                setOnItemClickListener {
                    this@ChatActivity.viewModel.refreshToDonateList.value = it
                }
            }.show(supportFragmentManager, "donate")
    }

    private fun checkEmptyPage() {
        if (viewModel.chatList.isNullOrEmpty()) {
            binding.noChatTextView.visibility = View.VISIBLE
        } else {
            binding.noChatTextView.visibility = View.GONE
        }
    }

    private fun unblockUser() {
        viewModel.unblockUser().observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                KindnessApplication.instance.getContact(viewModel.chatId)?.blockStatus?.let { it ->
                    it.contactIsBlocked = false
                    it.userIsBlocked = false
                }
                showOrHideBlockState(false)
            }
        }
    }

    private fun blockUser() {
        viewModel.blockUser().observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                KindnessApplication.instance.getContact(viewModel.chatId)?.blockStatus?.let { it ->
                    it.contactIsBlocked = true
                    it.userIsBlocked = true
                }
                showOrHideBlockState(true)
            }
        }
    }

    private fun showOrHideBlockState(show: Boolean) {
        if (show) {
            binding.blockUserImageView.visibility = View.GONE
            binding.unblockButton.visibility = View.VISIBLE
        } else {
            binding.blockUserImageView.visibility = View.VISIBLE
            binding.unblockButton.visibility = View.GONE
        }
    }

    private fun checkBlockState() {
        val contact = KindnessApplication.instance.getContact(viewModel.chatId)
        if (contact == null) {
            if (viewModel.isStartFromNotification) {

                if (viewModel.isContactListFetched) return

                viewModel.getConversationsList().observe(this) {
                    if (it.status == CustomResult.Status.SUCCESS) {
                        viewModel.isContactListFetched = true
                        checkBlockState()
                    }
                }
            } else {
                showOrHideBlockState(false)
            }

        } else {
            if (contact.blockStatus.contactIsBlocked or contact.blockStatus.userIsBlocked) {
                showOrHideBlockState(true)
            } else {
                showOrHideBlockState(false)
            }
        }
    }

    override fun onBackPressed() {
        if (viewModel.isStartFromNotification) {
            MainActivity.start(this)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        AppPref.isInChatPage = true
        viewModel.setSessionId()
    }

    override fun onStop() {
        super.onStop()
        AppPref.currentChatSessionId = -1
        AppPref.isInChatPage = false
    }

    override fun onDestroy() {
        super.onDestroy()
        AppPref.isInChatPage = false
        AppPref.currentChatSessionId = -1
        unregisterReceiver(chatBroadcastReceiver)
    }
}
