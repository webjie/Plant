package person.jack.plant.fragment;


import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import okhttp3.RequestBody;
import okhttp3.Response;
import person.jack.plant.R;
import person.jack.plant.activity.EnvHistoryActivity;
import person.jack.plant.db.dao.PlantsDao;
import person.jack.plant.db.entity.Plants;
import person.jack.plant.http.HttpClient;
import person.jack.plant.ui.quickadapter.BaseAdapterHelper;
import person.jack.plant.ui.quickadapter.QuickAdapter;

import static person.jack.plant.activity.PlantsDetailActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnvHistorySearchFragment extends Fragment {
    private TextView envHistoryBeginTime;
    private TextView envHistoryEndTime;
    private ListView envHistoryListView;

    private Spinner envHistorySpinner;
    private Button envHistorySearch;
    private LinearLayout envHide;

    private String devLigg="529698";
    private String beginTime;
    private String endTime;

    private EnvHistoryActivity activity;

    private List<Plants> plantsList;

    private PlantsDao plantsDao;
    private QuickAdapter<Plants> adapter;

    public EnvHistorySearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_env_history_search, container, false);

        envHistoryBeginTime = (TextView) view.findViewById(R.id.env_history_begin_time);
        envHistoryEndTime = (TextView) view.findViewById(R.id.env_history_end_time);
        envHistoryListView = (ListView) view.findViewById(R.id.env_history_list_view);
        final Calendar c = Calendar.getInstance();

        activity=(EnvHistoryActivity)getActivity();

        //选择时间
        envHistoryBeginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear , dayOfMonth);
                        envHistoryBeginTime.setText(DateFormat.format("yyyy-MM-dd", c) + "");
                        beginTime=DateFormat.format("yyyyMMddHHmm", c) + "";
                        Log.d("envHistory", DateFormat.format("yyyy年MM月dd日", c) + "");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        envHistoryEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear , dayOfMonth);
                        envHistoryEndTime.setText(DateFormat.format("yyyy-MM-dd", c) + "");
                        endTime=DateFormat.format("yyyyMMddHHmm", c) + "";
                        Log.d("envHistory", DateFormat.format("yyyy年MM月dd日", c) + "");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        envHistorySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        plantsList = new ArrayList<>();
        plantsDao = new PlantsDao(getActivity());

        adapter = new QuickAdapter<Plants>(getActivity(), R.layout.statistics_item_layout) {

            @Override
            protected void convert(final BaseAdapterHelper helper, Plants shop) {
                Log.d(TAG, "convert: " + shop.getName());
                helper.setText(R.id.item_name, shop.getName()); // 自动异步加载图片

                if (shop.getHum() != null) {
                    helper.setText(R.id.item_temp, "");
                    helper.setText(R.id.item_hum, "");
                    helper.setText(R.id.item_lig, "");
                }

                if (shop.getImage() != null) {
                    File file = new File(shop.getImage());
                    if (file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(shop.getImage());
                        helper.setImageBitmap(R.id.item_pic, bitmap);
                    }
                }

                //设置控件监听
//                helper.setOnClickListener(R.id.item_pic, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        curPlants = plantsList.get(helper.getPosition());
//                        DemoPtrFragment.curPlant = curPlants;
//                        UIHelper.showPlantsDetailActivity(EnvHistoryActivity.this);
//                    }
//                });
            }
        };
        envHistoryListView.setAdapter(adapter);
        plantsList = plantsDao.findAll();

        envHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(beginTime==null||endTime==null){
                    Toast.makeText(getActivity(),"请先选择时间",Toast.LENGTH_SHORT).show();
                }else{
                    activity.curPlants=plantsList.get(position);
                    getTempAndHumiHistory();
                }
            }
        });
        adapter.addAll(plantsList);
        adapter.notifyDataSetChanged();

        return view;
    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(getActivity(),"网络请求失败，请检查网络",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    JSONArray array=(JSONArray)msg.obj;
                    try{
                        for(int i=0;i<array.length();i++){
                            JSONObject object=array.getJSONObject(i);
                            activity.tempList.add(object.getInt("TempValue"));
                            activity.humiList.add(object.getInt("HumiValue"));
                            activity.timeList.add(object.getString("TimeValue"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    getLighHistory();
                    break;
                case 2:
                    JSONArray array1=(JSONArray)msg.obj;
                    try{
                        for(int i=0;i<array1.length();i++){
                            JSONObject object=array1.getJSONObject(i);
                            activity.lighList.add(object.getInt("HumiValue"));
                        }
                        Log.d(TAG, "handleMessage:ssssssssss ");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                   try{
                       activity.envHistoryChartFragment.setList();

                       android.support.v4.app.FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                       //添加栈
                       transaction.addToBackStack(null);
                       transaction.hide(activity.envHistorySearchFragment);
                       transaction.show(activity.envHistoryChartFragment);
//                       transaction.replace(R.id.env_container,activity.envHistoryChartFragment);
                       transaction.commit();
                   }catch (Exception e){
                        e.printStackTrace();
                   }
                    break;
            }
            return false;
        }
    });

    private void setBeginTimeAndEndTime(){
        int spinnerLocaltion=envHistorySpinner.getSelectedItemPosition();
        switch (spinnerLocaltion){
            case 1:
                beginTime=DateFormat.format("yyyyMMddHHmm", System.currentTimeMillis()-3600000) + "";
                endTime=DateFormat.format("yyyyMMddHHmm", System.currentTimeMillis()) + "";


        }
    }

    private void getTempAndHumiHistory(){
        RequestBody requestBody=RequestBody.create(HttpClient.JSONString,"");
        String url="http://www.0531yun.cn/wsjc/Device/getDevHisData.do?" +
                "devKey=529697&beginTime="+beginTime+"&endTime="+endTime+"&userID=180604htrj&userPassword=180604htrj";
        Toast.makeText(getActivity(),"请稍等...",Toast.LENGTH_LONG).show();
        HttpClient.postRequest(url,requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=new Message();
                message.what=0;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonString=response.body().string();
                Log.d("envHistory", "onResponse: "+jsonString);
                try {
                    JSONObject jsonObject=new JSONObject(jsonString);
                    JSONArray array=jsonObject.getJSONArray("HisData");
                    Message message=new Message();
                    message.what=1;
                    message.obj=array;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void getLighHistory(){
        RequestBody requestBody=RequestBody.create(HttpClient.JSONString,"");
        String url="http://www.0531yun.cn/wsjc/Device/getDevHisData.do?" +
                "devKey="+devLigg+"&beginTime="+beginTime+"&endTime="+endTime+"&userID=180604htrj&userPassword=180604htrj";
        Toast.makeText(getActivity(),"请稍等...",Toast.LENGTH_LONG).show();
        HttpClient.postRequest(url,requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=new Message();
                message.what=0;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonString=response.body().string();
                Log.d("envHistory", "onResponse: "+jsonString);
                try {
                    JSONObject jsonObject=new JSONObject(jsonString);
                    JSONArray array=jsonObject.getJSONArray("HisData");
                    Message message=new Message();
                    message.what=2;
                    message.obj=array;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}
