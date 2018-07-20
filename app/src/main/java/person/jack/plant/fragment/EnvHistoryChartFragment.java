package person.jack.plant.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import person.jack.plant.R;
import person.jack.plant.activity.EnvHistoryActivity;

import static person.jack.plant.activity.PlantsDetailActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnvHistoryChartFragment extends Fragment {
    private TextView plantName;
    private LineChart tempChart;
    private LineChart humiChart;
    private LineChart lighChart;

    private List<Entry> tempList=new ArrayList<>();
    private List<Entry> humiList=new ArrayList<>();
    private List<Entry> lighList=new ArrayList<>();

    private EnvHistoryActivity activity;

    public EnvHistoryChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_env_history_chart, container, false);
        plantName = (TextView) view.findViewById(R.id.plant_name);
        tempChart = (LineChart) view.findViewById(R.id.tempChart);
        humiChart = (LineChart) view.findViewById(R.id.humiChart);
        lighChart = (LineChart) view.findViewById(R.id.lighChart);

        activity=(EnvHistoryActivity)getActivity();

        EnvHistorySearchFragment envHistorySearchFragment=(EnvHistorySearchFragment)getFragmentManager().findFragmentByTag("search");

        tempChart.setDescription(null);
        tempChart.setSaveEnabled(false);
        tempChart.getLegend().setEnabled(false);
        tempChart.getAxisRight().setEnabled(false);
        tempChart.setClickable(false);
        XAxis xTempChart=tempChart.getXAxis();
        xTempChart.setDrawGridLines(false);
        xTempChart.setDrawLabels(false);
        xTempChart.setPosition(XAxis.XAxisPosition.BOTTOM);
        xTempChart.setAxisMaximum(tempList.size());
        YAxis yTempChart=tempChart.getAxisLeft();
        yTempChart.setAxisMaximum(45);
        yTempChart.setAxisMinimum(0);
        Log.d(TAG, "onCreateView: "+tempList.size());
        tempList.add(new Entry(0,0));
        LineDataSet lineDataSet=new LineDataSet(tempList,"");
        lineDataSet.setLineWidth(6f);
        lineDataSet.setColor(Color.BLUE);
        LineData lineData=new LineData(lineDataSet);
        tempChart.setData(lineData);

        //湿度
        humiChart.setDescription(null);
        humiChart.setSaveEnabled(false);
        humiChart.getLegend().setEnabled(false);
        humiChart.getAxisRight().setEnabled(false);
        humiChart.setClickable(false);
        XAxis xHumiChart=humiChart.getXAxis();
        xHumiChart.setDrawGridLines(false);
        xHumiChart.setDrawLabels(false);
        xHumiChart.setPosition(XAxis.XAxisPosition.BOTTOM);
        xHumiChart.setAxisMaximum(humiList.size());
        YAxis yHumiChart=humiChart.getAxisLeft();
        yHumiChart.setAxisMaximum(100);
        yHumiChart.setAxisMinimum(0);
        Log.d(TAG, "onCreateView: "+humiList.size());
        humiList.add(new Entry(0,0));
        LineDataSet lineDataSet1=new LineDataSet(humiList,"");
        lineDataSet1.setLineWidth(6f);
        lineDataSet1.setColor(Color.BLUE);
        LineData lineData1=new LineData(lineDataSet1);
        humiChart.setData(lineData1);

        lighChart.setDescription(null);
        lighChart.setSaveEnabled(false);
        lighChart.getLegend().setEnabled(false);
        lighChart.getAxisRight().setEnabled(false);
        lighChart.setClickable(false);
        XAxis xLighChart=lighChart.getXAxis();
        xLighChart.setDrawGridLines(false);
        xLighChart.setDrawLabels(false);
        xLighChart.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLighChart.setAxisMaximum(lighList.size());
        YAxis yLighChart=lighChart.getAxisLeft();
        yLighChart.setAxisMaximum(2000);
        yLighChart.setAxisMinimum(0);
        Log.d(TAG, "onCreateView: "+lighList.size());
        lighList.add(new Entry(0,0));
        LineDataSet lineDataSet2=new LineDataSet(lighList,"");
        lineDataSet2.setLineWidth(6f);
        lineDataSet2.setColor(Color.BLUE);
        LineData lineData2=new LineData(lineDataSet2);
        lighChart.setData(lineData2);


        return view;
    }


    public void setList(){
        tempList.clear();
        humiList.clear();
        lighList.clear();
        for(int i=0;i<activity.tempList.size();i++){
            tempList.add(new Entry(i,activity.tempList.get(i)));
        }

        for(int i=0;i<activity.humiList.size();i++){
            humiList.add(new Entry(i,activity.humiList.get(i)));
        }

        for(int i=0;i<activity.lighList.size();i++){
            lighList.add(new Entry(i,activity.lighList.get(i)));
        }

        tempChart.getXAxis().setAxisMaximum(tempList.size());
        tempChart.notifyDataSetChanged();
        tempChart.invalidate();

        humiChart.getXAxis().setAxisMaximum(humiList.size());
        humiChart.notifyDataSetChanged();
        humiChart.invalidate();

        lighChart.getXAxis().setAxisMaximum(lighList.size());
        lighChart.notifyDataSetChanged();
        lighChart.invalidate();
    }
}
