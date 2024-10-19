package it.uniba.berluxoding.AsilApp.controller.informazioni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import it.uniba.berluxoding.AsilApp.R;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Abilita EdgeToEdge
        EdgeToEdge.enable(this);
        // Imposta il layout associato a questa activity
        setContentView(R.layout.activity_information);

        // Gestisci la finestra insets per schermi a tutto schermo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Associa ImageButton a variabili
        ImageButton imgBtnAltro = findViewById(R.id.imgBtnAltro);
        ImageButton imgBtnCentro = findViewById(R.id.imgBtnCentro);
        ImageButton imgBtnUtente = findViewById(R.id.imgBtnUtente);
        ImageButton imgBtnLuogo = findViewById(R.id.imgBtnLuogo);

        // Set OnClickListener per imgBtnAltro
        imgBtnAltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Avvia "AltroActivity" (activity_other.xml)
                Intent intent = new Intent(InformationActivity.this, OtherActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener per imgBtnCentro
        imgBtnCentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Avvia "CentroActivity" (activity_center.xml)
                Intent intent = new Intent(InformationActivity.this, CenterActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener per imgBtnUtente (da definire in seguito)
        imgBtnUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Azione da definire in seguito
                // Placeholder: avvia una nuova Activity temporanea
                // Intent intent = new Intent(InformationActivity.this, PlaceholderUtenteActivity.class);
                // startActivity(intent);
            }
        });

        // Set OnClickListener per imgBtnLuogo
        imgBtnLuogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Avvia "LuogoActivity" (activity_place.xml)
                Intent intent = new Intent(InformationActivity.this, PlaceActivity.class);
                startActivity(intent);
            }
        });
    }
}
