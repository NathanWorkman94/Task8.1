package com.example.llama2chatbot;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    RecyclerView rvMessages;
    EditText etMessage;
    Button btnSend;
    ArrayList<Message> messageList;
    MessageAdapter messageAdapter;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get username from main activity
        username = getIntent().getStringExtra("username");

        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        // Create message list to store messages
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, username);

        String greetingMessage = "Hello " + username + "! How can I help you today?";

        messageList.add(new Message("llama", greetingMessage));

        // Handle message sending
        btnSend.setOnClickListener(v -> {
            String userInput = etMessage.getText().toString();

            if (!userInput.isEmpty()) {

                // Add user message to message list
                messageList.add(new Message(username, userInput));

                // Refresh the data to display the new message
                messageAdapter.notifyDataSetChanged();

                // Clear input
                etMessage.setText("");

                // Handle backend
                sendToLlama(userInput);

            } else {
                // Handle empty input
                Toast.makeText(ChatActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });

        // Arrange messages linearly
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // Start the list from the bottom
        layoutManager.setStackFromEnd(true);

        // Apply layout and adapter to messages
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(messageAdapter);

        messageAdapter.notifyDataSetChanged();
    }

    // Handle message sending to llama backend
    private void sendToLlama(String userMessage)
    {
        // URL where message will be sent
        String url = "http://10.0.2.2:5000/chat";

        // Create HTTP POST request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    String llamaReply = response.trim();

                    // Add llamas response to list of messages
                    messageList.add(new Message("llama", llamaReply));
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                },
                error -> {
                    messageList.add(new Message("llama", "Unable to get response from llama :("));
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                }
        ) { // Send Users message to llama
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userMessage", userMessage);
                return params;
            }
        };

        // Handle time outs
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(this).add(request);
    }
}