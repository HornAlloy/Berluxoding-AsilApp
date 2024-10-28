package it.uniba.berluxoding.AsilApp.controller.profilo.aggiunta;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.profilo.liste.ListaSpeseActivity;
import it.uniba.berluxoding.AsilApp.model.Spesa;

public class AggiungiSpesaActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, userRef;
    private EditText etArticolo, etCosto, anno, mese, giorno, ora, minuto;
    private Spinner spTipologia;

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aggiungi_spesa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("AsilApp").child(getUser());

        //etAmbito = findViewById(R.id.editTextAmbito);
        spTipologia = findViewById(R.id.spTipologia);
        etArticolo = findViewById(R.id.editTextArticolo);
        etCosto = findViewById(R.id.editTextCosto);
        giorno = findViewById(R.id.editTextGiorno);
        mese = findViewById(R.id.editTextMese);
        anno = findViewById(R.id.editTextAnno);
        ora = findViewById(R.id.editTextOra);
        minuto = findViewById(R.id.editTextMinuto);

        ArrayAdapter<CharSequence> tipologiaAdapter = ArrayAdapter.createFromResource(this,
                R.array.tipologia_array, android.R.layout.simple_spinner_item);
        tipologiaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipologia.setAdapter(tipologiaAdapter);

        Button salva = findViewById(R.id.buttonSave);
        salva.setOnClickListener(v -> {
            Log.d("BUTTONS", "User tapped the Save button");
            save();

        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel();
                    terminate();
                } else {
                    backToast = Toast.makeText(getBaseContext(), "Premi di nuovo per tornare alla lista", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        });
    }

    private void terminate() {
        Intent intent = new Intent(this, ListaSpeseActivity.class);
        startActivity(intent);
        finish();
    }

    public void save() {
        String TAG = "AGGIUNGI_SPESA_ACTIVITY";
        Log.d(TAG, "save");
        if (!validateForm()) {
            return;
        }
        String ambito = spTipologia.getSelectedItem().toString();
        String articolo = etArticolo.getText().toString();
        String costo = etCosto.getText().toString();
        String orario = ora.getText().toString() + ":" + minuto.getText().toString();
        String dataSpesa = giorno.getText().toString() + "/" + mese.getText().toString() + "/" +
                anno.getText().toString();

        Spesa spesa = new Spesa();
        spesa.setAmbito(ambito);
        spesa.setArticolo(articolo);
        spesa.setCosto(costo);
        spesa.setData(dataSpesa);
        spesa.setOrario(orario);

        String spesaKey = userRef.child("spese").push().getKey();
        spesa.setId(spesaKey);

        Map<String, Object> spesaValues = spesa.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/AsilApp/" + getUser() + "/spese/" + spesaKey, spesaValues);
        childUpdates.put("/AsilApp/" + getUser() + "/spese-ambito/" + spesa.getAmbito() + "/" + spesaKey, spesaValues);
        mDatabase.updateChildren(childUpdates);
        Log.d(TAG, "spesa salvata");
        Intent openPage = new Intent(AggiungiSpesaActivity.this, ListaSpeseActivity.class);
        // passo all'attivazione dell'activity page1.java
        startActivity(openPage);
        finish();
    }

    private String getUser () {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etArticolo.getText().toString())) {
            etArticolo.setError("Required");
            result = false;
        } else {
            etArticolo.setError(null);
        }
        if (TextUtils.isEmpty(etCosto.getText().toString())) {
            etCosto.setError("Required");
            result = false;
        } else {
            etCosto.setError(null);
        }

        if (TextUtils.isEmpty(anno.getText().toString())) {
            anno.setError("Required");
            result = false;
        } else {
            anno.setError(null);
        }
        if (TextUtils.isEmpty(mese.getText().toString())) {
            mese.setError("Required");
            result = false;
        } else {
            mese.setError(null);
        }

        if (TextUtils.isEmpty(giorno.getText().toString())) {
            giorno.setError("Required");
            result = false;
        } else {
            giorno.setError(null);
        }
        if (TextUtils.isEmpty(ora.getText().toString())) {
            ora.setError("Required");
            result = false;
        } else {
            ora.setError(null);
        }

        if (TextUtils.isEmpty(minuto.getText().toString())) {
            minuto.setError("Required");
            result = false;
        } else {
            minuto.setError(null);
        }
        return result;
    }
}