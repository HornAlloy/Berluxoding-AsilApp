package it.uniba.berluxoding.AsilApp.controller.informazioni.altriDocumentiEValutazione;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

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
import it.uniba.berluxoding.AsilApp.interfacce.OnDataReceived;
import it.uniba.berluxoding.AsilApp.model.Valutazione;

/**
 * Activity che gestisce la raccolta e la visualizzazione delle valutazioni degli utenti.
 * Implementa l'interfaccia OnDataReceived per gestire i dati delle valutazioni.
 */
public class EvaluationsActivity extends AppCompatActivity implements OnDataReceived<Valutazione> {
    private RatingBar ratingBarCenter, ratingBarApp;
    private EditText editTextCenterComments, editTextAppComments;
    private final String TAG = "EVALUATIONS_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Abilita EdgeToEdge per un'esperienza utente a schermo intero
        EdgeToEdge.enable(this);
        // Imposta il layout associato a questa activity
        setContentView(R.layout.activity_evaluations);

        // Gestisce gli insets delle finestre per supportare schermi a tutto schermo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Trova le view nel layout
        ratingBarCenter = findViewById(R.id.ratingBarCenter);
        ratingBarApp = findViewById(R.id.ratingBarApp);
        editTextCenterComments = findViewById(R.id.editTextCenterComments);
        editTextAppComments = findViewById(R.id.editTextAppComments);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child("AsilApp").child(getUser()).child("valutazioni");

        // Legge le valutazioni esistenti dal database
        leggi(userRef);

        // Imposta un listener per il click sul pulsante di invio
        buttonSubmit.setOnClickListener(v -> salvaRatings(userRef));
    }

    /**
     * Legge le valutazioni da un riferimento specificato nel database.
     *
     * @param ref     Riferimento al nodo del database da cui leggere le valutazioni.
     * @param callback Callback per gestire i dati ricevuti.
     */
    private void leggiRatings(DatabaseReference ref, OnDataReceived<Valutazione> callback) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Valutazione val = snapshot.getValue(Valutazione.class);
                    callback.onDataReceived(val);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestione dell'errore se necessario
            }
        });
    }

    /**
     * Legge le valutazioni per l'applicazione e il centro.
     *
     * @param ref Riferimento al nodo delle valutazioni dell'utente.
     */
    private void leggi(DatabaseReference ref) {
        leggiRatings(ref.child("applicazione"), data -> {
            if (data != null) {
                // Dati ricevuti per l'applicazione
                Log.d(TAG, "Valutazione ricevuta: " + data);
                ratingBarApp.setRating(data.getRating());
                editTextAppComments.setText(data.getComment());
            } else {
                // Nessun dato ricevuto
                Log.d(TAG, "Nessuna valutazione trovata.");
            }
        });
        leggiRatings(ref.child("centro"), data -> {
            if (data != null) {
                // Dati ricevuti per il centro
                Log.d(TAG, "Valutazione ricevuta: " + data);
                ratingBarCenter.setRating(data.getRating());
                editTextCenterComments.setText(data.getComment());
            } else {
                Log.d(TAG, "Nessuna valutazione trovata.");
            }
        });
    }

    /**
     * Salva le valutazioni nel database.
     *
     * @param ref Riferimento al nodo del database in cui salvare le valutazioni.
     */
    private void salvaRatings(DatabaseReference ref) {
        Valutazione app = new Valutazione();
        Valutazione centro = new Valutazione();

        // Prendi i valori dalle RatingBar e dagli EditText
        centro.setRating(ratingBarCenter.getRating());
        centro.setComment(editTextCenterComments.getText().toString());
        app.setRating(ratingBarApp.getRating());
        app.setComment(editTextAppComments.getText().toString());

        // Salva le valutazioni nel database
        salva(ref.child("applicazione"), app);
        salva(ref.child("centro"), centro);

        Toast.makeText(this, "Grazie :) !", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * Salva una valutazione nel database.
     *
     * @param ref Riferimento al nodo del database in cui salvare la valutazione.
     * @param val Valutazione da salvare.
     */
    private void salva(DatabaseReference ref, Valutazione val) {
        ref.setValue(val);
    }

    /**
     * Restituisce l'UID dell'utente attualmente loggato.
     *
     * @return L'UID dell'utente.
     */
    private String getUser() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    @Override
    public void onDataReceived(Valutazione data) {
        // Gestione dei dati ricevuti (non implementata)
    }
}
