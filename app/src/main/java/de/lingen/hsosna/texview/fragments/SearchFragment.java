package de.lingen.hsosna.texview.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import de.lingen.hsosna.texview.Article;
import de.lingen.hsosna.texview.ArticleAdapter;
import de.lingen.hsosna.texview.DatabaseHelper;
import de.lingen.hsosna.texview.Lagerplatz;
import de.lingen.hsosna.texview.R;
import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;

public class SearchFragment extends Fragment {
    private SearchFragmentListener listener;
    private EditText editText;
    private EditText editText2;
    private Button button;

    private EditText editArtikelNr;
    private EditText editArtikelBez;
    private EditText editFardId;
    private EditText editFarbBez;
    private EditText editGroesse;
    private EditText editFertigungszustand;


    private DatabaseHelper dbHelper;
    private SQLiteDatabase mDatabase;

    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BottomSheetBehavior mBottomSheetBehaviour;

    private TextView mSuchergebnisse;

    /**
     * Interface um Daten an die MainActivity zu senden.
     */
    public interface SearchFragmentListener {
        void onSearchInputSent (Lagerplatz input);
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);


        //TEST
        editText = v.findViewById(R.id.editText);
        editText2 = v.findViewById(R.id.editText2);
        button = v.findViewById(R.id.submitButton);

        //Belegung der Attribute
        editArtikelNr = v.findViewById(R.id.editTextArtikelNr);
        editArtikelBez = v.findViewById(R.id.editTextArtikelkurzbez);
        editFardId = v.findViewById(R.id.editTextFarbID);
        editFarbBez = v.findViewById(R.id.editTextFarbbezeichnung);
        editGroesse = v.findViewById(R.id.editTextGroesse);
        editFertigungszustand = v.findViewById(R.id.editTextFertigungszustand);


        //DB CON
        Context context = getActivity();
        dbHelper = new DatabaseHelper(context);
        mDatabase = dbHelper.getReadableDatabase();


        //SUCHERGEBN
        mRecyclerView = v.findViewById(R.id.recyclerVgvhgview_fach01);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //slide up pane
        View bottomSheet = v.findViewById(R.id.slideUpjmvgmjhPaneFach01);
        mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);


        mSuchergebnisse = v.findViewById(R.id.textViewSuchergebnisse);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String SqlWhereQuery = getSqlWhereQuery();
                if(SqlWhereQuery.length() != 0) {
                    final ArrayList<Article> suchErgebnisse = getListWithSearchResults(SqlWhereQuery);


                    String suchAnzeige = " Suchergebnisse";
                    mSuchergebnisse.setText((String.valueOf(suchErgebnisse.size())).concat(suchAnzeige));


                    mAdapter = new ArticleAdapter(suchErgebnisse);// LIST WITH CONTENTS




                    mAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick (int position) {
                        }

                        @Override
                        public void onDeleteClick (int position) {
                            Lagerplatz checkedLagerplatz = new Lagerplatz(60, suchErgebnisse.get(position).getLagerplatz(), suchErgebnisse.get(position).getRegalfach());
                            listener.onSearchInputSent(checkedLagerplatz);
                        }
                    });




                    mRecyclerView.setAdapter(mAdapter);


                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    //TODO ERROR MESSAGE DISPLAY
                    CharSequence errorMessage = "Bitte füllen Sie mindestens ein Feld aus";
                    //listener.onSearchInputSent(errorMessage);
                }

                //listener.onSearchInputSent(input2);
            }
        });



        //mAdapter.
        return v;
    }

    @Override
    public void onAttach (@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragmentListener) {
            listener = (SearchFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach () {
        super.onDetach();
        listener = null;
    }




    public ArrayList<Article> getListWithSearchResults (String SqlWhereQuery) {
        ArrayList<Article> articleList = new ArrayList<Article>();

        Cursor cursor = mDatabase.rawQuery(
                "SELECT " + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FERTIGUNGSZUSTAND + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_MENGE + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_MENGENEINHEIT + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG
                + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN + ""
                + " FROM " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + ""
                + " LEFT JOIN " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME + ""
                + " ON " + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + " = "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID
                + " AND " + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID + " = "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID
                + " AND " + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID + " = "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID
                + SqlWhereQuery + " LIMIT 200 "
                , null);
        try {
            while (cursor.moveToNext()) {
                int artikelId = cursor.getInt(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID));
                int farbId = cursor.getInt(
                        cursor.getColumnIndex(TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID));
                String menge = cursor.getString(
                        cursor.getColumnIndex(TableLagerbestand.LagerbestandEntry.COLUMN_MENGE));
                String mengeneinheit = cursor.getString(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_MENGENEINHEIT));
                String fertigungszustand = cursor.getString(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_FERTIGUNGSZUSTAND));
                int groessenId = cursor.getInt(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID));
                String artikelBez = cursor.getString(cursor.getColumnIndex(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG));
                String farbBez = cursor.getString(cursor.getColumnIndex(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN));
                int lagerplatz = cursor.getInt(cursor.getColumnIndex(TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ));
                Article article = new Article(artikelId, artikelBez, farbId, farbBez, groessenId,
                        fertigungszustand, menge, mengeneinheit, lagerplatz);
                articleList.add(article);
            }
        } finally {
            cursor.close();
        }
        return articleList;
    }



    //TODO prevent sql injection
    public String getSqlWhereQuery(){
        boolean hasQuery = false;
        StringBuilder SqlQuery = new StringBuilder();
        SqlQuery.append(" WHERE ");
        //-------ARTIKEL NR
        if(editArtikelNr.getText().toString().trim().length() != 0 && editArtikelNr.getText().toString().trim().matches("[0-9]+")){
            int artikelNr = Integer.parseInt(editArtikelNr.getText().toString().trim());
            SqlQuery.append(TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID)
                    .append(" LIKE '")
                    .append(artikelNr)
                    .append("%'");
            hasQuery = true;
        }
        //-------ARTIKEL BEZ
        if(editArtikelBez.getText().toString().trim().length() != 0){
            if(hasQuery){
                SqlQuery.append(" AND ");
            }
            String artikelBez = editArtikelBez.getText().toString().trim();
            SqlQuery.append(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG)
                    .append(" LIKE '%")
                    .append(artikelBez)
                    .append("%'");
            hasQuery = true;
        }
        //-------FARB ID
        if(editFardId.getText().toString().trim().length() != 0 && editFardId.getText().toString().trim().matches("[0-9]+")) {
            if (hasQuery) {
                SqlQuery.append(" AND ");
            }
            int farbId = Integer.parseInt(editFardId.getText().toString().trim());
            SqlQuery.append(TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID)
                    .append(" LIKE '")
                    .append(farbId)
                    .append("%'");
            hasQuery = true;
        }
        //-------FARB BEZ
        if(editFarbBez.getText().toString().trim().length() != 0){
            if(hasQuery){
                SqlQuery.append(" AND ");
            }
            String farbBez = editFarbBez.getText().toString().trim();
            SqlQuery.append(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN)
                    .append(" LIKE '%")
                    .append(farbBez)
                    .append("%'");
            hasQuery = true;
        }
        //-------GROESSE ID
        if(editGroesse.getText().toString().trim().length() != 0 && editGroesse.getText().toString().trim().matches("[0-9]+")) {
            if (hasQuery) {
                SqlQuery.append(" AND ");
            }
            int groesse = Integer.parseInt(editGroesse.getText().toString().trim());
            SqlQuery.append(TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID)
                    .append(" LIKE '")
                    .append(groesse)
                    .append("%'");
            hasQuery = true;
        }
        //-------FERTIGUNGSZUSTAND
        if(editFertigungszustand.getText().toString().trim().length() != 0 && editFertigungszustand.getText().toString().matches("[a-zA-Z]+")){
            if(hasQuery){
                SqlQuery.append(" AND ");
            }
            String fertZstd = editFertigungszustand.getText().toString().trim();
            SqlQuery.append(TableLagerbestand.LagerbestandEntry.COLUMN_FERTIGUNGSZUSTAND)
                    .append(" LIKE '")
                    .append(fertZstd)
                    .append("%'");
            hasQuery = true;
        }
        if(hasQuery){
            return new String(SqlQuery);
        } else {
            return "";
        }
    }
}