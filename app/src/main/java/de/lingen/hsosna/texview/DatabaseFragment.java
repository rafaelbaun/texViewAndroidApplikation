package de.lingen.hsosna.texview;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;
import de.lingen.hsosna.texview.database.TableLagerplaetze;

/**
 * Eine SQLite Datenbank wird erstellt und mittels der DBHelper Klasse werden drei Tabellen in ihr erstellt.
 */
public class DatabaseFragment extends Fragment {
    private SQLiteDatabase mDatabase;
    private GroceryAdapter mAdapter;
    private EditText mEditTextName;
    private TextView mTextViewAmount;
    private int mAmount = 0;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_database, container, false);
        GroceryDBHelper dbHelper = new GroceryDBHelper(getActivity());
        mDatabase = dbHelper.getWritableDatabase();
        Button buttonDeleteDb = v.findViewById(R.id.buttonDeleteDB);
        Button buttonFillDb = v.findViewById(R.id.buttonFillDB);
        buttonDeleteDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                deleteDatabaseContents(mDatabase);
            }
        });
        buttonFillDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                try {
                    readDataToDbArtikelkombinationen(mDatabase);
                    Toast.makeText(getActivity().getApplicationContext(),"Artikelkombinationen hinzugefügt", Toast.LENGTH_SHORT).show();
                    readDataToDbLagerbestand(mDatabase);
                    Toast.makeText(getActivity().getApplicationContext(),"Lagerbestand hinzugefügt", Toast.LENGTH_SHORT).show();
                    readDataToDbLagerplaetze(mDatabase);
                    Toast.makeText(getActivity().getApplicationContext(),"Lagerplaetze hinzugefügt", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ///////////////
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new GroceryAdapter(getActivity(), getAllItems());
        recyclerView.setAdapter(mAdapter);
        mEditTextName = v.findViewById(R.id.edittext_name);
        mTextViewAmount = v.findViewById(R.id.textview_amount);
        Button buttonIncrease = v.findViewById(R.id.button_increase);
        Button buttonDecrease = v.findViewById(R.id.button_decrease);
        Button buttonAdd = v.findViewById(R.id.button_add);
        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                increase();
            }
        });
        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                decrease();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                addItem();
            }
        });
        ///////////
        return v;
    }

    private void deleteDatabaseContents (SQLiteDatabase db) {
        String deleteString =
                "DELETE FROM " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);
        Toast.makeText(getActivity().getApplicationContext(),
                "Tabelle: " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME
                + " wurde gelöscht", Toast.LENGTH_SHORT).show();
        deleteString = "DELETE FROM " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);
        Toast.makeText(getActivity().getApplicationContext(),
                "Tabelle: " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + " wurde gelöscht",
                Toast.LENGTH_SHORT).show();
        deleteString = "DELETE FROM " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);
        Toast.makeText(getActivity().getApplicationContext(),
                "Tabelle: " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + " wurde gelöscht",
                Toast.LENGTH_SHORT).show();
    }

    private void increase () {
        mAmount++;
        mTextViewAmount.setText(String.valueOf(mAmount));
    }

    private void decrease () {
        if (mAmount > 0) {
            mAmount--;
            mTextViewAmount.setText(String.valueOf(mAmount));
        }
    }

    private void addItem () {
        if (mEditTextName.getText().toString().trim().length() == 0 || mAmount == 0) {
            return;
        }
        String name = mEditTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmount);
        mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());
        mEditTextName.getText().clear();
    }

    private Cursor getAllItems () {
        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    private void readDataToDbArtikelkombinationen (SQLiteDatabase db)
            throws IOException, JSONException {
        final String MNU_ARTIKEL_ID = "artikel_id_artikelkombinationen";
        final String MNU_ARTIKEL_BEZ = "artikel_bezeichnung_artikelkombinationen";
        final String MNU_GROESSE_ID = "groeße_id_artikelkombinationen"; // evtl groeße
        final String MNU_FARBE_ID = "farbe_id_artikelkombinationen";
        final String MNU_FARBE_BEZ = "farbe_bezeichnung_artikelkombinationen";
        try {
            String jsonDataString = readJsonDataFromFile(
                    getActivity().getResources().openRawResource(R.raw.artikelkombinationen));
            JSONArray artikelkombinationenJsonArray = new JSONArray(jsonDataString);
            for (int i = 0; i < artikelkombinationenJsonArray.length(); ++ i) {
                int artikelId;
                String artikelBezeichnung;
                int groesseId;
                int farbeId;
                String farbeBez;
                JSONObject artikelkombinationenObject = artikelkombinationenJsonArray.getJSONObject(
                        i);
                artikelId = Integer.parseInt(artikelkombinationenObject.getString(MNU_ARTIKEL_ID));
                artikelBezeichnung = artikelkombinationenObject.getString(MNU_ARTIKEL_BEZ);
                groesseId = Integer.parseInt(artikelkombinationenObject.getString(MNU_GROESSE_ID));
                farbeId = Integer.parseInt(artikelkombinationenObject.getString(MNU_FARBE_ID));
                farbeBez = artikelkombinationenObject.getString(MNU_FARBE_BEZ);
                ContentValues artikelkombinationenValues = new ContentValues();
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID,
                        artikelId);
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG,
                        artikelBezeichnung);
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSE_ID,
                        groesseId);
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID, farbeId);
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN,
                        farbeBez);
                db.insert(TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME, null,
                        artikelkombinationenValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void readDataToDbLagerbestand (SQLiteDatabase db)
            throws IOException, JSONException {
        //names of columns in json file
        final String MNU_LAGERPLATZ = "lagerplatz_lagerbestand";
        final String MNU_ARTIKEL_IDL = "artikel_id_lagerbestand";
        final String MNU_GROESSE_IDL = "groeße_id_lagerbestand";
        final String MNU_FARBE_IDL = "farbe_id_lagerbestand";
        final String MNU_FERTIGUNGSZUSTAND = "fertigungszustand_lagerbestand";
        final String MNU_MENGE = "menge_lagerbestand";
        final String MNU_MENGENEINHEIT = "mengeneinheit_lagerbestand";
        try {
            String jsonDataString = readJsonDataFromFile(
                    getActivity().getResources().openRawResource(R.raw.lagerbestand));
            JSONArray lagerbestandJsonArray = new JSONArray(jsonDataString);
            for (int i = 0; i < lagerbestandJsonArray.length(); ++ i) {
                int lagerplatz;
                int artikelId;
                int groesseId;
                int farbeId;
                String fertigungszustand;
                String menge;
                String mengeneinheit;
                JSONObject lagerbestandObject = lagerbestandJsonArray.getJSONObject(
                        i);
                lagerplatz = Integer.parseInt(lagerbestandObject.getString(MNU_LAGERPLATZ));
                artikelId = Integer.parseInt(lagerbestandObject.getString(MNU_ARTIKEL_IDL));
                groesseId = Integer.parseInt(lagerbestandObject.getString(MNU_GROESSE_IDL));
                farbeId = Integer.parseInt(lagerbestandObject.getString(MNU_FARBE_IDL));
                fertigungszustand = lagerbestandObject.getString(MNU_FERTIGUNGSZUSTAND);
                menge = lagerbestandObject.getString(MNU_MENGE);
                mengeneinheit = lagerbestandObject.getString(MNU_MENGENEINHEIT);
                ContentValues lagerbestandValues = new ContentValues();
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ, lagerplatz);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID, artikelId);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_GROESSE_ID, groesseId);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID, farbeId);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_FERTIGUNGSZUSTAND, fertigungszustand);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_MENGE, menge);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_MENGENEINHEIT, mengeneinheit);
                db.insert(TableLagerbestand.LagerbestandEntry.TABLE_NAME, null, lagerbestandValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void readDataToDbLagerplaetze (SQLiteDatabase db)
            throws IOException, JSONException {
        //names of columns in json file
        final String MNU_LAGERORT = "lagerort_lagerplaetze";
        final String MNU_LAGERPLATZ = "lagerplatz_lagerplaetze";
        final String MNU_REGALNR = "regal_nr_lagerplaetze";
        final String MNU_ZEILE = "zeile_lagerplaetze";
        final String MNU_SPALTE = "spalte_lagerplatze"; // FEHLER IN MAIN DB #PLATZE ANSTATT PLAETZE#
        try {
            String jsonDataString = readJsonDataFromFile(
                    getActivity().getResources().openRawResource(R.raw.lagerplaetze));
            JSONArray lagerplaetzeJsonArray = new JSONArray(jsonDataString);
            for (int i = 0; i < lagerplaetzeJsonArray.length(); ++ i) {
                int lagerort;
                int lagerplatz;
                int regalNr;
                int zeile;
                int spalte;
                JSONObject lagerplaetzeObject = lagerplaetzeJsonArray.getJSONObject(i);
                lagerort = Integer.parseInt(lagerplaetzeObject.getString(MNU_LAGERORT));
                lagerplatz = Integer.parseInt(lagerplaetzeObject.getString(MNU_LAGERPLATZ));
                regalNr = Integer.parseInt(lagerplaetzeObject.getString(MNU_REGALNR));
                zeile = Integer.parseInt(lagerplaetzeObject.getString(MNU_ZEILE));
                spalte = Integer.parseInt(lagerplaetzeObject.getString(MNU_SPALTE));

                ContentValues lagerplaetzeValues = new ContentValues();
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERORT, lagerort);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ, lagerplatz);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_REGAL_NR, regalNr);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_ZEILE, zeile);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_SPALTE, spalte);

                db.insert(TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME, null, lagerplaetzeValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String readJsonDataFromFile (InputStream is) throws IOException {
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();
        try {
            String jsonDataString = null;
            inputStream = is;
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((jsonDataString = bufferedReader.readLine()) != null) {
                builder.append(jsonDataString);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }
}