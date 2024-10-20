package it.uniba.berluxoding.AsilApp.controller.profilo.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.model.Patologia;

public class PatologiaViewHolder extends RecyclerView.ViewHolder {

    public TextView nomeView;
    public ImageButton dettagliView;


    public PatologiaViewHolder(View itemView) {
        super(itemView);

        nomeView = itemView.findViewById(R.id.textView);
        dettagliView = itemView.findViewById(R.id.imageButton);
    }

    public void bindToPatologia(Patologia patologia, View.OnClickListener dettagliListener) {
        nomeView.setText(patologia.getNome());

        dettagliView.setOnClickListener(dettagliListener);
    }
}
