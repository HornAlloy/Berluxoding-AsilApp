package it.uniba.berluxoding.AsilApp.controller.informazioni;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.informazioni.altriDocumentiEValutazione.EvaluationsActivity;
import it.uniba.berluxoding.AsilApp.controller.informazioni.altriDocumentiEValutazione.OtherDocumentsActivity; // Import dell'activity di destinazione

public class OtherActivity extends AppCompatActivity {

    // L'ID del video è la parte dopo ?v=, quindi in questo caso, https://www.youtube.com/watch?v=Op3hkJND21Q, è Op3hkJND21Q.
    private static final String HEALTHY_LIFESTYLE_VIDEO_ID = "Y8HIFRPU6pM"; // ID video di YouTube

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        EdgeToEdge.enable(this);
        // Imposta il layout della tua activity
        setContentView(R.layout.activity_other);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // Inizializzazione dei componenti della UI
        WebView stileVitaYoutubeWebview = findViewById(R.id.stile_vita_youtube_webview);
        Button btnAltriDoc = findViewById(R.id.btnAltriDoc);
        Button btnValutazioni = findViewById(R.id.btnValutazioni);

        // Configura il WebView per mostrare il contenuto di YouTube
        stileVitaYoutubeWebview.getSettings().setJavaScriptEnabled(true);
        stileVitaYoutubeWebview.setWebViewClient(new WebViewClient());
        // Carica l'URL del video di YouTube
        stileVitaYoutubeWebview.loadUrl("https://www.youtube.com/embed/" + HEALTHY_LIFESTYLE_VIDEO_ID + "?autoplay=1&vq=small");

        // Gestione click bottone "Altri documenti e riferimenti utili"
        btnAltriDoc.setOnClickListener(view -> {
            // Intent per passare a OtherDocumentsActivity
            Intent intent = new Intent(OtherActivity.this, OtherDocumentsActivity.class);
            startActivity(intent);
        });

        // Gestione click bottone "Valutazioni"
        btnValutazioni.setOnClickListener(view -> {
            Intent intent = new Intent(OtherActivity.this, EvaluationsActivity.class);
            startActivity(intent);
        });
    }
}
