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
    private String selectedTipologia; // Variabile per tenere traccia della tipologia selezionata

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

        // Inizializza lo Spinner
        spTipologia = findViewById(R.id.spAmbito); // Assicurati che l'ID sia corretto
        setupSpinner();

        Button btn = findViewById(R.id.btnAggiungi);
        btn.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the Add button");
            aggiungiSpesa();
        });
    }

    private void setupSpinner() {
        // Crea un array di tipologie di spesa
        String[] tipologie = {"Tipologia 1", "Tipologia 2", "Tipologia 3"}; // Sostituisci con le tue tipologie

        // Imposta l'adapter per lo Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipologie);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipologia.setAdapter(adapter);

        // Gestisce il cambiamento di selezione nello Spinner
        spTipologia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTipologia = (String) parent.getItemAtPosition(position);
                Log.d("SPINNER", "Selected: " + selectedTipologia);
                updateListBasedOnSelection(); // Aggiorna la lista in base alla selezione
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Non fare nulla
            }
        });
    }

    private void updateListBasedOnSelection() {
        // Ottieni la query basata sulla tipologia selezionata
        Query spesaQuery = getQuery(userRef, selectedTipologia);
        creaListaElementi(spesaQuery);
    }

    private void creaListaElementi(Query spesaQuery) {
        RecyclerView mRecycler = findViewById(R.id.listaSpese);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Configura FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Spesa> options = new FirebaseRecyclerOptions.Builder<Spesa>()
                .setQuery(spesaQuery, Spesa.class)
                .build();

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
                getRef(position);
                viewHolder.bindToSpesa(model, v -> mostraDettagli(model), v -> eliminaSpesa(model));
            }
        };

        // Collega l'adapter al RecyclerView
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Inizia l'ascolto dei dati Firebase
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Ferma l'ascolto dei dati Firebase
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public Query getQuery(DatabaseReference queryReference, String tipologia) {
        // Query per ottenere le spese in base alla tipologia selezionata
        return queryReference.child("spese").orderByChild("tipologia").equalTo(tipologia);
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
        Intent intent = new Intent(this, AggiungiSpesaActivity.class);
        startActivity(intent);
    }
}
