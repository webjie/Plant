package person.jack.plant.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.squareup.picasso.Picasso;

import person.jack.plant.R;
import person.jack.plant.activity.MainActivity;
import person.jack.plant.db.dao.PlantsDao;
import person.jack.plant.db.dao.WaterRecordDao;
import person.jack.plant.db.entity.Plants;
import person.jack.plant.db.entity.WaterRecord;
import person.jack.plant.http.HttpClient;
import person.jack.plant.http.HttpResponseHandler;
import person.jack.plant.http.RestApiResponse;
import person.jack.plant.model.SearchParam;
import person.jack.plant.ui.UIHelper;
import person.jack.plant.ui.loadmore.LoadMoreListView;
import person.jack.plant.ui.quickadapter.BaseAdapterHelper;
import person.jack.plant.ui.quickadapter.QuickAdapter;
import person.jack.plant.utils.BitmapUtil;
import person.jack.plant.utils.DeviceUtil;

import java.io.File;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import okhttp3.Request;

import static person.jack.plant.ui.UIHelper.TAG;

/**
 * 首页-获取远程服务器商品列表
 */
public class DemoPtrFragment extends Fragment {
    private MainActivity context;

    private RelativeLayout relativeLayout;

    private SearchParam param;
    private int pno = 1;
    private boolean isLoadAll;
    private WaterRecordDao waterRecordDao;
    @Bind(R.id.rotate_header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame;
    @Bind(R.id.listView)
    LoadMoreListView listView;
    QuickAdapter<Plants> adapter;
    List<Plants> list;
    public static Plants curPlant;
    private PlantsDao plantsDao;


    public void setLoadAll(boolean loadAll) {
        isLoadAll = loadAll;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo_ptr, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (MainActivity) getActivity();
        plantsDao=new PlantsDao(getContext());
        initData();
        initView();
    }

    void initView() {
        adapter = new QuickAdapter<Plants>(context, R.layout.recommend_shop_list_item) {
            @Override
            protected void convert(final BaseAdapterHelper helper, final Plants shop) {

                helper.setText(R.id.tv_name, shop.getName()); // 自动异步加载图片
//
//                if ("花生".equals(shop.getName().toString())){
//                    helper.setImageResource(R.id.logo,R.drawable.img1);
//                }
//                if ("辣椒".equals(shop.getName().toString())){
//                    helper.setImageResource(R.id.logo,R.drawable.img2);
//                }
//                if ("白掌".equals(shop.getName().toString())){
//                    helper.setImageResource(R.id.logo,R.drawable.img3);
//                }
//                if ("碧玉".equals(shop.getName().toString())){
//                    helper.setImageResource(R.id.logo,R.drawable.img4);
//                }
//                if ("双线竹语".equals(shop.getName().toString())){
//                    helper.setImageResource(R.id.logo,R.drawable.img5);
//                }
//                if ("长寿花".equals(shop.getName().toString())){
//                    helper.setImageResource(R.id.logo,R.drawable.img6);
//                }
                if(shop.getImage()!=null){
                    if(shop.getImage().equals("null")){
                        helper.setImageResource(R.id.logo,R.drawable.default_image);
                    }else{
                        File file=new File(shop.getImage());
                        if(file.exists()){
                            Bitmap bitmap=BitmapFactory.decodeFile(shop.getImage());
                            helper.setImageBitmap(R.id.logo,bitmap);
                        }
                    }
                }


               helper.setOnLongClickListener(R.id.shop_item,new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setMessage("确定删除吗？");
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              Plants plants=plantsDao.findByName(shop.getName());
                              if(plants!=null){
                                  plantsDao.deletePlant(plants);
                                  Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                                  try{
                                      MainActivity mainActivity=(MainActivity)getActivity();
                                      MainPagerFragment mainPagerFragment=(MainPagerFragment)mainActivity.getSupportFragmentManager().findFragmentByTag("HomeFragment");
                                      DemoPtrFragment demoPtrFragment=(DemoPtrFragment)mainPagerFragment.getChildFragmentManager().getFragments().get(1);
                                      demoPtrFragment.setLoadAll(false);
                                      demoPtrFragment.loadData();

                                      BufferKnifeFragment bufferKnifeFragment=(BufferKnifeFragment)mainActivity.getSupportFragmentManager().findFragmentByTag("ImFragment");
                                      if(bufferKnifeFragment!=null){
                                          bufferKnifeFragment.setLoadAll(false);
                                          bufferKnifeFragment.loadData();

                                      }
                                  }catch (Exception e){
                                      e.printStackTrace();
                                  }
                                  loadData();
                              }
                            }
                        });
                        builder.setPositiveButton("取消",null);
                        builder.show();
                        return false;
                    }
                });

                helper.setOnClickListener(R.id.btnWatering, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        waterRecordDao =new WaterRecordDao(getContext());
                        WaterRecord waterRecord=new WaterRecord(1,shop.getName(),new Date());
                        waterRecordDao.add(waterRecord);
                        //需要调用网络接口f
                        Toast.makeText(context, "浇水成功", Toast.LENGTH_SHORT).show();
                    }
                })
                        .setOnClickListener(R.id.tv_name, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "onClick: 点击");
                                //跳转碎片
                                curPlant = list.get(helper.getPosition());
                                UIHelper.showPlantsDetailActivity(getActivity());

                            }
                        })
                .setOnClickListener(R.id.logo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: 点击");
                        //跳转碎片
                        curPlant = list.get(helper.getPosition());
                        UIHelper.showPlantsDetailActivity(getActivity());
                    }
                });
            }
        };
        listView.setDrawingCacheEnabled(true);
        listView.setAdapter(adapter);

        // header custom begin
        final StoreHouseHeader header = new StoreHouseHeader(context);
        header.setPadding(0, DeviceUtil.dp2px(context, 15), 0, 0);
        header.initWithString("Plant");
        header.setTextColor(getResources().getColor(R.color.gray));
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        // header custom end

        // 下拉刷新
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
                loadData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        // 加载更多
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData();
            }
        });

        // 点击事件
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: itme");
                if (i != adapter.getCount()) {
                    UIHelper.showHouseDetailActivity(context);
                    //跳转碎片

                    curPlant = list.get(i);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, new PlantsDetailFragment(), "plantsDetail").commit();
                }

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
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

    }

    private void initData() {
        param = new SearchParam();
        pno = 1;
        isLoadAll = false;
    }

    public void loadData() {
        Log.d(TAG, "loadData: ");
        if (isLoadAll) {
            return;
        }
        param.setPno(pno);

        if(list!=null){
            list.clear();
            adapter.clear();
        }

//        //使用模拟数据
//        String body = "[" +
//                "{ \"name\":\"花生\" , \"logo\":\"img1.jpg\" }," +
//                "{ \"name\":\"辣椒\" , \"logo\":\"img1.jpg\" }," +
//                "{ \"name\":\"白掌\" , \"logo\":\"img1.jpg\" }," +
//                "{ \"name\":\"碧玉\" , \"logo\":\"img1.jpg\" }," +
//                "{ \"name\":\"双线竹语\" , \"logo\":\"img1.jpg\" }," +
//                "{ \"name\":\"长寿花\" , \"logo\":\"img1.jpg\" }," +
//                "]";
        try {
            list = plantsDao.findAll();
            PlantsDao dao=new PlantsDao(getContext());
            listView.updateLoadMoreViewText(list);
            isLoadAll = list.size() < HttpClient.PAGE_SIZE;
            if (pno == 1) {
                adapter.clear();
            }
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
            pno++;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        HttpClient.getRecommendShops(param, new HttpResponseHandler() {
//            @Override
//            public void onSuccess(RestApiResponse response) {
//                mPtrFrame.refreshComplete();
//                List<Plants> list = JSONArray.parseArray(response.body, Plants.class);
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
//                mPtrFrame.refreshComplete();
//
//
//
//
//                //listView.setLoadMoreViewTextError();  //使用模拟数据时，此条目注释
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(context).resumeTag(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.with(context).pauseTag(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(context).cancelTag(context);
    }

}
