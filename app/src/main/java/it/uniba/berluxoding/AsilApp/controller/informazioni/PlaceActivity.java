package it.uniba.berluxoding.AsilApp.controller.informazioni;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import it.uniba.berluxoding.AsilApp.R;

public class PlaceActivity extends AppCompatActivity {


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Trova la WebView nel layout
        WebView wvMaps = findViewById(R.id.wvMaps);

        // Abilita JavaScript nella WebView
        WebSettings webSettingsMaps = wvMaps.getSettings();
        webSettingsMaps.setJavaScriptEnabled(true); // Abilita JavaScript per la mappa

        // Evita l'apertura del browser esterno
        wvMaps.setWebViewClient(new WebViewClient());

        // Carica l'URL di Google Maps per mostrare i centri di accoglienza
        wvMaps.loadUrl("https://www.google.com/maps/search/centri+accoglienza+migranti");
    }
}
