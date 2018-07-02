package person.jack.plant.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import person.jack.plant.R;
import person.jack.plant.activity.PlantsStatusActivity;
import person.jack.plant.db.entity.Env;
import person.jack.plant.db.entity.Plants;

import static person.jack.plant.ui.UIHelper.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartTempFragment extends Fragment {
    private final static int TEMP=0;
    private final static int HUM=1;
    private final static int LIG=2;

    public ChartTempFragment() {
        // Required empty public constructor
    }

    private LineChart statusChart;
    private TextView statusName;
    private List<Entry> entryList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chart, container, false);
        statusChart = (LineChart) view.findViewById(R.id.status_chart);
        statusName = (TextView) view.findViewById(R.id.status_name);
        statusName.setText(BufferKnifeFragment.curPlants.getName()+"当前的温度");

        initChart();
        setChart(0);
        return view;
    }

//    Handler handler=new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message message) {
//            switch (message.what){
//                case 0:
//
//                    setChart();
//                    break;
//            }
//            return false;
//        }
//    });

//    Timer timer;
//    TimerTask timerTask;
//    Random random=new Random();
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        timer=new Timer();
//        timerTask=new TimerTask() {
//            @Override
//            public void run() {
//                Message message=new Message();
//                message.what=0;
//                handler.sendMessage(message);
//            }
//        };
//        timer.schedule(timerTask,0,3000);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if(timer!=null){
//            timer.cancel();
//            timerTask.cancel();
//            timerTask=null;
//            timer=null;
//        }
//    }

    private void initChart(){
        statusChart.setDescription(null);
        statusChart.setClickable(false);
        statusChart.setTouchEnabled(false);

        XAxis xAxis=statusChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMaximum(4);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm:ss");
                Date date;
                if(value==0.0){
                    date=new Date(System.currentTimeMillis()-12000);
                }else if(value==1.0){
                    date=new Date(System.currentTimeMillis()-9000);
                }else if(value==2.0){
                    date=new Date(System.currentTimeMillis()-6000);
                }else if(value==3.0){
                    date=new Date(System.currentTimeMillis()-3000);
                }else {
                    date=new Date(System.currentTimeMillis());
                }
                return dateFormat.format(date);
            }
        });
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(4);

        statusChart.getAxisRight().setEnabled(false);
        YAxis yAxis=statusChart.getAxisLeft();
        yAxis.setAxisMaximum(35);
        yAxis.setStartAtZero(true);
        yAxis.setDrawGridLines(true);
        yAxis.setLabelCount(4);


    }

    public  void setChart(int value){
        LineData data=null;
        LineDataSet set=null;

        if(statusChart.getLineData()!=null&&statusChart.getLineData().getDataSetCount()>0){

            if(entryList.size()==5){
                entryList.remove(0);
                for(int i=0;i<entryList.size();i++){
                    Entry entryq=(Entry)entryList.get(i);
                    entryq.setX(entryq.getX()-1);
                }
            }
            try{
                Entry entry=new Entry( entryList.size(),value);
                entryList.add(entry);
            }catch (Exception e){
                e.printStackTrace();
            }

            data=statusChart.getData();
            set=(LineDataSet)data.getDataSetByIndex(0);

            set.notifyDataSetChanged();
            data.notifyDataChanged();
            statusChart.notifyDataSetChanged();
            statusChart.invalidate();
            Log.d(TAG, "setChart: ");
        }else{
            try{
                entryList=new ArrayList<>();
                entryList.add(new Entry(0,BufferKnifeFragment.curPlants.getTemp()));
                set=new LineDataSet(entryList,null);
                set.setCircleColor(Color.BLUE);
                set.setCircleRadius(2f);
                set.setDrawCircleHole(false);
                set.setLineWidth(3f);
                set.setColor(Color.BLUE);
                set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                data=new LineData();
                data.addDataSet(set);
                data.setDrawValues(true);
                data.setValueTextSize(15f);


            }catch (Exception e){
                e.printStackTrace();
            }
            statusChart.setData(data);
            statusChart.invalidate();
            Log.d(TAG, "setChart: initialize chart");
        }
    }


}
