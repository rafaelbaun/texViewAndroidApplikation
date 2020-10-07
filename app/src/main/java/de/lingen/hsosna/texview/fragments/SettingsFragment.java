package de.lingen.hsosna.texview.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import de.lingen.hsosna.texview.Constants;
import de.lingen.hsosna.texview.DatabaseHelper;
import de.lingen.hsosna.texview.Lagerplatz;
import de.lingen.hsosna.texview.R;
import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableKpi;
import de.lingen.hsosna.texview.database.TableLagerbestand;
import de.lingen.hsosna.texview.database.TableLagerbestand_Summe;
import de.lingen.hsosna.texview.database.TableLagerplaetze;
import de.lingen.hsosna.texview.database.TableTimestamp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static de.lingen.hsosna.texview.MainActivity.freeShelveList;
import static de.lingen.hsosna.texview.MainActivity.timestampServerGlobal;

/**
 * Settings-Fragment
 * Eine SQLite Datenbank wird erstellt und mittels der DBHelper Klasse werden drei Tabellen in ihr erstellt.
 */
public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private SQLiteDatabase mDatabase;
    private ProgressBar progressBar;
    private AlertDialog alertDialog;

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        mDatabase = dbHelper.getWritableDatabase();
    }



    /**
     *
     *
     * @return view des SettingsFragments
     */
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        //Buttons
        ImageButton buttonRefreshDatabase = v.findViewById(R.id.button_reloadFromServer);
        ImageButton buttonLoadFromServer = v.findViewById(R.id.button_loadFromServer);
        ImageButton buttonDeleteDb = v.findViewById(R.id.button_deleteDbContent);
        ImageButton buttonLoadFromMemory = v.findViewById(R.id.button_loadFromMemory);

        //Alert Dialog
        progressBar = v.findViewById(R.id.fragmentSettings_progressBar);
        ((ViewGroup) progressBar.getParent()).removeView(progressBar);
        final AlertDialog.Builder alertDialogBuilder = initAlertDialog(v);
        alertDialog = alertDialogBuilder.create();

        //Refresh Database content
        buttonRefreshDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                alertDialog.setIcon(R.drawable.ic_refresh);
                alertDialog.setTitle(R.string.settingsFragment_refreshDescription);
                alertDialog.setMessage("Bitte warten...");

                mDatabase.execSQL("DELETE FROM " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + ";");
                updateLagerbestand();
                setTimestampToMostRecent();
            }
        });
        //Fill Database from Server
        buttonLoadFromServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                alertDialog.setIcon(R.drawable.ic_cloud_download);
                alertDialog.setTitle(R.string.settingsFragment_loadFromServerDescription);
                alertDialog.setMessage("Bitte warten...");

                deleteDatabaseContents(mDatabase);
                startAsyncTast(true);
                setTimestampToMostRecent();
            }
        });

        // Fill Database from Memory
        buttonLoadFromMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                alertDialog.setIcon(R.drawable.ic_document_download);
                alertDialog.setTitle(R.string.settingsFragment_loadFromMemoryDescription);
                alertDialog.setMessage("Bitte warten...");

                deleteDatabaseContents(mDatabase);
                startAsyncTast(false);
            }
        });

        // Delete Database
        buttonDeleteDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                alertDialog.setIcon(R.drawable.ic_trash);
                alertDialog.setTitle(R.string.settingsFragment_deleteDatabaseDescription);
                alertDialog.setMessage("Datenbestand wurde gelöscht!");

                progressBar.setVisibility(View.GONE);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                deleteDatabaseContents(mDatabase);
            }
        });

        return v;
    }

    private AlertDialog.Builder initAlertDialog (View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        alertDialogBuilder.setCancelable(false);

        //ProgressBar Setup
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(lp);
        progressBar.setPadding(20,0,20,0);
        alertDialogBuilder.setView(progressBar);


        return alertDialogBuilder;
    }

    public void updateLagerbestand (){
        String[][] dataToPull = new String[1][2];
        dataToPull[0][0] = TableLagerbestand.LagerbestandEntry.TABLE_NAME;
        dataToPull[0][1] = Constants.SERVER_URL_LAGERBESTAND;
        FillDatabaseAsyncTask task = new FillDatabaseAsyncTask(this, dataToPull);
        task.execute();

    }

    private void setTimestampToMostRecent (){
        mDatabase.execSQL("DELETE FROM " + TableTimestamp.TimestampEntry.TABLE_NAME + ";");
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableTimestamp.TimestampEntry.COLUMN_TIMESTAMP, timestampServerGlobal);
        mDatabase.insert(TableTimestamp.TimestampEntry.TABLE_NAME, null, contentValues);
    }



    /**
     *
     */
    private void fillKpis () {
        ContentValues cv = new ContentValues();
        // Freie Lagerplätze April 2020
        cv.put(TableKpi.KpiEntry.COLUMN_NAME, "Freie Lagerplätze");
        cv.put(TableKpi.KpiEntry.COLUMN_CURRENTVALUE, 100);
        cv.put(TableKpi.KpiEntry.COLUMN_MAXVALUE, 840);
        cv.put(TableKpi.KpiEntry.COLUMN_TIMESTAMP, "April 2020");
        mDatabase.insert(TableKpi.KpiEntry.TABLE_NAME, null, cv);

        // Freie Lagerplätze Mai 2020
        cv = new ContentValues();
        cv.put(TableKpi.KpiEntry.COLUMN_NAME, "Freie Lagerplätze");
        cv.put(TableKpi.KpiEntry.COLUMN_CURRENTVALUE, 188);
        cv.put(TableKpi.KpiEntry.COLUMN_MAXVALUE, 840);
        cv.put(TableKpi.KpiEntry.COLUMN_TIMESTAMP, "Mai 2020");
        mDatabase.insert(TableKpi.KpiEntry.TABLE_NAME, null, cv);

        // Komissionierte Artikel Mai 2020
        cv = new ContentValues();
        cv.put(TableKpi.KpiEntry.COLUMN_NAME, "Komissionierte Artikel");
        cv.put(TableKpi.KpiEntry.COLUMN_CURRENTVALUE, 1246);
        cv.put(TableKpi.KpiEntry.COLUMN_MAXVALUE, 0);
        cv.put(TableKpi.KpiEntry.COLUMN_TIMESTAMP, "Mai 2020");
        mDatabase.insert(TableKpi.KpiEntry.TABLE_NAME, null, cv);

        // Komissionierte Artikel Juni 2020
        cv = new ContentValues();
        cv.put(TableKpi.KpiEntry.COLUMN_NAME, "Komissionierte Artikel");
        cv.put(TableKpi.KpiEntry.COLUMN_CURRENTVALUE, 1300);
        cv.put(TableKpi.KpiEntry.COLUMN_MAXVALUE, 0);
        cv.put(TableKpi.KpiEntry.COLUMN_TIMESTAMP, "Juni 2020");
        mDatabase.insert(TableKpi.KpiEntry.TABLE_NAME, null, cv);

        // Belegte Lagerplätze September 2020
        cv = new ContentValues();
        cv.put(TableKpi.KpiEntry.COLUMN_NAME, "Belegte Lagerplätze");
        cv.put(TableKpi.KpiEntry.COLUMN_CURRENTVALUE, 812);
        cv.put(TableKpi.KpiEntry.COLUMN_MAXVALUE, 840);
        cv.put(TableKpi.KpiEntry.COLUMN_TIMESTAMP, "September 2020");
        mDatabase.insert(TableKpi.KpiEntry.TABLE_NAME, null, cv);

        // Belegte Lagerplätze Oktober 2020
        cv = new ContentValues();
        cv.put(TableKpi.KpiEntry.COLUMN_NAME, "Belegte Lagerplätze");
        cv.put(TableKpi.KpiEntry.COLUMN_CURRENTVALUE, 602);
        cv.put(TableKpi.KpiEntry.COLUMN_MAXVALUE, 840);
        cv.put(TableKpi.KpiEntry.COLUMN_TIMESTAMP, "Oktober 2020");
        mDatabase.insert(TableKpi.KpiEntry.TABLE_NAME, null, cv);
    }


    /**
     * Ein zweidimensionales Array wird mit den Tabellennamen und den PHP-Skripten auf dem Server
     * gefüllt und im Hintergrund ausgefüllt.
     *
     * @param getFromServer boolean, ob Daten von Server geladen werden sollen
     */
    // TODO startAsyncTas
    public void startAsyncTast(boolean getFromServer) {
        String[][] dataToPull = new String[5][2];
        dataToPull[0][0] = TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME;
        dataToPull[1][0] = TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME;
        dataToPull[2][0] = TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME;
        dataToPull[3][0] = TableLagerbestand.LagerbestandEntry.TABLE_NAME;
        dataToPull[4][0] = TableKpi.KpiEntry.TABLE_NAME;
        if(getFromServer) {
            dataToPull[0][1] = Constants.SERVER_URL_ARTIKELKOMBINATIONEN;
            dataToPull[1][1] = Constants.SERVER_URL_LAGERPLAETZE;
            dataToPull[2][1] = Constants.SERVER_URL_LAGERBESTAND_SUMME;
            dataToPull[3][1] = Constants.SERVER_URL_LAGERBESTAND;
            dataToPull[4][1] = Constants.SERVER_URL_KPI;
        } else {
            dataToPull[0][1] = null;
            dataToPull[1][1] = null;
            dataToPull[2][1] = null;
            dataToPull[3][1] = null;
            dataToPull[4][1] = null;
        }
        FillDatabaseAsyncTask task = new FillDatabaseAsyncTask(this, dataToPull);
        task.execute();
    }



    /**
     * Alle Inhalte der Datenbank werden gelöscht.
     *
     * @param db
     */
    private void deleteDatabaseContents (SQLiteDatabase db) {
        // Artikelkombinationen
        String deleteString =
                "DELETE FROM " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);
        // Lagerplaetze
        deleteString = "DELETE FROM " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);
        // Lagerbestand
        deleteString = "DELETE FROM " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);
        // LagerbestandSumme
        deleteString =
                "DELETE FROM " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);
        // Kpi
        deleteString =
                "DELETE FROM " + TableKpi.KpiEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);
    }



    /**
     * Mittels eines JSON Arrays werden die Inhalte der artikelkombinationen.json-Datei in Strings
     * konvertiert.
     *
     * @param array aus artikelkombinationen.json
     */
    private void readDataToDbArtikelkombinationen (JSONArray array)
            throws IOException, JSONException {
        // Spaltenbezeichnungen in json-Datei
        final String MNU_ARTIKEL_ID    = "artikel_id_artikelkombinationen";
        final String MNU_ARTIKEL_BEZ   = "artikel_bezeichnung_artikelkombinationen";
        final String MNU_GROESSEN_ID   = "groessen_id_artikelkombinationen";
        final String MNU_GROESSEN_BEZ  = "groessen_bezeichnung_artikelkombinationen";
        final String MNU_FARBE_ID      = "farbe_id_artikelkombinationen";
        final String MNU_FARBE_BEZ     = "farbe_bezeichnung_artikelkombinationen";
        final String MNU_VARIANTEN_ID  = "varianten_id_artikelkombinationen";
        final String MNU_VARIANTEN_BEZ = "varianten_bezeichnung_artikelkombinationen";

        // Konvertierung der json-Inhalte in Strings
        try {
            JSONArray artikelkombinationenJsonArray;
            if (array == null) {
                // Datenstrom zum Lesen der artikelkombinationen.json aus raw-Verzeichnis
                String jsonDataString = readJsonDataFromFile(getActivity().getResources().
                        openRawResource(R.raw.artikelkombinationen));
                artikelkombinationenJsonArray = new JSONArray(jsonDataString);
            } else {
                artikelkombinationenJsonArray = array;
            }

            // jsonArray durchlaufen
            for (int i = 0; i < artikelkombinationenJsonArray.length(); ++ i) {
                int    artikelId;
                String artikelBezeichnung;
                int    groessenId;
                String groessenBezeichnung;
                int    farbeId;
                String farbeBez;
                int    variantenId;
                String variantenBezeichnung;
                // JSONObject erstellen, um die Inhalte auslesen zu können
                JSONObject artikelkombinationenObject = artikelkombinationenJsonArray.getJSONObject(i);
                ContentValues artikelkombinationenValues = new ContentValues();

                // Strings generieren
                artikelId          = Integer.parseInt(artikelkombinationenObject.getString(MNU_ARTIKEL_ID));
                artikelBezeichnung = artikelkombinationenObject.getString(MNU_ARTIKEL_BEZ);
                groessenId         = Integer.parseInt(artikelkombinationenObject.getString(MNU_GROESSEN_ID));
                if (! artikelkombinationenObject.isNull(MNU_GROESSEN_BEZ)) {
                    groessenBezeichnung = artikelkombinationenObject.getString(MNU_GROESSEN_BEZ);
                    artikelkombinationenValues.put(
                            TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_BEZEICHNUNG,
                            groessenBezeichnung);
                }
                farbeId  = Integer.parseInt(artikelkombinationenObject.getString(MNU_FARBE_ID));
                farbeBez = artikelkombinationenObject.getString(MNU_FARBE_BEZ);
                if (! artikelkombinationenObject.isNull(MNU_VARIANTEN_ID)) {
                    variantenId = Integer.parseInt(
                            artikelkombinationenObject.getString(MNU_VARIANTEN_ID));
                    artikelkombinationenValues.put(
                            TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_VARIANTEN_ID,
                            variantenId);
                }
                if (! artikelkombinationenObject.isNull(MNU_VARIANTEN_BEZ)) {
                    variantenBezeichnung = artikelkombinationenObject.getString(MNU_VARIANTEN_BEZ);
                    artikelkombinationenValues.put(
                            TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_VARIANTEN_BEZEICHNUNG,
                            variantenBezeichnung);
                }

                // Strings den content values hinzufügen
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID,
                        artikelId);
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG,
                        artikelBezeichnung);
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID,
                        groessenId);
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID,
                        farbeId);
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN,
                        farbeBez);

                // Eintrag in die Datenbank
                mDatabase.insert(TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME,
                        null, artikelkombinationenValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Mittels eines JSON Arrays werden die Inhalte der lagerbestand.json-Datei in Strings
     * konvertiert.
     *
     * @param array aus lagerbestand.json
     */
    //TODO
    private void readDataToDbLagerbestand (JSONArray array)
            throws IOException, JSONException {
        // Spaltenbezeichnungen in json-Datei
        final String MNU_LAGERPLATZ_LAGERBESTAND        = "lagerplatz_lagerbestand";
        final String MNU_STUECKNUMMER_LAGERBESTAND      = "stuecknummer_lagerbestand";
        final String MNU_STUECKTEILUNG_LAGERBESTAND     = "stueckteilung_lagerbestand";
        final String MNU_ARTIKEL_ID_LAGERBESTAND        = "artikel_id_lagerbestand";
        final String MNU_GROESSEN_ID_LAGERBESTAND       = "groessen_id_lagerbestand";
        final String MNU_FARBE_ID_LAGERBESTAND          = "farbe_id_lagerbestand";
        final String MNU_VARIANTEN_ID_LAGERBESTAND      = "varianten_id_lagerbestand";
        final String MNU_FERTIGUNGSZUSTAND_LAGERBESTAND = "fertigungszustand_lagerbestand";
        final String MNU_MENGE_LAGERBESTAND             = "menge_lagerbestand";
        final String MNU_MENGENEINHEIT_LAGERBESTAND     = "mengeneinheit_lagerbestand";

        // Konvertierung der json-Inhalte in Strings
        try {
            JSONArray lagerbestandJsonArray;
            if (array == null) {
                // Datenstrom zum Lesen der lagerbestand.json aus raw-Verzeichnis
                String jsonDataString = readJsonDataFromFile(getActivity().getResources()
                        .openRawResource(R.raw.lagerbestand));
                lagerbestandJsonArray = new JSONArray(jsonDataString);
            } else {
                lagerbestandJsonArray = array;
            }

            // jsonArray durchlaufen
            for (int i = 0; i < lagerbestandJsonArray.length(); ++ i) {
                int    lagerplatz;
                int    stuecknummer;
                int    stueckteilung;
                int    artikelId;
                int    groesseId;
                int    farbeId;
                int    variantenId;
                String fertigungszustand;
                String menge;
                String mengeneinheit;
                // JSONObject erstellen, um die Inhalte auslesen zu können
                JSONObject lagerbestandObject = lagerbestandJsonArray.getJSONObject(i);
                ContentValues lagerbestandValues = new ContentValues();

                // Strings generieren
                lagerplatz = Integer.parseInt(
                        lagerbestandObject.getString(MNU_LAGERPLATZ_LAGERBESTAND));
                stuecknummer = Integer.parseInt(
                        lagerbestandObject.getString(MNU_STUECKNUMMER_LAGERBESTAND));
                stueckteilung = Integer.parseInt(
                        lagerbestandObject.getString(MNU_STUECKTEILUNG_LAGERBESTAND));
                artikelId = Integer.parseInt(
                        lagerbestandObject.getString(MNU_ARTIKEL_ID_LAGERBESTAND));
                groesseId = Integer.parseInt(
                        lagerbestandObject.getString(MNU_GROESSEN_ID_LAGERBESTAND));
                farbeId = Integer.parseInt(
                        lagerbestandObject.getString(MNU_FARBE_ID_LAGERBESTAND));
                if (! lagerbestandObject.isNull(MNU_VARIANTEN_ID_LAGERBESTAND)) {
                    variantenId = Integer.parseInt(
                            lagerbestandObject.getString(MNU_VARIANTEN_ID_LAGERBESTAND));
                    lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_VARIANTEN_ID,
                            variantenId);
                }
                fertigungszustand = lagerbestandObject.getString(MNU_FERTIGUNGSZUSTAND_LAGERBESTAND);
                menge             = lagerbestandObject.getString(MNU_MENGE_LAGERBESTAND);
                mengeneinheit     = lagerbestandObject.getString(MNU_MENGENEINHEIT_LAGERBESTAND);

                // Strings den content values hinzufügen
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ,
                        lagerplatz);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER,
                        stuecknummer);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG,
                        stueckteilung);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID,
                        artikelId);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID,
                        groesseId);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID,
                        farbeId);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_FERTIGUNGSZUSTAND,
                        fertigungszustand);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_MENGE,
                        menge);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_MENGENEINHEIT,
                        mengeneinheit);

                // Eintrag in die Datenbank
                mDatabase.insert(TableLagerbestand.LagerbestandEntry.TABLE_NAME,
                        null, lagerbestandValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * Mittels eines JSON Arrays werden die Inhalte der lagerbestand_summe.json-Datei in Strings
     * konvertiert.
     *
     * @param array aus lagerbestand_summe.json
     */
    private void readDataToDbLagerbestandSumme (JSONArray array)
            throws IOException, JSONException {
        // Spaltenbezeichnungen in json-Datei
        final String MNU_LAGERPLATZ              = "lagerplatz_lagerbestand_summe";
        final String MNU_ARTIKEL_ID_SUMME        = "artikel_id_lagerbestand_summe";
        final String MNU_GROESSEN_ID_SUMME       = "groessen_id_lagerbestand_summe";
        final String MNU_FARBE_ID_SUMME          = "farbe_id_lagerbestand_summe";
        final String MNU_VARIANTEN_ID_SUMME      = "varianten_id_lagerbestand_summe";
        final String MNU_FERTIGUNGSZUSTAND_SUMME = "fertigungszustand_lagerbestand_summe";
        final String MNU_MENGE_SUMME             = "menge_lagerbestand_summe";
        final String MNU_MENGENEINHEIT_SUMME     = "mengeneinheit_lagerbestand_summe";

        // Konvertierung der json-Inhalte in Strings
        try {
            JSONArray lagerbestandSummeJsonArray;
            if (array == null) {
                // Datenstrom zum Lesen der lagerbestandd_summe.json aus raw-Verzeichnis
                String jsonDataString = readJsonDataFromFile(getActivity().getResources()
                        .openRawResource(R.raw.lagerbestand_summe));
                lagerbestandSummeJsonArray = new JSONArray(jsonDataString);
            } else {
                lagerbestandSummeJsonArray = array;
            }

            // jsonArray durchlaufen
            for (int i = 0; i < lagerbestandSummeJsonArray.length(); ++ i) {
                int    lagerplatz;
                int    artikelId;
                int    groessenId;
                int    farbeId;
                int    variantenId;
                String fertigungszustand;
                String menge;
                String mengeneinheit;
                // JSONObject erstellen, um die Inhalte auslesen zu können
                JSONObject lagerbestandSummeObject = lagerbestandSummeJsonArray.getJSONObject(i);
                ContentValues lagerbestandSummeValues = new ContentValues();

                // Strings generieren
                lagerplatz = Integer.parseInt(
                        lagerbestandSummeObject.getString(MNU_LAGERPLATZ));
                artikelId = Integer.parseInt(
                        lagerbestandSummeObject.getString(MNU_ARTIKEL_ID_SUMME));
                groessenId = Integer.parseInt(
                        lagerbestandSummeObject.getString(MNU_GROESSEN_ID_SUMME));
                farbeId = Integer.parseInt(
                        lagerbestandSummeObject.getString(MNU_FARBE_ID_SUMME));
                if (! lagerbestandSummeObject.isNull(MNU_VARIANTEN_ID_SUMME)) {
                    variantenId = Integer.parseInt(
                            lagerbestandSummeObject.getString(MNU_VARIANTEN_ID_SUMME));
                    lagerbestandSummeValues.put(
                            TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_VARIANTEN_ID,
                            variantenId);
                }
                fertigungszustand = lagerbestandSummeObject.getString(MNU_FERTIGUNGSZUSTAND_SUMME);
                menge             = lagerbestandSummeObject.getString(MNU_MENGE_SUMME);
                mengeneinheit     = lagerbestandSummeObject.getString(MNU_MENGENEINHEIT_SUMME);

                // Strings den content values hinzufügen
                lagerbestandSummeValues.put(
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ,
                        lagerplatz);
                lagerbestandSummeValues.put(
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_ARTIKEL_ID,
                        artikelId);
                lagerbestandSummeValues.put(
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_GROESSEN_ID,
                        groessenId);
                lagerbestandSummeValues.put(
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID,
                        farbeId);
                lagerbestandSummeValues.put(
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FERTIGUNGSZUSTAND,
                        fertigungszustand);
                lagerbestandSummeValues.put(
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_MENGE,
                        menge);
                lagerbestandSummeValues.put(
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_MENGENEINHEIT,
                        mengeneinheit);

                // Eintrag in die Datenbank
                mDatabase.insert(TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME, null,
                        lagerbestandSummeValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * Mittels eines JSON Arrays werden die Inhalte der lagerplaetze.json-Datei in Strings
     * konvertiert.
     *
     * @param array aus lagerplaetze.json
     */
    private void readDataToDbLagerplaetze (JSONArray array)
            throws IOException, JSONException {
        // Spaltenbezeichnungen in json-Datei
        final String MNU_LAGERORT     = "lagerort_lagerplaetze";
        final String MNU_LAGERPLATZ   = "lagerplatz_lagerplaetze";
        final String MNU_REGALNR      = "regal_nr_lagerplaetze";
        final String MNU_ZEILE        = "zeile_lagerplaetze";
        final String MNU_SPALTE       = "spalte_lagerplaetze";
        final String MNU_BESCHREIBUNG = "beschreibung_lagerplaetze";

        // Konvertierung der json-Inhalte in Strings
        try {
            JSONArray lagerplaetzeJsonArray;
            if (array == null) {
                // Datenstrom zum Lesen der lagerplaetze.json aus raw-Verzeichnis
                String jsonDataString = readJsonDataFromFile(getActivity().getResources()
                        .openRawResource(R.raw.lagerplaetze));
                lagerplaetzeJsonArray = new JSONArray(jsonDataString);
            } else {
                lagerplaetzeJsonArray = array;
            }

            // jsonArray durchlaufen
            for (int i = 0; i < lagerplaetzeJsonArray.length(); ++ i) {
                int    lagerort;
                int    lagerplatz;
                int    regalNr;
                int    zeile;
                int    spalte;
                String beschreibung = "";
                // JSONObject erstellen, um die Inhalte auslesen zu können
                JSONObject lagerplaetzeObject = lagerplaetzeJsonArray.getJSONObject(i);
                ContentValues lagerplaetzeValues = new ContentValues();

                // Strings generieren
                lagerort   = Integer.parseInt(lagerplaetzeObject.getString(MNU_LAGERORT));
                lagerplatz = Integer.parseInt(lagerplaetzeObject.getString(MNU_LAGERPLATZ));
                regalNr    = Integer.parseInt(lagerplaetzeObject.getString(MNU_REGALNR));
                zeile      = Integer.parseInt(lagerplaetzeObject.getString(MNU_ZEILE));
                spalte     = Integer.parseInt(lagerplaetzeObject.getString(MNU_SPALTE));
                if (! lagerplaetzeObject.isNull(MNU_BESCHREIBUNG)) {
                    beschreibung = lagerplaetzeObject.getString(MNU_BESCHREIBUNG);
                    lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_BESCHREIBUNG,
                            beschreibung);
                }

                // Strings den content values hinzufügen
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERORT,
                        lagerort);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ,
                        lagerplatz);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_REGAL_NR,
                        regalNr);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_ZEILE,
                        zeile);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_SPALTE,
                        spalte);

                // Eintrag in die Datenbank
                mDatabase.insert(TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME,
                        null, lagerplaetzeValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void readDataToDbKpi (JSONArray array)
            throws IOException, JSONException {
        //names of columns in json file
        final String MNU_NAME = "name_kpi";
        final String MNU_CURRENTVALUE = "currentvalue_kpi";
        final String MNU_MAXVALUE = "maxvalue_kpi";
        final String MNU_TIMESTAMP = "timestamp_kpi";
        try {
            JSONArray kpiJsonArray;
            if(array == null) {
                String jsonDataString = readJsonDataFromFile(
                        getActivity().getResources().openRawResource(R.raw.kpi));
                kpiJsonArray = new JSONArray(jsonDataString);
            } else {
                kpiJsonArray = array;
            }
            for (int i = 0; i < kpiJsonArray.length(); ++ i) {
                String name;
                int currentvalue;
                int maxValue;
                String timestamp;

                JSONObject kpiObject = kpiJsonArray.getJSONObject(i);
                name = kpiObject.getString(MNU_NAME);
                currentvalue = Integer.parseInt(kpiObject.getString(MNU_CURRENTVALUE));
                maxValue = Integer.parseInt(kpiObject.getString(MNU_MAXVALUE));
                timestamp = kpiObject.getString(MNU_TIMESTAMP);

                ContentValues kpiValues = new ContentValues();
                kpiValues.put(TableKpi.KpiEntry.COLUMN_NAME, name);
                kpiValues.put(TableKpi.KpiEntry.COLUMN_CURRENTVALUE, currentvalue);
                kpiValues.put(TableKpi.KpiEntry.COLUMN_MAXVALUE, maxValue);
                kpiValues.put(TableKpi.KpiEntry.COLUMN_TIMESTAMP, timestamp);

                mDatabase.insert(TableKpi.KpiEntry.TABLE_NAME, null, kpiValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * Zum Auslesen der Daten aus den json-Dateien wird ein String mittels eines InputStreams
     * erstellt.
     *
     * @param is InputStream zum Lesen der Datei
     * @return erzeugter String des StringBuilders
     */
    private String readJsonDataFromFile (InputStream is) throws IOException {
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {
            String jsonDataString = null;
            inputStream = is;
            // inputStream mittels BufferedReader öffnen
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            // json-Inhalte dem builder anhängen
            while ((jsonDataString = bufferedReader.readLine()) != null) {
                builder.append(jsonDataString);
            }
        } finally {
            // inputStream schließen
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return new String(builder);
    }

    private void updateFreeShelvesList (){
        ArrayList<Lagerplatz> newFreeShelvesList = new ArrayList<>();
        try (Cursor cursor = mDatabase.rawQuery(
                "SELECT " + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ + ", "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERORT
                + " FROM " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + ""
                + " LEFT OUTER JOIN " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + ""
                + " ON " + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ + " = "
                + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ
                + " WHERE " + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + " IS NULL;"
                , null)) {
            while (cursor.moveToNext()) {
                int lagerplatz = cursor.getInt(cursor.getColumnIndex(
                        TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ));
                int lagerort = cursor.getInt(
                        cursor.getColumnIndex(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERORT));
                Lagerplatz lagerplatzObject = new Lagerplatz(lagerort, lagerplatz);
                newFreeShelvesList.add(lagerplatzObject);
            }
        }
        freeShelveList = newFreeShelvesList;
    }


    /**
     * Das Laden des temporären Lagerplatzes läuft im Hintergrund ab und wird mittels eines
     *      * AsyncTasks realisiert.
     */
    private static class FillDatabaseAsyncTask extends AsyncTask<Integer, Integer, String> {
        private WeakReference<SettingsFragment> activityWeakReference;
        private String[][] dataToPull;

        FillDatabaseAsyncTask (SettingsFragment fragment, String[][] dataToPullArray) {
            activityWeakReference = new WeakReference<SettingsFragment>(fragment);
            dataToPull            = dataToPullArray;
        }


        /**
         * Bevor der AsyncTask ausgeführt wird, wird mittels einer WeakReference die Aktivität
         * für das Fragment geladen.  Zudem erscheint der AlertDialog und der darin enthaltenen
         * ProgressBar.
         */
        @Override
        protected void onPreExecute () {
            super.onPreExecute();
            SettingsFragment fragment = activityWeakReference.get();
            if (fragment == null || fragment.isRemoving()) {
                return;
            }
            fragment.alertDialog.show();
            fragment.progressBar.setVisibility(View.VISIBLE);
        }


        /**
         * Es wird im Hintergrund ein Http-Client erstellt, welcher
         *
         * @param integers
         * @return "Finished"
         */
        @Override
        protected String doInBackground (Integer... integers) {
            OkHttpClient client = new OkHttpClient();

            // zweidimensionales Array durchlaufen
            for (int i = 0; i < dataToPull.length; i++) {
                SettingsFragment fragment = activityWeakReference.get();
                JSONArray array = null;
                try {
                    if (dataToPull[i][1] != null) {
                        // Anfrage
                        Request request = new Request.Builder().url(dataToPull[i][1]).build();
                        // Antwort
                        Response response = client.newCall(request).execute();
                        // json-Array aus Antwort generieren
                        array = new JSONArray(response.body().string());
                    }
                    // Entscheidung, welche Tabelle angefragt werden soll, und mit array füllen
                    switch (dataToPull[i][0]) {
                        case TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME:
                            fragment.readDataToDbArtikelkombinationen(array);
                            break;
                        case TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME:
                            fragment.readDataToDbLagerplaetze(array);
                            break;
                        case TableLagerbestand.LagerbestandEntry.TABLE_NAME:
                            fragment.readDataToDbLagerbestand(array);
                            break;
                        case TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME:
                            fragment.readDataToDbLagerbestandSumme(array);
                            break;
                        case TableKpi.KpiEntry.TABLE_NAME:
                            fragment.readDataToDbKpi(array);
                            break;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                // UI Thread aktualisieren
                publishProgress(((i+1) * 100) / dataToPull.length);
            }

            return "Finished!";
        }


        /**
         * Nachdem der UI-Thread aktualisiert worden ist, wird in der ProgressBar der Fortschritt
         * des Ladens angezeigt.
         * @param values
         */
        @Override
        protected void onProgressUpdate (Integer... values) {
            super.onProgressUpdate(values);
            SettingsFragment fragment = activityWeakReference.get();
            if (fragment == null || fragment.isRemoving()) {
                return;
            }
            fragment.progressBar.setProgress(values[0]);
        }


        /**
         * Nachdem alle Inhalte geladen worden sind, erlischt der AlertDialog und der AsyncTask wird
         * beendet.
         *
         * @param s
         */
        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute(s);
            SettingsFragment fragment = activityWeakReference.get();
            if (fragment == null || fragment.isRemoving()) {
                return;
            }
            fragment.alertDialog.dismiss();
            fragment.progressBar.setProgress(0);
            fragment.progressBar.setVisibility(View.INVISIBLE);
            fragment.updateFreeShelvesList();
        }

    }

}