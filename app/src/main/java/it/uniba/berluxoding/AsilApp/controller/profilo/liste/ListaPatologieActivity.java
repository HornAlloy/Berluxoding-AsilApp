package it.uniba.berluxoding.AsilApp.controller.profilo.liste;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
import it.uniba.berluxoding.AsilApp.controller.profilo.dettagli.DettagliPatologiaActivity;
import it.uniba.berluxoding.AsilApp.model.Patologia;
import it.uniba.berluxoding.AsilApp.controller.profilo.viewholder.PatologiaViewHolder;


/**
 * La classe {@code ListaPatologieActivity} estende {@code AppCompatActivity} e rappresenta
 * l'attività che visualizza una lista di patologie. Utilizza un adapter Firebase per
 * gestire e visualizzare i dati delle patologie in un RecyclerView.
 */
public class ListaPatologieActivity extends AppCompatActivity {
    private final String TAG = "LISTA_PATOLOGIE_ACTIVITY";

    private FirebaseRecyclerAdapter<Patologia, PatologiaViewHolder> mAdapter;
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
        setContentView(R.layout.activity_lista_patologie);

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
     * utilizzando un adapter Firebase per recuperare i dati delle patologie.
     *
     * @param patologiaQuery La query per recuperare i dati delle patologie dal database.
     */
    private void creaListaElementi(Query patologiaQuery) {
        RecyclerView mRecycler = findViewById(R.id.listaPatologie);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Configura il LayoutManager
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Configura FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Patologia> options = new FirebaseRecyclerOptions.Builder<Patologia>()
                .setQuery(patologiaQuery, Patologia.class)
                .build();

        // Imposta l'adapter
        mAdapter = new FirebaseRecyclerAdapter<>(options) {
            @NonNull
            @Override
            public PatologiaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PatologiaViewHolder(inflater.inflate(R.layout.item_patologia, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull PatologiaViewHolder viewHolder, int position, @NonNull final Patologia model) {
                getRef(position);

                // Associa il modello al ViewHolder
                viewHolder.bindToPatologia(model, v -> mostraDettagli(model));
            }
        };

        // Collega l'adapter al RecyclerView
        mRecycler.setAdapter(mAdapter);
        Log.d(TAG, "View created!");
    }

    /**
     * Questo metodo viene chiamato quando l'attività viene avviata.
     * Qui si inizializza la query per recuperare i dati delle patologie
     * e si avvia l'ascolto delle modifiche nel database.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Ottieni la query dal database Firebase
        Query patologiaQuery = getQuery(userRef);
        creaListaElementi(patologiaQuery);

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
     * Restituisce la query per recuperare le patologie dal database.
     *
     * @param queryReference Riferimento al nodo del database da cui eseguire la query.
     * @return La query per le patologie ordinate per chiave.
     */
    public Query getQuery(DatabaseReference queryReference) {
        return queryReference.child("patologie").orderByKey();
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
     * Mostra i dettagli di una patologia specifica in una nuova attività.
     *
     * @param model Il modello della patologia da visualizzare.
     */
    private void mostraDettagli(Patologia model) {
        // Mostra i dettagli della patologia
        Intent intent = new Intent(this, DettagliPatologiaActivity.class);
        intent.putExtra("patologiaId", model.getNome());
        Log.d(TAG, "patologiaId = " + model.getNome());
        startActivity(intent);
    }
}
