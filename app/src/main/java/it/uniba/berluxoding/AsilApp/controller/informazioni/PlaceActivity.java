package it.uniba.berluxoding.AsilApp.controller.informazioni;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.berluxoding.AsilApp.R;

public class PlaceActivity extends AppCompatActivity {


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

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
