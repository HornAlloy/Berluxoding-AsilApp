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

/**
 * Activity che visualizza un video di YouTube relativo ai tipi di utenti.
 */
public class UserActivity extends AppCompatActivity {

    // ID del video di YouTube
    private static final String USER_VIDEO_ID = "A_05YMRASzU"; // ID video di YouTube

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Abilita EdgeToEdge per un'esperienza utente a schermo intero
        EdgeToEdge.enable(this);
        // Imposta il layout associato a questa activity
        setContentView(R.layout.activity_user);

        // Gestisce gli insets delle finestre per supportare schermi a tutto schermo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inizializzazione della WebView
        WebView tipiUtentiWebview = findViewById(R.id.tipi_utenti_webview);

        // Configura il WebView per mostrare il contenuto di YouTube
        tipiUtentiWebview.getSettings().setJavaScriptEnabled(true);
        tipiUtentiWebview.setWebViewClient(new WebViewClient());

        // Carica l'URL del video di YouTube
        tipiUtentiWebview.loadUrl("https://www.youtube.com/embed/" + USER_VIDEO_ID + "?autoplay=1&vq=small");
    }
}
