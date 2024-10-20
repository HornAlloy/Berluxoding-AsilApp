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


public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private  EditText et1;
    private  EditText et2;

    private final String TAG = "LOGIN_ACTIVITY";

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
     //setProgressBar(R.id.progressBar);

        et1 = findViewById(R.id.fieldEmail);
     //   String value1=et1.getText().toString();

        et2 = findViewById(R.id.fieldPassword);
      //  String value2 =et2.getText().toString();

        Button bt1 = findViewById(R.id.buttonSignIn);
        Button bt2 = findViewById(R.id.buttonSignUp);
        Button btnBackdoor = findViewById(R.id.backdoor);

        bt1.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the SignIn button");
            signIn();
        });


        bt2.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the SignUp button");
            signUp();
        });

        btnBackdoor.setOnClickListener(v -> authentication ("username@abcd.com", "password"));

        // Inizializza il dispatcher per onBackPressed
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


    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
//       if (mAuth.getCurrentUser() != null) {
//            onAuthSuccess(mAuth.getCurrentUser());
//        }
    }

    private void signIn() {
        Log.d(TAG, "signIn");
        if (validateForm()) {
            return;
        }

      //  showProgressBar();
        String email = et1.getText().toString();
        String password = et2.getText().toString();

        authentication(email, password);
    }

    private void authentication (String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                    // hideProgressBar();

                    if (task.isSuccessful()) {
                        onAuthSuccess(Objects.requireNonNull(task.getResult().getUser()));
                    } else {
                        Toast.makeText(getApplicationContext(), "Sign In Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (validateForm()) {
            return;
        }

     //   showProgressBar();
        String email = et1.getText().toString();
        String password = et2.getText().toString();

        authentication(email, password);
    }

    private void onAuthSuccess(FirebaseUser user) {

        // Write new user
        writeNewUser(user.getUid());

        checkRegistration(user.getUid()).addOnSuccessListener(isRegistered -> {
            Intent openPage;
            if (isRegistered) {
                // L'utente è registrato, apri l'activity RegistrationActivity
                openPage = new Intent(LoginActivity.this, HomeActivity.class);
            } else {
                // L'utente non è registrato, apri l'activity HomeActivity
                openPage = new Intent(LoginActivity.this, RegistrationActivity.class);
            }
            // Avvia l'activity appropriata
            startActivity(openPage);
            finish();
        }).addOnFailureListener(e -> {
            // Gestisci eventuali errori che possono verificarsi durante il controllo della registrazione
            Toast.makeText(LoginActivity.this, "Errore durante la verifica della registrazione: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });

    }

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

    public Task<Boolean> checkRegistration(String userId) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("AsilApp").child(userId).child("anagrafica")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getValue() != null) {
                            taskCompletionSource.setResult(true); // Utente registrato
                        } else {
                            taskCompletionSource.setResult(false); // Utente non registrato
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        taskCompletionSource.setException(error.toException()); // Errore
                    }
                });

        return taskCompletionSource.getTask();
    }

    private void writeNewUser(String userId) {
        Utente utente = new Utente();

        mDatabase.child("users").child(userId).setValue(utente);
    }
}