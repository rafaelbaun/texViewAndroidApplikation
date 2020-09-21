package de.lingen.hsosna.texview.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import static de.lingen.hsosna.texview.MainActivity.colorSwitchState;
import static de.lingen.hsosna.texview.MainActivity.freeShelveList;

public class RegalfrontFragment extends Fragment {
    private static final String TAG = "RegalfrontFragment";
    public static final String ARG_SHELVESTOMARKRED = "argShelvesToMarkRed";
    public static final String ARG_CLICKEDSHELF = "argClickedShelf";
    private ArrayList<Lagerplatz> shelvesToMarkRed = new ArrayList<>();
    private CharSequence clickedShelf;
    private ShelfFrontFragmentListener listener;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase mDatabase;
    private RecyclerView recyclerViewPane1;
    private RecyclerView.Adapter articleAdapterPane1;
    private RecyclerView.LayoutManager layoutManagerPane1;
    private RecyclerView recyclerViewPane2;
    private RecyclerView.Adapter articleAdapterPane2;
    private RecyclerView.LayoutManager layoutManagerPane2;
    private RecyclerView recyclerViewPane3;
    private RecyclerView.Adapter articleAdapterPane3;
    private RecyclerView.LayoutManager layoutManagerPane3;
    private RecyclerView recyclerViewPane4;
    private RecyclerView.Adapter articleAdapterPane4;
    private RecyclerView.LayoutManager layoutManagerPane4;
    private RecyclerView recyclerViewPane5;
    private RecyclerView.Adapter articleAdapterPane5;
    private RecyclerView.LayoutManager layoutManagerPane5;
    private RecyclerView recyclerViewPane6;
    private RecyclerView.Adapter articleAdapterPane6;
    private RecyclerView.LayoutManager layoutManagerPane6;
    private RecyclerView recyclerViewPane7;
    private RecyclerView.Adapter articleAdapterPane7;
    private RecyclerView.LayoutManager layoutManagerPane7;
    private ProgressBar progressBar;
    private AlertDialog alertDialog;

    /**
     * Um verarbeiten zu können, welche Parameter übergeben wurden, muss ein Bundle erstellt werden,
     * dass die übergebenen Parameter als Argumente setzt und somit die Daten in dieser Klasse verfügbar macht.
     *
     * @return Ein RegalfrontFragment wird zurückgegeben mit den übergebenen Werten als Argumente gesetzt.
     */
    public static RegalfrontFragment newInstance (ArrayList<Lagerplatz> shelvesToMarkRed,
                                                  CharSequence clickedShelf) {
        RegalfrontFragment fragment = new RegalfrontFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SHELVESTOMARKRED,
                (ArrayList<? extends Parcelable>) shelvesToMarkRed);
        args.putCharSequence(ARG_CLICKEDSHELF, clickedShelf);
        fragment.setArguments(args);
        return fragment;
    }

    public interface ShelfFrontFragmentListener {
        void onShelfFrontInputSent (Lagerplatz input);
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shelf_frontal, container, false);
        progressBar = v.findViewById(R.id.shelfProgressBar);
        ((ViewGroup) progressBar.getParent()).removeView(progressBar);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.fragment_shelf_frontal_loadingTitle);
        alertDialogBuilder.setMessage(R.string.fragment_shelf_frontal_loadingDescription);
        alertDialogBuilder.setView(progressBar);
        alertDialogBuilder.setCancelable(false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(lp);
        progressBar.setPadding(20, 0, 20, 0);
        alertDialog = alertDialogBuilder.create();
        /////////////
        Context context = getActivity();
        dbHelper = new DatabaseHelper(context);
        mDatabase = dbHelper.getReadableDatabase();
        TextView textView = v.findViewById(
                R.id.fragment_shelf_frontal_shelfonly_textView_shelfNumber);
        if (getArguments() != null) {
            shelvesToMarkRed = getArguments().getParcelableArrayList(ARG_SHELVESTOMARKRED);
            clickedShelf = getArguments().getCharSequence(ARG_CLICKEDSHELF);
        }
        if (colorSwitchState) {
            markFreeShelves(v);
        } else {
            unmarkFreeShelves(v);
        }
        markFaecher(v);
        String regalPrefix = "RegalNr: ";
        textView.setText(regalPrefix.concat((String) clickedShelf));
        for (TextView slideUpPaneHeader : getSlideUpPaneHeadersArrayList(v)) {
            String slideUpPaneHeaderText = (String) slideUpPaneHeader.getText();
            slideUpPaneHeader.setText(slideUpPaneHeaderText.concat((String) clickedShelf));
        }
        initRecyclerViews(v);
        fillRecyclerViews();
        return v;
    }

    private void initRecyclerViews (View v) {
        recyclerViewPane1 = v.findViewById(R.id.slideUp_recyclerView_compartment01);
        recyclerViewPane1.setHasFixedSize(true);
        layoutManagerPane1 = new LinearLayoutManager(getActivity());
        articleAdapterPane1 = new ArticleAdapter(new ArrayList<Article>());
        recyclerViewPane1.setLayoutManager(layoutManagerPane1);
        recyclerViewPane1.setAdapter(articleAdapterPane1);
        recyclerViewPane2 = v.findViewById(R.id.slideUp_recyclerView_compartment02);
        recyclerViewPane2.setHasFixedSize(true);
        layoutManagerPane2 = new LinearLayoutManager(getActivity());
        articleAdapterPane2 = new ArticleAdapter(new ArrayList<Article>());
        recyclerViewPane2.setLayoutManager(layoutManagerPane2);
        recyclerViewPane2.setAdapter(articleAdapterPane2);
        recyclerViewPane3 = v.findViewById(R.id.slideUp_recyclerView_compartment03);
        recyclerViewPane3.setHasFixedSize(true);
        layoutManagerPane3 = new LinearLayoutManager(getActivity());
        articleAdapterPane3 = new ArticleAdapter(new ArrayList<Article>());
        recyclerViewPane3.setLayoutManager(layoutManagerPane3);
        recyclerViewPane3.setAdapter(articleAdapterPane3);
        recyclerViewPane4 = v.findViewById(R.id.slideUp_recyclerView_compartment04);
        recyclerViewPane4.setHasFixedSize(true);
        layoutManagerPane4 = new LinearLayoutManager(getActivity());
        articleAdapterPane4 = new ArticleAdapter(new ArrayList<Article>());
        recyclerViewPane4.setLayoutManager(layoutManagerPane4);
        recyclerViewPane4.setAdapter(articleAdapterPane4);
        recyclerViewPane5 = v.findViewById(R.id.slideUp_recyclerView_compartment05);
        recyclerViewPane5.setHasFixedSize(true);
        layoutManagerPane5 = new LinearLayoutManager(getActivity());
        articleAdapterPane5 = new ArticleAdapter(new ArrayList<Article>());
        recyclerViewPane5.setLayoutManager(layoutManagerPane5);
        recyclerViewPane5.setAdapter(articleAdapterPane5);
        recyclerViewPane6 = v.findViewById(R.id.slideUp_recyclerView_compartment06);
        recyclerViewPane6.setHasFixedSize(true);
        layoutManagerPane6 = new LinearLayoutManager(getActivity());
        articleAdapterPane6 = new ArticleAdapter(new ArrayList<Article>());
        recyclerViewPane6.setLayoutManager(layoutManagerPane6);
        recyclerViewPane6.setAdapter(articleAdapterPane6);
        recyclerViewPane7 = v.findViewById(R.id.slideUp_recyclerView_compartment07);
        recyclerViewPane7.setHasFixedSize(true);
        layoutManagerPane7 = new LinearLayoutManager(getActivity());
        articleAdapterPane7 = new ArticleAdapter(new ArrayList<Article>());
        recyclerViewPane7.setLayoutManager(layoutManagerPane7);
        recyclerViewPane7.setAdapter(articleAdapterPane7);
    }

    @SuppressLint ("StaticFieldLeak")
    private class FillRecyclerViewsAsyncTask extends AsyncTask<Integer,Integer,Void> {
        private WeakReference<RegalfrontFragment> activityWeakReference;

        FillRecyclerViewsAsyncTask (RegalfrontFragment fragment) {
            activityWeakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute () {
            RegalfrontFragment fragment = activityWeakReference.get();
            if (fragment == null || fragment.getActivity().isFinishing()) {
                return;
            }
            Log.d(TAG, "onPreExecute: -------------------------------------- onPreExecute: ");
            super.onPreExecute();
            fragment.alertDialog.show();
            fragment.progressBar.setVisibility(View.VISIBLE);
        }

        //adapter werden gesetzt mit onclick listeners
        @Override
        protected Void doInBackground (Integer... integers) {
            RegalfrontFragment fragment = activityWeakReference.get();
            Log.d(TAG, "doInBackground: -------------------- do in background ");
            for (int i = 1; i <= integers[0]; i++) {
                switch (i) {
                    case 1:
                        final ArrayList<Article> pane1Content = fragment.getListWithContents(i);
                        articleAdapterPane1 = new ArticleAdapter(pane1Content);
                        ((ArticleAdapter) articleAdapterPane1).setOnItemClickListener(
                                new ArticleAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick (int position) {
                                    }

                                    @Override
                                    public void onLocationMarkerClick (int position) {
                                        Lagerplatz checkedLagerplatz = new Lagerplatz(
                                                pane1Content.get(position).getLagerort(),
                                                pane1Content.get(position).getLagerplatz(),
                                                pane1Content.get(position).getRegalfach());
                                        listener.onShelfFrontInputSent(checkedLagerplatz);
                                    }
                                });
                        break;
                    case 2:
                        final ArrayList<Article> pane2Content = fragment.getListWithContents(i);
                        articleAdapterPane2 = new ArticleAdapter(pane2Content);
                        ((ArticleAdapter) articleAdapterPane2).setOnItemClickListener(
                                new ArticleAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick (int position) {
                                    }

                                    @Override
                                    public void onLocationMarkerClick (int position) {
                                        Lagerplatz checkedLagerplatz = new Lagerplatz(
                                                pane2Content.get(position).getLagerort(),
                                                pane2Content.get(position).getLagerplatz(),
                                                pane2Content.get(position).getRegalfach());
                                        listener.onShelfFrontInputSent(checkedLagerplatz);
                                    }
                                });
                        break;
                    case 3:
                        final ArrayList<Article> pane3Content = fragment.getListWithContents(i);
                        articleAdapterPane3 = new ArticleAdapter(pane3Content);
                        ((ArticleAdapter) articleAdapterPane3).setOnItemClickListener(
                                new ArticleAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick (int position) {
                                    }

                                    @Override
                                    public void onLocationMarkerClick (int position) {
                                        Lagerplatz checkedLagerplatz = new Lagerplatz(
                                                pane3Content.get(position).getLagerort(),
                                                pane3Content.get(position).getLagerplatz(),
                                                pane3Content.get(position).getRegalfach());
                                        listener.onShelfFrontInputSent(checkedLagerplatz);
                                    }
                                });
                        break;
                    case 4:
                        final ArrayList<Article> pane4Content = fragment.getListWithContents(i);
                        articleAdapterPane4 = new ArticleAdapter(pane4Content);
                        ((ArticleAdapter) articleAdapterPane4).setOnItemClickListener(
                                new ArticleAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick (int position) {
                                    }

                                    @Override
                                    public void onLocationMarkerClick (int position) {
                                        Lagerplatz checkedLagerplatz = new Lagerplatz(
                                                pane4Content.get(position).getLagerort(),
                                                pane4Content.get(position).getLagerplatz(),
                                                pane4Content.get(position).getRegalfach());
                                        listener.onShelfFrontInputSent(checkedLagerplatz);
                                    }
                                });
                        break;
                    case 5:
                        final ArrayList<Article> pane5Content = fragment.getListWithContents(i);
                        articleAdapterPane5 = new ArticleAdapter(pane5Content);
                        ((ArticleAdapter) articleAdapterPane5).setOnItemClickListener(
                                new ArticleAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick (int position) {
                                    }

                                    @Override
                                    public void onLocationMarkerClick (int position) {
                                        Lagerplatz checkedLagerplatz = new Lagerplatz(
                                                pane5Content.get(position).getLagerort(),
                                                pane5Content.get(position).getLagerplatz(),
                                                pane5Content.get(position).getRegalfach());
                                        listener.onShelfFrontInputSent(checkedLagerplatz);
                                    }
                                });
                        break;
                    case 6:
                        final ArrayList<Article> pane6Content = fragment.getListWithContents(i);
                        articleAdapterPane6 = new ArticleAdapter(pane6Content);
                        ((ArticleAdapter) articleAdapterPane6).setOnItemClickListener(
                                new ArticleAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick (int position) {
                                    }

                                    @Override
                                    public void onLocationMarkerClick (int position) {
                                        Lagerplatz checkedLagerplatz = new Lagerplatz(
                                                pane6Content.get(position).getLagerort(),
                                                pane6Content.get(position).getLagerplatz(),
                                                pane6Content.get(position).getRegalfach());
                                        listener.onShelfFrontInputSent(checkedLagerplatz);
                                    }
                                });
                        break;
                    case 7:
                        final ArrayList<Article> pane7Content = fragment.getListWithContents(i);
                        articleAdapterPane7 = new ArticleAdapter(pane7Content);
                        ((ArticleAdapter) articleAdapterPane7).setOnItemClickListener(
                                new ArticleAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick (int position) {
                                    }

                                    @Override
                                    public void onLocationMarkerClick (int position) {
                                        Lagerplatz checkedLagerplatz = new Lagerplatz(
                                                pane7Content.get(position).getLagerort(),
                                                pane7Content.get(position).getLagerplatz(),
                                                pane7Content.get(position).getRegalfach());
                                        listener.onShelfFrontInputSent(checkedLagerplatz);
                                    }
                                });
                        break;
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate (Integer... values) {
            Log.d(TAG, "onProgressUpdate: " + (values[0] * 100) / 7);
            switch (values[0]) {
                case 1:
                    recyclerViewPane1.setAdapter(articleAdapterPane1);
                    break;
                case 2:
                    recyclerViewPane2.setAdapter(articleAdapterPane2);
                    break;
                case 3:
                    recyclerViewPane3.setAdapter(articleAdapterPane3);
                    break;
                case 4:
                    recyclerViewPane4.setAdapter(articleAdapterPane4);
                    break;
                case 5:
                    recyclerViewPane5.setAdapter(articleAdapterPane5);
                    break;
                case 6:
                    recyclerViewPane6.setAdapter(articleAdapterPane6);
                    break;
                case 7:
                    recyclerViewPane7.setAdapter(articleAdapterPane7);
                    break;
            }
            progressBar.setProgress((values[0] * 100) / 7);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute (Void aVoid) {
            super.onPostExecute(aVoid);
            RegalfrontFragment fragment = activityWeakReference.get();
            if (fragment == null || fragment.getActivity().isFinishing()) {
                return;
            }
            fragment.alertDialog.dismiss();
            fragment.progressBar.setProgress(0);
            fragment.progressBar.setVisibility(View.INVISIBLE);
            Log.d(TAG,
                    "onPostExecute: ------------------------------------------------------------------------------------ ONPOSTEXECUTE ");
        }
    }

    public ArrayList<TextView> getSlideUpPaneHeadersArrayList (View v) {
        ArrayList<TextView> slideUpPaneHeaders = new ArrayList<>();
        slideUpPaneHeaders.add(
                (TextView) v.findViewById(R.id.bottomsheet_slideup_shelfcompartments_header1));
        slideUpPaneHeaders.add(
                (TextView) v.findViewById(R.id.bottomsheet_slideup_shelfcompartments_header2));
        slideUpPaneHeaders.add(
                (TextView) v.findViewById(R.id.bottomsheet_slideup_shelfcompartments_header3));
        slideUpPaneHeaders.add(
                (TextView) v.findViewById(R.id.bottomsheet_slideup_shelfcompartments_header4));
        slideUpPaneHeaders.add(
                (TextView) v.findViewById(R.id.bottomsheet_slideup_shelfcompartments_header5));
        slideUpPaneHeaders.add(
                (TextView) v.findViewById(R.id.bottomsheet_slideup_shelfcompartments_header6));
        slideUpPaneHeaders.add(
                (TextView) v.findViewById(R.id.bottomsheet_slideup_shelfcompartments_header7));
        return slideUpPaneHeaders;
    }

    /**
     * Der RecyclerView wird mit den Daten des ausgewählten Regals gefüllt
     */
    public void fillRecyclerViews () {
        FillRecyclerViewsAsyncTask task = new FillRecyclerViewsAsyncTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 7);
    }

    public ArrayList<Article> getListWithContents (int regalFachNummer) {
        ArrayList<Article> articleList = new ArrayList<Article>();
        int lagerort = Integer.parseInt(clickedShelf.subSequence(0, 2).toString());
        int regal_nr = Integer.parseInt(clickedShelf.subSequence(2, 4).toString());
        int zeile = Integer.parseInt(clickedShelf.subSequence(4, 6).toString());
        int lagerplatz = Integer.parseInt("" + regal_nr + "" + zeile + "" + regalFachNummer + "");
        Cursor cursor = mDatabase.rawQuery(
                "SELECT " + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + ", "
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
                + " WHERE " + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + "="
                + lagerplatz
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
                Article article = new Article(artikelId, stuecknummer, stueckteilung, artikelBez,
                        farbId, farbBez, groessenId,
                        fertigungszustand, menge, mengeneinheit, lagerplatz);
                articleList.add(article);
            }
        } finally {
            cursor.close();
        }
        return articleList;
    }

    public void markFaecher (View v) {
        if (shelvesToMarkRed != null && shelvesToMarkRed.size() != 0) {
            ArrayList<View> imageViewsOfShelvesToMark = new ArrayList<>();
            for (Lagerplatz lagerplatz : shelvesToMarkRed) {
                if (lagerplatz.getLocation().equals(clickedShelf)) {
                    v.findViewsWithText(imageViewsOfShelvesToMark, "" + lagerplatz.getRegalfach(),
                            View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                }
            }
            for (View singleShelf : imageViewsOfShelvesToMark) {
                ImageView oneShelf = (ImageView) singleShelf;
                oneShelf.setImageResource(R.drawable.ic_shelf_marked_red);
            }
        }
    }

    public void markFreeShelves (View v) {
        if (colorSwitchState && v != null) {
            if (freeShelveList != null && freeShelveList.size() != 0) {
                ArrayList<View> imageViewsOfShelvesToMarkAsFree = new ArrayList<>();
                for (Lagerplatz lagerplatz : freeShelveList) {
                    if (lagerplatz.getLocation().equals(clickedShelf)) {
                        v.findViewsWithText(imageViewsOfShelvesToMarkAsFree,
                                "" + lagerplatz.getRegalfach(),
                                View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    }
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFree) {
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_high);
                }
            }
        }
    }

    public void unmarkFreeShelves (View v) {
        if (v != null) {
            if (freeShelveList != null && freeShelveList.size() != 0) {
                ArrayList<View> imageViewsOfShelvesToMarkAsFree = new ArrayList<>();
                for (Lagerplatz lagerplatz : freeShelveList) {
                    if (lagerplatz.getLocation().equals(clickedShelf)) {
                        v.findViewsWithText(imageViewsOfShelvesToMarkAsFree,
                                "" + lagerplatz.getRegalfach(),
                                View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    }
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFree) {
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_normal);
                }
            }
        }
    }

    @Override
    public void onAttach (@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegalfrontFragment.ShelfFrontFragmentListener) {
            listener = (RegalfrontFragment.ShelfFrontFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement Shelffront Fragment Listener");
        }
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
    }
}
