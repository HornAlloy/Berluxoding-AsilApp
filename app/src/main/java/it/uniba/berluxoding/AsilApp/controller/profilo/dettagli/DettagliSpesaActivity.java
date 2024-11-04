package it.uniba.berluxoding.AsilApp.controller.profilo.dettagli;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
import it.uniba.berluxoding.AsilApp.model.Spesa;

/**
 * La classe {@code DettagliSpesaActivity} estende {@code AppCompatActivity} e rappresenta
 * l'attività che mostra i dettagli di una spesa specifica.
 * Gli utenti possono visualizzare informazioni come l'ambito,
 * l'articolo, il costo e la data e l'orario della spesa.
 */
public class DettagliSpesaActivity extends AppCompatActivity {

    private TextView txtAmbito, txArticolo, txtCosto, txtGiorno, txtMese, txtAnno, txtOra, txtMinuto;
    final private String TAG = "DETTAGLI_SPESA_ACTIVITY";

    /**
     * Questo metodo viene chiamato quando l'attività viene creata.
     * Qui vengono inizializzati i componenti dell'interfaccia utente e viene
     * recuperato l'ID della spesa passato tramite l'intent.
     *
     * @param savedInstanceState Lo stato salvato dell'attività, se presente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dettagli_spesa);

        // Setup per la gestione degli insets di sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Collegamento delle TextView dell'interfaccia
        txtAmbito = findViewById(R.id.txtAmbito);
        txArticolo = findViewById(R.id.txArticolo);
        txtCosto = findViewById(R.id.txtCosto);
        txtGiorno = findViewById(R.id.txtGiorno);
        txtMese = findViewById(R.id.txtMese);
        txtAnno = findViewById(R.id.txtAnno);
        txtOra = findViewById(R.id.txtOra);
        txtMinuto = findViewById(R.id.txtMinuto);

        // Recupera lo spesaId passato tramite l'intent
        String spesaId = getIntent().getStringExtra("spesaId");

        // Inizializzazione Firebase Database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataRef = mDatabase.child("AsilApp").child(getUid()).child("spese").child(Objects.requireNonNull(spesaId));

        // Popola i dettagli della spesa
        loadSpesaDetails(dataRef);
    }

    /**
     * Carica i dettagli della spesa dal database Firebase
     * e aggiorna l'interfaccia utente con queste informazioni.
     *
     * @param dataRef Riferimento al nodo del database contenente i dettagli della spesa.
     */
    private void loadSpesaDetails(DatabaseReference dataRef) {
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifica se la spesa esiste
                if (dataSnapshot.exists()) {
                    Spesa spesa = dataSnapshot.getValue(Spesa.class);

                    // Popola le TextView con i dettagli della spesa
                    if (spesa != null) {
                        txtAmbito.setText(spesa.getAmbito());
                        txArticolo.setText(spesa.getArticolo());
                        txtCosto.setText(spesa.getCosto());

                        // Dividi la data nel formato giorno/mese/anno
                        String[] dataParts = spesa.getData().split("/");
                        txtGiorno.setText(dataParts[0]);
                        txtMese.setText(dataParts[1]);
                        txtAnno.setText(dataParts[2]);

                        // Dividi l'orario nel formato ora/minuto
                        String[] orarioParts = spesa.getOrario().split(":");
                        txtOra.setText(orarioParts[0]);
                        txtMinuto.setText(orarioParts[1]);
                    }
                } else {
                    Log.e(TAG, "Spesa non trovata!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Errore nel caricamento della spesa", databaseError.toException());
            }
        });
    }

    /**
     * Restituisce l'UID dell'utente attualmente autenticato.
     *
     * @return L'UID dell'utente corrente.
     */
    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }
}
