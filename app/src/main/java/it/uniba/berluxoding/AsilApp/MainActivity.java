/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.uniba.berluxoding.AsilApp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import it.uniba.berluxoding.AsilApp.controller.LoginActivity;

/**
 * Classe principale dell'applicazione che gestisce l'avvio e l'inizializzazione dell'interfaccia utente.
 * Estende {@link AppCompatActivity} e implementa il metodo {@link #onCreate(Bundle)} per gestire
 * il ciclo di vita dell'attività.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Metodo chiamato all'avvio dell'attività. Configura l'interfaccia utente in modalità Edge-to-Edge,
     * imposta il layout principale e avvia l'activity di login dopo un ritardo di 2 secondi.
     *
     * @param savedInstanceState Il bundle che contiene lo stato salvato dell'attività, se presente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Abilita la modalità Edge-to-Edge per questa attività.
        EdgeToEdge.enable(this);

        // Imposta il layout principale dell'attività.
        setContentView(R.layout.activity_main);

        // Avvia l'activity LoginActivity dopo un ritardo di 2 secondi.
        new Handler().postDelayed(() -> {

            // Crea un'intenzione per aprire l'activity LoginActivity
            Intent openPage = new Intent(MainActivity.this, LoginActivity.class);

            // Avvia l'activity LoginActivity
            startActivity(openPage);

            // Termina l'attuale activity MainActivity
            finish();
        }, 2000);
    }
}

