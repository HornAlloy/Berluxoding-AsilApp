package it.uniba.berluxoding.AsilApp.controller.profilo.patologie;

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
import it.uniba.berluxoding.AsilApp.model.Patologia;

public class DettagliPatologiaActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, userRef;
    private TextView nomeV, dataV, diagnostaV;
    private String patologiaId;
    private Button btnIndietro;
    final private String TAG = "DETTAGLI_PATOLOGIA_ACTIVITY";


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dettagli_patologia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("AsilApp").child(getUid());

        nomeV = findViewById(R.id.nomePatologia);
        dataV = findViewById(R.id.dataDiagnosi);
        diagnostaV = findViewById(R.id.diagnosta);
        patologiaId = getIntent().getStringExtra(patologiaId);
        //btnIndietro = findViewById(R.id.indietro);
        /*
        btnIndietro.setOnClickListener(v -> {
            Log.d(TAG, "Back button pressed");
            // Torna alla lista patologie
            Intent intent = new Intent(DettagliPatologiaActivity.this, ListaPatologieActivity.class);
            startActivity(intent);
        });
         */


        getPatologia();

    }

    private void getPatologia() {
        DatabaseReference spesaRef = userRef.child("patologie").child(patologiaId);

        spesaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Verifica se la patologia esiste
                if (dataSnapshot.exists()) {
                    Patologia patologia = dataSnapshot.getValue(Patologia.class);

                    // Popola le TextView con i dettagli della spesa
                    if (patologia != null) {
                        nomeV.setText(patologia.getNome());
                        dataV.setText(patologia.getDataDiagnosi());
                        diagnostaV.setText(patologia.getDataDiagnosi());
                    }
                } else {
                    Log.e(TAG, "Patologia non trovata!");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Errore nel caricamento della patologia", databaseError.toException());
            }
        });

    }

    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }
}