package it.uniba.berluxoding.AsilApp.controller.medbox;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import it.uniba.berluxoding.AsilApp.interfacce.OnDataReceived;
import it.uniba.berluxoding.AsilApp.R;

public class MedboxActivity extends AppCompatActivity {

    private final String TAG = "MEDBOXAPP_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medbox);

        // Inizializzazione del database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mStrumenti = mDatabase.child("medbox").child("strumenti");

        // Recupero dei dati per ottenere i nomi degli strumenti
        getStrumentiData(mStrumenti, this::aggiornaStrumentiUI);
    }

    // Metodo che recupera i dati e invoca il callback una volta che i dati sono pronti
    private void getStrumentiData(DatabaseReference ref, final OnDataReceived<String[]> callback) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String[] strumentiArray = new String[3];
                Map<String, String> strumenti = (Map<String, String>) dataSnapshot.getValue();
                if (strumenti != null) {
                    strumentiArray[0] = strumenti.get("strumento1");
                    strumentiArray[1] = strumenti.get("strumento2");
                    strumentiArray[2] = strumenti.get("strumento3");
                }

                // Restituisci i dati attraverso il callback
                callback.onDataReceived(strumentiArray);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Errore nel leggere i dati: " + databaseError.getMessage());
            }
        });
    }

    // Metodo per aggiornare la UI con i nomi degli strumenti e impostare i listener
    private void aggiornaStrumentiUI(String[] strumentiArray) {
        Button strumento1 = findViewById(R.id.button);
        Button strumento2 = findViewById(R.id.button2);
        Button strumento3 = findViewById(R.id.button3);

        strumento1.setText(strumentiArray[0]);
        strumento2.setText(strumentiArray[1]);
        strumento3.setText(strumentiArray[2]);

        View.OnClickListener listener = v -> {
            Button clickedButton = (Button) v;
            String strumentoName = clickedButton.getText().toString();
            Log.d(TAG, strumentoName + " button pressed!");

            // Crea un bundle e salvaci lo strumento selezionato
            Bundle bundle = new Bundle();
            bundle.putString("strumento", strumentoName);

            // Passa al fragment di inserimento PIN
            replaceFragment(new PinFragment(), bundle);
        };

        strumento1.setOnClickListener(listener);
        strumento2.setOnClickListener(listener);
        strumento3.setOnClickListener(listener);
    }

    // Metodo per sostituire i fragment
    protected void replaceFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
//    protected void onCreate (Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_medbox);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        medboxRef = mDatabase.child("medbox");
//        mStrumenti = medboxRef.child("strumenti");
//
//        // Chiamata per ottenere i dati con callback
//        getStrumentiData(mStrumento, new OnDataReceived<String[]>() {
//            @Override
//            public void onDataReceived(String[] strumentiArray) {
//                // Aggiungere qui il codice per aggiornare la UI, come ad esempio aggiornare un TextView
//                strumento1.setText(strumentiArray[0]);
//                strumento2.setText(strumentiArray[1]);
//                strumento3.setText(strumentiArray[2]);
//                strumento1.setOnClickListener(v -> {
//                    Log.d(TAG,"strumento1 button pressed!");
//                    mStrumento = medboxRef.child(strumentiArray[0]);
//                    //toast con il pin --> qua
//                    //serve impostare un listener nella posizione mStrumento in attesa della risposta
//                    //del medbox app (per quando termina) restituendo l'id e salvandolo in misurazioneId
//                    //toast per l'attesa --> qua
//                    //va distrutto il contenuto dello strumento e il listener
////                    Intent intent = new Intent(MedboxActivity.this, ListaMisurazioniActivity.class);
////                    intent.putExtra("misurazioneId", misurazione.getId()); //va sistemato, bisogna studiare bene la risposta del db
////                    startActivity(intent);
//                });
//                strumento2.setOnClickListener(v -> {
//                    Log.d(TAG,"strumento1 button pressed!");
//                    mStrumento = medboxRef.child(strumentiArray[1]);
//                    //toast con il pin --> qua
//                    //serve impostare un listener nella posizione mStrumento in attesa della risposta
//                    //del medbox app (per quando termina) restituendo l'id e salvandolo in misurazioneId
//                    //toast per l'attesa --> qua
//                    //va distrutto il contenuto dello strumento e il listener
////                    Intent intent = new Intent(MedboxActivity.this, ListaMisurazioniActivity.class);
////                    intent.putExtra("misurazioneId", misurazione.getId()); //va sistemato, bisogna studiare bene la risposta del db
////                    startActivity(intent);
//                });
//                strumento3.setOnClickListener(v -> {
//                    Log.d(TAG,"strumento1 button pressed!");
//                    mStrumento = medboxRef.child(strumentiArray[2]);
//                    //toast con il pin --> qua
//                    //serve impostare un listener nella posizione mStrumento in attesa della risposta
//                    //del medbox app (per quando termina) restituendo l'id e salvandolo in misurazioneId
//                    //toast per l'attesa --> qua
//                    //va distrutto il contenuto dello strumento e il listener
////                    Intent intent = new Intent(MedboxActivity.this, ListaMisurazioniActivity.class);
////                    intent.putExtra("misurazioneId", misurazione.getId()); //va sistemato, bisogna studiare bene la risposta del db
////                    startActivity(intent);
//                });
//            }
//        });
//
//    }
//
//    // Metodo che recupera i dati e invoca il callback una volta che i dati sono pronti
//    private void getStrumentiData(DatabaseReference ref, final FirebaseCallback firebaseCallback) {
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Definire il vettore di stringhe (array) di 3 elementi
//                String[] strumentiArray = new String[3];
//
//                // Ottieni i dati come una mappa
//                Map<String, String> strumenti = (Map<String, String>) dataSnapshot.getValue();
//
//                // Salva i dati nel vettore
//                strumentiArray[0] = strumenti.get("strumento1");
//                strumentiArray[1] = strumenti.get("strumento2");
//                strumentiArray[2] = strumenti.get("strumento3");
//
//                // Usa il callback per restituire i dati
//                firebaseCallback.onCallback(strumentiArray);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Gestisci eventuali errori
//                System.out.println("Errore nel leggere i dati: " + databaseError.getMessage());
//            }
//        });
//    }
//
//    // Metodo per recuperare il misurazioneId dal database
//    private void fetchMisurazioneId() {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("misurazioni");
//        ValueEventListener singleUpdateListener1 = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Leggi la stringa misurazioneId
//                String misurazioneId = dataSnapshot.getValue(String.class);
//                Log.d("Firebase", "Misurazione 1 ID: " + misurazioneId);
//
//                // Rimuovi il listener
//                ref.removeEventListener(this);
//
//                // Passa il misurazioneId al listener (la stessa Activity)
//                if (misurazioneId != null) {
//                    onDataReceived(misurazioneId);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("Firebase", "Error reading data: " + databaseError.getMessage());
//            }
//        };
//
//        // Aggiungi il listener
//        ref.addValueEventListener(singleUpdateListener1);
//    }
//
//    @Override
//    public void onDataReceived(String misurazioneId) {
//        Intent intent = new Intent(MedboxActivity.this, DettagliMisurazioneActivity.class);
//        intent.putExtra("misurazioneId", misurazioneId);
//        startActivity(intent);
//    }
//}