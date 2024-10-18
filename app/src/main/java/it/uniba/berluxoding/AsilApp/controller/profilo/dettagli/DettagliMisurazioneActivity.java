package it.uniba.berluxoding.AsilApp.controller.profilo.dettagli;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.model.Misurazione;

public class DettagliMisurazioneActivity extends AppCompatActivity {

    private TextView strumentoV, valoreV, dataV, oraV;
    private Button btnIndietro;
    private DatabaseReference mDatabase, userRef;
    private String misurazioneId;
    final private String TAG = "DETTAGLI_MISURAZIONE_ACTIVITY";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dettagli_misurazione);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inizializzazione Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("AsilApp").child(getUid());
        // Collegamento delle TextView dell'interfaccia
        strumentoV = findViewById(R.id.strumento);
        valoreV = findViewById(R.id.valore);
        dataV = findViewById(R.id.dataMisurazione);
        oraV = findViewById(R.id.oraMisurazione);
        //btnIndietro = findViewById(R.id.indietro);
        // Recupera il misurazioneId passato tramite l'intent
        misurazioneId = getIntent().getStringExtra("misurazioneId");
        // Popola i dettagli della misurazione
        getMisurazione();
        // Azione sul bottone Indietro
        /*
        btnIndietro.setOnClickListener(v -> {
            Log.d(TAG, "Back button pressed");
            // Torna alla lista misurazioni
            Intent intent = new Intent(DettagliMisurazioneActivity.this, ListaMisurazioniActivity.class);
            startActivity(intent);
        });
         */

    }

    private void getMisurazione() {
        DatabaseReference misurazioneRef = userRef.child("misurazioni").child(misurazioneId);

        misurazioneRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Verifica se la patologia esiste
                if (dataSnapshot.exists()) {
                    Misurazione misurazione = dataSnapshot.getValue(Misurazione.class);

                    // Popola le TextView con i dettagli della spesa
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
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Errore nel caricamento della misurazione", databaseError.toException());
            }
        });

    }

    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }
}