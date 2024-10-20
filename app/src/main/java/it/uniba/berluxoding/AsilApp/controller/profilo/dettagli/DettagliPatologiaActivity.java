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

public class DettagliPatologiaActivity extends AppCompatActivity {

    private TextView nomeV, dataV, diagnostaV;
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

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        nomeV = findViewById(R.id.nomePatologia);
        dataV = findViewById(R.id.dataDiagnosi);
        diagnostaV = findViewById(R.id.diagnosta);
        String patologiaId = getIntent().getStringExtra("patologiaId");
        Log.d(TAG, "patologiaId = " + patologiaId);

        DatabaseReference dataRef = mDatabase.child("AsilApp").child(getUid()).child("patologie")
                .child(Objects.requireNonNull(patologiaId));



        getPatologia(dataRef);

    }

    private void getPatologia(DatabaseReference dataRef) {
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifica se la patologia esiste
                if (dataSnapshot.exists()) {
                    Patologia patologia = dataSnapshot.getValue(Patologia.class);

                    // Popola le TextView con i dettagli della spesa
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

    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }
}