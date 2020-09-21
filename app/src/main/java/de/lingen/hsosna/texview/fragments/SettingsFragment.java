package de.lingen.hsosna.texview.fragments;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;

import de.lingen.hsosna.texview.DatabaseHelper;
import de.lingen.hsosna.texview.R;
import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;
import de.lingen.hsosna.texview.database.TableLagerbestand_Summe;
import de.lingen.hsosna.texview.database.TableLagerplaetze;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Eine SQLite Datenbank wird erstellt und mittels der DBHelper Klasse werden drei Tabellen in ihr erstellt.
 */
public class SettingsFragment extends Fragment {
    private SQLiteDatabase mDatabase;
    private ProgressBar progressBar;
    private AlertDialog alertDialog;


    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        mDatabase = dbHelper.getWritableDatabase();

        progressBar = v.findViewById(R.id.fragmentSettings_progressBar);
        ((ViewGroup) progressBar.getParent()).removeView(progressBar);


        //Buttons
        ImageButton buttonRefreshDatabase = v.findViewById(R.id.button_reloadFromServer);
        ImageButton buttonLoadFromServer = v.findViewById(R.id.button_loadFromServer);
        ImageButton buttonDeleteDb = v.findViewById(R.id.button_deleteDbContent);
        ImageButton buttonLoadFromMemory = v.findViewById(R.id.button_loadFromMemory);


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(R.string.settingsFragment_loadingDescription);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(lp);
        progressBar.setPadding(20,0,20,0);
        alertDialogBuilder.setView(progressBar);

        //TODO
        //Refresh Database content
        buttonRefreshDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                alertDialogBuilder.setTitle(R.string.settingsFragment_refreshDescription);
                alertDialogBuilder.setIcon(R.drawable.ic_refresh);
                alertDialog = alertDialogBuilder.create();
            }
        });
        //Fill Database from Server
        buttonLoadFromServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                alertDialogBuilder.setTitle(R.string.settingsFragment_loadFromServerDescription);
                alertDialogBuilder.setIcon(R.drawable.ic_cloud_download);
                alertDialog = alertDialogBuilder.create();
                startAsyncTast(true);
            }
        });

        //Fill Database from Memory
        buttonLoadFromMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                alertDialogBuilder.setTitle(R.string.settingsFragment_loadFromMemoryDescription);
                alertDialogBuilder.setIcon(R.drawable.ic_document_download);
                alertDialog = alertDialogBuilder.create();
                startAsyncTast(false);
            }
        });

        //Delete Database
        buttonDeleteDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                deleteDatabaseContents(mDatabase);
            }
        });

        return v;
    }

    public void startAsyncTast(boolean getFromServer){
        String[][] dataToPull = new String[4][2];
        dataToPull[0][0] = TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME;
        dataToPull[1][0] = TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME;
        dataToPull[2][0] = TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME;
        dataToPull[3][0] = TableLagerbestand.LagerbestandEntry.TABLE_NAME;

        if(getFromServer) {
            dataToPull[0][1] = "http://131.173.65.147/artikelkombinationen.php";
            dataToPull[1][1] = "http://131.173.65.147/lagerplaetze.php";
            dataToPull[2][1] = "http://131.173.65.147/lagerbestand_summe.php";
            dataToPull[3][1] = "http://131.173.65.147/lagerbestand.php";
        }
        else{
            dataToPull[0][1] = null;
            dataToPull[1][1] = null;
            dataToPull[2][1] = null;
            dataToPull[3][1] = null;
        }
        FillDatabaseAsyncTask task = new FillDatabaseAsyncTask(this, dataToPull);
        task.execute();

    }

    private void deleteDatabaseContents (SQLiteDatabase db) {
        String deleteString =
                "DELETE FROM " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);

        deleteString = "DELETE FROM " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);

        deleteString = "DELETE FROM " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);

        deleteString =
                "DELETE FROM " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME + ";";
        db.execSQL(deleteString);
    }

    private void readDataToDbArtikelkombinationen (JSONArray array)
            throws IOException, JSONException {
        final String MNU_ARTIKEL_ID = "artikel_id_artikelkombinationen";
        final String MNU_ARTIKEL_BEZ = "artikel_bezeichnung_artikelkombinationen";
        final String MNU_GROESSEN_ID = "groessen_id_artikelkombinationen";
        final String MNU_GROESSEN_BEZ = "groessen_bezeichnung_artikelkombinationen";
        final String MNU_FARBE_ID = "farbe_id_artikelkombinationen";
        final String MNU_FARBE_BEZ = "farbe_bezeichnung_artikelkombinationen";
        final String MNU_VARIANTEN_ID = "varianten_id_artikelkombinationen";
        final String MNU_VARIANTEN_BEZ = "varianten_bezeichnung_artikelkombinationen";
        try {
            JSONArray artikelkombinationenJsonArray;
            if (array == null) {
                String jsonDataString = readJsonDataFromFile(
                        getActivity().getResources().openRawResource(
                                R.raw.artikelkombinationen)); // json datei kommt hier hin
                artikelkombinationenJsonArray = new JSONArray(jsonDataString);
            } else {
                artikelkombinationenJsonArray = array;
            }
            for (int i = 0; i < artikelkombinationenJsonArray.length(); ++ i) {
                int artikelId;
                String artikelBezeichnung;
                int groessenId;
                String groessenBezeichnung;
                int farbeId;
                String farbeBez;
                int variantenId;
                String variantenBezeichnung;
                JSONObject artikelkombinationenObject = artikelkombinationenJsonArray.getJSONObject(
                        i);
                ContentValues artikelkombinationenValues = new ContentValues();
                artikelId = Integer.parseInt(artikelkombinationenObject.getString(MNU_ARTIKEL_ID));
                artikelBezeichnung = artikelkombinationenObject.getString(MNU_ARTIKEL_BEZ);
                groessenId = Integer.parseInt(artikelkombinationenObject.getString(MNU_GROESSEN_ID));
                if (! artikelkombinationenObject.isNull(MNU_GROESSEN_BEZ)) {
                    groessenBezeichnung = artikelkombinationenObject.getString(MNU_GROESSEN_BEZ);
                    artikelkombinationenValues.put(
                            TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_BEZEICHNUNG,
                            groessenBezeichnung);
                }
                farbeId = Integer.parseInt(artikelkombinationenObject.getString(MNU_FARBE_ID));
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
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID, farbeId);
                artikelkombinationenValues.put(
                        TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN,
                        farbeBez);
                mDatabase.insert(TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME, null,
                        artikelkombinationenValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void readDataToDbLagerbestand (JSONArray array)
            throws IOException, JSONException {
        //names of columns in json file
        final String MNU_LAGERPLATZ_LAGERBESTAND = "lagerplatz_lagerbestand";
        final String MNU_STUECKNUMMER_LAGERBESTAND = "stuecknummer_lagerbestand";
        final String MNU_STUECKTEILUNG_LAGERBESTAND = "stueckteilung_lagerbestand";
        final String MNU_ARTIKEL_ID_LAGERBESTAND = "artikel_id_lagerbestand";
        final String MNU_GROESSEN_ID_LAGERBESTAND = "groessen_id_lagerbestand";
        final String MNU_FARBE_ID_LAGERBESTAND = "farbe_id_lagerbestand";
        final String MNU_VARIANTEN_ID_LAGERBESTAND = "varianten_id_lagerbestand";
        final String MNU_FERTIGUNGSZUSTAND_LAGERBESTAND = "fertigungszustand_lagerbestand";
        final String MNU_MENGE_LAGERBESTAND = "menge_lagerbestand";
        final String MNU_MENGENEINHEIT_LAGERBESTAND = "mengeneinheit_lagerbestand";
        try {
            JSONArray lagerbestandJsonArray;
            if(array == null) {
                String jsonDataString = readJsonDataFromFile(
                        getActivity().getResources().openRawResource(R.raw.lagerbestand));
                lagerbestandJsonArray = new JSONArray(jsonDataString);
            } else {
                lagerbestandJsonArray = array;
            }
            for (int i = 0; i < lagerbestandJsonArray.length(); ++ i) {
                int lagerplatz;
                int stuecknummer;
                int stueckteilung;
                int artikelId;
                int groesseId;
                int farbeId;
                int variantenId;
                String fertigungszustand;
                String menge;
                String mengeneinheit;
                JSONObject lagerbestandObject = lagerbestandJsonArray.getJSONObject(
                        i);
                ContentValues lagerbestandValues = new ContentValues();
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
                farbeId = Integer.parseInt(lagerbestandObject.getString(MNU_FARBE_ID_LAGERBESTAND));
                if (! lagerbestandObject.isNull(MNU_VARIANTEN_ID_LAGERBESTAND)) {
                    variantenId = Integer.parseInt(
                            lagerbestandObject.getString(MNU_VARIANTEN_ID_LAGERBESTAND));
                    lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_VARIANTEN_ID,
                            variantenId);
                }
                fertigungszustand = lagerbestandObject.getString(
                        MNU_FERTIGUNGSZUSTAND_LAGERBESTAND);
                menge = lagerbestandObject.getString(MNU_MENGE_LAGERBESTAND);
                mengeneinheit = lagerbestandObject.getString(MNU_MENGENEINHEIT_LAGERBESTAND);
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
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_MENGE, menge);
                lagerbestandValues.put(TableLagerbestand.LagerbestandEntry.COLUMN_MENGENEINHEIT,
                        mengeneinheit);
                mDatabase.insert(TableLagerbestand.LagerbestandEntry.TABLE_NAME, null, lagerbestandValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showToast (String abc){
        Toast.makeText(getActivity().getApplicationContext(), abc, Toast.LENGTH_SHORT).show();
    }

    private void readDataToDbLagerbestandSumme (JSONArray array)
            throws IOException, JSONException {
        //names of columns in json file
        final String MNU_LAGERPLATZ = "lagerplatz_lagerbestand_summe";
        final String MNU_ARTIKEL_ID_SUMME = "artikel_id_lagerbestand_summe";
        final String MNU_GROESSEN_ID_SUMME = "groessen_id_lagerbestand_summe";
        final String MNU_FARBE_ID_SUMME = "farbe_id_lagerbestand_summe";
        final String MNU_VARIANTEN_ID_SUMME = "varianten_id_lagerbestand_summe";
        final String MNU_FERTIGUNGSZUSTAND_SUMME = "fertigungszustand_lagerbestand_summe";
        final String MNU_MENGE_SUMME = "menge_lagerbestand_summe";
        final String MNU_MENGENEINHEIT_SUMME = "mengeneinheit_lagerbestand_summe";
        try {
            JSONArray lagerbestandSummeJsonArray;
            if(array == null) {
                String jsonDataString = readJsonDataFromFile(
                        getActivity().getResources().openRawResource(R.raw.lagerbestand_summe));
                lagerbestandSummeJsonArray = new JSONArray(jsonDataString);
            } else {
                lagerbestandSummeJsonArray = array;
            }
            for (int i = 0; i < lagerbestandSummeJsonArray.length(); ++ i) {
                int lagerplatz;
                int artikelId;
                int groessenId;
                int farbeId;
                int variantenId;
                String fertigungszustand;
                String menge;
                String mengeneinheit;
                JSONObject lagerbestandSummeObject = lagerbestandSummeJsonArray.getJSONObject(
                        i);
                ContentValues lagerbestandSummeValues = new ContentValues();
                lagerplatz = Integer.parseInt(lagerbestandSummeObject.getString(MNU_LAGERPLATZ));
                artikelId = Integer.parseInt(
                        lagerbestandSummeObject.getString(MNU_ARTIKEL_ID_SUMME));
                groessenId = Integer.parseInt(
                        lagerbestandSummeObject.getString(MNU_GROESSEN_ID_SUMME));
                farbeId = Integer.parseInt(lagerbestandSummeObject.getString(MNU_FARBE_ID_SUMME));
                if (! lagerbestandSummeObject.isNull(MNU_VARIANTEN_ID_SUMME)) {
                    variantenId = Integer.parseInt(
                            lagerbestandSummeObject.getString(MNU_VARIANTEN_ID_SUMME));
                    lagerbestandSummeValues.put(
                            TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_VARIANTEN_ID,
                            variantenId);
                }
                fertigungszustand = lagerbestandSummeObject.getString(MNU_FERTIGUNGSZUSTAND_SUMME);
                menge = lagerbestandSummeObject.getString(MNU_MENGE_SUMME);
                mengeneinheit = lagerbestandSummeObject.getString(MNU_MENGENEINHEIT_SUMME);

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
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID, farbeId);

                lagerbestandSummeValues.put(
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FERTIGUNGSZUSTAND,
                        fertigungszustand);
                lagerbestandSummeValues.put(
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_MENGE, menge);
                lagerbestandSummeValues.put(
                        TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_MENGENEINHEIT,
                        mengeneinheit);
                mDatabase.insert(TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME, null,
                        lagerbestandSummeValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void readDataToDbLagerplaetze (JSONArray array)
            throws IOException, JSONException {
        //names of columns in json file
        final String MNU_LAGERORT = "lagerort_lagerplaetze";
        final String MNU_LAGERPLATZ = "lagerplatz_lagerplaetze";
        final String MNU_REGALNR = "regal_nr_lagerplaetze";
        final String MNU_ZEILE = "zeile_lagerplaetze";
        final String MNU_SPALTE = "spalte_lagerplaetze";
        final String MNU_BESCHREIBUNG = "beschreibung_lagerplaetze";
        try {
            JSONArray lagerplaetzeJsonArray;
            if(array == null) {
                String jsonDataString = readJsonDataFromFile(
                        getActivity().getResources().openRawResource(R.raw.lagerplaetze));
                lagerplaetzeJsonArray = new JSONArray(jsonDataString);
            } else {
                lagerplaetzeJsonArray = array;
            }
            for (int i = 0; i < lagerplaetzeJsonArray.length(); ++ i) {
                int lagerort;
                int lagerplatz;
                int regalNr;
                int zeile;
                int spalte;
                String beschreibung = "";
                JSONObject lagerplaetzeObject = lagerplaetzeJsonArray.getJSONObject(i);
                ContentValues lagerplaetzeValues = new ContentValues();
                lagerort = Integer.parseInt(lagerplaetzeObject.getString(MNU_LAGERORT));
                lagerplatz = Integer.parseInt(lagerplaetzeObject.getString(MNU_LAGERPLATZ));
                regalNr = Integer.parseInt(lagerplaetzeObject.getString(MNU_REGALNR));
                zeile = Integer.parseInt(lagerplaetzeObject.getString(MNU_ZEILE));
                spalte = Integer.parseInt(lagerplaetzeObject.getString(MNU_SPALTE));
                if (! lagerplaetzeObject.isNull(MNU_BESCHREIBUNG)) {
                    beschreibung = lagerplaetzeObject.getString(MNU_BESCHREIBUNG);
                    lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_BESCHREIBUNG,
                            beschreibung);
                }

                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERORT,
                        lagerort);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ,
                        lagerplatz);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_REGAL_NR,
                        regalNr);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_ZEILE, zeile);
                lagerplaetzeValues.put(TableLagerplaetze.LagerplaetzeEntry.COLUMN_SPALTE, spalte);

                mDatabase.insert(TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME, null, lagerplaetzeValues);
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

    private static class FillDatabaseAsyncTask extends AsyncTask<Integer, Integer, String>{
        private WeakReference<SettingsFragment> activityWeakReference;
        private String[][] dataToPull;


        FillDatabaseAsyncTask (SettingsFragment fragment, String[][] dataToPullArray){
            activityWeakReference = new WeakReference<SettingsFragment>(fragment);
            dataToPull = dataToPullArray;

        }

        @Override
        protected void onPreExecute () {
            super.onPreExecute();

            SettingsFragment fragment = activityWeakReference.get();
            if(fragment == null || fragment.isRemoving()) {
                return;
            }


            fragment.alertDialog.show();
            fragment.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground (Integer... integers) {
            OkHttpClient client = new OkHttpClient();

            for (int i = 0; i < dataToPull.length; i++) {
                SettingsFragment fragment = activityWeakReference.get();
                JSONArray array = null;
                try {
                    if(dataToPull[i][1] != null) {
                        Request request = new Request.Builder().url(dataToPull[i][1]).build();
                        Response response = client.newCall(request).execute();
                        array = new JSONArray(response.body().string());
                    }
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
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                publishProgress(((i+1) *100) / dataToPull.length);
            }
            return "Finished!";
        }

        @Override
        protected void onProgressUpdate (Integer... values) {
            super.onProgressUpdate(values);
            SettingsFragment fragment = activityWeakReference.get();
            if(fragment == null || fragment.isRemoving()) {
                return;
            }
            fragment.progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute(s);
            SettingsFragment fragment = activityWeakReference.get();
            if(fragment == null || fragment.isRemoving()) {
                return;
            }
            fragment.alertDialog.dismiss();
            fragment.progressBar.setProgress(0);
            fragment.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}