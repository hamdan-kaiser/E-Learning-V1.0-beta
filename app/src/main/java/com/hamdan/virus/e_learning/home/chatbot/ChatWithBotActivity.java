package com.hamdan.virus.e_learning.home.chatbot;

import android.app.Service;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.home.student.ChatHolder;
import com.hamdan.virus.e_learning.home.student.ChatMessage;

import java.util.ArrayList;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

                                /*Ceating A Chatbot*/
                    /*This portion is ongoing, under developement*/

public class ChatWithBotActivity extends AppCompatActivity implements AIListener {

    RecyclerView recyclerView;
    EditText messageText;
    RelativeLayout sendButton;

    private static String TAG = "MainActivity";

    static final AIConfiguration config = new AIConfiguration("eb5cd7ddd8454c3a9cb133d34e6d4d16",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System); //implementation of dialogflow
    static AIDataService aiDataService = null;
    static final AIRequest aiRequest = new AIRequest();

    private AIService aiService;

    public static ArrayList<ChatMessage> chatMessages = new ArrayList<>();
    RecyclerView.Adapter<ChatHolder> recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_bot);

        recyclerView = findViewById(R.id.recyclerViewBot);
        messageText = findViewById(R.id.messageText);
        sendButton = findViewById(R.id.sendButton);

        chatMessages.clear();

        aiDataService = new AIDataService(ChatWithBotActivity.this, config);

        // Setup Recycler view
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternet();
            }
        });

        recyclerViewAdapter = new RecyclerView.Adapter<ChatHolder>() {
            boolean testing = false;
            @NonNull
            @Override
            public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_list, parent, false);

                return new ChatHolder(view);
            }


            @Override
            public void onBindViewHolder(@NonNull ChatHolder holder, int position) {

                if (position < chatMessages.size() - 1) {
                    Log.e(TAG, "---------------------------------------------------------");
                    holder.rightText.setText(chatMessages.get(position).getMsgText());
                    holder.rightText.setVisibility(View.VISIBLE);
                    holder.leftText.setVisibility(View.VISIBLE);
                }
                else {
                    Log.e(TAG, "--------------------------2-------------------------------");
                    holder.leftText.setText(chatMessages.get(position).getMsgText());
                    holder.rightText.setVisibility(View.VISIBLE);
                    holder.leftText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public int getItemCount() {
                return chatMessages.size();
            }
        };

        recyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int msgCount = recyclerViewAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 || (positionStart >= (msgCount - 1) &&
                        lastVisiblePosition == (positionStart -1))) {
                    recyclerView.scrollToPosition(positionStart);

                }

            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Service.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {

                String message = messageText.getText().toString().trim();

                if (!message.equals("")) {

                    aiRequest.setQuery(message);
                    new SendMessage().execute(aiRequest);
                }
                else {
                    aiService.startListening();
                }

                messageText.setText("");

            } else {
                Toast.makeText(ChatWithBotActivity.this,"Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(ChatWithBotActivity.this,"Please enable internet to chat", Toast.LENGTH_LONG).show();
        }
    }

    private static class SendMessage extends AsyncTask<AIRequest, Void, AIResponse> {

        final ChatMessage model = new ChatMessage();

        @Override
        protected AIResponse doInBackground(AIRequest... aiRequests) {

            try {
                return aiDataService.request(aiRequest);
            } catch (AIServiceException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AIResponse response) {
            super.onPostExecute(response);
            Log.e(TAG, "Response Not NULL");

            Result result = response.getResult();

            model.setMsgText(result.getResolvedQuery());
            model.setMsgUser("user");
            chatMessages.add(model);
            Log.e(TAG, "--- " + model.getMsgText());

            model.setMsgText(result.getFulfillment().getSpeech());
            model.setMsgUser("bot");
            chatMessages.add(model);

            String message = result.getResolvedQuery();
            Log.e(TAG, "User = " + message);

            String reply = result.getFulfillment().getSpeech();
            Log.e(TAG, "Bot = " + reply);
        }
    }

    @Override
    public void onResult(AIResponse result) {

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
