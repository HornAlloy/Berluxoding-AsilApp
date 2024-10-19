package it.uniba.berluxoding.AsilApp.controller.informazioni;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.controller.altriDocumentiEValutazione.OtherDocumentsActivity; // Import dell'activity di destinazione

public class OtherActivity extends AppCompatActivity {

    private TextView textViewStileDiVitaSalutare;
    private WebView stileVitaYoutubeWebview;
    private Button btnAltriDoc;
    private Button btnValutazioni;

    // L'ID del video è la parte dopo ?v=, quindi in questo caso, https://www.youtube.com/watch?v=Op3hkJND21Q, è Op3hkJND21Q.
    private static final String HEALTHY_LIFESTYLE_VIDEO_ID = "Y8HIFRPU6pM"; // ID video di YouTube

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Imposta il layout della tua activity
        setContentView(R.layout.activity_other);

        // Inizializzazione dei componenti della UI
        textViewStileDiVitaSalutare = findViewById(R.id.textViewStileDiVitaSalutare);
        stileVitaYoutubeWebview = findViewById(R.id.stile_vita_youtube_webview);
        btnAltriDoc = findViewById(R.id.btnAltriDoc);
        btnValutazioni = findViewById(R.id.btnValutazioni);

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
            // Codice per gestire il click del bottone 'Valutazioni'
            // In questo caso potresti voler implementare una nuova activity o funzione
        });
    }
}
