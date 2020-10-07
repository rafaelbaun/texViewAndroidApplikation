package de.lingen.hsosna.texview.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Currency;

import de.lingen.hsosna.texview.DatabaseHelper;
import de.lingen.hsosna.texview.Kpi;
import de.lingen.hsosna.texview.KpiAdapter;
import de.lingen.hsosna.texview.R;
import de.lingen.hsosna.texview.database.TableKpi;
import de.lingen.hsosna.texview.database.TableLagerbestand;

public class KPIFragment extends Fragment {

    ViewPager2 viewPager2;
    KpiAdapter kpiAdapter;
    ArrayList<Kpi> kpis;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kpi, container, false);

        Context context = getActivity();
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();

        kpis = new ArrayList<>();
        kpis = getKpiArrayList();

        kpiAdapter = new KpiAdapter(kpis, context);

        viewPager2 = v.findViewById(R.id.fragment_kpi_viewPager);
        viewPager2.setAdapter(kpiAdapter);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.setPadding(200,0,200, 0);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage (@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        return v;
    }

    private ArrayList<Kpi> getKpiArrayList () {
        ArrayList<Kpi> kpiArrayList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT " + TableKpi.KpiEntry.COLUMN_NAME + ", "
                                          + TableKpi.KpiEntry.COLUMN_CURRENTVALUE + ", "
                                          + TableKpi.KpiEntry.COLUMN_MAXVALUE + ", "
                                          + TableKpi.KpiEntry.COLUMN_TIMESTAMP + ""
                                          + " FROM " + TableKpi.KpiEntry.TABLE_NAME + ";", null);
        try{
            while (cursor.moveToNext())
            {
                String kpiName = cursor.getString(cursor.getColumnIndex(TableKpi.KpiEntry.COLUMN_NAME));
                int kpiCurrentValue = cursor.getInt(cursor.getColumnIndex(TableKpi.KpiEntry.COLUMN_CURRENTVALUE));
                int kpiMaxValue = cursor.getInt(cursor.getColumnIndex(TableKpi.KpiEntry.COLUMN_MAXVALUE));
                String kpiTimestamp = cursor.getString(cursor.getColumnIndex(TableKpi.KpiEntry.COLUMN_TIMESTAMP));
                Kpi kpi = new Kpi(kpiName,kpiCurrentValue,kpiMaxValue,kpiTimestamp);
                kpiArrayList.add(kpi);
            }
        } finally {
            cursor.close();
        }
        return kpiArrayList;
    }
}