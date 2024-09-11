package it.uniba.berluxoding.AsilApp.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.model.Spesa;

public class SpesaViewHolder extends RecyclerView.ViewHolder {

        public TextView ambitoView;
        public TextView dataView;
        public Button dettagliView;
        public Button aggiornaView;
        public Button cancellaView;

        public SpesaViewHolder(View itemView) {
            super(itemView);

            ambitoView = itemView.findViewById(R.id.textView);
            dataView = itemView.findViewById(R.id.textView2);
            dettagliView = itemView.findViewById(R.id.btnVedi);
            aggiornaView = itemView.findViewById(R.id.btnAggiorna);
            cancellaView = itemView.findViewById(R.id.btnCancella);
        }

        public void bindToSpesa(Spesa spesa, View.OnClickListener dettagliListener, View.OnClickListener aggiornaListener, View.OnClickListener cancellaListener) {
            ambitoView.setText(spesa.getAmbito());
            dataView.setText(spesa.getData());

            dettagliView.setOnClickListener(dettagliListener);
            aggiornaView.setOnClickListener(aggiornaListener);
            cancellaView.setOnClickListener(cancellaListener);
        }
}

