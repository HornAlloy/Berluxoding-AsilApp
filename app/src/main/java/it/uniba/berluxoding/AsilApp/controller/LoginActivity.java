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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.model.Utente;


/**
 * Classe per la gestione dell'attività di login dell'applicazione. Consente agli utenti di effettuare
 * l'accesso o la registrazione utilizzando Firebase Authentication.
 */
public class LoginActivity extends AppCompatActivity {

    /** Riferimento al database Firebase per la gestione dei dati dell'utente. */
    private DatabaseReference mDatabase;

    /** Istanza di FirebaseAuth per l'autenticazione. */
    private FirebaseAuth mAuth;

    /** Campo di input per l'indirizzo email. */
    private EditText et1;

    /** Campo di input per la password. */
    private EditText et2;

    /** Tag per i log dell'attività di login. */
    private final String TAG = "LOGIN_ACTIVITY";

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
        setContentView(R.layout.activity_login);

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
        et1 = findViewById(R.id.fieldEmail);
        et2 = findViewById(R.id.fieldPassword);

        Button bt1 = findViewById(R.id.buttonSignIn);
        Button bt2 = findViewById(R.id.buttonSignUp);
        Button btnBackdoor = findViewById(R.id.backdoor);

        // Gestione evento clic sul pulsante SignIn
        bt1.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the SignIn button");
            signIn();
        });

        // Gestione evento clic sul pulsante SignUp
        bt2.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the SignUp button");
            signUp();
        });

        // Backdoor per accedere automaticamente con dati di esempio
        btnBackdoor.setOnClickListener(v -> authentication("username@abcd.com", "password"));

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
     * Metodo chiamato all'avvio dell'attività per verificare se l'utente è già autenticato.
     */
    @Override
    public void onStart() {
        super.onStart();
        // Verifica l'autenticazione utente se già presente
    }

    /**
     * Esegue l'autenticazione dell'utente con le credenziali inserite.
     */
    private void signIn() {
        Log.d(TAG, "signIn");
        if (validateForm()) {
            return;
        }

        String email = et1.getText().toString();
        String password = et2.getText().toString();

        authentication(email, password);
    }

    /**
     * Esegue l'autenticazione Firebase con le credenziali fornite.
     *
     * @param email L'email dell'utente.
     * @param password La password dell'utente.
     */
    private void authentication(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());

                    if (task.isSuccessful()) {
                        onAuthSuccess(Objects.requireNonNull(task.getResult().getUser()));
                    } else {
                        Toast.makeText(getApplicationContext(), "Sign In Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Esegue la registrazione dell'utente con le credenziali inserite.
     */
    private void signUp() {
        Log.d(TAG, "signUp");
        if (validateForm()) {
            return;
        }

        String email = et1.getText().toString();
        String password = et2.getText().toString();

        authentication(email, password);
    }

    /**
     * Metodo chiamato al successo dell'autenticazione per gestire la registrazione o l'accesso.
     *
     * @param user L'utente autenticato.
     */
    private void onAuthSuccess(FirebaseUser user) {
        // Scrive i dati del nuovo utente
        writeNewUser(user.getUid());

        // Controlla se l'utente è già registrato e avvia l'activity appropriata
        checkRegistration(user.getUid()).addOnSuccessListener(isRegistered -> {
            Intent openPage;
            if (isRegistered) {
                openPage = new Intent(LoginActivity.this, HomeActivity.class);
            } else {
                openPage = new Intent(LoginActivity.this, RegistrationActivity.class);
            }
            startActivity(openPage);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(LoginActivity.this, "Errore durante la verifica della registrazione: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    /**
     * Verifica la validità dei campi di input.
     *
     * @return true se uno dei campi non è valido, altrimenti false.
     */
    private boolean validateForm() {
        boolean result = false;
        if (TextUtils.isEmpty(et1.getText().toString())) {
            et1.setError("Required");
            result = true;
        } else {
            et1.setError(null);
        }

        if (TextUtils.isEmpty(et2.getText().toString())) {
            et2.setError("Required");
            result = true;
        } else {
            et2.setError(null);
        }

        return result;
    }

    /**
     * Verifica se l'utente è registrato.
     *
     * @param userId L'ID dell'utente da verificare.
     * @return Un Task<Boolean> che indica se l'utente è registrato o meno.
     */
    public Task<Boolean> checkRegistration(String userId) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("AsilApp").child(userId).child("anagrafica")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getValue() != null) {
                            taskCompletionSource.setResult(true);
                        } else {
                            taskCompletionSource.setResult(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        taskCompletionSource.setException(error.toException());
                    }
                });

        return taskCompletionSource.getTask();
    }

    /**
     * Scrive i dati del nuovo utente nel database.
     *
     * @param userId L'ID dell'utente da registrare.
     */
    private void writeNewUser(String userId) {
        Utente utente = new Utente();
        mDatabase.child("users").child(userId).setValue(utente);
    }
}
