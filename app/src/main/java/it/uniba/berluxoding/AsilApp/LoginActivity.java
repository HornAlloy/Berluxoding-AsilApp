package it.uniba.berluxoding.AsilApp;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.berluxoding.AsilApp.model.Utente;
import it.uniba.berluxoding.AsilApp.models.User;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ProgressBar pr;
    private Button bt1;
    private Button bt2;

    private  EditText et1;
    private  EditText et2;

    private final String TAG = "LOGIN_ACTIVITY";


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

        bt1 = (Button) findViewById(R.id.buttonSignIn);
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the SignIn button");
                signIn();
            }
        });

        bt2 = (Button) findViewById(R.id.buttonSignUp);
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the SignUp button");
                signUp();
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
       /* if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }*/
    }

    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }

      //  showProgressBar();
        String email = et1.getText().toString();
        String password = et2.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                       // hideProgressBar();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(getApplicationContext(), "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

     //   showProgressBar();
        String email = et1.getText().toString();
        String password = et2.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                      //  hideProgressBar();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.signup_failed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainFragment
       // NavHostFragment.findNavController(this).navigate(R.id.action_SignInFragment_to_MainFragment);

        if (checkRegistration(user.getUid())) {
            Intent openPage = new Intent(LoginActivity.this, RegistrationActivity.class);
            // passo all'attivazione dell'activity page1.java
            startActivity(openPage);
        }
        else {
            Intent openPage = new Intent(LoginActivity.this, HomeActivity.class);
            // passo all'attivazione dell'activity page1.java
            startActivity(openPage);
        }

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(et1.getText().toString())) {
            et1.setError("Required");
            result = false;
        } else {
            et1.setError(null);
        }

        if (TextUtils.isEmpty(et2.getText().toString())) {
            et2.setError("Required");
            result = false;
        } else {
            et2.setError(null);
        }

        return result;
    }

    private boolean checkRegistration (String userId) {
        final boolean[] value = {true};
        // Aggiungi un listener per recuperare il valore del nodo
        mDatabase.child("AsilApp").child("Utenti").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            boolean returnValue = true;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Object valore = snapshot.getValue();
                    if (valore != null) {
                        returnValue = false;
                    }
                }
                value[0] = returnValue;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Errore durante la verifica: " + error.getMessage());
            }
        });
        return value[0];
    }

    private void writeNewUser(String userId, String name, String email) {
        Utente utente = new Utente(name, email);

        mDatabase.child("users").child(userId).setValue(utente);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonSignIn) {
            signIn();
        } else if (i == R.id.buttonSignUp) {
            signUp();
        }
    }


}