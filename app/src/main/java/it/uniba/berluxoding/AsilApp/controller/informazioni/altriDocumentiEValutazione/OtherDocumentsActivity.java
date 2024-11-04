package it.uniba.berluxoding.AsilApp.controller.informazioni.altriDocumentiEValutazione;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import it.uniba.berluxoding.AsilApp.R;

/**
 * Activity che gestisce la visualizzazione di documenti e riferimenti utili.
 */
public class OtherDocumentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Abilita EdgeToEdge per un'esperienza utente a schermo intero
        EdgeToEdge.enable(this);
        // Imposta il layout associato a questa activity
        setContentView(R.layout.activity_altri_documenti);

        // Gestisce gli insets delle finestre per supportare schermi a tutto schermo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}

