package de.lingen.hsosna.texview.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.lingen.hsosna.texview.Article;
import de.lingen.hsosna.texview.ArticleAdapter;
import de.lingen.hsosna.texview.DatabaseHelper;
import de.lingen.hsosna.texview.Lagerplatz;
import de.lingen.hsosna.texview.R;
import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;

/**
 * Shelfront Temporary Fragment
 */
public class ShelffrontTemporaryFragment extends Fragment {

    private static final String TAG = "ShelffrontTemporaryFrag";
    public static final String ARG_CLICKEDSHELF = "argClickedShelf";

    private CharSequence clickedShelf;
    private ShelfFrontFragmentListener listener;
    private SQLiteDatabase mDatabase;

    private RecyclerView recyclerViewTempPane;
    private RecyclerView.Adapter articleAdapterTempPane;
    private RecyclerView.LayoutManager layoutManagerTempPane;


    public ShelffrontTemporaryFragment () {
    }


    /**
     * Um verarbeiten zu können, welche Parameter übergeben wurden, muss ein Bundle erstellt werden,
     * dass die übergebenen Parameter als Argumente setzt und somit die Daten in dieser Klasse verfügbar macht.
     *
     * @param clickedShelf ausgewähltes Regal
     * @return RegalfrontFragment wird zurückgegeben mit den übergebenen Werten als Argumente gesetzt.
     */
    public static ShelffrontTemporaryFragment newInstance (CharSequence clickedShelf) {
        ShelffrontTemporaryFragment fragment = new ShelffrontTemporaryFragment();
        Bundle args = new Bundle();
        args.putCharSequence(ARG_CLICKEDSHELF, clickedShelf);
        fragment.setArguments(args);

        return fragment;
    }


    /**
     *
     */
    public interface ShelfFrontFragmentListener {
        void onShelfFrontInputSent (Lagerplatz input);
    }


    /**
     *
     *
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shelf_temporal, container, false);

        if (getArguments() != null) {
            clickedShelf = getArguments().getCharSequence(ARG_CLICKEDSHELF);
        }

        // Context ziehen
        Context context = getActivity();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        mDatabase = dbHelper.getReadableDatabase();
        // Inhalte anzeigen
        initRecyclerViews(v);
        fillRecyclerViews();

        return v;
    }


    /**
     * Das temporäre Lager wird mit Hilfe der RecyclerView dargestellt. Dies wird mittels des
     * ArticleAdapters mit den entsprechenden Daten gefüllt.
     *
     * @param v
     */
    private void initRecyclerViews (View v) {
        recyclerViewTempPane = v.findViewById(R.id.fragment_shelf_temporal_recyclerView);
        recyclerViewTempPane.setHasFixedSize(true);
        layoutManagerTempPane  = new LinearLayoutManager(getActivity());
        articleAdapterTempPane = new ArticleAdapter(new ArrayList<Article>());
        recyclerViewTempPane.setLayoutManager(layoutManagerTempPane);
        recyclerViewTempPane.setAdapter(articleAdapterTempPane);
    }



    /**
     * Das Laden des temporären Lagerplatzes läuft im Hintergrund ab und wird mittels eines
     * AsyncTasks realisiert.
     */
    @SuppressLint ("StaticFieldLeak")
    private class FillRecyclerViewsAsyncTask extends AsyncTask<Integer, Integer, Void> {

        private WeakReference<ShelffrontTemporaryFragment> activityWeakReference;

        FillRecyclerViewsAsyncTask (ShelffrontTemporaryFragment fragment) {
            activityWeakReference = new WeakReference<>(fragment);
        }

        /**
         * Bevor der AsyncTask ausgeführt wird, wird mittels einer WeakReference die Aktivität
         * für das Fragment geladen.
         */
        @Override
        protected void onPreExecute () {
            ShelffrontTemporaryFragment fragment = activityWeakReference.get();
            if (fragment == null || fragment.getActivity().isFinishing()) {
                return;
            }
            Log.d(TAG, "onPreExecute: -------------------------------------- onPreExecute: ");
            super.onPreExecute();
        }

        /**
         * Im Hintergrund des Programmablaufs wird die RecyclerView mit Hilfe des ArticleAdapters
         * mit Daten gefüllt. Mittels eines onItemClickListeners werden werden die Inhalte geladen.
         *
         * @param integers
         * @return
         */
        @Override
        protected Void doInBackground (Integer... integers) {
            ShelffrontTemporaryFragment fragment = activityWeakReference.get();
            Log.d(TAG, "doInBackground: ---------------------------------- doInBackground: ");
            // temporäres Lager nur in Regalfachnummer 5 !!
            final ArrayList<Article> paneContent = fragment.getListWithContents(5);
            articleAdapterTempPane = new ArticleAdapter(paneContent);
            ((ArticleAdapter) articleAdapterTempPane).setOnItemClickListener(
                    new ArticleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick (int position) {
                        }

                        @Override
                        public void onLocationMarkerClick (int position) {
                            Lagerplatz checkedLagerplatz = new Lagerplatz(
                                    paneContent.get(position).getLagerort(),
                                    paneContent.get(position).getLagerplatz(),
                                    paneContent.get(position).getRegalfach());
                            listener.onShelfFrontInputSent(checkedLagerplatz);
                        }
                    });

            return null;
        }

        /**
         * Nachdem alle Inhalte geladen worden sind, wird der AsyncTask beendet.
         *
         * @param aVoid
         */
        @Override
        protected void onPostExecute (Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerViewTempPane.setAdapter(articleAdapterTempPane);
            ShelffrontTemporaryFragment fragment = activityWeakReference.get();
            if (fragment == null || fragment.getActivity().isFinishing()) {
                return;
            }
            Log.d(TAG, "onPostExecute: ------------------------------------ onPostExecute: ");
        }
    }



    /**
     * Die RecyclerView wird im Hintergrund mit den Daten gefüllt.
     */
    public void fillRecyclerViews () {
        FillRecyclerViewsAsyncTask task = new FillRecyclerViewsAsyncTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /**
     *
     *
     * @param regalFachNummer
     * @return Artikelliste
     */
    public ArrayList<Article> getListWithContents (int regalFachNummer) {
        ArrayList<Article> articleList = new ArrayList<Article>();
        int lagerort   = Integer.parseInt(clickedShelf.subSequence(0, 2).toString());
        int regal_nr   = Integer.parseInt(clickedShelf.subSequence(2, 4).toString());
        int zeile      = Integer.parseInt(clickedShelf.subSequence(4, 6).toString());
        int lagerplatz = Integer.parseInt("" + regal_nr + "" + zeile + "" + regalFachNummer + "");
        // raw query durch cursor
        Cursor cursor = mDatabase.rawQuery("SELECT "
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID                        + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER                      + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG                     + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID                       + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID                          + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FERTIGUNGSZUSTAND                 + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_MENGE                             + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_MENGENEINHEIT                     + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN + ""
                + " FROM " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + ""
                + " LEFT JOIN " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME + ""
                + " ON " + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID   + " = "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID
                + " AND " + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID + " = "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID
                + " AND " + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID    + " = "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID
                + " WHERE " + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + "="
                + lagerplatz
                , null);
        try {
            // Zuordnung der Attribute durch cursor
            while (cursor.moveToNext()) {
                int artikelId = cursor.getInt(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID));
                int stuecknummer = cursor.getInt(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER));
                int stueckteilung = cursor.getInt(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG));
                int farbId = cursor.getInt(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID));
                String menge = cursor.getString(cursor.getColumnIndex(
                        TableLagerbestand.LagerbestandEntry.COLUMN_MENGE));
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

                // Artikelliste
                Article article = new Article(artikelId, stuecknummer, stueckteilung, artikelBez,
                        farbId, farbBez, groessenId, fertigungszustand, menge, mengeneinheit, lagerplatz);
                articleList.add(article);
            }
        } finally {
            cursor.close();
        }

        return articleList;
    }



    /**
     *
     * @param context
     */
    @Override
    public void onAttach (@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ShelffrontTemporaryFragment.ShelfFrontFragmentListener) {
            listener = (ShelffrontTemporaryFragment.ShelfFrontFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement Shelffront Fragment Listener");
        }
    }


    /**
     *
     */
    @Override
    public void onDestroy () {
        super.onDestroy();
    }
}
