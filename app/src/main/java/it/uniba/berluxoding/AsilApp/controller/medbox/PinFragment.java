package it.uniba.berluxoding.AsilApp.controller.medbox;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
 * PinFragment è un Fragment che gestisce l'input dell'utente per un PIN
 * e verifica il PIN inserito rispetto a quello memorizzato in un database Firebase.
 * Fa parte dell'applicazione Medbox, consentendo agli utenti di
 * richiedere l'accesso a strumenti medici specifici in modo sicuro.
 */
public class PinFragment extends Fragment implements OnDataReceived<String> {

    private EditText pinEditText;  // Campo di input per l'inserimento del PIN da parte dell'utente
    private Button submitButton;     // Pulsante per inviare il PIN
    private ProgressBar progressBar; // Barra di progresso per indicare che il processo è in corso
    private DatabaseReference pinPath; // Riferimento al percorso del database per il PIN
    private DatabaseReference richiestaRef; // Riferimento al percorso del database per le richieste
    private DatabaseReference rispostaRef; // Riferimento al percorso del database per le richieste
    private String strumento; // Nome dello strumento a cui si sta accedendo
    private static final String TAG = "PinFragment"; // Tag per il logging

    /**
     * Inflaziona il layout per questo fragment.
     *
     * @param inflater           LayoutInflater per inflazionare la vista
     * @param container          ViewGroup a cui l'interfaccia utente di questo fragment deve essere collegata
     * @param savedInstanceState Bundle contenente lo stato salvato precedentemente del fragment
     * @return Vista per l'interfaccia utente del fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pin, container, false);
    }

    /**
     * Inizializza i componenti dell'interfaccia utente e imposta i riferimenti al database.
     * Inoltre, recupera il nome dello strumento dagli argomenti del fragment.
     *
     * @param view               La vista del fragment
     * @param savedInstanceState Bundle contenente lo stato salvato precedentemente del fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backPressed();

        pinEditText = view.findViewById(R.id.pinEditText);
        submitButton = view.findViewById(R.id.submitButton);
        progressBar = view.findViewById(R.id.progressBar);

        // Inizializza i riferimenti al database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        pinPath = mDatabase.child("AsilApp").child(getUid()).child("anagrafica").child("pin");
        richiestaRef = mDatabase.child("medbox").child("richiesta");
        rispostaRef = mDatabase.child("medbox").child("risposta");


        if(getArguments() != null) {
            strumento = getArguments().getString("strumento");
            assert strumento != null;
            Log.d("strumento = ", strumento);
        }

        checkAnswer();
        // Imposta il listener per il pulsante di invio del PIN
        submitButton.setOnClickListener(v -> {
            String pin = pinEditText.getText().toString().trim();

            // Verifica se il PIN non è vuoto
            if (!pin.isEmpty()) {
                // Disabilita il pulsante e mostra la barra di progresso per evitare ulteriori azioni fino a ottenere il risultato
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

                // Rende il pulsante di invio di nuovo abilitato e nasconde la barra di progresso
                submitButton.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Metodo chiamato quando il PIN memorizzato viene ricevuto dal database.
     * Confronta il PIN inserito dall'utente con quello memorizzato.
     * Se il PIN è corretto, invia una richiesta al database e passa al
     * fragment di attesa; altrimenti, mostra un messaggio di errore.
     *
     * @param storedPin Il PIN memorizzato nel database, recuperato tramite callback.
     */
    @Override
    public void onDataReceived(String storedPin) {
        String enteredPin = pinEditText.getText().toString().trim();


        // Confronta il PIN inserito con quello memorizzato nel database
        if (storedPin != null && storedPin.equals(enteredPin)) {
            // Il PIN inserito è corretto
            Bundle args = new Bundle();
            args.putString("userId", getUid());

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

        // Rende il pulsante di invio di nuovo abilitato e nasconde la barra di progresso
        submitButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Restituisce l'UID (Unique Identifier) dell'utente attualmente loggato.
     * Questo metodo è utilizzato per identificare in modo univoco l'utente nel
     * database Firebase e per eseguire operazioni relative a quell'utente.
     *
     * @return L'UID dell'utente loggato, ottenuto da FirebaseAuth.
     *         Se l'utente non è loggato, genera un'eccezione di tipo NullPointerException.
     */
    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    private void checkAnswer() {
        rispostaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    rispostaRef.removeValue();
                } else {
                    rispostaRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {
                Log.e(TAG, "errore nella cancellazione preventiva della risposta");
            }
        });
    }

    private void backPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MedboxActivity activity = (MedboxActivity) getActivity();
                if (activity != null) {
                    activity.resumeActivity();
                }
                // Controlla se ci sono Fragment nel back stack
                if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    requireActivity().getSupportFragmentManager().popBackStack(); // Torna indietro nel back stack
                } else {
                    // Se non ci sono altri fragment, chiudi l'activity
                    requireActivity().finish();
                }
            }
        };

        // Aggiungi il callback al dispatcher
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}
