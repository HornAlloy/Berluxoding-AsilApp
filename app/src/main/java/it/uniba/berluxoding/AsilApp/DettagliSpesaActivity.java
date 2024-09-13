package it.uniba.berluxoding.AsilApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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

import it.uniba.berluxoding.AsilApp.model.Spesa;

public class DettagliSpesaActivity extends AppCompatActivity {

    private TextView txtAmbito, txArticolo, txtCosto, txtGiorno, txtMese, txtAnno, txtOra, txtMinuto;
    private Button btnIndietro;
    private DatabaseReference mDatabase;
    private String spesaId;

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
        btnIndietro = findViewById(R.id.btnIndietro);
        // Inizializzazione Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Recupera lo spesaId passato tramite l'intent
        spesaId = getIntent().getStringExtra("spesaId");
        // Popola i dettagli della spesa
        loadSpesaDetails();
        // Azione sul bottone Indietro
        btnIndietro.setOnClickListener(v -> {
            // Torna alla lista spese
            Intent intent = new Intent(DettagliSpesaActivity.this, ListaSpeseActivity.class);
            startActivity(intent);
        });
    }
    // Metodo per caricare i dettagli della spesa da Firebase
    private void loadSpesaDetails() {
        // Ottieni l'user ID dell'utente corrente
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Referenzia il nodo della spesa specifica
        DatabaseReference spesaRef = mDatabase.child("spese").child(userId).child(spesaId);

        spesaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    Log.e("DETTAGLI_SPESA", "Spesa non trovata!");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DETTAGLI_SPESA", "Errore nel caricamento della spesa", databaseError.toException());
            }
        });
    }
}
