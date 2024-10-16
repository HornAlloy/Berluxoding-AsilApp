package it.uniba.berluxoding.AsilApp;

import android.os.Bundle;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinFragment extends Fragment implements OnDataReceived<String> {

    private EditText pinEditText;
    private Button submitButton;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase, pinPath, richiestaRef;
    private String strumento;
    private static final String TAG = "PinFragment";

    public static PinFragment newInstance() {
        // Crea una nuova istanza di PinFragment
        PinFragment fragment = new PinFragment();

        // Se desideri passare dei dati, puoi farlo tramite un Bundle
        // Bundle args = new Bundle();
        // args.putString("userRef", userRef);
        // fragment.setArguments(args);

        return fragment;
    }

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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pinPath = mDatabase.child("AsilApp").child(getUid()).child("anagrafica").child("pin");
        richiestaRef = mDatabase.child("medbox").child("richiesta");

        if(getArguments() != null) {
            strumento = getArguments().getString("strumento");
        }


            submitButton.setOnClickListener(v -> {
            String pin = pinEditText.getText().toString().trim();

            // Verifica se il PIN non è vuoto
            if (!pin.isEmpty()) {
                // Disabilita il bottone e mostra il ProgressBar per evitare ulteriori azioni fino a ottenere il risultato
                submitButton.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                // Esegui la query sul database per recuperare il PIN
                checkPin(pin);
            } else {
                Toast.makeText(getContext(), "Inserisci il PIN!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Verifica il PIN inserito confrontandolo con quello nel database.
     */
    private void checkPin(String pin) {
        pinPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String storedPin = dataSnapshot.getValue(String.class);  // Recupera il PIN memorizzato

                // Utilizza il callback per passare il PIN memorizzato
                onDataReceived(storedPin);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
            richiestaMap.put("userId", "dsajhfaskhjADGdsad"); // esempio di userId
            richiestaMap.put("strumento", "chitarra"); // servizio richiesto

            // Invia la richiesta
            richiestaRef.setValue(richiestaMap)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "Richiesta inviata con successo.");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Errore nell'invio della richiesta: " + e.getMessage());
                    });

            // Passa al fragment successivo o invia l'utente alla schermata successiva
            ((MedboxActivity) getActivity()).replaceFragment(new AttesaFragment(), false, args);

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }
}