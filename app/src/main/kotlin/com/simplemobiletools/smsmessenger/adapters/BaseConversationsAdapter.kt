package com.simplemobiletools.smsmessenger.adapters

import android.graphics.Typeface
import android.os.Parcelable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.messenger.utils.PreferenceUtil
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller
import com.simplemobiletools.commons.adapters.MyRecyclerViewListAdapter
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.SimpleContactsHelper
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.commons.views.MyRecyclerView
import com.simplemobiletools.smsmessenger.activities.SimpleActivity
import com.simplemobiletools.smsmessenger.databinding.ItemConversationBinding
import com.simplemobiletools.smsmessenger.extensions.*
import com.simplemobiletools.smsmessenger.models.Conversation

@Suppress("LeakingThis")
abstract class BaseConversationsAdapter(
    activity: SimpleActivity, recyclerView: MyRecyclerView, onRefresh: () -> Unit, itemClick: (Any) -> Unit
) : MyRecyclerViewListAdapter<Conversation>(activity, recyclerView, ConversationDiffCallback(), itemClick, onRefresh),
    RecyclerViewFastScroller.OnPopupTextUpdate {
    private var fontSize = activity.getTextSize()
    private var drafts = HashMap<Long, String?>()

    private var recyclerViewState: Parcelable? = null

    init {
        setupDragListener(true)
        ensureBackgroundThread {
            fetchDrafts(drafts)
        }
        setHasStableIds(true)

        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() = restoreRecyclerViewState()
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) = restoreRecyclerViewState()
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = restoreRecyclerViewState()
        })
    }

    fun updateFontSize() {
        fontSize = activity.getTextSize()
        notifyDataSetChanged()
    }

    fun updateConversations(newConversations: ArrayList<Conversation>, commitCallback: (() -> Unit)? = null) {
        saveRecyclerViewState()
        submitList(newConversations.toList(), commitCallback)
    }

    fun updateDrafts() {
        ensureBackgroundThread {
            val newDrafts = HashMap<Long, String?>()
            fetchDrafts(newDrafts)
            if (drafts.hashCode() != newDrafts.hashCode()) {
                drafts = newDrafts
                activity.runOnUiThread {
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getSelectableItemCount() = itemCount

    protected fun getSelectedItems() = currentList.filter { selectedKeys.contains(it.hashCode()) } as ArrayList<Conversation>

    override fun getIsItemSelectable(position: Int) = true

    override fun getItemSelectionKey(position: Int) = currentList.getOrNull(position)?.hashCode()

    override fun getItemKeyPosition(key: Int) = currentList.indexOfFirst { it.hashCode() == key }

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConversationBinding.inflate(layoutInflater, parent, false)
        return createViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversation = getItem(position)
        holder.bindView(conversation, allowSingleClick = true, allowLongClick = true) { itemView, _ ->
            setupView(itemView, conversation)
        }
        bindViewHolder(holder)
    }

    override fun getItemId(position: Int) = getItem(position).threadId

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (!activity.isDestroyed && !activity.isFinishing) {
            val itemView = ItemConversationBinding.bind(holder.itemView)
            Glide.with(activity).clear(itemView.conversationImage)
        }
    }

    private fun fetchDrafts(drafts: HashMap<Long, String?>) {
        drafts.clear()
        for ((threadId, draft) in activity.getAllDrafts()) {
            drafts[threadId] = draft
        }
    }

    private fun setupView(view: View, conversation: Conversation) {
        var allMessProtect = getAllProtectedMess();
        ItemConversationBinding.bind(view).apply {
            root.setupViewBackground(activity)
            val smsDraft = drafts[conversation.threadId]
            draftIndicator.beVisibleIf(smsDraft != null)
            draftIndicator.setTextColor(properPrimaryColor)

            pinIndicator.beVisibleIf(activity.config.pinnedConversations.contains(conversation.threadId.toString()))
            pinIndicator.applyColorFilter(textColor)

            conversationFrame.isSelected = selectedKeys.contains(conversation.hashCode())

            var check = false;
            for (i in 0 until allMessProtect.size){
                if(allMessProtect[i] == conversation.threadId.toString()){
                    check = true
                    break
                }
            }

            conversationAddress.apply {
                if(check){
                    text = "*****"
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 1.2f)
                }else{
                    text = conversation.title
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 1.2f)
                }

            }

            conversationBodyShort.apply {
                if(check){
                    text = "*****"
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 0.9f)
                }else{
                    text = smsDraft ?: conversation.snippet
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 0.9f)
                }

            }

            conversationDate.apply {
                text = conversation.date.formatDateOrTime(context, true, false)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 0.8f)
            }

            val style = if (conversation.read) {
                conversationBodyShort.alpha = 0.7f
                if (conversation.isScheduled) Typeface.ITALIC else Typeface.NORMAL
            } else {
                conversationBodyShort.alpha = 1f
                if (conversation.isScheduled) Typeface.BOLD_ITALIC else Typeface.BOLD

            }
            conversationAddress.setTypeface(null, style)
            conversationBodyShort.setTypeface(null, style)

            arrayListOf(conversationAddress, conversationBodyShort, conversationDate).forEach {
                it.setTextColor(textColor)
            }

            // at group conversations we use an icon as the placeholder, not any letter
            val placeholder = if (conversation.isGroupConversation) {
                SimpleContactsHelper(activity).getColoredGroupIcon(conversation.title)
            } else {
                null
            }

            SimpleContactsHelper(activity).loadContactImage(conversation.photoUri, conversationImage, conversation.title, placeholder)
        }
    }

    override fun onChange(position: Int) = currentList.getOrNull(position)?.title ?: ""

    private fun saveRecyclerViewState() {
        recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
    }

    private fun restoreRecyclerViewState() {
        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private class ConversationDiffCallback : DiffUtil.ItemCallback<Conversation>() {
        override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return Conversation.areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return Conversation.areContentsTheSame(oldItem, newItem)
        }
    }

    public fun getAllProtectedMess(): ArrayList<String>{
        var messString = PreferenceUtil.getInstance(activity).getValue("protectedMess", "ok")
        if (messString == "ok"){
            return ArrayList<String>()
        }else{
            val gson = Gson()
            val type = object : TypeToken<ArrayList<String>>() {}.type
            return gson.fromJson(messString, type)
        }
    }

    public fun removeDuplicates(arrayList: ArrayList<String>): ArrayList<String> {
        return ArrayList(arrayList.toSet())
    }

    public fun addProtectedMess(mess:ArrayList<String>){
        var messString = PreferenceUtil.getInstance(activity).getValue("protectedMess", "ok")
        if (messString == "ok"){
            var list = ArrayList<String>()
            list.addAll(mess)
            PreferenceUtil.getInstance(activity).setValue("protectedMess", Gson().toJson(list))
        }else{
            val gson = Gson()
            val type = object : TypeToken<ArrayList<String>>() {}.type
            var list : ArrayList<String> =  gson.fromJson(messString, type)
            list.addAll(mess)
            val uniqueArrayList = removeDuplicates(list)
            PreferenceUtil.getInstance(activity).setValue("protectedMess", Gson().toJson(uniqueArrayList))

        }
    }
}
