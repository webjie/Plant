package person.jack.plant.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import person.jack.plant.R;
import person.jack.plant.db.dao.PlantsDao;
import person.jack.plant.db.entity.Env;
import person.jack.plant.db.entity.Plants;
import person.jack.plant.fragment.ChartHumFragment;
import person.jack.plant.fragment.ChartLigFragment;
import person.jack.plant.fragment.ChartTempFragment;
import person.jack.plant.model.SerializableMap;

/**
 * 图表界面
 */
public class PlantsStatusActivity extends BaseFragmentActivity {
    public final static String TAG="plant";

    private RelativeLayout layoutHeader;
    private Button btnBack;
    private TextView textHeadTitle;
    private RadioGroup statusGroup;
    private RadioButton statusR1;
    private RadioButton statusR2;
    private RadioButton statusR3;

    private ViewPager statusPager;
    private List<Fragment> list;
    private static List<Plants>plantsList;
    private List<RadioButton> radioButtonList=new ArrayList<>();
    private Map<String,List<Entry>> chartEntryMap=new HashMap<>();

    private PlantsDao PlantsDao;
    MyBroadcastReceiver receiver;


    public List<Plants> getplantsList() {
        return plantsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_status);
        PlantsDao=new PlantsDao(this);
        receiver=new MyBroadcastReceiver();
        IntentFilter filter=new IntentFilter("plants.chart.update");
        registerReceiver(receiver,filter);
        initFragment();
        initView();

    }

    private void initView(){
        int curPosition=getIntent().getIntExtra("fragmentPosition",0);

        layoutHeader = (RelativeLayout) findViewById(R.id.layout_header);
        btnBack = (Button) findViewById(R.id.btnBack);
        textHeadTitle = (TextView) findViewById(R.id.textHeadTitle);
        statusPager = (ViewPager) findViewById(R.id.status_pager);
        statusGroup = (RadioGroup) findViewById(R.id.status_group);

        statusR1 = (RadioButton) findViewById(R.id.status_r1);
        statusR2 = (RadioButton) findViewById(R.id.status_r2);
        statusR3 = (RadioButton) findViewById(R.id.status_r3);

        radioButtonList.add(statusR1);radioButtonList.add(statusR2);radioButtonList.add(statusR3);

        for(int i=0;i<radioButtonList.size();i++){
            radioButtonList.get(i).setClickable(false);
            if(i==curPosition){
                radioButtonList.get(i).setChecked(true);
            }
        }

        MViewPagerAdapter adapter=new MViewPagerAdapter(getSupportFragmentManager(),list);
        statusPager.setAdapter(adapter);
        statusPager.setCurrentItem(curPosition);
        statusPager.setOffscreenPageLimit(2);

        statusPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<radioButtonList.size();i++){
                    if(i==position){
                        radioButtonList.get(i).setChecked(true);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        List<Entry> tempEntry=new ArrayList<>();
        List<Entry> humiEntry=new ArrayList<>();
        List<Entry> ligtEntry=new ArrayList<>();

        chartEntryMap.put("tempChart",tempEntry);
        chartEntryMap.put("humiEntry",humiEntry);
        chartEntryMap.put("ligtEntry",ligtEntry);


        textHeadTitle.setText("实时环境信息");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void initFragment(){
        list=new ArrayList<>();
        ChartTempFragment tempFragment=new ChartTempFragment();
        ChartHumFragment humFragment=new ChartHumFragment();
        ChartLigFragment ligFragment=new ChartLigFragment();
        list.add(tempFragment);
        list.add(humFragment);
        list.add(ligFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class MViewPagerAdapter extends FragmentPagerAdapter{
        List<Fragment> list;

        public MViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list=list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


    class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Bundle bundle = intent.getExtras();
                SerializableMap serializableMap = (SerializableMap) bundle.get("map");
                Map<String, int[]> map = serializableMap.getMap();
                int[] value = map.get("values");

                ChartTempFragment chartTempFragment=(ChartTempFragment)list.get(0);
                chartTempFragment.setChart(value[0]);
                ChartHumFragment chartHumFragment=(ChartHumFragment)list.get(1);
                chartHumFragment.setChart(value[1]);
                ChartLigFragment chartLigFragment=(ChartLigFragment)list.get(2);
                chartLigFragment.setChart(value[2]);


            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d(TAG, "onReceive: 更新图表");
        }
    }

}
