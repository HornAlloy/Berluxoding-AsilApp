package it.uniba.berluxoding.AsilApp.controller.profilo.liste;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.profilo.aggiunta.AggiungiSpesaActivity;
import it.uniba.berluxoding.AsilApp.controller.profilo.dettagli.DettagliSpesaActivity;
import it.uniba.berluxoding.AsilApp.model.Spesa;
import it.uniba.berluxoding.AsilApp.controller.profilo.viewholder.SpesaViewHolder;

/**
 * La classe {@code ListaSpeseActivity} estende {@code AppCompatActivity} e rappresenta
 * l'attività che gestisce e visualizza una lista di spese. Utilizza un adapter Firebase
 * per recuperare e visualizzare i dati delle spese in un RecyclerView.
 */
public class ListaSpeseActivity extends AppCompatActivity {

    private DatabaseReference userRef;
    private FirebaseRecyclerAdapter<Spesa, SpesaViewHolder> mAdapter;
    private Spinner spTipologia;
    private final String TAG = "LISTA_SPESE_ACTIVITY";
    private RecyclerView mRecycler;
    private FirebaseRecyclerOptions<Spesa> options;
    private TextView tvTotaleSpese;

    // Variabile per tenere traccia del totale
    private double totaleSpese = 0.0;

    /**
     * Questo metodo viene chiamato quando l'attività viene creata.
     * Qui vengono inizializzati i componenti dell'interfaccia utente,
     * configurato il riferimento al database Firebase e impostato lo spinner
     * per la selezione della tipologia di spesa.
     *
     * @param savedInstanceState Lo stato salvato dell'attività, se presente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_spese);

        // Gestione degli insets di sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inizializzazione del database Firebase
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("AsilApp").child(getUid());

        tvTotaleSpese = findViewById(R.id.tvTotaleSpeseEuro);

        // Imposta lo spinner e il suo listener
        spTipologia = findViewById(R.id.spAmbito);
        ArrayAdapter<CharSequence> ambitoAdapter = ArrayAdapter.createFromResource(this,
                R.array.tipologia_per_la_visualizzazione_array, android.R.layout.simple_spinner_item);
        ambitoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipologia.setAdapter(ambitoAdapter);

        // Configura il listener per lo spinner
        spTipologia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Aggiorna la query quando viene selezionato un elemento nello spinner
                String selectedFilter = parentView.getItemAtPosition(position).toString();
                Log.d(TAG, "Filtro selezionato: " + selectedFilter);
                calcolaTotaleSpese(getQuery(userRef));
                updateQueryBasedOnSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nessuna azione necessaria se non viene selezionato nulla
            }
        });

        // Configura il RecyclerView
        mRecycler = findViewById(R.id.listaSpese);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Button btn = findViewById(R.id.btnAggiungi);
        btn.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the Add button");
            aggiungiSpesa();
        });
    }

    /**
     * Metodo per aggiornare la query e ricaricare i dati quando lo spinner cambia.
     */
    private void updateQueryBasedOnSelection() {
        Query updatedQuery = getQuery(userRef); // Ottieni la nuova query basata sul filtro

        // Ferma l'ascolto dell'adapter corrente se esiste
        if (mAdapter != null) {
            mAdapter.stopListening();
        }

        // Crea nuove opzioni per l'adapter basate sulla nuova query
        options = new FirebaseRecyclerOptions.Builder<Spesa>()
                .setQuery(updatedQuery, Spesa.class)
                .build();

        // Ricrea l'adapter e collega il RecyclerView
        creaListaElementi(options);
    }

    /**
     * Crea e configura la lista di elementi da visualizzare nel RecyclerView
     * utilizzando un adapter Firebase per recuperare i dati delle spese.
     *
     * @param options Le opzioni per l'adapter FirebaseRecycler.
     */
    private void creaListaElementi(FirebaseRecyclerOptions<Spesa> options) {
        // Imposta l'adapter
        mAdapter = new FirebaseRecyclerAdapter<>(options) {
            @NonNull
            @Override
            public SpesaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new SpesaViewHolder(inflater.inflate(R.layout.item_spesa, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull SpesaViewHolder viewHolder, int position, @NonNull final Spesa model) {
                model.setData(convertDateFormat(model.getData()));
                viewHolder.bindToSpesa(model, v -> mostraDettagli(model), v -> eliminaSpesa(model));
                Log.d(TAG, "Binding avvenuto!");
            }
        };

        // Collega l'adapter al RecyclerView e avvia l'ascolto
        mRecycler.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    /**
     * Converte la data in formato {@code yyyy/MM/dd} in formato {@code dd/MM/yyyy}.
     *
     * @param dateStr La data in formato di input.
     * @return La data formattata nel nuovo formato.
     */
    private String convertDateFormat(String dateStr) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);

        String formattedDate = null;
        try {
            Date date = inputFormat.parse(dateStr);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    /**
     * Calcola il totale delle spese basato su una query specificata.
     *
     * @param spesaQuery La query per recuperare i dati delle spese.
     */
    private void calcolaTotaleSpese(Query spesaQuery) {
        spesaQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Resetta il totale prima di ricalcolare
                totaleSpese = 0.0;

                // Itera su tutti i figli del nodo 'spese' per calcolare il totale
                for (DataSnapshot spesaSnapshot : dataSnapshot.getChildren()) {
                    Spesa spesa = spesaSnapshot.getValue(Spesa.class);
                    if (spesa != null && spesa.getCosto() != null && !spesa.getCosto().isEmpty()) {
                        totaleSpese += Double.parseDouble(spesa.getCosto());
                    }
                }

                // Aggiorna il TextView con il totale delle spese
                tvTotaleSpese.setText(String.format(Locale.getDefault(), "%.2f", totaleSpese));
                Log.d(TAG, "Totale spese calcolato: " + totaleSpese);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Errore nel recupero delle spese: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Questo metodo viene chiamato quando l'attività viene avviata.
     * Qui si inizializza la query per recuperare i dati delle spese
     * e si avvia l'ascolto delle modifiche nel database.
     */
    @Override
    protected void onStart() {
        super.onStart();

        Query spesaQuery = getQuery(userRef);
        options = new FirebaseRecyclerOptions.Builder<Spesa>()
                .setQuery(spesaQuery, Spesa.class)
                .build();

        creaListaElementi(options);
    }

    /**
     * Questo metodo viene chiamato quando l'attività viene fermata.
     * Qui si interrompe l'ascolto delle modifiche nel database Firebase.
     */
    @Override
    protected void onStop() {
        super.onStop();

        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    /**
     * Restituisce la query per recuperare le spese dal database
     * in base alla selezione dello spinner.
     *
     * @param queryReference Riferimento al nodo del database da cui eseguire la query.
     * @return La query per le spese in base all'ambito selezionato.
     */
    public Query getQuery(DatabaseReference queryReference) {
        String ambito = spTipologia.getSelectedItem().toString();
        Query query;

        if (ambito.equals("Tutto")) {
            query = queryReference.child("spese").orderByChild("data");
        } else {
            query = queryReference.child("spese-ambito").child(ambito).orderByChild("data");
        }

        Log.d(TAG, "Query created for ambito: " + ambito);
        return query;
    }

    /**
     * Restituisce l'UID dell'utente attualmente loggato.
     *
     * @return L'UID dell'utente loggato.
     */
    private String getUid() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    /**
     * Mostra i dettagli della spesa selezionata.
     *
     * @param model Il modello della spesa di cui mostrare i dettagli.
     */
    private void mostraDettagli(Spesa model) {
        Intent intent = new Intent(this, DettagliSpesaActivity.class);
        intent.putExtra("spesaId", model.getId());
        startActivity(intent);
    }

    /**
     * Elimina la spesa specificata dal database Firebase.
     *
     * @param model Il modello della spesa da eliminare.
     */
    private void eliminaSpesa(Spesa model) {
        DatabaseReference spesaRef = userRef.child("spese").child(model.getId());
        spesaRef.removeValue();
        spesaRef = userRef.child("spese-ambito").child(model.getAmbito()).child(model.getId());
        spesaRef.removeValue();
    }

    /**
     * Apre l'attività per aggiungere una nuova spesa.
     */
    private void aggiungiSpesa() {
        Intent intent = new Intent(this, AggiungiSpesaActivity.class);
        startActivity(intent);
        finish();
    }
}
