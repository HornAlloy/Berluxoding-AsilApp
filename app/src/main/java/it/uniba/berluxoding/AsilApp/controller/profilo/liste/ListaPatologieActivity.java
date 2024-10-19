package it.uniba.berluxoding.AsilApp.controller.profilo.liste;

import android.content.Intent;
import android.os.Bundle;

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


public class ListaPatologieActivity extends AppCompatActivity {

    private FirebaseRecyclerAdapter<Patologia, PatologiaViewHolder> mAdapter;
//    private Button btn;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_patologie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child("AsilApp").child(getUid());

        RecyclerView mRecycler = findViewById(R.id.listaPatologie);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Configura il LayoutManager
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Ottieni la query dal database Firebase
        Query patologiaQuery = getQuery(userRef);

        // Configura FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Patologia> options = new FirebaseRecyclerOptions.Builder<Patologia>()
                .setQuery(patologiaQuery, Patologia.class)
                .build();

        // Imposta l'adapter
        mAdapter = new FirebaseRecyclerAdapter<>(options) {
            @NonNull
            @Override
            public PatologiaViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PatologiaViewHolder(inflater.inflate(R.layout.item_patologia, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder (@NonNull PatologiaViewHolder viewHolder, int position, @NonNull final Patologia model) {
                getRef(position);

                viewHolder.bindToPatologia(model, v -> mostraDettagli(model));
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

    public Query getQuery(DatabaseReference queryReference) {

        return queryReference.child("patologie").orderByKey();
    }

    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    private void mostraDettagli(Patologia model) {
        // Mostra i dettagli della patologia
        Intent intent = new Intent(this, DettagliPatologiaActivity.class);
        intent.putExtra("patologiaId", model.getNome());
        startActivity(intent);
    }
}