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
import it.uniba.berluxoding.AsilApp.model.Patologia;

/**
 * La classe {@code DettagliPatologiaActivity} estende {@code AppCompatActivity} e rappresenta
 * l'attività che mostra i dettagli di una patologia specifica.
 * Gli utenti possono visualizzare informazioni come il nome della patologia,
 * la data della diagnosi e il nome del medico che ha effettuato la diagnosi.
 */
public class DettagliPatologiaActivity extends AppCompatActivity {

    private TextView nomeV, dataV, diagnostaV;
    final private String TAG = "DETTAGLI_PATOLOGIA_ACTIVITY";

    /**
     * Questo metodo viene chiamato quando l'attività viene creata.
     * Qui vengono inizializzati i componenti dell'interfaccia utente e viene
     * recuperato l'ID della patologia passato tramite l'intent.
     *
     * @param savedInstanceState Lo stato salvato dell'attività, se presente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dettagli_patologia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        nomeV = findViewById(R.id.nomePatologia);
        dataV = findViewById(R.id.dataDiagnosi);
        diagnostaV = findViewById(R.id.diagnosta);
        String patologiaId = getIntent().getStringExtra("patologiaId");
        Log.d(TAG, "patologiaId = " + patologiaId);

        DatabaseReference dataRef = mDatabase.child("AsilApp").child(getUid()).child("patologie")
                .child(Objects.requireNonNull(patologiaId));

        // Recupera i dettagli della patologia
        getPatologia(dataRef);
    }

    /**
     * Recupera i dettagli della patologia dal database Firebase
     * e aggiorna l'interfaccia utente con queste informazioni.
     *
     * @param dataRef Riferimento al nodo del database contenente i dettagli della patologia.
     */
    private void getPatologia(DatabaseReference dataRef) {
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifica se la patologia esiste
                if (dataSnapshot.exists()) {
                    Patologia patologia = dataSnapshot.getValue(Patologia.class);

                    // Popola le TextView con i dettagli della patologia
                    if (patologia != null) {
                        nomeV.setText(patologia.getNome());
                        dataV.setText(patologia.getDataDiagnosi());
                        diagnostaV.setText(patologia.getDiagnosta());
                    }
                } else {
                    Log.e(TAG, "Patologia non trovata!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Errore nel caricamento della patologia", databaseError.toException());
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
