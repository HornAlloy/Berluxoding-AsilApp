package it.uniba.berluxoding.AsilApp.controller.medbox;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import it.uniba.berluxoding.AsilApp.interfacce.OnDataReceived;
import it.uniba.berluxoding.AsilApp.R;

/**
 * L'attività {@code MedboxActivity} gestisce l'interfaccia utente per la selezione
 * degli strumenti medici e la navigazione verso il frammento di inserimento PIN.
 */
public class MedboxActivity extends AppCompatActivity {

    private final String TAG = "MEDBOXAPP_ACTIVITY";

    private Button strumento1, strumento2, strumento3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medbox);

        // Gestisce le finestre di sistema per il padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inizializzazione del database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mStrumenti = mDatabase.child("medbox").child("strumenti");

        // Recupero dei dati per ottenere i nomi degli strumenti
        getStrumentiData(mStrumenti, this::aggiornaStrumentiUI);
    }

    /**
     * Recupera i nomi degli strumenti dal database e invoca il callback fornito
     * una volta completato il recupero dei dati.
     *
     * @param ref  Il riferimento al nodo del database da cui recuperare i dati.
     * @param dati Il callback da invocare con i dati ricevuti.
     */
    private void getStrumentiData(DatabaseReference ref, final OnDataReceived<String[]> dati) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String[] strumentiArray = new String[3];
                Map<String, String> strumenti = (Map<String, String>) dataSnapshot.getValue();
                if (strumenti != null) {
                    strumentiArray[0] = strumenti.get("strumento1");
                    strumentiArray[1] = strumenti.get("strumento2");
                    strumentiArray[2] = strumenti.get("strumento3");
                }

                // Restituisci i dati
                dati.onDataReceived(strumentiArray);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Errore nel leggere i dati: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Aggiorna l'interfaccia utente con i nomi degli strumenti e imposta i listener
     * per i pulsanti corrispondenti.
     *
     * @param strumentiArray L'array dei nomi degli strumenti da visualizzare.
     */
    private void aggiornaStrumentiUI(String[] strumentiArray) {
        strumento1 = findViewById(R.id.button);
        strumento2 = findViewById(R.id.button2);
        strumento3 = findViewById(R.id.button3);

        strumento1.setText(strumentiArray[0]);
        strumento2.setText(strumentiArray[1]);
        strumento3.setText(strumentiArray[2]);

        View.OnClickListener listener = v -> {
            Button clickedButton = (Button) v;
            String strumentoName = clickedButton.getText().toString();
            Log.d(TAG, strumentoName + " button pressed!");

            changeFrame();
            // Crea un bundle e salvaci lo strumento selezionato
            Bundle bundle = new Bundle();
            bundle.putString("strumento", strumentoName);

            // Passa al fragment di inserimento PIN
            replaceFragment(new PinFragment(), bundle);
        };

        strumento1.setOnClickListener(listener);
        strumento2.setOnClickListener(listener);
        strumento3.setOnClickListener(listener);
    }

    /**
     * Nasconde i pulsanti degli strumenti, rendendo l'interfaccia utente più pulita
     * dopo che un pulsante è stato premuto.
     */
    private void changeFrame() {
        strumento1.setVisibility(View.GONE);
        strumento2.setVisibility(View.GONE);
        strumento3.setVisibility(View.GONE);
    }

    /**
     * Sostituisce il frammento corrente con un nuovo frammento, passando i dati
     * attraverso un Bundle.
     *
     * @param fragment Il frammento da sostituire.
     * @param bundle   I dati da passare al nuovo frammento.
     */
    protected void replaceFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}