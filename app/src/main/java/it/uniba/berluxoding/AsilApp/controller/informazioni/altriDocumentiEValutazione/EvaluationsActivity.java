package it.uniba.berluxoding.AsilApp.controller.informazioni.altriDocumentiEValutazione;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class EvaluationsActivity extends AppCompatActivity implements OnDataReceived<Valutazione> {
    private RatingBar ratingBarCenter, ratingBarApp;
    private EditText editTextCenterComments, editTextAppComments;
    private Valutazione app, centro;
    private final String TAG = "EVALUATIONS_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluations);

        // Trova le view nel layout
        ratingBarCenter = findViewById(R.id.ratingBarCenter);
        ratingBarApp = findViewById(R.id.ratingBarApp);
        editTextCenterComments = findViewById(R.id.editTextCenterComments);
        editTextAppComments = findViewById(R.id.editTextAppComments);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child("AsilApp").child(getUser()).child("valutazioni");

        app = new Valutazione();
        centro = new Valutazione();

        leggi(userRef);

        // Imposta un listener per il click sul pulsante di invio
        buttonSubmit.setOnClickListener(v -> salvaRatings(userRef));
    }

    private void leggiRatings(DatabaseReference ref, OnDataReceived<Valutazione> callback) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Valutazione val = snapshot.getValue(Valutazione.class);
                    callback.onDataReceived(val);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });
    }

    private void leggi(DatabaseReference ref) {
        leggiRatings(ref.child("applicazione"), new OnDataReceived<Valutazione>() {
            @Override
            public void onDataReceived (Valutazione data) {
                if (data != null) {
                    // Dati ricevuti, puoi lavorarci
                    Log.d(TAG, "Valutazione ricevuta: " + data.toString());
                    //app
                    ratingBarApp.setRating(data.getRating());
                    editTextAppComments.setText(data.getComment());
                } else {
                    // Nessun dato ricevuto
                    Log.d(TAG, "Nessuna valutazione trovata.");
                }
            }
        });
        leggiRatings(ref.child("centro"), new OnDataReceived<Valutazione>() {
            @Override
            public void onDataReceived (Valutazione data) {
                if (data != null) {
                    Log.d(TAG, "Valutazione ricevuta: " + data.toString());
                    //centro
                    ratingBarCenter.setRating(data.getRating());
                    editTextCenterComments.setText(data.getComment());
                } else {
                    Log.d(TAG, "Nessuna valutazione trovata.");
                }
            }
        });
    }


    private void salvaRatings(DatabaseReference ref) {

        // Prendi i valori dalle RatingBar e dagli EditText
        centro.setRating(ratingBarCenter.getRating());
        centro.setComment(editTextCenterComments.getText().toString());
        app.setRating(ratingBarApp.getRating());
        app.setComment(editTextAppComments.getText().toString());

        // Qui puoi gestire i dati raccolti, ad esempio inviarli a un server o salvarli sul db
        salva(ref.child("applicazione"), app);
        salva(ref.child("centro"), centro);

        Toast.makeText(this, "Grazie :) !", Toast.LENGTH_LONG).show();
        finish();

    }

    private void salva (DatabaseReference ref, Valutazione val) {
        ref.setValue(val);
    }

    private String getUser () {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    @Override
    public void onDataReceived (Valutazione data) {

    }
}
