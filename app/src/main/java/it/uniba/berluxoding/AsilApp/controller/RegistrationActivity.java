package it.uniba.berluxoding.AsilApp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.model.Utente;

public class RegistrationActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private final String TAG = "REGISTRATION_ACTIVITY";

    private Button bt1;

    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText etg;
    private EditText etm;
    private EditText eta;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        et1 = findViewById(R.id.editTextNome);
        et2 = findViewById(R.id.editTextCognome);
        et3 = findViewById(R.id.editTextPaeseDiProvenienza);
        et4 = findViewById(R.id.editMedboxCode);
        etg = findViewById(R.id.editTextGiorno);
        etm = findViewById(R.id.editTextMese);
        eta = findViewById(R.id.editTextAnno);

        bt1 = (Button) findViewById(R.id.buttonRegister);
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the registration button");
                register();
            }
        });

    }

    private void register() {
        Log.d(TAG, "register");
        if (!validateForm()) {
            return;
        }

        String nome = et1.getText().toString();
        String cognome = et2.getText().toString();
        String luogoProvenienza = et3.getText().toString();
        String medboxCode = et4.getText().toString();
        String dataNascita = etg.getText().toString() + "/" + etm.getText().toString() + "/" +
                eta.getText().toString();

        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setLuogoProvenienza(luogoProvenienza);
        utente.setDataNascita(dataNascita);
        utente.setPin(medboxCode);

        mDatabase.child("AsilApp").child(mAuth.getCurrentUser().getUid()).child("anagrafica").setValue(utente);
        Intent openPage = new Intent(RegistrationActivity.this, HomeActivity.class);
        // passo all'attivazione dell'activity page1.java
        startActivity(openPage);
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
        if (TextUtils.isEmpty(et1.getText().toString())) {
            et3.setError("Required");
            result = false;
        } else {
            et3.setError(null);
        }

        if (TextUtils.isEmpty(et2.getText().toString())) {
            et4.setError("Required");
            result = false;
        } else {
            et4.setError(null);
        }
        if (TextUtils.isEmpty(et1.getText().toString())) {
            etg.setError("Required");
            result = false;
        } else {
            etg.setError(null);
        }

        if (TextUtils.isEmpty(et2.getText().toString())) {
            etm.setError("Required");
            result = false;
        } else {
            etm.setError(null);
        }
        if (TextUtils.isEmpty(et1.getText().toString())) {
            eta.setError("Required");
            result = false;
        } else {
            eta.setError(null);
        }

        return result;
    }
}