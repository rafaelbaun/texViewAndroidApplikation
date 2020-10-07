package de.lingen.hsosna.texview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;


/**
 *
 */
public class KpiAdapter extends RecyclerView.Adapter<KpiAdapter.KpiViewHolder> {

    private ArrayList<Kpi> kpis;
    private Context context;

    /**
     * View Holder durch RecyclerView
     */
    public static class KpiViewHolder extends RecyclerView.ViewHolder {
        public TextView kpiName;
        public TextView kpiPercentageValue;
        public TextView kpiCurrentValue;
        public TextView kpiMaximumValue;
        public TextView kpiTimestamp;
        public LinearLayout percentageDetails;
        public ProgressBar progressBar;


        public KpiViewHolder (@NonNull View itemView) {
            super(itemView);
            kpiName            = itemView.findViewById(R.id.viewpager_item_kpi_name);
            kpiPercentageValue = itemView.findViewById(R.id.viewpager_item_kpi_currentvalueinpercent);
            kpiCurrentValue    = itemView.findViewById(R.id.viewpager_item_kpi_currentvalue);
            kpiMaximumValue    = itemView.findViewById(R.id.viewpager_item_kpi_maxvalue);
            kpiTimestamp       = itemView.findViewById(R.id.viewpager_item_kpi_currentTimestamp);
            percentageDetails  = itemView.findViewById(R.id.viewpager_item_kpi_percentDetails);
            progressBar        = itemView.findViewById(R.id.viewpager_item_kpi_progressBar);
        }
    }


    public KpiAdapter (ArrayList<Kpi> kpis, Context context) {
        this.kpis    = kpis;
        this.context = context;
    }


    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public KpiViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_item_kpi,
                parent, false);
        KpiViewHolder kpiViewHolder = new KpiViewHolder(v);

        return kpiViewHolder;
    }


    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder (@NonNull KpiViewHolder holder, int position) {
        Kpi currentKpi = kpis.get(position);
        holder.kpiName.setText(currentKpi.getName());
        holder.kpiCurrentValue.setText(String.valueOf(currentKpi.getCurrentValue()));
        holder.kpiMaximumValue.setText(String.valueOf(currentKpi.getMaxValue()));

        if (currentKpi.getMaxValue() != 0) {
            int percent = (currentKpi.getCurrentValue()*100) / currentKpi.getMaxValue();
            String percentAsString = String.valueOf(percent) + " %";
            holder.kpiPercentageValue.setText(percentAsString);
            holder.progressBar.setProgress(percent);
        } else {
            holder.kpiPercentageValue.setText(String.valueOf(currentKpi.getCurrentValue()));
            Drawable circularProgressSolid = ContextCompat.getDrawable(context ,R.drawable.circularprogress_solid);
            holder.progressBar.setProgressDrawable(circularProgressSolid);
            holder.progressBar.setProgress(100);
            holder.percentageDetails.setVisibility(LinearLayout.GONE);
        }

        holder.kpiTimestamp.setText(currentKpi.getTimestamp());
    }

    @Override
    public int getItemCount () {
        return kpis.size();
    }

}
