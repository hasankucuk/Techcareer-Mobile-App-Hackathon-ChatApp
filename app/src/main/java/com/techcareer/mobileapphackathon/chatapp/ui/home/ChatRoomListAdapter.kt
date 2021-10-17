package com.techcareer.mobileapphackathon.chatapp.ui.home

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.techcareer.mobileapphackathon.chatapp.databinding.ItemChatRoomBinding
import com.techcareer.mobileapphackathon.chatapp.repository.home.ChatModel

class ChatRoomListAdapter(private val clickListener: OnClickListener) : ListAdapter<ChatModel,
        RecyclerView.ViewHolder>(ChatModelViewDiffCallback) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as MsgViewHolder).bind(clickListener, product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgViewHolder {
        return MsgViewHolder(ItemChatRoomBinding.inflate(from(parent.context), parent, false))
    }

    class MsgViewHolder constructor(val binding: ItemChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnClickListener, item: ChatModel) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object ChatModelViewDiffCallback : DiffUtil.ItemCallback<ChatModel>() {
        override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem.chatId == newItem.chatId
        }

        override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem == newItem
        }
    }

     class OnClickListener(val clickListener: (productId: ChatModel) -> Unit) {
        fun onClick(v: View, product: ChatModel) = clickListener(product)
    }
}