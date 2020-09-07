package de.lingen.hsosna.texview.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import de.lingen.hsosna.texview.Lagerplatz;
import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;
import de.lingen.hsosna.texview.layouts.ZoomLinearLayout;

import static de.lingen.hsosna.texview.MainActivity.colorSwitchState;
import static de.lingen.hsosna.texview.MainActivity.freeShelveList;

public class HomeFragment extends Fragment {

    public static final String ARG_SHELVESTOMARKRED = "argShelvesToMarkRed";
    private ArrayList<Lagerplatz> shelvesToMarkRed = new ArrayList<>();

     public static HomeFragment newInstance (ArrayList<Lagerplatz> shelvesToMarkRed){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SHELVESTOMARKRED,
                (ArrayList<? extends Parcelable>) shelvesToMarkRed);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint ("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_home_copy, container, false);
        View v = inflater.inflate(R.layout.fragment_home_lagerort_60, container, false);


        //ZOOMMTEST
        final ZoomLinearLayout zoomLinearLayout = (ZoomLinearLayout) v.findViewById(R.id.zoom_linear_layout);
        zoomLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {
                zoomLinearLayout.init(getActivity());
                return false;
            }
        });
        //////////////

        if (getArguments() != null){
            shelvesToMarkRed = getArguments().getParcelableArrayList(ARG_SHELVESTOMARKRED);
        }


        // wenn home frag mit color switch = on aufgerufen wird
        if(colorSwitchState){
            markFreeShelves(v);
        }
        else{
            unmarkFreeShelves(v);
        }


        markRegale(v);

        return v;
    }


    public void markRegale(View v){
        if(shelvesToMarkRed != null && shelvesToMarkRed.size() != 0){
            ArrayList<View> imageViewsOfShelvesToMark = new ArrayList<>();
            for (Lagerplatz lagerplatz : shelvesToMarkRed) {
                v.findViewsWithText(imageViewsOfShelvesToMark, lagerplatz.getLocation(),
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
            }
            for (View singleShelf : imageViewsOfShelvesToMark){
                ImageView oneShelf = (ImageView) singleShelf;
                oneShelf.setImageResource(R.drawable.ic_shelf_marked_red);
            }

        }

    }

    public void markFreeShelves(View v){
        if(colorSwitchState && v != null) {
            if(freeShelveList != null && freeShelveList.size() != 0){
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeLow = new ArrayList<>();
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeMedium = new ArrayList<>();
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeHigh = new ArrayList<>();
                ArrayList<View> imageViewsOfShelvesToMarkAsFreeFull = new ArrayList<>();


                int numberOfFreeShelfCompartments = 1;
                int lastShelfLocation = 0;


                for (Lagerplatz lagerplatz : freeShelveList) {
                    boolean contains = false;
                    for (Lagerplatz shelveToMarkRed : shelvesToMarkRed){
                        if (shelveToMarkRed.getLocation().equals(lagerplatz.getLocation()))
                            contains = true;
                    }
                    if(!contains)
                    {
                        if(lagerplatz.getLagerplatz() != lastShelfLocation) { // nicht der davor
                            lastShelfLocation = lagerplatz.getLagerplatz();
                            numberOfFreeShelfCompartments = 1;
                        }
                        else{
                            numberOfFreeShelfCompartments++;
                        }

                        switch (numberOfFreeShelfCompartments){
                            case 1:
                            case 2:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeLow, lagerplatz.getLocation(),
                                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                            case 3:
                            case 4:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeMedium, lagerplatz.getLocation(),
                                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                            case 5:
                            case 6:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeHigh, lagerplatz.getLocation(),
                                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                            case 7:
                                v.findViewsWithText(imageViewsOfShelvesToMarkAsFreeFull, lagerplatz.getLocation(),
                                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                break;
                        }
                    }
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeLow){
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_low);
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeMedium){
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_medium);
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeHigh){
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_high);
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFreeFull){
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_shelf_free_full);
                }
            }
        }
    }

    public void unmarkFreeShelves(View v){
        if(v != null) {
            if(freeShelveList != null && freeShelveList.size() != 0){
                ArrayList<View> imageViewsOfShelvesToMarkAsFree = new ArrayList<>();
                for (Lagerplatz lagerplatz : freeShelveList) {
                    boolean contains = false;
                    for (Lagerplatz shelveToMarkRed : shelvesToMarkRed){
                        if (shelveToMarkRed.getLocation().equals(lagerplatz.getLocation()))
                            contains = true;
                    }
                    if(!contains) // for schleife
                    {
                        v.findViewsWithText(imageViewsOfShelvesToMarkAsFree, lagerplatz.getLocation(),
                                View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    }
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFree){
                        ImageView oneShelf = (ImageView) singleShelf;
                        oneShelf.setImageResource(R.drawable.ic_shelf_normal);
                }
            }
        }
            //liste markieren
            // check if regal is checked
    }


}