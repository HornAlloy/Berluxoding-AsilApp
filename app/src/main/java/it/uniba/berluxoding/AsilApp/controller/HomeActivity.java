package it.uniba.berluxoding.AsilApp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import it.uniba.berluxoding.AsilApp.controller.informazioni.InformationActivity;
import it.uniba.berluxoding.AsilApp.controller.medbox.MedboxActivity;
import it.uniba.berluxoding.AsilApp.controller.profilo.ProfileActivity;
import it.uniba.berluxoding.AsilApp.R;

/**
 * Classe per la gestione dell'attività Home dell'applicazione. Fornisce l'interfaccia principale
 * con pulsanti per navigare verso altre attività.
 */
public class HomeActivity extends AppCompatActivity {

    /** Variabile per memorizzare l'ultimo tempo di pressione del tasto indietro. */
    private long backPressedTime;

    /** Toast per avvisare l'utente di premere di nuovo per uscire. */
    private Toast backToast;

    /**
     * Metodo chiamato quando l'attività viene creata. Configura la modalità Edge-to-Edge,
     * imposta il layout e gestisce gli eventi di clic dei pulsanti.
     *
     * @param savedInstanceState Il bundle che contiene lo stato salvato dell'attività, se presente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Abilita la modalità Edge-to-Edge per questa attività.
        EdgeToEdge.enable(this);

        // Imposta il layout principale dell'attività.
        setContentView(R.layout.activity_home);

        // Configura le finestre dell'interfaccia utente con i margini di sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configura i pulsanti di navigazione
        ImageButton ibt1 = findViewById(R.id.imgBtnProfilo);
        ImageButton ibt2 = findViewById(R.id.imgBtnInformativa);
        ImageButton ibt3 = findViewById(R.id.imgBtnMedBox);

        // Gestione evento clic sul pulsante Profilo
        ibt1.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the profile button");
            startActivityWithIntent(ProfileActivity.class);
        });

        // Gestione evento clic sul pulsante Informativa
        ibt2.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the information button");
            startActivityWithIntent(InformationActivity.class);
        });

        // Gestione evento clic sul pulsante MedBox
        ibt3.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the medbox button");
            startActivityWithIntent(MedboxActivity.class);
        });

        // Configura la gestione della pressione del tasto indietro
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel();
                    finish();
                } else {
                    backToast = Toast.makeText(getBaseContext(), "Premi di nuovo per uscire", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        });
    }

    /**
     * Avvia una nuova attività in base alla classe passata come parametro.
     *
     * @param targetActivity La classe dell'attività da avviare.
     */
    private void startActivityWithIntent(Class<?> targetActivity) {
        Intent intent = new Intent(HomeActivity.this, targetActivity);
        startActivity(intent);
    }

}
