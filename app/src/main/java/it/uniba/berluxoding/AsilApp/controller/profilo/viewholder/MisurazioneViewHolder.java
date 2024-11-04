package it.uniba.berluxoding.AsilApp.controller.profilo.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.model.Misurazione;

/**
 * La classe {@code MisurazioneViewHolder} estende {@code RecyclerView.ViewHolder}
 * e rappresenta un elemento della lista per visualizzare i dettagli di una misurazione.
 * Contiene riferimenti a viste per mostrare il nome dello strumento, la data,
 * l'ora e un pulsante per ulteriori dettagli.
 */
public class MisurazioneViewHolder extends RecyclerView.ViewHolder {
    public TextView nomeView;
    public TextView dataView;
    public TextView oraView;
    public Button dettagliBtn;

    /**
     * Costruttore della classe {@code MisurazioneViewHolder}.
     * Inizializza i riferimenti alle viste contenute nell'elemento della lista.
     *
     * @param itemView La vista dell'elemento della lista.
     */
    public MisurazioneViewHolder(View itemView) {
        super(itemView);

        nomeView = itemView.findViewById(R.id.textView);
        dataView = itemView.findViewById(R.id.textView2);
        oraView = itemView.findViewById(R.id.textView3);
        dettagliBtn = itemView.findViewById(R.id.btnDettagli);
    }

    /**
     * Collega i dati della misurazione alle viste dell'elemento della lista.
     *
     * @param misurazione La misurazione da visualizzare.
     * @param dettagliListener Listener per il pulsante dei dettagli.
     */
    public void bindToMisurazione(Misurazione misurazione, View.OnClickListener dettagliListener) {
        nomeView.setText(misurazione.getStrumento());
        dataView.setText(misurazione.getData());
        oraView.setText(misurazione.getOrario());

        dettagliBtn.setOnClickListener(dettagliListener);
    }
}
