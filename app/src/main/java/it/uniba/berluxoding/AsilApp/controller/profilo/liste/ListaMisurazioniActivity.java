package it.uniba.berluxoding.AsilApp.controller.profilo.liste;

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

import it.uniba.berluxoding.AsilApp.controller.medbox.MedboxActivity;
import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.profilo.dettagli.DettagliMisurazioneActivity;
import it.uniba.berluxoding.AsilApp.model.Misurazione;
import it.uniba.berluxoding.AsilApp.controller.profilo.viewholder.MisurazioneViewHolder;

public class ListaMisurazioniActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, userRef;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Misurazione, MisurazioneViewHolder> mAdapter;
    private Button btn;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_misurazioni);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("AsilApp").child(getUid());

        mRecycler = findViewById(R.id.listaMisurazioni);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Query misurazioneQuery = getQuery(userRef);

        FirebaseRecyclerOptions<Misurazione> options = new FirebaseRecyclerOptions.Builder<Misurazione>()
                .setQuery(misurazioneQuery, Misurazione.class)
                .build();

        btn = findViewById(R.id.btnAggiungi);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the Add button");
                aggiungiMisurazione();
            }
        });

        mAdapter = new FirebaseRecyclerAdapter<Misurazione, MisurazioneViewHolder>(options) {
            @Override
            public MisurazioneViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MisurazioneViewHolder(inflater.inflate(R.layout.item_misurazione, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(MisurazioneViewHolder viewHolder, int position, final Misurazione model) {
                final DatabaseReference misurazioneRef = getRef(position);
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
                viewHolder.bindToMisurazione(model, new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        mostraDettagli(model);
                    }

                });
            }
        };

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

    private String getUid() {
        // Restituisce l'UID dell'utente attualmente loggato
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public Query getQuery(DatabaseReference queryReference) {
        // My top posts by number of stars
        Query mSpeseQuery = queryReference.child("misurazioni").orderByChild("data");

        return mSpeseQuery;
    }

    private void mostraDettagli(Misurazione model) {
        // Mostra i dettagli della spesa (ad esempio, apri una nuova activity o un frammento)
        Intent intent = new Intent(this, DettagliMisurazioneActivity.class);
        intent.putExtra("misurazioneId", model.getId());
        startActivity(intent);
    }

    private void aggiungiMisurazione() {
        Intent intent = new Intent(this, MedboxActivity.class);
        startActivity(intent);
    }
}