package de.lingen.hsosna.texview.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    public static final String ARG_SHELVESTOMARKRED = "argShelvesToMarkRed";
    public static final String ARG_CLICKEDSHELF = "argClickedShelf";
    private ArrayList<Lagerplatz> shelvesToMarkRed = new ArrayList<>();
    private CharSequence clickedShelf;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase mDatabase;

    /**
     * Um verarbeiten zu können, welche Parameter übergeben wurden, muss ein Bundle erstellt werden,
     * dass die übergebenen Parameter als Argumente setzt und somit die Daten in dieser Klasse verfügbar macht.
     *
     * @return Ein RegalfrontFragment wird zurückgegeben mit den übergebenen Werten als Argumente gesetzt.
     */
    public static RegalfrontFragment newInstance (ArrayList<Lagerplatz> shelvesToMarkRed, CharSequence clickedShelf) {
        RegalfrontFragment fragment = new RegalfrontFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SHELVESTOMARKRED, (ArrayList<? extends Parcelable>) shelvesToMarkRed);
        args.putCharSequence(ARG_CLICKEDSHELF, clickedShelf);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        //Klappt View v = inflater.inflate(R.layout.bottomsheet_test, container, false);
        View v = inflater.inflate(R.layout.fragment_shelf_frontal, container, false);
        Context context = getActivity();
        dbHelper = new DatabaseHelper(context);
        mDatabase = dbHelper.getReadableDatabase();
        TextView textView = v.findViewById(R.id.fragment_shelf_frontal_shelfonly_textView_shelfNumber);
        if (getArguments() != null) {
            shelvesToMarkRed = getArguments().getParcelableArrayList(ARG_SHELVESTOMARKRED);
            clickedShelf = getArguments().getCharSequence(ARG_CLICKEDSHELF);
        }
        //clickedShelf = v.getContentDescription();

        if(colorSwitchState){
            markFreeShelves(v);
        } else {
            unmarkFreeShelves(v);
        }


        markFaecher(v);

        String regalPrefix = "RegalNr: ";
        textView.setText(regalPrefix.concat((String) clickedShelf));


        for (TextView slideUpPaneHeader : getSlideUpPaneHeadersArrayList(v)){
            String slideUpPaneHeaderText = (String)slideUpPaneHeader.getText();
            slideUpPaneHeader.setText(slideUpPaneHeaderText.concat((String) clickedShelf));
        }
        fillRecyclerView(v);
        return v;
    }

    public ArrayList<TextView> getSlideUpPaneHeadersArrayList(View v){
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
     *
     * @param v Der aktuelle View wird übergeben
     */
    public void fillRecyclerView (View v) {
        mRecyclerView = v.findViewById(R.id.slideUp_recyclerView_compartment01);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ArticleAdapter(getListWithContents(1));// LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.slideUp_recyclerView_compartment02);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ArticleAdapter(getListWithContents(2));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.slideUp_recyclerView_compartment03);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ArticleAdapter(getListWithContents(3));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.slideUp_recyclerView_compartment04);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ArticleAdapter(getListWithContents(4));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.slideUp_recyclerView_compartment05);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ArticleAdapter(getListWithContents(5));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.slideUp_recyclerView_compartment06);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ArticleAdapter(getListWithContents(6));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.slideUp_recyclerView_compartment07);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ArticleAdapter(getListWithContents(7));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
                Article article = new Article(artikelId, stuecknummer, stueckteilung, artikelBez, farbId, farbBez, groessenId,
                        fertigungszustand, menge, mengeneinheit, lagerplatz);
                articleList.add(article);
            }
        } finally {
            cursor.close();
        }
        return articleList;
    }

    public void markFaecher(View v){
        if(shelvesToMarkRed != null && shelvesToMarkRed.size() != 0){
            ArrayList<View> imageViewsOfShelvesToMark = new ArrayList<>();
            for (Lagerplatz lagerplatz : shelvesToMarkRed){
                if(lagerplatz.getLocation().equals(clickedShelf)){
                    v.findViewsWithText(imageViewsOfShelvesToMark, "" + lagerplatz.getRegalfach(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                }
            }
            for (View singleShelf : imageViewsOfShelvesToMark){
                ImageView oneShelf = (ImageView) singleShelf;
                oneShelf.setImageResource(R.drawable.ic_shelf_marked_red);
            }

        }
    }

    public void markFreeShelves (View v) {
        if (colorSwitchState && v != null) {
            if (freeShelveList != null && freeShelveList.size() != 0) {
                ArrayList<View> imageViewsOfShelvesToMarkAsFree = new ArrayList<>();

                for(Lagerplatz lagerplatz : freeShelveList){
                    if (lagerplatz.getLocation().equals(clickedShelf)){
                        v.findViewsWithText(imageViewsOfShelvesToMarkAsFree, "" + lagerplatz.getRegalfach(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    }
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFree){
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

                for(Lagerplatz lagerplatz : freeShelveList){
                    if (lagerplatz.getLocation().equals(clickedShelf)){
                        v.findViewsWithText(imageViewsOfShelvesToMarkAsFree, "" + lagerplatz.getRegalfach(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    }
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFree){
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_normal);
                }
            }
        }
    }
}
