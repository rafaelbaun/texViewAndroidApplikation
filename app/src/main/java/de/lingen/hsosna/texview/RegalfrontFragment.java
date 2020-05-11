package de.lingen.hsosna.texview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RegalfrontFragment extends Fragment {
    private static final String ARG_REGALNUMMER = "argRegalNummer";
    public static final String ARG_FACHNUMMER = "argFachNummer";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private CharSequence regalID;
    public static CharSequence fachID;

    public static RegalfrontFragment newInstance(CharSequence regalNummer, CharSequence fachNummer){
        RegalfrontFragment fragment = new RegalfrontFragment();
        Bundle args = new Bundle();

        args.putCharSequence(ARG_REGALNUMMER, regalNummer);
        args.putCharSequence(ARG_FACHNUMMER, fachNummer);

        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_regalfront, container, false);
        View v = inflater.inflate(R.layout.bottomsheet_test, container, false);

        TextView textView = v.findViewById(R.id.textviewRegalfachbezeichnung);

        if (getArguments() != null) {
            regalID = getArguments().getCharSequence(ARG_REGALNUMMER);
            fachID = getArguments().getCharSequence(ARG_FACHNUMMER);
        }

        String regalPrefix = "RegalNr: ";
        textView.setText(regalPrefix.concat((String) regalID));

        //inflateBottomSheet(v);

      /*  ArrayList<Artikel> exampleList = new ArrayList<>();
        ArrayList<Artikel> exampleList2 = new ArrayList<>();


        exampleList.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));

        exampleList2.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
        exampleList2.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
        exampleList2.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
        exampleList2.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
        exampleList2.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));


        mRecyclerView = v.findViewById(R.id.recyclerView_fach01);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(exampleList);                     // LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach02);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(exampleList2);                     // LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        */
        fillRecyclerView(v);

        return v;
    }

    public void fillRecyclerView(View v){

        mRecyclerView = v.findViewById(R.id.recyclerView_fach01);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(fillArrayWithData(1));                     // LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach02);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(fillArrayWithData(2));                     // LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach03);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(fillArrayWithData(3));                     // LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach04);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(fillArrayWithData(4));                     // LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach05);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(fillArrayWithData(5));                     // LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach06);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(fillArrayWithData(6));                     // LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = v.findViewById(R.id.recyclerView_fach07);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(fillArrayWithData(7));                     // LIST WITH CONTENTS
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    public ArrayList<Artikel> fillArrayWithData(int regalNummer){
        ArrayList<Artikel> artikelListe = new ArrayList<Artikel>();
        switch(regalNummer){
            case 1:
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                break;
            case 2:
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                break;
            case 3:
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                break;
            case 4:
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                break;
            case 5:
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                break;
            case 6:
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                break;
            case 7:
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                artikelListe.add(new Artikel("79987","Sheldon","100935","Punkte, 2,5cm, erika, rose","140","FW", "44", "m"));
                break;
        }
        return artikelListe;
    }
}
