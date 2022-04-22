package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.food_product_comparison_android_app.Adapters.ChatRecyclerViewAdapter;
import com.example.food_product_comparison_android_app.GeneralJavaClasses.BotChatMessage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatBotActivity extends AppCompatActivity {
    private EditText message_et;
    private FloatingActionButton send_msg_btn;
    private List<BotChatMessage> messages;
    private RecyclerView chatRecyclerView;
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private ExecutorService executor;
    private Handler uiHandler;

    //DialogFlow
    private SessionsClient sessionsClient;
    private SessionName sessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        this.findViews();
        this.setUpToolbar();
        this.setUpContent();
    }

    private void findViews()
    {
        this.message_et = findViewById(R.id.message_et);
        this.send_msg_btn = findViewById(R.id.send_msg_btn);
        this.chatRecyclerView = findViewById(R.id.chatRecyclerView);
    }

    private void setUpContent()
    {
        this.messages = new ArrayList<>();
        this.executor = Executors.newSingleThreadExecutor();
        this.uiHandler = new Handler(Looper.getMainLooper());

        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(this.messages);
        chatRecyclerView.setAdapter(chatRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);

        this.send_msg_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String message = message_et.getText().toString();

                if(!message.isEmpty())
                {
                    messages.add(new BotChatMessage(message, false));
                    message_et.setText("");
                    sendMessageToBot(message);
                    chatRecyclerViewAdapter.notifyDataSetChanged();
                    Objects.requireNonNull(chatRecyclerView.getLayoutManager()).scrollToPosition(messages.size() - 1);
                }
                else
                {
                    Animation shake = AnimationUtils.loadAnimation(ChatBotActivity.this, R.anim.shake);
                    message_et.startAnimation(shake);
                    Toast.makeText(ChatBotActivity.this, getString(R.string.message_empty_error), Toast.LENGTH_LONG).show();
                }
            }
        });

        setUpBot();
    }

    private void setUpBot()
    {
        try {
            InputStream stream = this.getResources().openRawResource(R.raw.dialogflow_credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, UUID.randomUUID().toString());
        } catch (IOException e)
        {
            Toast.makeText(ChatBotActivity.this, getString(R.string.bot_setup_io_exception_error), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void sendMessageToBot(String message) {
        QueryInput queryInput = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-AU")).build();

        executor.execute(() -> {
            DetectIntentRequest detectIntentRequest =
                    DetectIntentRequest.newBuilder()
                            .setSession(sessionName.toString())
                            .setQueryInput(queryInput)
                            .build();

            uiHandler.post(() -> {
                DetectIntentResponse detectIntentResponse = sessionsClient.detectIntent(detectIntentRequest);

                if(detectIntentResponse != null)
                {
                    QueryResult queryResult = detectIntentResponse.getQueryResult();

                    if(queryResult.getFulfillmentMessagesCount() != 0)
                    {
                        for(int i = 0; i < queryResult.getFulfillmentMessagesCount(); i++)
                        {
                            String botReply = queryResult.getFulfillmentMessages(i).getText().getText(0);
                            if(!botReply.isEmpty())
                            {
                                messages.add(new BotChatMessage(botReply, true));
                                chatRecyclerViewAdapter.notifyItemInserted(messages.size() - 1);
                                Objects.requireNonNull(chatRecyclerView.getLayoutManager()).scrollToPosition(messages.size() - 1);
                            }
                            else
                            {
                                Toast.makeText(ChatBotActivity.this, getString(R.string.bot_reply_error), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(ChatBotActivity.this, getString(R.string.bot_reply_error), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(ChatBotActivity.this, getString(R.string.bot_reply_error), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.chat_bot_activity_title));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            // This is to sync the toolbar up button with the back button
            onBackPressed();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}