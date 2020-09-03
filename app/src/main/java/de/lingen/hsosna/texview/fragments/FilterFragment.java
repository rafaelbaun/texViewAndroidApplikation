package de.lingen.hsosna.texview.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.lingen.hsosna.texview.R;

public class FilterFragment extends Fragment {
    private FilterFragmentListener listener;

    /**
     * Um Daten an die MainActivity zu senden wird ein Interface implementiert, was auch in der
     * MainActivity implemnentiert werden muss.
     */
    public interface FilterFragmentListener {
        void onFilterInputSent (CharSequence input);
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter, container, false);
        return v;
    }
}
