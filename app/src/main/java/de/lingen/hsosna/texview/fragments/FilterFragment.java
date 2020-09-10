package de.lingen.hsosna.texview.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import de.lingen.hsosna.texview.Article;
import de.lingen.hsosna.texview.DatabaseHelper;
import de.lingen.hsosna.texview.Lagerplatz;
import de.lingen.hsosna.texview.R;
import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;

import static android.view.KeyEvent.KEYCODE_ENTER;
import static de.lingen.hsosna.texview.MainActivity.colorSwitchState;
import static de.lingen.hsosna.texview.MainActivity.freeShelveList;

public class FilterFragment extends Fragment {

    //Slideuppane
    private EditText editArtikelNr;
    private EditText editArtikelBez;
    private EditText editStuecknummer;
    private EditText editStueckteilung;
    private EditText editFardId;
    private EditText editFarbBez;
    private EditText editGroesse;
    private EditText editFertigungszustand;
    private Button button;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase mDatabase;

    private BottomSheetBehavior mBottomSheetBehaviour;


    private ArrayList<Lagerplatz> shelvesToMarkRed = new ArrayList<>();

     /**
     * Um Daten an die MainActivity zu senden wird ein Interface implementiert, was auch in der
     * MainActivity implemnentiert werden muss.
     */

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter, container, false);


        if (colorSwitchState) {
            markFreeShelves(v);
        } else {
            unmarkFreeShelves(v);
        }
        markRegale(v);


        editArtikelNr = v.findViewById(R.id.filterFragment_editText_articleId);
        editArtikelBez = v.findViewById(R.id.filterFragment_editText_articleShortDesc);
        editStuecknummer = v.findViewById(R.id.filterFragment_editText_pieceId);
        editStueckteilung = v.findViewById(R.id.filterFragment_editText_pieceDivision);
        editFardId = v.findViewById(R.id.filterFragment_editText_colorId);
        editFarbBez = v.findViewById(R.id.filterFragment_editText_colorDescription);
        editGroesse = v.findViewById(R.id.filterFragment_editText_size);
        editFertigungszustand = v.findViewById(R.id.filterFragment_editText_manufacturingState);

        editArtikelNr.setOnEditorActionListener(onEditorActionListener);
        editArtikelBez.setOnEditorActionListener(onEditorActionListener);
        editStuecknummer.setOnEditorActionListener(onEditorActionListener);
        editStueckteilung.setOnEditorActionListener(onEditorActionListener);
        editFardId.setOnEditorActionListener(onEditorActionListener);
        editFarbBez.setOnEditorActionListener(onEditorActionListener);
        editGroesse.setOnEditorActionListener(onEditorActionListener);
        editFertigungszustand.setOnEditorActionListener(onEditorActionListener);

        button = v.findViewById(R.id.filterFragment_button_submit);
        dbHelper = new DatabaseHelper(getActivity());
        mDatabase = dbHelper.getReadableDatabase();


        View bottomSheet = v.findViewById(R.id.filterFragment_searchSlideUpPane);
        mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                performFilter();
            }
        });
        return v;
    }

    public void performFilter () {
        unmarkShelves(getView());
        if (colorSwitchState) {
            markFreeShelves(getView());
        }
        String SqlWhereQuery = getSqlWhereQuery();
        if (SqlWhereQuery.length() != 0) {
            final ArrayList<Article> suchErgebnisse = getListWithSearchResults(SqlWhereQuery);

            shelvesToMarkRed = getShelvesToMarkFromFilterResults(suchErgebnisse);


            markRegale(getView()); // view missing
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            //TODO ERROR MESSAGE DISPLAY
            CharSequence errorMessage = "Bitte füllen Sie mindestens ein Feld aus";
        }
    }

    public ArrayList<Lagerplatz> getShelvesToMarkRed (){
        return shelvesToMarkRed;
    }



    public ArrayList<Lagerplatz> getShelvesToMarkFromFilterResults (
            ArrayList<Article> suchErgebnisse) {
        ArrayList<Lagerplatz> shelvestoMark = new ArrayList<>();
        for (Article article : suchErgebnisse) {
            shelvestoMark.add(article.getLagerplatzObject());
        }
        return shelvestoMark;
    }

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KEYCODE_ENTER){
                performFilter();
                return true;
            }
            return false;
        }
    };

    public void markRegale (View v) {
        if (shelvesToMarkRed != null && shelvesToMarkRed.size() != 0) {
            ArrayList<View> imageViewsOfShelvesToMark = new ArrayList<>();
            for (Lagerplatz lagerplatz : shelvesToMarkRed) {
                v.findViewsWithText(imageViewsOfShelvesToMark, lagerplatz.getLocation(),
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
            }
            for (View singleShelf : imageViewsOfShelvesToMark) {
                ImageView oneShelf = (ImageView) singleShelf;
                oneShelf.setImageResource(R.drawable.ic_shelf_marked_red);
            }
        }
    }

    public void unmarkShelves (View v){
        if (shelvesToMarkRed != null && shelvesToMarkRed.size() != 0) {
            ArrayList<View> imageViewsOfShelvesToMark = new ArrayList<>();
            for (Lagerplatz lagerplatz : shelvesToMarkRed) {
                v.findViewsWithText(imageViewsOfShelvesToMark, lagerplatz.getLocation(),
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
            }
            for (View singleShelf : imageViewsOfShelvesToMark) {
                ImageView oneShelf = (ImageView) singleShelf;
                oneShelf.setImageResource(R.drawable.ic_shelf_normal);
            }
        }
    }

    public void markFreeShelves (View v) {
        if (colorSwitchState && v != null) {
            if (freeShelveList != null && freeShelveList.size() != 0) {
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeLow = new ArrayList<>();
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeMedium = new ArrayList<>();
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeHigh = new ArrayList<>();
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeFull = new ArrayList<>();
                int numberOfFreeShelfCompartments = 1;
                int lastShelfLocation = 0;
                for (Lagerplatz lagerplatz : freeShelveList) {
                    boolean contains = false;
                    for (Lagerplatz shelveToMarkRed : shelvesToMarkRed) {
                        if (shelveToMarkRed.getLocation().equals(lagerplatz.getLocation())) {
                            contains = true;
                        }
                    }
                    if (! contains) {
                        if (lagerplatz.getLagerplatz() != lastShelfLocation) { // nicht der davor
                            lastShelfLocation = lagerplatz.getLagerplatz();
                            numberOfFreeShelfCompartments = 1;
                        } else {
                            numberOfFreeShelfCompartments++;
                        }
                        switch (numberOfFreeShelfCompartments) {
                            case 1:
                            case 2:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeLow,
                                        lagerplatz.getLocation(),
                                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                            case 3:
                            case 4:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeMedium,
                                        lagerplatz.getLocation(),
                                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                            case 5:
                            case 6:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeHigh,
                                        lagerplatz.getLocation(),
                                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                            case 7:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeFull,
                                        lagerplatz.getLocation(),
                                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                        }
                    }
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeLow) {
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_low);
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeMedium) {
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_medium);
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeHigh) {
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_high);
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeFull) {
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_full);
                }
            }
        }
    }

    public void unmarkFreeShelves (View v) {
        if (v != null) {
            if (freeShelveList != null && freeShelveList.size() != 0) {
                ArrayList<View> imageViewsOfShelvesToMarkAsFree = new ArrayList<>();
                for (Lagerplatz lagerplatz : freeShelveList) {
                    boolean contains = false;
                    for (Lagerplatz shelveToMarkRed : shelvesToMarkRed) {
                        if (shelveToMarkRed.getLocation().equals(lagerplatz.getLocation())) {
                            contains = true;
                        }
                    }
                    if (! contains) // for schleife
                    {
                        v.findViewsWithText(imageViewsOfShelvesToMarkAsFree,
                                lagerplatz.getLocation(),
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
                + SqlWhereQuery// + " LIMIT 201 "
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
        if(editArtikelBez.getText().toString().trim().length() != 0){
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
        if(editFarbBez.getText().toString().trim().length() != 0){
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
