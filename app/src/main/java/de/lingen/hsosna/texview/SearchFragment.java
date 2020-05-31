package de.lingen.hsosna.texview;

import android.content.Context;
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

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private SearchFragmentListener listener;
    private EditText editText;
    private EditText editText2;
    private Button button;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                CharSequence input = editText.getText();
                CharSequence input2 = editText2.getText();
                listener.onSearchInputSent(input);
                mRecyclerView = v.findViewById(R.id.recyclerVgvhgview_fach01);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getActivity());

                ArrayList<Artikel> ar = new ArrayList<>();
                ar.add(new Artikel(79987, "Sheldon", 100935, "Punkte, 2,5cm, erika, rose", 140,"FW", "44", "m"));

                mAdapter = new ExampleAdapter(ar);// LIST WITH CONTENTS
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
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
}
