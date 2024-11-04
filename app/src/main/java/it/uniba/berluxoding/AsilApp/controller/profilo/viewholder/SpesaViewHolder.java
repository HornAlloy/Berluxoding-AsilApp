package it.uniba.berluxoding.AsilApp.controller.profilo.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.model.Spesa;

/**
 * La classe {@code SpesaViewHolder} estende {@code RecyclerView.ViewHolder}
 * e rappresenta un elemento della lista per visualizzare i dettagli di una spesa.
 * Contiene riferimenti a viste per mostrare il nome dell'articolo, la data della spesa,
 * e pulsanti per visualizzare dettagli e cancellare la spesa.
 */
public class SpesaViewHolder extends RecyclerView.ViewHolder {

    public TextView articoloView;
    public TextView dataView;
    public Button dettagliView;
    public Button cancellaView;

    /**
     * Costruttore della classe {@code SpesaViewHolder}.
     * Inizializza i riferimenti alle viste contenute nell'elemento della lista.
     *
     * @param itemView La vista dell'elemento della lista.
     */
    public SpesaViewHolder(View itemView) {
        super(itemView);

        articoloView = itemView.findViewById(R.id.textView);
        dataView = itemView.findViewById(R.id.textView2);
        dettagliView = itemView.findViewById(R.id.btnVedi);
        cancellaView = itemView.findViewById(R.id.btnCancella);
    }

    /**
     * Collega i dati della spesa alle viste dell'elemento della lista.
     *
     * @param spesa La spesa da visualizzare.
     * @param dettagliListener Listener per il pulsante dei dettagli.
     * @param cancellaListener Listener per il pulsante di cancellazione.
     */
    public void bindToSpesa(Spesa spesa, View.OnClickListener dettagliListener, View.OnClickListener cancellaListener) {
        articoloView.setText(spesa.getArticolo());
        dataView.setText(spesa.getData());

        dettagliView.setOnClickListener(dettagliListener);
        cancellaView.setOnClickListener(cancellaListener);
    }
}


