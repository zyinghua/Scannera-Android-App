package com.example.food_product_comparison_android_app.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_product_comparison_android_app.GeneralJavaClasses.BotChatMessage;
import com.example.food_product_comparison_android_app.R;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private List<BotChatMessage> messageList;
    private Activity activity;

    public ChatRecyclerViewAdapter(List<BotChatMessage> messageList, Activity activity) {
        this.messageList = messageList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(activity).inflate(R.layout.chat_message, parent, false);
        //return new ViewHolder(view);
        return null;
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String message = messageList.get(position).getMessage();
        boolean isReceived = messageList.get(position).isFromBot();
        if(isReceived){
            holder.messageReceive.setVisibility(View.VISIBLE);
            holder.messageSend.setVisibility(View.GONE);
            holder.messageReceive.setText(message);
        }else {
            holder.messageSend.setVisibility(View.VISIBLE);
            holder.messageReceive.setVisibility(View.GONE);
            holder.messageSend.setText(message);
        }
    }

    @Override public int getItemCount() {
        return messageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView messageSend;
        TextView messageReceive;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //messageSend = itemView.findViewById(R.id.message_send);
            //messageReceive = itemView.findViewById(R.id.message_receive);
        }
    }
}
