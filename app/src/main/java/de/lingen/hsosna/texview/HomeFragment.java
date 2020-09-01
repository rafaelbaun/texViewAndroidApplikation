package de.lingen.hsosna.texview;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import static de.lingen.hsosna.texview.MainActivity.colorSwitchState;
import static de.lingen.hsosna.texview.MainActivity.freeShelveList;

public class HomeFragment extends Fragment {

    public static final String ARG_REGALETOMARK = "argRegaleToMark";
    public static final String ARG_FAECHERTOMARK = "argFaecherToMark";
    public static final String ARG_SHELVESTOMARKRED = "argShelvesToMarkRed";
    private ArrayList<CharSequence> regaleToMark = new ArrayList<>();
    private ArrayList<CharSequence> facherToMark = new ArrayList<>();
    private ArrayList<Lagerplatz> shelvesToMarkRed = new ArrayList<>();

    public static HomeFragment newInstance (ArrayList<CharSequence> regaleToMark, ArrayList<CharSequence> faecherToMark){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putCharSequenceArrayList(ARG_REGALETOMARK, regaleToMark);
        args.putCharSequenceArrayList(ARG_FAECHERTOMARK, faecherToMark);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance2 (ArrayList<Lagerplatz> shelvesToMarkRed){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SHELVESTOMARKRED,
                (ArrayList<? extends Parcelable>) shelvesToMarkRed);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_copy, container, false);

        if (getArguments() != null){
            regaleToMark = getArguments().getCharSequenceArrayList(ARG_REGALETOMARK);
            facherToMark = getArguments().getCharSequenceArrayList(ARG_FAECHERTOMARK);
            shelvesToMarkRed = getArguments().getParcelableArrayList(ARG_SHELVESTOMARKRED);
        }


        // wenn home frag mit color switch = on aufgerufen wird
        if(colorSwitchState){
            markFreeShelves(v);
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
                oneShelf.setImageResource(R.drawable.ic_regal_marked);
            }

        }

    }

    public void markFreeShelves(View v){
        if(colorSwitchState && v != null) {
            if(freeShelveList != null && freeShelveList.size() != 0){
                ArrayList<View> imageViewsOfShelvesToMarkAsFree = new ArrayList<>();
                for (Lagerplatz lagerplatz : freeShelveList) {
                    boolean contains = false;
                    for (Lagerplatz shelveToMarkRed : shelvesToMarkRed){
                        if (shelveToMarkRed.getLocation().equals(lagerplatz.getLocation()))
                            contains = true;
                    }
                    if(!contains)
                    {
                        v.findViewsWithText(imageViewsOfShelvesToMarkAsFree, lagerplatz.getLocation(),
                                View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    }
                }
                for (View singleShelf : imageViewsOfShelvesToMarkAsFree){
                    ImageView oneShelf = (ImageView) singleShelf;
                    oneShelf.setImageResource(R.drawable.ic_regal_free);
                }
            }
        }
        //liste markieren
        // check if regal is checked

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
                        oneShelf.setImageResource(R.drawable.ic_regal2);
                }
            }
        }
            //liste markieren
            // check if regal is checked
    }


}