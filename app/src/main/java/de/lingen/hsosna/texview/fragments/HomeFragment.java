package de.lingen.hsosna.texview.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import de.lingen.hsosna.texview.Lagerplatz;
import de.lingen.hsosna.texview.R;

import static de.lingen.hsosna.texview.MainActivity.colorSwitchState;
import static de.lingen.hsosna.texview.MainActivity.freeShelveList;

/**
 * HomeFragment
 */
public class HomeFragment extends Fragment {

    public static final String ARG_SHELVESTOMARKRED = "argShelvesToMarkRed";
    private ArrayList<Lagerplatz> shelvesToMarkRed = new ArrayList<>();



    /*
    * LEBENSZYKLUS
    *   An Aktivität gebunden werden        - onAttach (Aktivität)
    *   Fragment erstellen                  - onCreate (Bundle)
    *   View                                - onCreateView erstellen (LayoutInflater, ViewGroup, Bundle)
    *   Aktivitätserstellung                - onActivityCreated (Bundle)
    *   Ansichtsstatus wiederhergestellt    - onViewStateRestored (Bundle)
    *   Für den Benutzer sichtbar gemacht   - onStart ()
    *   Beginn der Benutzerinteraktion      - onResume ()
    *   Pause der Benutzerinteraktion       - onPause ()
    *   Für den Benutzer unsichtbar gemacht - onStop ()
    *   On-View-Zerstörung                  - onDestroyView ()
    *   Fragment zerstören                  - onDestroy ()
    *   Losgelöst von einer Aktivität       - onDetach ()
    * */


    /**
     * Um verarbeiten zu können, welche Parameter übergeben wurden, muss ein Bundle erstellt werden,
     * dass die übergebenen Parameter als Argumente setzt und somit die Daten in dieser Klasse verfügbar macht.
     *
     * @param shelvesToMarkRed Liste von tor zu markierenden Regalen
     * @return HomeFragment wird zurückgegeben mit den übergebenen Werten als Argumente gesetzt.
     */
     public static HomeFragment newInstance (ArrayList<Lagerplatz> shelvesToMarkRed) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SHELVESTOMARKRED,
                (ArrayList<? extends Parcelable>) shelvesToMarkRed);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     *
     *
     * @return
     */
    @SuppressLint ("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_lagerort_60, container, false);
        if (getArguments() != null) {
            shelvesToMarkRed = getArguments().getParcelableArrayList(ARG_SHELVESTOMARKRED);
        }
        // wenn home frag mit color switch = on aufgerufen wird
        if (colorSwitchState) {
            markFreeShelves(v);
        } else {
            unmarkFreeShelves(v);
        }
        markRegale(v);

        return v;
    }


    /**
     * Markierung der Regale.
     * Anhand einer Liste von Regalen, welche rot markiert werden sollen, werden die Lagerplätze
     * lokalisiert und rot markiert.
     *
     * @param v
     */
    //TODO markShelfs
    public void markRegale (View v) {
        if (shelvesToMarkRed != null && shelvesToMarkRed.size() != 0) {
            ArrayList<View> imageViewsOfShelvesToMark = new ArrayList<>();
            // Lagerplätze lokalisieren
            for (Lagerplatz lagerplatz : shelvesToMarkRed) {
                v.findViewsWithText(imageViewsOfShelvesToMark, lagerplatz.getLocation(),
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
            }
            // Regale rot markieren
            for (View singleShelf : imageViewsOfShelvesToMark){
                ImageView oneShelf = (ImageView) singleShelf;
                oneShelf.setImageResource(R.drawable.ic_shelf_marked_red);
            }
        }
    }


    /**
     *
     *
     * @param v
     */
    public void markFreeShelves (View v) {
        if (colorSwitchState && v != null) {
            if (freeShelveList != null && freeShelveList.size() != 0) {
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeLow    = new ArrayList<>();
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeMedium = new ArrayList<>();
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeHigh   = new ArrayList<>();
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeFull   = new ArrayList<>();
                int numberOfFreeShelfCompartments = 1;
                int lastShelfLocation = 0;

                // freie Lagerplätze durchlaufen
                for (Lagerplatz lagerplatz : freeShelveList) {
                    boolean contains = false;
                    for (Lagerplatz shelveToMarkRed : shelvesToMarkRed) {
                        if (shelveToMarkRed.getLocation().equals(lagerplatz.getLocation())) {
                            contains = true;
                        }
                    }
                    // freie Lagerplätze vorhanden
                    if (!contains) {
                        if (lagerplatz.getLagerplatz() != lastShelfLocation) { // nicht der davor
                            lastShelfLocation = lagerplatz.getLagerplatz();
                            numberOfFreeShelfCompartments = 1;
                        } else {
                            numberOfFreeShelfCompartments++;
                        }
                        // Unterteilung Füllgrad der Regalfächer
                        switch (numberOfFreeShelfCompartments) {
                            // 1 bis 2 von 7 Fächern frei
                            case 1:
                            case 2:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeLow,
                                        lagerplatz.getLocation(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                            // 3 bis 4 von 7 Fächern frei
                            case 3:
                            case 4:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeMedium,
                                        lagerplatz.getLocation(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                            // 5 bis 6 von 7 Fächern frei
                            case 5:
                            case 6:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeHigh,
                                        lagerplatz.getLocation(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                            // 7 von 7 Fächern frei
                            case 7:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeFull,
                                        lagerplatz.getLocation(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                        }
                    }
                }
                // icons setzen
                // low
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeLow) {
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_low);
                }
                // medium
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeMedium) {
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_medium);
                }
                // high
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeHigh) {
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_high);
                }
                // full
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeFull) {
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_full);
                }
            }
        }
    }



    /**
     * Die Markierung der freien Regale wird aufgehoben.
     *
     * @param v
     */
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
                    if (!contains) {// TODO for schleife
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


    /**
     * Es wird ein AlertDialog erstellt
     */
    // TODO AlertDialog
    public void showDBUpdate () {
        AlertDialog.Builder alertDialogBuilder
                = new AlertDialog.Builder(this.requireContext(), R.style.AlertDialogTheme);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Bitte laden Sie den neuesten Datenbestand herunter");
        alertDialogBuilder.setTitle("Datenbestand ist veraltet");
        alertDialogBuilder.setIcon(R.drawable.ic_warning);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment(), "DB_FRAGMENT").addToBackStack(null).commit();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}