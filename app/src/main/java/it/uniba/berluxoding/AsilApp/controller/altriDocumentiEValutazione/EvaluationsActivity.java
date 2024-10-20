package it.uniba.berluxoding.AsilApp.controller.altriDocumentiEValutazione;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.berluxoding.AsilApp.R;

public class EvaluationsActivity extends AppCompatActivity {
    private RatingBar ratingBarCenter, ratingBarApp;
    private EditText editTextCenterComments, editTextAppComments;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluations);

        // Trova le view nel layout
        ratingBarCenter = findViewById(R.id.ratingBarCenter);
        ratingBarApp = findViewById(R.id.ratingBarApp);
        editTextCenterComments = findViewById(R.id.editTextCenterComments);
        editTextAppComments = findViewById(R.id.editTextAppComments);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Imposta un listener per il click sul pulsante di invio
        buttonSubmit.setOnClickListener(v -> salvaRatings());
    }

    private void salvaRatings(){

        // Prendi i valori dalle RatingBar e dagli EditText
        float centerRating = ratingBarCenter.getRating();
        String centerComments = editTextCenterComments.getText().toString();
        float appRating = ratingBarApp.getRating();
        String appComments = editTextAppComments.getText().toString();

        // Qui puoi gestire i dati raccolti, ad esempio inviarli a un server o salvarli lsu db

        Toast.makeText(this, "Grazie :) !", Toast.LENGTH_LONG).show();
        finish();

    }
}
