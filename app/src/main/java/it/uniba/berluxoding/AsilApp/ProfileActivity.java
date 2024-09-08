package it.uniba.berluxoding.AsilApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import it.uniba.berluxoding.AsilApp.model.Utente;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ImageButton bt1;
    private ImageButton bt2;
    private ImageButton bt3;

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        bt1 = findViewById(R.id.button);
        bt2 = findViewById(R.id.button2);
        bt3 = findViewById(R.id.button3);

        tv1 = findViewById(R.id.text_view);
        tv2 = findViewById(R.id.text_view2);
        tv3 = findViewById(R.id.text_view3);
        tv4 = findViewById(R.id.text_view4);
        getUtente();

    }

    private void getUtente () {
        mDatabase.child("AsilApp").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("anagrafica").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Converti il DataSnapshot in un oggetto User
                        Utente utente = dataSnapshot.getValue(Utente.class);
                        if (utente != null) {
                            // Usa l'oggetto User
                            Log.d("FirebaseData", "Nome: " + utente.getNome());
                            Log.d("FirebaseData", "Cognome: " + utente.getCognome());
                            Log.d("FirebaseData", "Luogo di Nascita: " + utente.getLuogoProvenienza());
                            Log.d("FirebaseData", "Data di Nascita" + utente.getDataNascita());
                            tv1.setText(utente.getNome());
                            tv2.setText(utente.getCognome());
                            tv3.setText(utente.getDataNascita());
                            tv4.setText(utente.getLuogoProvenienza());
                        } else {
                            Log.d("FirebaseData", "Utente non trovato");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Gestisci eventuali errori
                        Log.e("FirebaseData", "Errore nella lettura del dato", databaseError.toException());
                    }
                });

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the profile button");
                Intent openPage = new Intent(ProfileActivity.this,ListaPatologieActivity.class);
                // passo all'attivazione dell'activity page1.java
                startActivity(openPage);

            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the information button");
                Intent openPage = new Intent(ProfileActivity.this,ListaMisurazioniActivity.class);
                // passo all'attivazione dell'activity page1.java
                startActivity(openPage);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the medbox button");
                Intent openPage = new Intent(ProfileActivity.this,ListaSpeseActivity.class);
                // passo all'attivazione dell'activity page1.java
                startActivity(openPage);
            }
        });
    }

}