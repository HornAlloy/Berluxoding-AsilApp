package it.uniba.berluxoding.AsilApp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import it.uniba.berluxoding.AsilApp.controller.informazioni.InformationActivity;
import it.uniba.berluxoding.AsilApp.controller.medbox.MedboxActivity;
import it.uniba.berluxoding.AsilApp.controller.profilo.ProfileActivity;
import it.uniba.berluxoding.AsilApp.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ImageButton ibt1 = findViewById(R.id.imgBtnProfilo);
        ImageButton ibt2 = findViewById(R.id.imgBtnInformativa);
        ImageButton ibt3 = findViewById(R.id.imgBtnMedBox);

        ibt1.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the profile button");
            Intent openPage = new Intent(HomeActivity.this, ProfileActivity.class);
            // passo all'attivazione dell'activity page1.java
            startActivity(openPage);

        });
        ibt2.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the information button");
            Intent openPage = new Intent(HomeActivity.this, InformationActivity.class);
            // passo all'attivazione dell'activity page1.java
            startActivity(openPage);
        });
        ibt3.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the medbox button");
            Intent openPage = new Intent(HomeActivity.this, MedboxActivity.class);
            // passo all'attivazione dell'activity page1.java
            startActivity(openPage);
        });


    }
}