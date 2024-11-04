package it.uniba.berluxoding.AsilApp.controller.informazioni;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.HomeActivity;

/**
 * Activity che mostra informazioni e fornisce accesso a diverse altre attività tramite pulsanti.
 * Questa classe gestisce l'interfaccia utente associata e le interazioni con i pulsanti.
 */
public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Abilita EdgeToEdge per un'esperienza utente a schermo intero
        EdgeToEdge.enable(this);
        // Imposta il layout associato a questa activity
        setContentView(R.layout.activity_information);

        // Gestisce gli insets delle finestre per supportare schermi a tutto schermo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Associa i pulsanti dell'interfaccia utente a variabili
        ImageButton imgBtnAltro = findViewById(R.id.imgBtnAltro);
        ImageButton imgBtnCentro = findViewById(R.id.imgBtnCentro);
        ImageButton imgBtnUtente = findViewById(R.id.imgBtnUtente);
        ImageButton imgBtnLuogo = findViewById(R.id.imgBtnLuogo);

        // Imposta un listener per il pulsante imgBtnAltro
        imgBtnAltro.setOnClickListener(v -> startActivityWithIntent(OtherActivity.class));

        // Imposta un listener per il pulsante imgBtnCentro
        imgBtnCentro.setOnClickListener(v -> startActivityWithIntent(CenterActivity.class));

        // Imposta un listener per il pulsante imgBtnUtente (da definire in seguito)
        imgBtnUtente.setOnClickListener(v -> startActivityWithIntent(UserActivity.class));

        // Imposta un listener per il pulsante imgBtnLuogo
        imgBtnLuogo.setOnClickListener(v -> startActivityWithIntent(PlaceActivity.class));
    }

    /**
     * Avvia una nuova attività in base alla classe passata come parametro.
     *
     * @param targetActivity La classe dell'attività da avviare.
     */
    private void startActivityWithIntent(Class<?> targetActivity) {
        Intent intent = new Intent(InformationActivity.this, targetActivity);
        startActivity(intent);
    }
}

