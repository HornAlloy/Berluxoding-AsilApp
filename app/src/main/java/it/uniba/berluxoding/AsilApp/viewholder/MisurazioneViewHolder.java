package it.uniba.berluxoding.AsilApp.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import it.uniba.berluxoding.AsilApp.R;
import it.uniba.berluxoding.AsilApp.model.Misurazione;

public class MisurazioneViewHolder extends RecyclerView.ViewHolder {
    public TextView nomeView;
    public TextView dataView;
    public TextView oraView;
    public Button dettagliBtn;

    public MisurazioneViewHolder(View itemView) {
        super(itemView);

        nomeView = itemView.findViewById(R.id.textView);
        dataView = itemView.findViewById(R.id.textView2);
        oraView = itemView.findViewById(R.id.textView3);
        dettagliBtn = itemView.findViewById(R.id.btnVedi);
    }

    public void bindToMisurazione(Misurazione misurazione, View.OnClickListener dettagliListener) {
        nomeView.setText(misurazione.getStrumento());
        dataView.setText(misurazione.getData());
        oraView.setText(misurazione.getOrario());

        dettagliBtn.setOnClickListener(dettagliListener);
    }
}
