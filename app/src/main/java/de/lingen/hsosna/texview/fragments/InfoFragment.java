package de.lingen.hsosna.texview.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;

import de.lingen.hsosna.texview.R;

/**
 * Info-Fragment
 */
public class InfoFragment extends Fragment {


    PDFView pdfViewer;

    /**
     *
     *
     * @return view des InfoFragments
     */
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);


        pdfViewer = v.findViewById(R.id.pdfView);
        pdfViewer.fromAsset("Benutzerhandbuch_SEP.pdf").load();
        return v;
    }

}