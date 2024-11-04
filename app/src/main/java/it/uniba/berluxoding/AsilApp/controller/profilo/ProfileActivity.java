package it.uniba.berluxoding.AsilApp.controller.profilo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

import java.util.Objects;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.HomeActivity;
import it.uniba.berluxoding.AsilApp.controller.profilo.liste.ListaSpeseActivity;
import it.uniba.berluxoding.AsilApp.controller.profilo.liste.ListaMisurazioniActivity;
import it.uniba.berluxoding.AsilApp.controller.profilo.liste.ListaPatologieActivity;
import it.uniba.berluxoding.AsilApp.model.Utente;

/**
 * La classe {@code ProfileActivity} estende {@code AppCompatActivity} e rappresenta l'attività
 * che mostra il profilo dell'utente. In questa attività, vengono visualizzati i dettagli dell'utente
 * recuperati da Firebase, come nome, cognome, data di nascita e paese di provenienza.
 * Inoltre, fornisce pulsanti per accedere ad altre attività relative a patologie, misurazioni e spese.
 */
public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    /**
     * Questo metodo viene chiamato quando l'attività viene creata.
     * Qui vengono inizializzati i componenti dell'interfaccia utente e viene recuperato
     * l'oggetto utente dal database Firebase.
     *
     * @param savedInstanceState Lo stato salvato dell'attività, se presente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageButton bt1 = findViewById(R.id.button);
        ImageButton bt2 = findViewById(R.id.button2);
        ImageButton bt3 = findViewById(R.id.button3);

        tv1 = findViewById(R.id.tvNome);
        tv2 = findViewById(R.id.tvCognome);
        tv3 = findViewById(R.id.tvDataNascita);
        tv4 = findViewById(R.id.tvPaeseDiProvenienza);
        getUtente();

        bt1.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the patologies button");
            startActivityWithIntent(ListaPatologieActivity.class);
        });
        bt2.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the misurations button");
            startActivityWithIntent(ListaMisurazioniActivity.class);
        });
        bt3.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the expenses button");
            startActivityWithIntent(ListaSpeseActivity.class);
        });
    }

    /**
     * Recupera i dati dell'utente dal database Firebase e aggiorna
     * l'interfaccia utente con le informazioni ottenute.
     */
    private void getUtente() {
        mDatabase.child("AsilApp").child(getUser()).child("anagrafica").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Converti il DataSnapshot in un oggetto User
                        Utente utente = dataSnapshot.getValue(Utente.class);
                        if (utente != null) {
                            // Usa l'oggetto User
                            Log.d("FirebaseData", "Nome: " + utente.getNome());
                            Log.d("FirebaseData", "Cognome: " + utente.getCognome());
                            Log.d("FirebaseData", "Luogo di Nascita: " + utente.getLuogoProvenienza());
                            Log.d("FirebaseData", "Data di Nascita: " + utente.getDataNascita());
                            tv1.setText(utente.getNome());
                            tv2.setText(utente.getCognome());
                            tv3.setText(utente.getDataNascita());
                            tv4.setText(utente.getLuogoProvenienza());
                        } else {
                            Log.d("FirebaseData", "Utente non trovato");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Gestisci eventuali errori
                        Log.e("FirebaseData", "Errore nella lettura del dato", databaseError.toException());
                    }
                });
    }

    /**
     * Restituisce l'ID dell'utente attualmente autenticato.
     *
     * @return L'ID dell'utente corrente.
     */
    private String getUser() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    /**
     * Avvia una nuova attività in base alla classe passata come parametro.
     *
     * @param targetActivity La classe dell'attività da avviare.
     */
    private void startActivityWithIntent(Class<?> targetActivity) {
        Intent intent = new Intent(ProfileActivity.this, targetActivity);
        startActivity(intent);
    }
}
