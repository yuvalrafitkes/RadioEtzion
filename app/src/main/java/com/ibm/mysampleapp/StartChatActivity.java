package com.ibm.mysampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.Backendless;

public class StartChatActivity extends AppCompatActivity {

  private EditText userNameEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Backendless.setUrl(Defaults.SERVER_URL);
    Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.API_KEY);

    setContentView(R.layout.activity_start_chat);

    userNameEditText = findViewById(R.id.userName);

    Button startChatButton = findViewById(R.id.start_chat_button);
    startChatButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startChat();
      }
    });
  }

  private void startChat() {
    Intent intent = new Intent(StartChatActivity.this, ChatRoomActivity.class);
    String name = userNameEditText.getText().toString();
    intent.putExtra("name", name);
    startActivity(intent);

  }

}