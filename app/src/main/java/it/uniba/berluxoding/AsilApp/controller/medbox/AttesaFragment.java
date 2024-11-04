package it.uniba.berluxoding.AsilApp.controller.medbox;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.berluxoding.AsilApp.interfacce.OnDataReceived;
import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.profilo.dettagli.DettagliMisurazioneActivity;


/**
 * Il frammento {@code AttesaFragment} gestisce l'attesa per ricevere una risposta
 * dal database Firebase e passa alla schermata dei dettagli della misurazione.
 */
public class AttesaFragment extends Fragment implements OnDataReceived<String> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflazione del layout per questo frammento
        return inflater.inflate(R.layout.fragment_attesa, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        // Riferimento al nodo del database per ricevere la risposta
        DatabaseReference path = FirebaseDatabase.getInstance().getReference("medbox/risposta");

        // Mostra il progresso dell'attesa
        progressBar.setVisibility(View.VISIBLE);

        setListener(path);
    }

    /**
     * Imposta un listener sul percorso del database fornito per ricevere
     * la risposta relativa alla misurazione.
     *
     * @param listenerPath Il percorso del database su cui impostare il listener.
     */
    private void setListener(DatabaseReference listenerPath) {
        // Listener per ricevere la risposta
        listenerPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String misurazioneId = dataSnapshot.child("misurazioneId").getValue(String.class);

                    Log.d("Firebase", "Risposta ricevuta: risultato=" + misurazioneId);

                    listenerPath.removeEventListener(this);

                    // Elimina la risposta dopo averla letta
                    listenerPath.removeValue()
                            .addOnSuccessListener(aVoid -> Log.d("Firebase", "Risposta eliminata con successo."))
                            .addOnFailureListener(e -> Log.e("Firebase", "Errore nell'eliminazione della risposta: " + e.getMessage()));

                    onDataReceived(misurazioneId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Errore nel recupero della risposta: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Metodo per ricevere il misurazioneId dal database e avviare
     * una nuova attività con i dettagli della misurazione.
     *
     * @param misurazioneId L'ID della misurazione ricevuto dal database.
     */
    @Override
    public void onDataReceived(String misurazioneId) {
        // Passaggio alla nuova Activity con il misurazioneId
        Intent intent = new Intent(getContext(), DettagliMisurazioneActivity.class);
        intent.putExtra("misurazioneId", misurazioneId);
        startActivity(intent);
    }
}
