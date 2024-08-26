package it.uniba.berluxoding.AsilApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.fragment.NavHostFragment;

public class HomeActivity extends AppCompatActivity {
    private ImageButton ibt1, ibt2, ibt3;

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


        ibt1 = findViewById(R.id.button);
        ibt2 = findViewById(R.id.button2);
        ibt3 = findViewById(R.id.button3);
        ibt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the profile button");
                Intent openPage = new Intent(HomeActivity.this,ProfileActivity.class);
                // passo all'attivazione dell'activity page1.java
                startActivity(openPage);

            }
        });
        ibt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the information button");
                Intent openPage = new Intent(HomeActivity.this,InformationActivity.class);
                // passo all'attivazione dell'activity page1.java
                startActivity(openPage);
            }
        });
        ibt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the medbox button");
                Intent openPage = new Intent(HomeActivity.this,MedboxActivity.class);
                // passo all'attivazione dell'activity page1.java
                startActivity(openPage);
            }
        });


    }
}