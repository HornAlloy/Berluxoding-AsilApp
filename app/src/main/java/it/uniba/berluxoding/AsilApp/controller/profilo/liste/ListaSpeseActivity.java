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

import java.util.Locale;
import java.util.Objects;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.profilo.aggiunta.AggiungiSpesaActivity;
import it.uniba.berluxoding.AsilApp.controller.profilo.dettagli.DettagliSpesaActivity;
import it.uniba.berluxoding.AsilApp.model.Spesa;
import it.uniba.berluxoding.AsilApp.controller.profilo.viewholder.SpesaViewHolder;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_spese);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
                // Quando viene selezionato un elemento nello spinner, aggiorna la query
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

    // Metodo per aggiornare la query e ricaricare i dati quando lo spinner cambia
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

        //setTotaleSpese();
        // Ricrea l'adapter e collega il RecyclerView
        creaListaElementi(options);
    }

    // Metodo per creare la lista degli elementi nel RecyclerView
    private void creaListaElementi(FirebaseRecyclerOptions<Spesa> options) {
        // Imposta l'adapter
        mAdapter = new FirebaseRecyclerAdapter<>(options) {
            @NonNull
            @Override
            public SpesaViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new SpesaViewHolder(inflater.inflate(R.layout.item_spesa, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder (@NonNull SpesaViewHolder viewHolder, int position, @NonNull final Spesa model) {
                viewHolder.bindToSpesa(model, v -> mostraDettagli(model), v -> eliminaSpesa(model));
                Log.d(TAG, "Binding avvenuto!");
            }
        };

        // Collega l'adapter al RecyclerView e avvia l'ascolto
        mRecycler.setAdapter(mAdapter);
        mAdapter.startListening();
    }

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
                tvTotaleSpese.setText(String.format(Locale.getDefault(),"%.2f", totaleSpese));
                Log.d(TAG, "Totale spese calcolato: " + totaleSpese);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Errore nel recupero delle spese: " + databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Inizia con la query predefinita quando l'activity viene avviata
        Query spesaQuery = getQuery(userRef);
        options = new FirebaseRecyclerOptions.Builder<Spesa>()
                .setQuery(spesaQuery, Spesa.class)
                .build();

        //setTotaleSpese();
        creaListaElementi(options);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Ferma l'ascolto dei dati Firebase quando l'activity viene fermata
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    // Metodo per ottenere la query da Firebase in base alla selezione dello spinner
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

    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    private void mostraDettagli(Spesa model) {
        // Mostra i dettagli della spesa
        Intent intent = new Intent(this, DettagliSpesaActivity.class);
        intent.putExtra("spesaId", model.getId());
        startActivity(intent);
    }

    private void eliminaSpesa(Spesa model) {
        // Elimina la spesa dal database Firebase
        DatabaseReference spesaRef = userRef.child("spese").child(model.getId());
        spesaRef.removeValue();
        spesaRef = userRef.child("spese-ambito").child(model.getAmbito()).child(model.getId());
        spesaRef.removeValue();
    }

    private void aggiungiSpesa() {
        // Apre l'activity per aggiungere una nuova spesa
        Intent intent = new Intent(this, AggiungiSpesaActivity.class);
        startActivity(intent);
        finish();
    }
}
