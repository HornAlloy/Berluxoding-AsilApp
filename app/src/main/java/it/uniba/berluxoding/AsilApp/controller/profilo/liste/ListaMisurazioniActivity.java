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
import java.util.Objects;

import it.uniba.berluxoding.AsilApp.controller.medbox.MedboxActivity;
import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.profilo.dettagli.DettagliMisurazioneActivity;
import it.uniba.berluxoding.AsilApp.model.Misurazione;
import it.uniba.berluxoding.AsilApp.controller.profilo.viewholder.MisurazioneViewHolder;

public class ListaMisurazioniActivity extends AppCompatActivity {

    private FirebaseRecyclerAdapter<Misurazione, MisurazioneViewHolder> mAdapter;

    private DatabaseReference userRef;

    //private List<Misurazione> misurazioniList = new ArrayList<>();

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

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("AsilApp").child(getUid());
    }

    private void creaListaElementi(Query misurazioneQuery) {
        RecyclerView mRecycler = findViewById(R.id.listaMisurazioni);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));

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
            public MisurazioneViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MisurazioneViewHolder(inflater.inflate(R.layout.item_misurazione, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder (@NonNull MisurazioneViewHolder viewHolder, int position, @NonNull final Misurazione model) {
                getRef(position);

                model.setData(convertDateFormat(model.getData()));
                // Bind del model al ViewHolder
                viewHolder.bindToMisurazione(model, v -> mostraDettagli(model));
                //misurazioniList.add(model);
            }
        };

        mRecycler.setAdapter(mAdapter);
    }

    private String convertDateFormat(String dateStr) {
        // Definire il formato di input e output
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");

        String formattedDate = null;
        try {
            // Parsing della data in formato yyyy/MM/dd
            Date date = inputFormat.parse(dateStr);
            // Formattazione della data in formato dd/MM/yyyy
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate; // Restituisce la data nel nuovo formato
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query misurazioneQuery = getQuery(userRef);

        creaListaElementi(misurazioneQuery);

        //ordinaMisurazioniPerDataEOra(misurazioniList);

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
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    public Query getQuery(DatabaseReference queryReference) {
        // My top posts by number of stars

        return queryReference.child("misurazioni").orderByChild("data");
    }

    private void mostraDettagli(Misurazione model) {
        // Mostra i dettagli della misurazione
        Intent intent = new Intent(this, DettagliMisurazioneActivity.class);
        intent.putExtra("misurazioneId", model.getId());
        startActivity(intent);
    }

    private void aggiungiMisurazione() {
        Intent intent = new Intent(this, MedboxActivity.class);
        startActivity(intent);
    }


//    // Formatter per convertire la stringa in LocalDate e LocalTime
//    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
//
//    // Metodo per ordinare una lista di misurazioni per data e orario (dalla più recente alla meno recente)
//    private void ordinaMisurazioniPerDataEOra(List<Misurazione> misurazioni) {
//        Collections.sort(misurazioni, (m1, m2) -> {
//            // Converte la data da stringa a LocalDate
//            LocalDate data1 = LocalDate.parse(m1.getData(), DATE_FORMATTER);
//            LocalDate data2 = LocalDate.parse(m2.getData(), DATE_FORMATTER);
//
//            // Confronta le date
//            int dataCompare = data2.compareTo(data1);
//
//            // Se le date sono uguali, confronta gli orari
//            if (dataCompare == 0) {
//                LocalTime orario1 = LocalTime.parse(m1.getOrario(), TIME_FORMATTER);
//                LocalTime orario2 = LocalTime.parse(m2.getOrario(), TIME_FORMATTER);
//                return orario2.compareTo(orario1); // ordine decrescente per l'orario
//            }
//
//            return dataCompare; // ordine decrescente per la data
//        });
//    }







}