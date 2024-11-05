package it.uniba.berluxoding.AsilApp.controller.profilo.dettagli;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.profilo.liste.ListaMisurazioniActivity;
import it.uniba.berluxoding.AsilApp.model.Misurazione;import android.content.Intent;
import android.view.View; // Importa questa libreria


/**
 * La classe {@code DettagliMisurazioneActivity} estende {@code AppCompatActivity} e rappresenta
 * l'attività che mostra i dettagli di una misurazione specifica.
 * Gli utenti possono visualizzare informazioni come strumento, valore, data e ora della misurazione,
 * oltre a condividere questi dettagli tramite Gmail.
 */
public class DettagliMisurazioneActivity extends AppCompatActivity {

    private TextView strumentoV, valoreV, dataV, oraV;
    private DatabaseReference userRef;
    private String misurazioneId;
    final private String TAG = "DETTAGLI_MISURAZIONE_ACTIVITY";

    /**
     * Questo metodo viene chiamato quando l'attività viene creata.
     * Qui vengono inizializzati i componenti dell'interfaccia utente e viene
     * recuperato l'ID della misurazione passato tramite l'intent.
     *
     * @param savedInstanceState Lo stato salvato dell'attività, se presente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dettagli_misurazione);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inizializzazione Firebase Database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("AsilApp").child(getUid());

        // Collegamento delle TextView dell'interfaccia
        strumentoV = findViewById(R.id.strumento);
        valoreV = findViewById(R.id.valore);
        dataV = findViewById(R.id.dataMisurazione);
        oraV = findViewById(R.id.oraMisurazione);

        // Recupera il misurazioneId passato tramite l'intent
        misurazioneId = getIntent().getStringExtra("misurazioneId");
        backButton();

        // Popola i dettagli della misurazione
        getMisurazione();

    }

    /**
     * Recupera i dettagli della misurazione dal database Firebase
     * e aggiorna l'interfaccia utente con queste informazioni.
     */
    private void getMisurazione() {
        DatabaseReference misurazioneRef = userRef.child("misurazioni").child(misurazioneId);

        misurazioneRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Misurazione misurazione = dataSnapshot.getValue(Misurazione.class);
                    if (misurazione != null) {
                        strumentoV.setText(misurazione.getStrumento());
                        valoreV.setText(misurazione.getValore());
                        dataV.setText(misurazione.getData());
                        oraV.setText(misurazione.getOrario());
                    }
                } else {
                    Log.e(TAG, "Misurazione non trovata!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Errore nel caricamento della misurazione", databaseError.toException());
            }
        });
    }

    /**
     * Restituisce l'ID dell'utente attualmente autenticato.
     *
     * @return L'ID dell'utente corrente.
     */
    private String getUid() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    /**
     * Condivide i dettagli della misurazione tramite Gmail.
     * Questo metodo raccoglie i dettagli visualizzati e crea un'Intent
     * per avviare un'app di posta per la condivisione.
     *
     * @param view La vista che ha attivato il metodo.
     */
    public void condividiMisurazione(View view) {
        // Recupera i dettagli della misurazione
        String strumento = strumentoV.getText().toString();
        String valore = valoreV.getText().toString();
        String data = dataV.getText().toString();
        String ora = oraV.getText().toString();

        // Crea il contenuto da condividere
        String contenuto = "Dettagli Misurazione:\n" +
                "Strumento: " + strumento + "\n" +
                "Valore: " + valore + "\n" +
                "Data: " + data + "\n" +
                "Ora: " + ora;

        // Crea l'Intent per condividere tramite Gmail
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");  // Tipo di contenuto da condividere
        intent.putExtra(Intent.EXTRA_SUBJECT, "Dettagli Misurazione");
        intent.putExtra(Intent.EXTRA_TEXT, contenuto);

        // Verifica che ci sia un'app di Gmail installata
        intent.setPackage("com.google.android.gm");

        try {
            startActivity(Intent.createChooser(intent, "Condividi i dettagli della misurazione con:"));
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e(TAG, "Gmail non è installato", ex);
        }
    }

    /**
     * Configura la gestione della pressione del tasto indietro.
     * Ritorna alla lista delle misurazioni, in modo da permettere all'utente di poter confrontare la misurazione con le altre.
     * Termina l'esecuzione dell'activity corrente.
     */
    private void backButton() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed () {
                Intent intent = new Intent(DettagliMisurazioneActivity.this, ListaMisurazioniActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

