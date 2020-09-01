package de.lingen.hsosna.texview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Um den Recyclerview zu füllen, wird der Adapter mit den TextView's des Layouts gefüllt und diesen
 * die zugehörigen Daten zugeordnet.
 */
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<Artikel> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick (int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    /**
     * In der ViewHolder Klasse werden die TextView's als Attribute gesetzt und mittels des Konstruktors
     * werden die TextViews aus dem Layout den Attributen zugeordnet.
     */
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextArtikelNr;
        public TextView mTextArtikelBez;
        public TextView mTextFarbeID;
        public TextView mTextFarbeBez;
        public TextView mTextGroessenID;
        public TextView mTextFertZust;
        public TextView mTextMenge;
        public TextView mTextMengenEinheit;
        public ImageView mLocationMarkerImage;

        /**
         * Die Klassenparameter werden mit den TextViews des Layouts initialisiert.
         *
         * @param itemView Layout eines einzelnen Artikels, das mittels des RecyclerView wiederverwendet wird.
         */
        public ExampleViewHolder (@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextArtikelNr = itemView.findViewById(R.id.text_artikelNr);
            mTextArtikelBez = itemView.findViewById(R.id.text_artikelBez);
            mTextFarbeID = itemView.findViewById(R.id.text_farbID);
            mTextFarbeBez = itemView.findViewById(R.id.text_farbBez);
            mTextGroessenID = itemView.findViewById(R.id.text_groesse);
            mTextFertZust = itemView.findViewById(R.id.text_fertigungszustand);
            mTextMenge = itemView.findViewById(R.id.text_menge);
            mTextMengenEinheit = itemView.findViewById(R.id.text_mengenEinheit);
            mLocationMarkerImage = itemView.findViewById(R.id.icon_locationMarker);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mLocationMarkerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        //int test = mExampleList.get(position).getArtikelID();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public ExampleAdapter (ArrayList<Artikel> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent,
                false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder (@NonNull ExampleViewHolder holder, int position) {
        Artikel currentItem = mExampleList.get(position);
        holder.mTextArtikelNr.setText(String.valueOf(currentItem.getArtikelID()));
        holder.mTextArtikelBez.setText(currentItem.getArtikel_Bezeichnung());
        holder.mTextFarbeID.setText(String.valueOf(currentItem.getFarbe_ID()));
        holder.mTextFarbeBez.setText(currentItem.getFarbe_Bezeichnung());
        holder.mTextGroessenID.setText(String.valueOf(currentItem.getGroessen_ID()));
        holder.mTextFertZust.setText(currentItem.getFertigungszustand());
        holder.mTextMenge.setText(currentItem.getMenge());
        holder.mTextMengenEinheit.setText(currentItem.getMengenEinheit());
    }

    @Override
    public int getItemCount () {
        return mExampleList.size();
    }

}
