package de.lingen.hsosna.texview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<Artikel> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextArtikelNr;
        public TextView mTextArtikelBez;
        public TextView mTextFarbeID;
        public TextView mTextFarbeBez;
        public TextView mTextGroessenID;
        public TextView mTextFertZust;
        public TextView mTextMenge;
        public TextView mTextMengenEinheit;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextArtikelNr = itemView.findViewById(R.id.text_artikelNr);
            mTextArtikelBez = itemView.findViewById(R.id.text_artikelBez);
            mTextFarbeID = itemView.findViewById(R.id.text_farbID);
            mTextFarbeBez = itemView.findViewById(R.id.text_farbBez);
            mTextGroessenID = itemView.findViewById(R.id.text_groesse);
            mTextFertZust = itemView.findViewById(R.id.text_fertigungszustand);
            mTextMenge = itemView.findViewById(R.id.text_menge);
            mTextMengenEinheit = itemView.findViewById(R.id.text_mengenEinheit);
        }
    }

    public ExampleAdapter (ArrayList<Artikel> exampleList){
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Artikel currentItem = mExampleList.get(position);

        holder.mTextArtikelNr.setText(currentItem.getArtikelID());
        holder.mTextArtikelBez.setText(currentItem.getArtikel_Bezeichnung());
        holder.mTextFarbeID.setText(currentItem.getFarbe_ID());
        holder.mTextFarbeBez.setText(currentItem.getFarbe_Bezeichnung());
        holder.mTextGroessenID.setText(currentItem.getGroessen_ID());
        holder.mTextFertZust.setText(currentItem.getFertigungszustand());
        holder.mTextMenge.setText(currentItem.getMenge());
        holder.mTextMengenEinheit.setText(currentItem.getMengenEinheit());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
