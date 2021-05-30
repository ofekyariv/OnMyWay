package com.example.omw;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DataActivity extends AppCompatActivity {
    final int SEND_SMS_REQUEST_CODE = 1;
    private EditText editTextPhone;
    private Button setButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        editTextPhone = findViewById(R.id.editTextPhone);
        setButton = findViewById(R.id.phoneButton);
        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        String Phone = sharedPreferences.getString("phoneNum", "Phone Number");
        if(Phone.equals("Phone Number")){
            editTextPhone.setHint(Phone);
        }
        else{
            editTextPhone.setText(Phone);
        }
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.SEND_SMS)) {
                    setPhoneNum();
                } else {
                    ActivityCompat.requestPermissions(DataActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            SEND_SMS_REQUEST_CODE);
                }
            }
        });
    }

    private void setPhoneNum() {
        if(editTextPhone.getText().toString().length()!=10){
            Toast.makeText(this, "Please Enter A Valid Phone Number", Toast.LENGTH_SHORT).show();
        }
        else{
            SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("phoneNum", editTextPhone.getText().toString());
            editor.apply();
            Intent intent = new Intent(this, ExplanationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}