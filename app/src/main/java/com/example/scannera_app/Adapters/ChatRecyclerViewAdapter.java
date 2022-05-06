package com.example.scannera_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scannera_app.GeneralJavaClasses.BotChatMessage;
import com.example.scannera_app.R;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {
    private static final int USER_MESSAGE = 0;
    private static final int BOT_MESSAGE = 1;
    private List<BotChatMessage> messages;

    public ChatRecyclerViewAdapter(List<BotChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isFromBot() ? BOT_MESSAGE : USER_MESSAGE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == USER_MESSAGE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_msg_send, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_msg_receive, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.message_tv.setText(messages.get(position).getMessage());
    }

    @Override public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView message_tv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_tv = itemView.findViewById(R.id.message_tv);
        }
    }
}
