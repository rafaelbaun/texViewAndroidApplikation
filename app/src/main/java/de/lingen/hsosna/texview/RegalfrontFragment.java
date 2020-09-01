package de.lingen.hsosna.texview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;

import static de.lingen.hsosna.texview.MainActivity.colorSwitchState;

public class RegalfrontFragment extends Fragment {
    public static final String ARG_REGALNUMMER = "argRegalNummer";
    public static final String ARG_FACHNUMMER = "argFachNummer";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroceryDBHelper dbHelper;
    private SQLiteDatabase mDatabase;
    private CharSequence regalID;
    private ArrayList<CharSequence> faecherToMark = new ArrayList<>();

    /**
     * Um verarbeiten zu können, welche Parameter übergeben wurden, muss ein Bundle erstellt werden,
     * dass die übergebenen Parameter als Argumente setzt und somit die Daten in dieser Klasse verfügbar macht.
     *
     * @param regalNummer Regalnummer des darzustellenden Regales
     * @param fachNummer
     * @return Ein RegalfrontFragment wird zurückgegeben mit den übergebenen Werten als Argumente gesetzt.
     */
    public static RegalfrontFragment newInstance (CharSequence regalNummer,
                                                  ArrayList<CharSequence> fachNummer) {
        RegalfrontFragment fragment = new RegalfrontFragment();
        Bundle args = new Bundle();
        args.putCharSequence(ARG_REGALNUMMER, regalNummer);
        args.putCharSequenceArrayList(ARG_FACHNUMMER, fachNummer);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_regalfront, container, false);
        View v = inflater.inflate(R.layout.bottomsheet_test, container, false);
        Context context = getActivity();
        dbHelper = new GroceryDBHelper(context);
        mDatabase = dbHelper.getReadableDatabase();
        TextView textView = v.findViewById(R.id.textviewRegalfachbezeichnung);
        if (getArguments() != null) {
            regalID = getArguments().getCharSequence(ARG_REGALNUMMER);
            faecherToMark = getArguments().getCharSequenceArrayList(ARG_FACHNUMMER);
        }

        if(colorSwitchState){
            ImageView abcdef = v.findViewById(R.id.fach07);
            abcdef.setImageResource(R.drawable.ic_regal_free);
        }


        markFaecher(v);

        String regalPrefix = "RegalNr: ";
        textView.setText(regalPrefix.concat((String) regalID));
        fillRecyclerView(v);
        return v;
    }

    /**
     * Der RecyclerView wird mit den Daten des ausgewählten Regals gefüllt
     *
     * @param v Der aktuelle View wird übergeben
     */
    public void fillRecyclerView (View v) {
        mRecyclerView = v.findViewById(R.id.recyclerView_fach01);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(getListWithContents(1));// LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach02);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(getListWithContents(2));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach03);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(getListWithContents(3));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach04);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(getListWithContents(4));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach05);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(getListWithContents(5));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach06);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(getListWithContents(6));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach07);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(getListWithContents(7));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Eine ArrayList wird mit Artikeln gefüllt die sich in dem Regal befinden, welches übergeben wird
     *
     * @param regalFachNummer Die Regalfachnummer wird als Integer übergeben
     * @return Eine ArrayListe mit Objekten der Klasse "Artikel" wird zurückgegeben
     */
    /*public ArrayList<Artikel> fillArrayWithData (int regalFachNummer) {
        ArrayList<Artikel> artikelListe = new ArrayList<Artikel>();
        switch (regalFachNummer) {
            case 1:
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                break;
            case 2:
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                break;
            case 3:
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                break;
            case 4:
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                break;
            case 5:
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                break;
            case 6:
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                break;
            case 7:
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                artikelListe.add(
                        new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,
                                "FW", "44", "m"));
                break;
        }
        return artikelListe;
    }*/

    public ArrayList<Artikel> getListWithContents (int regalFachNummer) {
        ArrayList<Artikel> artikelListe = new ArrayList<Artikel>();
        int lagerort = Integer.parseInt(regalID.subSequence(0, 2).toString());
        int regal_nr = Integer.parseInt(regalID.subSequence(2, 4).toString());
        int zeile = Integer.parseInt(regalID.subSequence(4, 6).toString());
        int lagerplatz = Integer.parseInt("" + regal_nr + "" + zeile + "" + regalFachNummer + "");
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
                + " WHERE " + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + "="
                + lagerplatz
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
                        fertigungszustand, menge, mengeneinheit, lagerplatz);
                artikelListe.add(artikel);
            }
        } finally {
            cursor.close();
        }
        return artikelListe;
    }

    public void markFaecher(View v){
        if(faecherToMark != null && faecherToMark.size() != 0){
            ArrayList<View> imageViewsOfShelvesToMark = new ArrayList<>();
            for (CharSequence fach : faecherToMark){
                v.findViewsWithText(imageViewsOfShelvesToMark, fach, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                for (View singleShelf : imageViewsOfShelvesToMark){
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_regal_marked);
                }
            }

        }

    }
}
