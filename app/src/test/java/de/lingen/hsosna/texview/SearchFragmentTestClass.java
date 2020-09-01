package de.lingen.hsosna.texview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;

public class SearchFragmentTestClass extends Fragment {
    private String editArtikelNr;
    private String editArtikelBez;
    private String editFardId;
    private String editFarbBez;
    private String editGroesse;
    private String editFertigungszustand;


    private Button button;
    private GroceryDBHelper dbHelper;
    private SQLiteDatabase mDatabase;

    public SearchFragmentTestClass (String editArtikelNr, String editArtikelBez,
                                    String editFardId, String editFarbBez, String editGroesse,
                                    String editFertigungszustand) {
        this.editArtikelNr = editArtikelNr;
        this.editArtikelBez = editArtikelBez;
        this.editFardId = editFardId;
        this.editFarbBez = editFarbBez;
        this.editGroesse = editGroesse;
        this.editFertigungszustand = editFertigungszustand;
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        //DB CON
        Context context = getActivity();
        dbHelper = new GroceryDBHelper(context);
        mDatabase = dbHelper.getReadableDatabase();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String SqlWhereQuery = getSqlWhereQuery();
                if(SqlWhereQuery.length() != 0) {
                    ArrayList<Artikel> suchErgebnisse = getListWithSearchResults(SqlWhereQuery);


                } else {
                    CharSequence errorMessage = "Bitte füllen Sie mindestens ein Feld aus";
                }
           }
        });
        return v;
    }



    public ArrayList<Artikel> getListWithSearchResults (String SqlWhereQuery) {
        ArrayList<Artikel> artikelListe = new ArrayList<Artikel>();
//        int lagerort = Integer.parseInt(regalID.subSequence(0, 2).toString());
//        int regal_nr = Integer.parseInt(regalID.subSequence(2, 4).toString());
//        int zeile = Integer.parseInt(regalID.subSequence(4, 6).toString());

        Cursor cursor = mDatabase.rawQuery(
                "SELECT " + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSE_ID + ", "
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
                + " AND " + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSE_ID + " = "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSE_ID
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
                        TableLagerbestand.LagerbestandEntry.COLUMN_GROESSE_ID));
                String artikelBez = cursor.getString(cursor.getColumnIndex(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG));
                String farbBez = cursor.getString(cursor.getColumnIndex(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN));
                Artikel artikel = new Artikel(artikelId, artikelBez, farbId, farbBez, groessenId,
                        fertigungszustand, menge, mengeneinheit);
                artikelListe.add(artikel);
            }
        } finally {
            cursor.close();
        }
        return artikelListe;
    }

    public String getSqlWhereQuery(){
        boolean hasQuery = false;
        StringBuilder SqlQuery = new StringBuilder();
        SqlQuery.append(" WHERE ");

        // mEditTextName.trim().length() == 0 || mAmount == 0

        //-------ARTIKEL NR
        if(editArtikelNr.trim().length() != 0 && editArtikelNr.trim().matches("[0-9]+")){
            int artikelNr = Integer.parseInt(editArtikelNr.trim());
            SqlQuery.append(TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID)
                    .append(" LIKE '")
                    .append(artikelNr)
                    .append("%'");
            hasQuery = true;
        }
        //-------ARTIKEL BEZ
        if(editArtikelBez.trim().length() != 0){
            if(hasQuery){
                SqlQuery.append(" AND ");
            }
            String artikelBez = editArtikelBez.trim();
            SqlQuery.append(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG)
                    .append(" LIKE '%")
                    .append(artikelBez)
                    .append("%'");
            hasQuery = true;
        }
        //-------FARB ID
        if(editFardId.trim().length() != 0 && editFardId.trim().matches("[0-9]+")) {
            if (hasQuery) {
                SqlQuery.append(" AND ");
            }
            int farbId = Integer.parseInt(editFardId.trim());
            SqlQuery.append(TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID)
                    .append(" LIKE '")
                    .append(farbId)
                    .append("%'");
            hasQuery = true;
        }
        //-------FARB BEZ
        if(editFarbBez.trim().length() != 0){
            if(hasQuery){
                SqlQuery.append(" AND ");
            }
            String farbBez = editFarbBez.trim();
            SqlQuery.append(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN)
                    .append(" LIKE '%")
                    .append(farbBez)
                    .append("%'");
            hasQuery = true;
        }
        //-------GROESSE ID
        if(editGroesse.trim().length() != 0 && editGroesse.trim().matches("[0-9]+")) {
            if (hasQuery) {
                SqlQuery.append(" AND ");
            }
            int groesse = Integer.parseInt(editGroesse.trim());
            SqlQuery.append(TableLagerbestand.LagerbestandEntry.COLUMN_GROESSE_ID)
                    .append(" LIKE '")
                    .append(groesse)
                    .append("%'");
            hasQuery = true;
        }
        //-------FERTIGUNGSZUSTAND
        if(editFertigungszustand.trim().length() != 0){
            if(hasQuery){
                SqlQuery.append(" AND ");
            }
            String fertZstd = editFertigungszustand.trim();
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
