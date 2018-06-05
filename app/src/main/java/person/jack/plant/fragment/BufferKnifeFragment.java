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
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import person.jack.plant.R;
import person.jack.plant.db.dao.PlantsDao;
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

    @Bind(R.id.listView)
    ListView listView;

    QuickAdapter<Plants> adapter;

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
        adapter = new QuickAdapter<Plants>(context, R.layout.statistics_item_layout) {

            @Override
            protected void convert(BaseAdapterHelper helper, Plants shop) {
                Log.d(TAG, "convert: "+shop.getName());
                helper.setText(R.id.item_name, shop.getName()); // 自动异步加载图片

                if ("花生".equals(shop.getName().toString())){
                    helper.setBackgroundRes(R.id.item_pic,R.drawable.img1);
                }
                if ("辣椒".equals(shop.getName().toString())){
                    helper.setBackgroundRes(R.id.item_pic,R.drawable.img2);
                }
                if ("白掌".equals(shop.getName().toString())){
                    helper.setBackgroundRes(R.id.item_pic,R.drawable.img3);
                }
                if ("碧玉".equals(shop.getName().toString())){
                    helper.setBackgroundRes(R.id.item_pic,R.drawable.img4);
                }
                if ("双线竹语".equals(shop.getName().toString())){
                    helper.setBackgroundRes(R.id.item_pic,R.drawable.img5);
                }
                if ("长寿花".equals(shop.getName().toString())){
                    helper.setBackgroundRes(R.id.item_pic,R.drawable.img6);
                }
                if(shop.getHum()!=null){
                    helper.setText(R.id.item_temp,shop.getTemp()+"");
                    helper.setText(R.id.item_hum,shop.getHum()+"");
                    helper.setText(R.id.item_lig,shop.getLight()+"");
                }


            }
        };

        listView.setAdapter(adapter);

        // 点击事件
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.showHouseDetailActivity(context);
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
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                HttpClient.getRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject=new JSONObject(new JSONObject(response.body().string()).getString("serverInfo"));
                            int [] temp=new int[3];
                            temp[0]=jsonObject.getInt("temperature");
                            temp[1]=jsonObject.getInt("humidity");
                            temp[2]=jsonObject.getInt("LightIntensity");
                            Message message=new Message();
                            message.what=1;
                            message.obj=temp;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask,500,5000);

        Picasso.with(context).resumeTag(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.with(context).pauseTag(context);
        if(timer!=null){
            timer.cancel();
            timerTask.cancel();
            timerTask=null;
            timer=null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(context).cancelTag(context);
    }
    Timer timer;
    TimerTask timerTask;
    String url="http://192.168.1.107:8080/Parking/GetAllSense.do";

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 1:
                    int [] value=(int[])message.obj;
                    List<Plants> list=new ArrayList<>();
                    for(int i=0;i<list.size();i++){
                        Plants plants=adapter.getItem(i);

                        plants.setTemp(value[0]);
                        plants.setHum(value[1]);
                        plants.setLight(value[2]);

                        list.add(plants);
                    }
                    adapter.clear();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                   break;
            }
            return false;
        }
    });


}