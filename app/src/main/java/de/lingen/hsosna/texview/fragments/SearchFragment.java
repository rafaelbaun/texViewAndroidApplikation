package de.lingen.hsosna.texview.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Objects;

import de.lingen.hsosna.texview.Article;
import de.lingen.hsosna.texview.ArticleAdapter;
import de.lingen.hsosna.texview.DatabaseHelper;
import de.lingen.hsosna.texview.Lagerplatz;
import de.lingen.hsosna.texview.R;
import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;

import static android.view.KeyEvent.KEYCODE_ENTER;
import static de.lingen.hsosna.texview.MainActivity.hideKeyboardFrom;

public class SearchFragment extends Fragment {
    private SearchFragmentListener listener;
    private Button button;

    private EditText editArtikelNr;
    private EditText editArtikelBez;
    private EditText editStuecknummer;
    private EditText editStueckteilung;
    private EditText editFardId;
    private EditText editFarbBez;
    private EditText editGroesse;
    private EditText editFertigungszustand;
    private AlertDialog alertDialog;



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


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.requireContext(), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Suchanfrage fehlgeschlagen");
        alertDialogBuilder.setMessage("Bitte füllen Sie mindestens ein Feld aus!");
        alertDialogBuilder.setPositiveButton("OK", null);
        alertDialog = alertDialogBuilder.create();


        //Belegung der Attribute
        editArtikelNr = v.findViewById(R.id.searchFragment_editText_articleId);
        editArtikelBez = v.findViewById(R.id.searchFragment_editText_articleShortDesc);
        editStuecknummer = v.findViewById(R.id.searchFragment_editText_pieceId);
        editStueckteilung = v.findViewById(R.id.searchFragment_editText_pieceDivision);
        editFardId = v.findViewById(R.id.searchFragment_editText_colorId);
        editFarbBez = v.findViewById(R.id.searchFragment_editText_colorDescription);
        editGroesse = v.findViewById(R.id.searchFragment_editText_size);
        editFertigungszustand = v.findViewById(R.id.searchFragment_editText_manufacturingState);

        editArtikelNr.setOnEditorActionListener(onEditorActionListener);
        editArtikelBez.setOnEditorActionListener(onEditorActionListener);
        editStuecknummer.setOnEditorActionListener(onEditorActionListener);
        editStueckteilung.setOnEditorActionListener(onEditorActionListener);
        editFardId.setOnEditorActionListener(onEditorActionListener);
        editFarbBez.setOnEditorActionListener(onEditorActionListener);
        editGroesse.setOnEditorActionListener(onEditorActionListener);
        editFertigungszustand.setOnEditorActionListener(onEditorActionListener);

        button = v.findViewById(R.id.searchFragment_button_submit);
        //DB CON
        Context context = getActivity();
        dbHelper = new DatabaseHelper(context);
        mDatabase = dbHelper.getReadableDatabase();


        //SUCHERGEBN
        mRecyclerView = v.findViewById(R.id.searchFragment_recyclerView_searchResult);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //slide up pane
        View bottomSheet = v.findViewById(R.id.searchFragment_searchResultSlideUpPane);
        mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);


        mSuchergebnisse = v.findViewById(R.id.searchFragment_searchResultHeader);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                performSearch();

            }
        });
        return v;
    }

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KEYCODE_ENTER){
                performSearch();
                return true;
            }
            return false;
        }
    };

    public void performSearch(){
        hideKeyboardFrom(getContext(), getView());
        String SqlWhereQuery = getSqlWhereQuery();
        if(SqlWhereQuery.length() != 0) {
            final ArrayList<Article> suchErgebnisse = getListWithSearchResults(SqlWhereQuery);

            switch (suchErgebnisse.size()){
                case 1:
                    mSuchergebnisse.setText((String.valueOf(suchErgebnisse.size())).concat(" Suchergebnis"));
                    break;
                case 201:
                    mSuchergebnisse.setText((String.valueOf(suchErgebnisse.size()-1)).concat("+ Suchergebnisse"));
                    break;
                default:
                    mSuchergebnisse.setText((String.valueOf(suchErgebnisse.size())).concat(" Suchergebnisse"));
                    break;
            }
            mAdapter = new ArticleAdapter(suchErgebnisse);// LIST WITH CONTENTS
            mAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
                @Override
                public void onItemClick (int position) {
                }

                @Override
                public void onLocationMarkerClick (int position) {
                    Lagerplatz checkedLagerplatz = new Lagerplatz(60, suchErgebnisse.get(position).getLagerplatz(), suchErgebnisse.get(position).getRegalfach());
                    listener.onSearchInputSent(checkedLagerplatz);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            alertDialog.show();
        }
    }

    @Override
    public void onAttach (@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragmentListener) {
            listener = (SearchFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement Search Fragment Listener");
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
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG + ", "
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
                + SqlWhereQuery + " LIMIT 201 "
                , null);
        try {
            while (cursor.moveToNext()) {
                int artikelId = cursor.getInt(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID));
                int stuecknummer = cursor.getInt(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER));
                int stueckteilung = cursor.getInt(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG));
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
                Article article = new Article(artikelId, stuecknummer, stueckteilung, artikelBez, farbId, farbBez, groessenId,
                        fertigungszustand, menge, mengeneinheit, lagerplatz);
                articleList.add(article);
            }
        } finally {
            cursor.close();
        }
        return articleList;
    }

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
        if(editArtikelBez.getText().toString().trim().length() != 0 && editArtikelBez.getText().toString().trim().matches("[a-zA-ZäöüÄÖÜ0-9 /*-]+")){
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
        //-------STUECKNUMMER
        if(editStuecknummer.getText().toString().trim().length() != 0 && editStuecknummer.getText().toString().trim().matches("[0-9]+")){
            if (hasQuery) {
                SqlQuery.append(" AND ");
            }
            int stuecknummer = Integer.parseInt(editStuecknummer.getText().toString().trim());
            SqlQuery.append(TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER)
                    .append(" LIKE '")
                    .append(stuecknummer)
                    .append("%'");
            hasQuery = true;
        }
        //-------STUECKTEILUNG
        if(editStueckteilung.getText().toString().trim().length() != 0 && editStueckteilung.getText().toString().trim().matches("[0-9]+")){
            if (hasQuery) {
                SqlQuery.append(" AND ");
            }
            int stueckteilung = Integer.parseInt(editStueckteilung.getText().toString().trim());
            SqlQuery.append(TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG)
                    .append(" LIKE '")
                    .append(stueckteilung)
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
        if(editFarbBez.getText().toString().trim().length() != 0 && editFarbBez.getText().toString().trim().matches("[a-zA-ZäöüÄÖÜ0-9 *-,]+")){
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
