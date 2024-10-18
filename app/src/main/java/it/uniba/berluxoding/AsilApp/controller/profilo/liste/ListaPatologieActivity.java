package it.uniba.berluxoding.AsilApp.controller.profilo.liste;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.profilo.dettagli.DettagliPatologiaActivity;
import it.uniba.berluxoding.AsilApp.model.Patologia;
import it.uniba.berluxoding.AsilApp.controller.profilo.viewholder.PatologiaViewHolder;


public class ListaPatologieActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, userRef;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
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

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("AsilApp").child(getUid());

        RecyclerView mRecycler = findViewById(R.id.listaPatologie);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Configura il LayoutManager
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Ottieni la query dal database Firebase
        Query patologiaQuery = getQuery(mDatabase);

        // Configura FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Patologia> options = new FirebaseRecyclerOptions.Builder<Patologia>()
                .setQuery(patologiaQuery, Patologia.class)
                .build();

//        btn = findViewById(R.id.btnAggiungi);
//        btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.d("BUTTONS", "User tapped the Add button");
//                aggiungiPatologia();
//            }
//        });

        // Imposta l'adapter
        mAdapter = new FirebaseRecyclerAdapter<Patologia, PatologiaViewHolder>(options) {
            @Override
            public PatologiaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PatologiaViewHolder(inflater.inflate(R.layout.item_patologia, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(PatologiaViewHolder viewHolder, int position, final Patologia model) {
                final DatabaseReference patologiaRef = getRef(position);
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
                viewHolder.bindToPatologia(model, new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        mostraDettagli(model);
                    }

//                }, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        aggiorna(model);
//                    }
//                }, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        elimina(model);
//                    }
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
        Query mPatologieQuery = queryReference.child("patologie").orderByKey();

        return mPatologieQuery;
    }

    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    private void mostraDettagli(Patologia model) {
        // Mostra i dettagli della patologia
        Intent intent = new Intent(this, DettagliPatologiaActivity.class);
        intent.putExtra("patologiaId", model.getNome());
        startActivity(intent);
    }

//    private void aggiorna(Patologia model) {
//        // Logica per aggiornare la spesa (ad esempio, apri un dialogo di modifica)
//        Intent intent = new Intent(this, ModificaPatologiaActivity.class);
//        intent.putExtra("patologiaId", model.getNome());
//        startActivity(intent);
//    }

//    private void elimina(Patologia model) {
//        // Elimina la spesa dal database Firebase
//        DatabaseReference patologiaRef = mDatabase.child("patologie").child(getUid()).child(model.getNome());
//        patologiaRef.removeValue();
//    }

//    private void aggiungiSpesa() {
//        Intent intent = new Intent(this, AggiungiPatologiaActivity.class);
//        startActivity(intent);
//    }


}