package it.uniba.berluxoding.AsilApp.controller.profilo.liste;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import it.uniba.berluxoding.AsilApp.controller.medbox.MedboxActivity;
import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.profilo.dettagli.DettagliMisurazioneActivity;
import it.uniba.berluxoding.AsilApp.model.Misurazione;
import it.uniba.berluxoding.AsilApp.controller.profilo.viewholder.MisurazioneViewHolder;

/**
 * La classe {@code ListaMisurazioniActivity} estende {@code AppCompatActivity} e rappresenta
 * l'attività che visualizza una lista di misurazioni. Utilizza un adapter Firebase per
 * gestire e visualizzare i dati delle misurazioni in un RecyclerView.
 */
public class ListaMisurazioniActivity extends AppCompatActivity {

    private FirebaseRecyclerAdapter<Misurazione, MisurazioneViewHolder> mAdapter;
    private DatabaseReference userRef;

    /**
     * Questo metodo viene chiamato quando l'attività viene creata.
     * Qui vengono inizializzati i componenti dell'interfaccia utente
     * e viene configurato il riferimento al database Firebase.
     *
     * @param savedInstanceState Lo stato salvato dell'attività, se presente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_misurazioni);

        // Gestione degli insets di sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inizializzazione del database Firebase
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("AsilApp").child(getUid());
    }

    /**
     * Crea e configura la lista di elementi da visualizzare nel RecyclerView
     * utilizzando un adapter Firebase per recuperare i dati delle misurazioni.
     *
     * @param misurazioneQuery La query per recuperare i dati delle misurazioni dal database.
     */
    private void creaListaElementi(Query misurazioneQuery) {
        RecyclerView mRecycler = findViewById(R.id.listaMisurazioni);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Configurazione del layout manager
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        FirebaseRecyclerOptions<Misurazione> options = new FirebaseRecyclerOptions.Builder<Misurazione>()
                .setQuery(misurazioneQuery, Misurazione.class)
                .build();

        Button btn = findViewById(R.id.btnAggiungi);
        btn.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the Add button");
            aggiungiMisurazione();
        });

        mAdapter = new FirebaseRecyclerAdapter<>(options) {
            @NonNull
            @Override
            public MisurazioneViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MisurazioneViewHolder(inflater.inflate(R.layout.item_misurazione, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull MisurazioneViewHolder viewHolder, int position, @NonNull final Misurazione model) {
                getRef(position);

                // Conversione della data in un formato leggibile
                model.checkDate();
                // Associa il modello al ViewHolder
                viewHolder.bindToMisurazione(model, v -> mostraDettagli(model));
            }
        };

        mRecycler.setAdapter(mAdapter);
    }

    /**
     * Questo metodo viene chiamato quando l'attività viene avviata.
     * Qui si inizializza la query per recuperare i dati delle misurazioni
     * e si avvia l'ascolto delle modifiche nel database.
     */
    @Override
    protected void onStart() {
        super.onStart();

        Query misurazioneQuery = getQuery(userRef);
        creaListaElementi(misurazioneQuery);

        // Inizia l'ascolto dei dati Firebase
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    /**
     * Questo metodo viene chiamato quando l'attività viene fermata.
     * Qui si interrompe l'ascolto delle modifiche nel database Firebase.
     */
    @Override
    protected void onStop() {
        super.onStop();
        // Ferma l'ascolto dei dati Firebase
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    /**
     * Restituisce l'UID dell'utente attualmente autenticato.
     *
     * @return L'UID dell'utente corrente.
     */
    private String getUid() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    /**
     * Restituisce la query per recuperare le misurazioni dal database.
     *
     * @param queryReference Riferimento al nodo del database da cui eseguire la query.
     * @return La query per le misurazioni ordinate per data.
     */
    public Query getQuery(DatabaseReference queryReference) {
        return queryReference.child("misurazioni").orderByChild("data");
    }

    /**
     * Mostra i dettagli di una misurazione specifica in una nuova attività.
     *
     * @param model Il modello della misurazione da visualizzare.
     */
    private void mostraDettagli(Misurazione model) {
        Intent intent = new Intent(this, DettagliMisurazioneActivity.class);
        intent.putExtra("misurazioneId", model.getId());
        startActivity(intent);
    }

    /**
     * Avvia l'attività per aggiungere una nuova misurazione.
     */
    private void aggiungiMisurazione() {
        Intent intent = new Intent(this, MedboxActivity.class);
        startActivity(intent);
    }
}
