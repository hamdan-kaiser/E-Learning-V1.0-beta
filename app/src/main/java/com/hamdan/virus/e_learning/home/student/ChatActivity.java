package com.hamdan.virus.e_learning.home.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.utils.AllStaticNames;

import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;

public class ChatActivity extends AppCompatActivity {
    private EditText editText;
    private RecyclerView recyclerView;
    private RelativeLayout sendButton;

    SharedPreferences prefs;
    String myEmail, teacherName, teacherEmail;

    DatabaseReference ref;
    FirebaseRecyclerAdapter<ChatMessage, ChatHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editText);
        sendButton = findViewById(R.id.addBtn);

        prefs = getSharedPreferences(AllStaticNames.sharedTable, MODE_PRIVATE);
        myEmail = prefs.getString("email", null);

        teacherName = getIntent().getStringExtra("name");
        teacherEmail = getIntent().getStringExtra("email");

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editText.getText().toString().trim();
                ChatMessage chatMessage = new ChatMessage(message, "student");
                ref.child("ChatView").child(getIdFromEmail(teacherEmail))
                        .child(getIdFromEmail(myEmail)).push().setValue(chatMessage);

                editText.setText("");

            }
        });

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("ChatView")
                .child(getIdFromEmail(teacherEmail))
                .child(getIdFromEmail(myEmail));

        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatHolder>(options) {

            @NonNull
            @Override
            public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_list, parent, false);

                return new ChatHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull ChatMessage model) {

                if (model.getMsgUser().equals("student")) {
                    holder.rightText.setText(model.getMsgText());

                    holder.rightText.setVisibility(View.VISIBLE);
                    holder.leftText.setVisibility(View.GONE);
                }
                else {
                    holder.leftText.setText(model.getMsgText());

                    holder.rightText.setVisibility(View.GONE);
                    holder.leftText.setVisibility(View.VISIBLE);
                }

            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int msgCount = adapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 || (positionStart >= (msgCount - 1) &&
                        lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);

                }

            }
        });

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}
