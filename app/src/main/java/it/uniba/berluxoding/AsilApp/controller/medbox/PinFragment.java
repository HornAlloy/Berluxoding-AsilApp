package it.uniba.berluxoding.AsilApp.controller.medbox;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import it.uniba.berluxoding.AsilApp.interfacce.OnDataReceived;
import it.uniba.berluxoding.AsilApp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinFragment extends Fragment implements OnDataReceived<String> {

    private EditText pinEditText;
    private Button submitButton;
    private ProgressBar progressBar;
    private DatabaseReference pinPath;
    private DatabaseReference richiestaRef;
    private String strumento;
    private static final String TAG = "PinFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pin, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pinEditText = view.findViewById(R.id.pinEditText);
        submitButton = view.findViewById(R.id.submitButton);
        progressBar = view.findViewById(R.id.progressBar);

        // Inizializzazione del riferimento al database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        pinPath = mDatabase.child("AsilApp").child(getUid()).child("anagrafica").child("pin");
        richiestaRef = mDatabase.child("medbox").child("richiesta");

        if(getArguments() != null) {
            strumento = getArguments().getString("strumento");
            assert strumento != null;
            Log.d("strumento = ", strumento);
        }


            submitButton.setOnClickListener(v -> {
            String pin = pinEditText.getText().toString().trim();

            // Verifica se il PIN non è vuoto
            if (!pin.isEmpty()) {
                // Disabilita il bottone e mostra il ProgressBar per evitare ulteriori azioni fino a ottenere il risultato
                submitButton.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                // Esegui la query sul database per recuperare il PIN
                checkPin();
            } else {
                Toast.makeText(getContext(), "Inserisci il PIN!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Verifica il PIN inserito confrontandolo con quello nel database.
     */
    private void checkPin() {
        pinPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String storedPin = dataSnapshot.getValue(String.class);  // Recupera il PIN memorizzato

                // Utilizza il callback per passare il PIN memorizzato
                onDataReceived(storedPin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Errore di lettura dal database
                Log.e(TAG, "Errore nel recuperare il PIN dal database: " + databaseError.getMessage());

                // Rende il bottone di invio di nuovo abilitato e nasconde il ProgressBar
                submitButton.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDataReceived(String storedPin) {
        String enteredPin = pinEditText.getText().toString().trim();

        // Confronta il PIN inserito con quello memorizzato nel database
        if (storedPin != null && storedPin.equals(enteredPin)) {
            // Il PIN inserito è corretto
            Bundle args = new Bundle();
            args.putString("userId", getUid());
            // fragment.setArguments(args);

            // Creiamo un HashMap per inviare i dati della richiesta
            HashMap<String, Object> richiestaMap = new HashMap<>();
            richiestaMap.put("userId", getUid()); // esempio di userId
            richiestaMap.put("strumento", strumento); // servizio richiesto

            // Invia la richiesta
            richiestaRef.setValue(richiestaMap)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Richiesta inviata con successo."))
                    .addOnFailureListener(e -> Log.e("Firebase", "Errore nell'invio della richiesta: " + e.getMessage()));

            // Passa al fragment successivo o invia l'utente alla schermata successiva
            ((MedboxActivity) requireActivity()).replaceFragment(new AttesaFragment(), args);

        } else {
            // Il PIN inserito non è corretto
            Toast.makeText(getContext(), "PIN errato. Riprova.", Toast.LENGTH_SHORT).show();
        }

        // Rende il bottone di invio di nuovo abilitato e nasconde il ProgressBar
        submitButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }
}