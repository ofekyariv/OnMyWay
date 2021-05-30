package com.example.omw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });
    }

    public void checkPassword() {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String password = preferences.getString("password", "");
        if (editText.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            //first entry
            if (password.equals("")) {
                editor.putString("password", editText.getText().toString());
                editor.apply();
                Toast.makeText(this, "PASSWORD SET", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DataActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (editText.getText().toString().equals(password)) {
                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Wrong password! Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}