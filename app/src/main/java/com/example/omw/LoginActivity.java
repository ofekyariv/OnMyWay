package com.example.omw;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;


public class LoginActivity extends AppCompatActivity implements LocationListener {
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    NotificationHelper notificationHelper;
    private Button button;
    private ImageButton emergency;
    private EditText editText;
    private LocationManager locationManager;
    public static LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        emergency = findViewById(R.id.emergency);
        notificationHelper = new NotificationHelper(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        }
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    notificationHelper.sendSMS("Child in danger!, you can see him here: https://maps.google.com/?q=" + latLng.latitude + "," + latLng.longitude);
                    notificationHelper.sendHighPriorityNotification("ENTER", "", MapsActivity.class);
                }
                catch(Exception ignored){};

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //יש אישור
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                }
            }
        }
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }
}