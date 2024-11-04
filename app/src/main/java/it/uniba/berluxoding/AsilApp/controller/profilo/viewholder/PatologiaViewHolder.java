package it.uniba.berluxoding.AsilApp.controller.profilo.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.model.Patologia;

/**
 * La classe {@code PatologiaViewHolder} estende {@code RecyclerView.ViewHolder}
 * e rappresenta un elemento della lista per visualizzare i dettagli di una patologia.
 * Contiene riferimenti a viste per mostrare il nome della patologia e un pulsante
 * per ulteriori dettagli.
 */
public class PatologiaViewHolder extends RecyclerView.ViewHolder {

    public TextView nomeView;
    public ImageButton dettagliView;

    /**
     * Costruttore della classe {@code PatologiaViewHolder}.
     * Inizializza i riferimenti alle viste contenute nell'elemento della lista.
     *
     * @param itemView La vista dell'elemento della lista.
     */
    public PatologiaViewHolder(View itemView) {
        super(itemView);

        nomeView = itemView.findViewById(R.id.textView);
        dettagliView = itemView.findViewById(R.id.imageButton);
    }

    /**
     * Collega i dati della patologia alle viste dell'elemento della lista.
     *
     * @param patologia La patologia da visualizzare.
     * @param dettagliListener Listener per il pulsante dei dettagli.
     */
    public void bindToPatologia(Patologia patologia, View.OnClickListener dettagliListener) {
        nomeView.setText(patologia.getNome());

        dettagliView.setOnClickListener(dettagliListener);
    }
}
