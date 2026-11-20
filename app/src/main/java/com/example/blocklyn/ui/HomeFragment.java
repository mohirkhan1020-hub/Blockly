package com.example.blocklyn.ui;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.blocklyn.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.*;

public class HomeFragment extends Fragment {
    private BarChart chart;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup c, @Nullable Bundle s) {
        View v = inflater.inflate(R.layout.fragment_home, c, false);
        chart = v.findViewById(R.id.hourlyChart);
        setupChart();
        loadHourlyUsage();
        return v;
    }

    private void setupChart() {
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setTextColor(Color.parseColor("#8A96A8"));
        chart.getXAxis().setTextColor(Color.parseColor("#8A96A8"));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setGranularity(1f);
        chart.setDrawGridBackground(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
    }

    private void loadHourlyUsage() {
        UsageStatsManager usm = (UsageStatsManager)
            requireContext().getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
        long start = cal.getTimeInMillis();
        long end = System.currentTimeMillis();

        Map<String, UsageStats> stats = usm.queryAndAggregateUsageStats(start, end);
        float[] hourly = new float[24];
        for (UsageStats s : stats.values()) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(s.getLastTimeUsed());
            int hr = c.get(Calendar.HOUR_OF_DAY);
            hourly[hr] += s.getTotalTimeInForeground() / 60000f / 60f;
        }

        List<BarEntry> entries = new ArrayList<>();
        for (int h = 0; h <= 21; h++) entries.add(new BarEntry(h, hourly[h]));

        BarDataSet set = new BarDataSet(entries, "Hours");
        set.setColor(Color.parseColor("#4FC3F7"));
        set.setDrawValues(false);

        BarData data = new BarData(set);
        data.setBarWidth(0.5f);
        chart.setData(data);

        String[] labels = new String[22];
        for (int i = 0; i <= 21; i++) labels[i] = (i % 6 == 0) ? i + ":00" : "";
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.invalidate();
    }
}
