package person.jack.plant.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.alibaba.fastjson.JSONArray;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.EnvDao;
import person.jack.plant.db.dao.PlantsDao;
import person.jack.plant.db.entity.Env;
import person.jack.plant.db.entity.Plants;
import person.jack.plant.http.HttpClient;
import person.jack.plant.http.HttpResponseHandler;
import person.jack.plant.http.RestApiResponse;
import person.jack.plant.model.SearchParam;
import person.jack.plant.model.SearchPlant;
import person.jack.plant.ui.UIHelper;
import person.jack.plant.ui.loadmore.LoadMoreListView;
import person.jack.plant.ui.pulltorefresh.PullToRefreshBase;
import person.jack.plant.ui.pulltorefresh.PullToRefreshListView;
import person.jack.plant.ui.quickadapter.BaseAdapterHelper;
import person.jack.plant.ui.quickadapter.QuickAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

import static person.jack.plant.activity.PlantsDetailActivity.TAG;

/**
 * 统计界面 kakee
 * 2018-6-5
 */
public class BufferKnifeFragment extends Fragment {

    private Activity context;

    private SearchParam param;
    private int pno = 1;
    private boolean isLoadAll;
    private List<Plants> list;
    public static Plants curPlants;

    @Bind(R.id.listView)
    ListView listView;

    QuickAdapter<Plants> adapter;
    private EnvDao envDao;
    private List<Env> envList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_shop_list, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initData();
        initView();
        loadData();
    }

    void initView() {
        envDao = new EnvDao(AppContext.getInstance());
        envList = envDao.findAll();
        if (envList.size() == 0) {
            init();
            envList=envDao.findAll();
        }

        adapter = new QuickAdapter<Plants>(context, R.layout.statistics_item_layout) {

            @Override
            protected void convert(final BaseAdapterHelper helper, Plants shop) {
                Log.d(TAG, "convert: " + shop.getName());
                helper.setText(R.id.item_name, shop.getName()); // 自动异步加载图片

                if ("花生".equals(shop.getName().toString())) {
                    helper.setBackgroundRes(R.id.item_pic, R.drawable.img1);
                }
                if ("辣椒".equals(shop.getName().toString())) {
                    helper.setBackgroundRes(R.id.item_pic, R.drawable.img2);
                }
                if ("白掌".equals(shop.getName().toString())) {
                    helper.setBackgroundRes(R.id.item_pic, R.drawable.img3);
                }
                if ("碧玉".equals(shop.getName().toString())) {
                    helper.setBackgroundRes(R.id.item_pic, R.drawable.img4);
                }
                if ("双线竹语".equals(shop.getName().toString())) {
                    helper.setBackgroundRes(R.id.item_pic, R.drawable.img5);
                }
                if ("长寿花".equals(shop.getName().toString())) {
                    helper.setBackgroundRes(R.id.item_pic, R.drawable.img6);
                }
                if (shop.getHum() != null) {
                    helper.setText(R.id.item_temp, shop.getTemp() + "");
                    helper.setText(R.id.item_hum, shop.getHum() + "");
                    helper.setText(R.id.item_lig, shop.getLight() + "");
                }
                helper.setOnClickListener(R.id.item_temp, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UIHelper.showChartActivity(getActivity(), 0);
                        curPlants=list.get(helper.getPosition());
                    }
                }).setOnClickListener(R.id.item_hum, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UIHelper.showChartActivity(getActivity(), 1);
                        curPlants=list.get(helper.getPosition());
                    }
                }).setOnClickListener(R.id.item_lig, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UIHelper.showChartActivity(getActivity(), 2);
                        curPlants=list.get(helper.getPosition());
                    }
                }).setOnClickListener(R.id.item_pic, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UIHelper.showChartActivity(getActivity(), 0);
                        curPlants=list.get(helper.getPosition());
                    }
                });


            }
        };

        listView.setAdapter(adapter);

        // 点击事件
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                UIHelper.showChartActivity(getActivity(),0);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    Picasso.with(context).pauseTag(context);
                } else {
                    Picasso.with(context).resumeTag(context);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }


    public void init() {
        Env env1 = new Env(1, 25, 63, 1500);
        Env env2 = new Env(1, 24, 70, 1450);
        Env env3 = new Env(1, 24, 65, 1332);
        Env env4 = new Env(1, 23, 71, 1200);
        Env env5 = new Env(1, 24, 66, 1450);
        Env env6 = new Env(1, 22, 70, 1333);
        Env env7 = new Env(1, 25, 67, 1456);
        Env env8 = new Env(1, 27, 65, 1250);
        Env env9 = new Env(1, 25, 60, 1350);
        Env env10 = new Env(1, 24, 60, 1300);
        envDao.add(env1);
        envDao.add(env2);
        envDao.add(env3);
        envDao.add(env4);
        envDao.add(env5);
        envDao.add(env6);
        envDao.add(env7);
        envDao.add(env8);
        envDao.add(env9);
        envDao.add(env10);


    }

    private void initData() {
        param = new SearchParam();
        pno = 1;
        isLoadAll = false;
    }

    private void loadData() {
        if (isLoadAll) {
            return;
        }
        param.setPno(pno);
        //使用模拟数据
        String body = "[" +
                "{ \"name\":\"花生\" , \"logo\":\"img1.jpg\" }," +
                "{ \"name\":\"辣椒\" , \"logo\":\"img1.jpg\" }," +
                "{ \"name\":\"白掌\" , \"logo\":\"img1.jpg\" }," +
                "{ \"name\":\"碧玉\" , \"logo\":\"img1.jpg\" }," +
                "{ \"name\":\"双线竹语\" , \"logo\":\"img1.jpg\" }," +
                "{ \"name\":\"长寿花\" , \"logo\":\"img1.jpg\" }," +
                "]";
        try {
            list = JSONArray.parseArray(body, Plants.class);
            isLoadAll = list.size() < HttpClient.PAGE_SIZE;
            if (pno == 1) {
                adapter.clear();
            }
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
            pno++;
            Log.d(TAG, "loadData: 模拟数据使用完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //模拟数据使用完毕


//        HttpClient.getRecommendShops(param, new HttpResponseHandler() {
//
//            @Override
//            public void onSuccess(RestApiResponse response) {
//                listView.onRefreshComplete();
//                List<SearchPlant> list = JSONArray.parseArray(response.body, SearchPlant.class);
//                listView.updateLoadMoreViewText(list);
//                isLoadAll = list.size() < HttpClient.PAGE_SIZE;
//                if(pno == 1) {
//                    adapter.clear();
//                }
//                adapter.addAll(list);
//                pno++;
//            }
//
//            @Override
//            public void onFailure(Request request, Exception e) {
//                listView.onRefreshComplete();
//                listView.setLoadMoreViewTextError();
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
//                HttpClient.getRequest(url, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//                            JSONObject jsonObject=new JSONObject(new JSONObject(response.body().string()).getString("serverInfo"));
//                            int [] temp=new int[3];
//                            temp[0]=jsonObject.getInt("temperature")/3;
//                            temp[1]=jsonObject.getInt("humidity")+20;
//                            temp[2]=jsonObject.getInt("LightIntensity");
//                            Message message=new Message();
//                            message.what=1;
//                            message.obj=temp;
//                            handler.sendMessage(message);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
                int[] temp = new int[3];


                Env env = envList.get(random.nextInt(9));
                temp[0] = env.getTemperature();
                temp[1] = env.getHumidity();
                temp[2] = env.getLight();
                Message message = new Message();
                message.what = 1;
                message.obj = temp;
                handler.sendMessage(message);

            }
        };
        timer.schedule(timerTask, 500, 5000);

        Picasso.with(context).resumeTag(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.with(context).pauseTag(context);
        if (timer != null) {
            timer.cancel();
            timerTask.cancel();
            timerTask = null;
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(context).cancelTag(context);
    }

    Timer timer;
    TimerTask timerTask;
    Random random = new Random();
    String url = "http://192.168.1.107:8080/Parking/GetAllSense.do";

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    int[] value = (int[]) message.obj;
                    List<Plants> temList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        Plants plants = adapter.getItem(i);

                        plants.setTemp(value[0]-random.nextInt(5));
                        plants.setHum(value[1]-random.nextInt(5));
                        plants.setLight(value[2]+random.nextInt(100));

                        temList.add(plants);
                    }
                    adapter.clear();
                    adapter.addAll(temList);


                    Log.d(TAG, "handleMessage: " + temList.size());
                    adapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });


}