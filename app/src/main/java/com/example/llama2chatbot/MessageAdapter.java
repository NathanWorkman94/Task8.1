package com.example.llama2chatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Define the two types of users that will require different views
    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_LLAMA = 1;

    private List<Message> messageList;
    private String currentUser;

    // Constructor
    public MessageAdapter(List<Message> messageList, String currentUser){
        this.messageList = messageList;
        this.currentUser = currentUser;
    }

    // Check if the message is from the current user or llama
    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        if (message.getSender().equals(currentUser)) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_LLAMA;
        }
    }

    // Method to create new view holder depending on the message sender
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the view using the correct layout based on if user or llama message
        if (viewType == VIEW_TYPE_USER) {
            View view = inflater.inflate(R.layout.item_message_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_message_llama, parent, false);
            return new LlamaMessageViewHolder(view);
        }
    }

    // Viewholder for user messages
    public static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvUserAvatar;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessageContentUser);
            tvUserAvatar = itemView.findViewById(R.id.tvUserAvatar);

        }
    }

    // Viewholder for llama messages
    public static class LlamaMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        public LlamaMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessageContentLlama);

        }
    }

    // Bind the message to the appropriate viewholder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Position specifies which message to show
        Message message = messageList.get(position);

        // Bind message to viewholder based on type
        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).tvMessage.setText(message.getContent());
            ((UserMessageViewHolder) holder).tvUserAvatar.setText(
                    String.valueOf(currentUser.toUpperCase().charAt(0))
            );
        } else if (holder instanceof LlamaMessageViewHolder) {
            ((LlamaMessageViewHolder) holder).tvMessage.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
