package it.uniba.berluxoding.AsilApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import it.uniba.berluxoding.AsilApp.model.Spesa;
import it.uniba.berluxoding.AsilApp.viewholder.SpesaViewHolder;

public class ListaSpeseActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, userRef;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Spesa, SpesaViewHolder> mAdapter;
    private Button btn;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_spese);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("AsilApp").child(getUid());

        mRecycler = findViewById(R.id.listaSpese);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Configura il LayoutManager
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Ottieni la query dal database Firebase
        Query spesaQuery = getQuery(userRef);

        // Configura FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Spesa> options = new FirebaseRecyclerOptions.Builder<Spesa>()
                .setQuery(spesaQuery, Spesa.class)
                .build();

        btn = findViewById(R.id.btnAggiungi);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the Add button");
                aggiungiSpesa();
            }
        });

        // Imposta l'adapter
        mAdapter = new FirebaseRecyclerAdapter<Spesa, SpesaViewHolder>(options) {
            @Override
            public SpesaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new SpesaViewHolder(inflater.inflate(R.layout.item_spesa, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(SpesaViewHolder viewHolder, int position, final Spesa model) {
                final DatabaseReference spesaRef = getRef(position);
//                final String uid = model.getId();

                // Click listener per il post
//                final String spesaKey = spesaRef.getKey();

//                // Gestisce il like del post
//                if (model.stars.containsKey(getUid())) {
//                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
//                } else {
//                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
//                }

                // Bind del post al ViewHolder
                viewHolder.bindToSpesa(model, new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        mostraDettagli(model);
                    }

                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aggiornaSpesa(model);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eliminaSpesa(model);
                    }
//                    @Override
//                    public void onClick(View starView) {
//                        DatabaseReference globalPostRef = mDatabase.child("posts").child(spesaRef.getKey());
//                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(uid).child(spesaRef.getKey());

                        // Aggiorna i like in entrambe le posizioni
//                        onStarClicked(globalPostRef);
//                        onStarClicked(userPostRef);
//                    }
                });
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
        // My top posts by number of stars
        Query mSpeseQuery = queryReference.child("spese").orderByChild("data");

        return mSpeseQuery;
    }

    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    private void mostraDettagli(Spesa model) {
        // Mostra i dettagli della spesa (ad esempio, apri una nuova activity o un frammento)
        Intent intent = new Intent(this, DettagliSpesaActivity.class);
        intent.putExtra("spesaId", model.getId());
        startActivity(intent);
    }

    private void aggiornaSpesa(Spesa model) {
        // Logica per aggiornare la spesa (ad esempio, apri un dialogo di modifica)
        Intent intent = new Intent(this, ModificaSpesaActivity.class);
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