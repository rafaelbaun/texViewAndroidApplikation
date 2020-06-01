package de.lingen.hsosna.texview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;

public class SearchFragment extends Fragment {
    private SearchFragmentListener listener;
    private EditText editText;
    private EditText editText2;
    private Button button;


    private GroceryDBHelper dbHelper;
    private SQLiteDatabase mDatabase;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BottomSheetBehavior mBottomSheetBehaviour;


    /**
     * Interface um Daten an die MainActivity zu senden.
     */
    public interface SearchFragmentListener {
        void onSearchInputSent (CharSequence input);
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);


        editText = v.findViewById(R.id.editText);
        editText2 = v.findViewById(R.id.editText2);
        button = v.findViewById(R.id.submitButton);

        Context context = getActivity();
        dbHelper = new GroceryDBHelper(context);
        mDatabase = dbHelper.getReadableDatabase();


        mRecyclerView = v.findViewById(R.id.recyclerVgvhgview_fach01);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        View bottomSheet = v.findViewById(R.id.slideUpjmvgmjhPaneFach01);

        mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                CharSequence input = editText.getText();
                CharSequence input2 = editText2.getText();
                listener.onSearchInputSent(input);



                mAdapter = new ExampleAdapter(getListWithContents());// LIST WITH CONTENTS
                mRecyclerView.setAdapter(mAdapter);

                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

                //listener.onSearchInputSent(input2);
            }
        });


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




    public ArrayList<Artikel> getListWithContents () {
        ArrayList<Artikel> artikelListe = new ArrayList<Artikel>();
//        int lagerort = Integer.parseInt(regalID.subSequence(0, 2).toString());
//        int regal_nr = Integer.parseInt(regalID.subSequence(2, 4).toString());
//        int zeile = Integer.parseInt(regalID.subSequence(4, 6).toString());
        int lagerplatz = 1017;
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
                        fertigungszustand, menge, mengeneinheit);
                artikelListe.add(artikel);
            }
        } finally {
            cursor.close();
        }
        return artikelListe;
    }
}
