package it.uniba.berluxoding.AsilApp.controller.informazioni;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import it.uniba.berluxoding.AsilApp.R;

public class UserActivity extends AppCompatActivity {

    // Riferimento al TextView

    // L'ID del video YouTube
    private static final String USER_VIDEO_ID = "A_05YMRASzU"; // ID video di YouTube

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
         // Assicurati che il nome del layout XML sia corretto

        // Inizializzazione dei componenti della UI
        // Riferimento alla WebView
        WebView tipiUtentiWebview = findViewById(R.id.tipi_utenti_webview);

        // Configura il WebView per mostrare il contenuto di YouTube
        tipiUtentiWebview.getSettings().setJavaScriptEnabled(true);
        tipiUtentiWebview.setWebViewClient(new WebViewClient());

        // Carica l'URL del video di YouTube
        tipiUtentiWebview.loadUrl("https://www.youtube.com/embed/" + USER_VIDEO_ID + "?autoplay=1&vq=small");
    }
}
