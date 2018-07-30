package person.jack.plant.activity;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import person.jack.plant.R;
import person.jack.plant.db.dao.PlantsDao;
import person.jack.plant.db.entity.Plants;
import person.jack.plant.fragment.DemoPtrFragment;
import person.jack.plant.fragment.EnvHistoryChartFragment;
import person.jack.plant.fragment.EnvHistorySearchFragment;
import person.jack.plant.http.HttpClient;
import person.jack.plant.ui.UIHelper;
import person.jack.plant.ui.quickadapter.BaseAdapterHelper;
import person.jack.plant.ui.quickadapter.QuickAdapter;
import person.jack.plant.ui.swipebacklayout.SwipeBackActivity;

/**
 * 环境历史信息记录
 */
public class EnvHistoryActivity extends SwipeBackActivity {



    public EnvHistoryChartFragment envHistoryChartFragment;
    public EnvHistorySearchFragment envHistorySearchFragment;
    public FragmentTransaction transaction;

    private Button btnBack;
    private TextView textHeadTitle;

    public Plants curPlants;
    public List<Integer> tempList;
    public List<Integer> humiList;
    public List<Integer> lighList;
    public List<String>  timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env_history);

        btnBack = (Button) findViewById(R.id.btnBack);
        textHeadTitle = (TextView) findViewById(R.id.textHeadTitle);
        btnBack.setVisibility(View.VISIBLE);
        textHeadTitle.setText("历史统计");
        tempList=new ArrayList<>();
        humiList=new ArrayList<>();
        lighList=new ArrayList<>();
      timeList=new ArrayList<>();

      btnBack.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              onBackPressed();
          }
      });

    }


    @Override
    protected void onStart() {
        super.onStart();
        envHistoryChartFragment=new EnvHistoryChartFragment();
        envHistorySearchFragment=new EnvHistorySearchFragment();

        transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.env_container,envHistorySearchFragment,"search").show(envHistorySearchFragment);
        transaction.add(R.id.env_container,envHistoryChartFragment,"chart").hide(envHistoryChartFragment);

        transaction.commit();
    }



}
