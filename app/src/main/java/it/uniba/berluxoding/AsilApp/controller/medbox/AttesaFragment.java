package it.uniba.berluxoding.AsilApp.controller.medbox;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.berluxoding.AsilApp.interfacce.OnDataReceived;
import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.profilo.misurazioni.DettagliMisurazioneActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttesaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttesaFragment extends Fragment implements OnDataReceived<String> {

    private ProgressBar progressBar;
    private TextView waitingText;
    private DatabaseReference path;
    private String misurazioneId;

    public static AttesaFragment newInstance() {
        // Crea una nuova istanza di PinFragment
        AttesaFragment fragment = new AttesaFragment();

        // Se desideri passare dei dati, puoi farlo tramite un Bundle
        // Bundle args = new Bundle();
        // args.putString("key", value);
        // fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attesa, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        progressBar = view.findViewById(R.id.progressBar);
        waitingText = view.findViewById(R.id.waitingText);

        path = FirebaseDatabase.getInstance().getReference("medbox/risposta");

        // Mostra il progresso dell'attesa
        progressBar.setVisibility(View.VISIBLE);
        waitingText.setText("Attendere la risposta dal Medbox...");

        setListener(path);
    }

    private void setListener (DatabaseReference listenerPath) {
        // Listener per ricevere la risposta
        listenerPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    misurazioneId = dataSnapshot.child("misurazioneId").getValue(String.class);

                    Log.d("Firebase", "Risposta ricevuta: userId=" + ", risultato=" + misurazioneId);

                    listenerPath.removeEventListener(this);

                    // Elimina la risposta dopo averla letta
                    listenerPath.removeValue()
                            .addOnSuccessListener(aVoid -> Log.d("Firebase", "Risposta eliminata con successo."))
                            .addOnFailureListener(e -> Log.e("Firebase", "Errore nell'eliminazione della risposta: " + e.getMessage()));

                    onDataReceived(misurazioneId);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Errore nel recupero della risposta: " + databaseError.getMessage());
            }
        });


    }



    // Metodo per ricevere il misurazioneId dal database
    @Override
    public void onDataReceived(String misurazioneId) {
        // Passaggio alla nuova Activity con il misurazioneId
        Intent intent = new Intent(getContext(), DettagliMisurazioneActivity.class);
        intent.putExtra("misurazioneId", misurazioneId);
        startActivity(intent);
    }
}