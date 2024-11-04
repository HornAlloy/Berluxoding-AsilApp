package it.uniba.berluxoding.AsilApp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.model.Utente;

/**
 * Classe per la gestione dell'attività di registrazione dell'utente. Permette di inserire i dati personali
 * e salvarli nel database Firebase.
 */
public class RegistrationActivity extends AppCompatActivity {

    /** Riferimento al database Firebase per la gestione dei dati dell'utente. */
    private DatabaseReference mDatabase;

    /** Istanza di FirebaseAuth per l'autenticazione. */
    private FirebaseAuth mAuth;

    /** Campo di input per il nome. */
    private EditText et1;

    /** Campo di input per il cognome. */
    private EditText et2;

    /** Campo di input per il paese di provenienza. */
    private EditText et3;

    /** Campo di input per il codice Medbox. */
    private EditText et4;

    /** Campo di input per il giorno di nascita. */
    private EditText etg;

    /** Campo di input per il mese di nascita. */
    private EditText etm;

    /** Campo di input per l'anno di nascita. */
    private EditText eta;

    /** Variabile per memorizzare l'ultimo tempo di pressione del tasto indietro. */
    private long backPressedTime;

    /** Toast per avvisare l'utente di premere di nuovo per uscire. */
    private Toast backToast;

    /**
     * Metodo chiamato quando l'attività viene creata. Configura la modalità Edge-to-Edge,
     * imposta il layout e inizializza i componenti di autenticazione.
     *
     * @param savedInstanceState Il bundle che contiene lo stato salvato dell'attività, se presente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Abilita la modalità Edge-to-Edge per questa attività.
        EdgeToEdge.enable(this);

        // Imposta il layout principale dell'attività.
        setContentView(R.layout.activity_registration);

        // Configura le finestre dell'interfaccia utente con i margini di sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inizializza il database e l'autenticazione Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Collegamento alle view
        et1 = findViewById(R.id.editTextNome);
        et2 = findViewById(R.id.editTextCognome);
        et3 = findViewById(R.id.editTextPaeseDiProvenienza);
        et4 = findViewById(R.id.editMedboxCode);
        etg = findViewById(R.id.editTextGiorno);
        etm = findViewById(R.id.editTextMese);
        eta = findViewById(R.id.editTextAnno);

        Button bt1 = findViewById(R.id.buttonRegister);
        bt1.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the registration button");
            register();
        });

        // Configura la gestione della pressione del tasto indietro
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel();
                    finish();
                } else {
                    backToast = Toast.makeText(getBaseContext(), "Premi di nuovo per uscire", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        });
    }

    /**
     * Esegue la registrazione dell'utente e salva i suoi dati nel database Firebase.
     */
    private void register() {
        String TAG = "REGISTRATION_ACTIVITY";
        Log.d(TAG, "register");
        if (!validateForm()) {
            return;
        }

        String nome = et1.getText().toString();
        String cognome = et2.getText().toString();
        String luogoProvenienza = et3.getText().toString();
        String medboxCode = et4.getText().toString();
        String dataNascita = etg.getText().toString() + "/" + etm.getText().toString() + "/" + eta.getText().toString();

        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setLuogoProvenienza(luogoProvenienza);
        utente.setDataNascita(dataNascita);
        utente.setPin(medboxCode);

        mDatabase.child("AsilApp").child(getUserId()).child("anagrafica").setValue(utente);

        Intent openPage = new Intent(RegistrationActivity.this, HomeActivity.class);
        startActivity(openPage);
        finish();
    }

    /**
     * Ottiene l'ID dell'utente attualmente autenticato.
     *
     * @return L'ID dell'utente autenticato.
     */
    private String getUserId() {
        return Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    }

    /**
     * Verifica la validità dei campi di input.
     *
     * @return true se tutti i campi sono validi, altrimenti false.
     */
    private boolean validateForm() {
        boolean result = true;

        result &= validateEt(et1);
        result &= validateEt(et2);
        result &= validateEt(et3);
        result &= validateEt(et4);
        result &= validateEt(etg);
        result &= validateEt(etm);
        result &= validateEt(eta);

        return result;
    }

    /**
     * Verifica se un campo di testo è vuoto e, in tal caso, imposta un messaggio di errore.
     *
     * @param et Il campo di testo da validare.
     * @return true se il campo non è vuoto, false altrimenti.
     */
    private boolean validateEt(EditText et) {
        if (TextUtils.isEmpty(et.getText().toString())) {
            et.setError("Required");
            return false;
        } else {
            et.setError(null);
            return true;
        }
    }


}
