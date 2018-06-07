package person.jack.plant.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import person.jack.plant.R;
import person.jack.plant.db.entity.Plants;
import person.jack.plant.fragment.DemoPtrFragment;
import person.jack.plant.http.HttpClient;
import person.jack.plant.ui.swipebacklayout.SwipeBackActivity;

public class PlantsDetailActivity extends SwipeBackActivity {
    public static final String TAG = "Kaa";

    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;

    private ImageView ivPlant;
    private TextView tvName;
    private TextView tvDate;
    private TextView tvState;
    private Plants plant;

    private TextView tvDetailTemp;
    private TextView tvDetailHum;
    private TextView tvDetailLig;
    private WebView webView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_detail);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        textHeadTitle.setText("植物详情");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivPlant = (ImageView) findViewById(R.id.iv_plant);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvDetailTemp = (TextView) findViewById(R.id.tv_detail_temp);
        tvDetailHum = (TextView) findViewById(R.id.tv_detail_hum);
        tvDetailLig = (TextView) findViewById(R.id.tv_detail_lig);
        webView = (WebView) findViewById(R.id.web_view);

        plant = DemoPtrFragment.curPlant;

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        String encode=null;
        try {
             encode = URLEncoder.encode(plant.getName(), "utf-8");
            Log.d(TAG, "initView: "+encode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(encode!=null){
            webView.loadUrl("https://baike.baidu.com/item/"+encode);
        }





        if (plant != null) {
            if ("花生".equals(plant.getName().toString())) {
                ivPlant.setImageResource(R.drawable.img1);
            }
            if ("辣椒".equals(plant.getName().toString())) {
                ivPlant.setImageResource(R.drawable.img2);
            }
            if ("白掌".equals(plant.getName().toString())) {
                ivPlant.setImageResource(R.drawable.img3);
            }
            if ("碧玉".equals(plant.getName().toString())) {
                ivPlant.setImageResource(R.drawable.img4);
            }
            if ("双线竹语".equals(plant.getName().toString())) {
                ivPlant.setImageResource(R.drawable.img5);
            }
            if ("长寿花".equals(plant.getName().toString())) {
                ivPlant.setImageResource(R.drawable.img6);
            }

                if(plant.getImage()!=null){
                    File file=new File(plant.getImage());
                    if(file.exists()){
                        Bitmap bitmap= BitmapFactory.decodeFile(plant.getImage());
                        ivPlant.setImageBitmap(bitmap);
                    }

                }

            tvName.setText(plant.getName());
            if(plant.getGrowthStage()!=null){
                tvState.setText("当前生长状态："+plant.getGrowthStage());
            }else{
                tvState.setText("当前生长状态：生长中");
            }

            if(plant.getPlantingDate()!=null){
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                tvDate.setText(dateFormat.format(plant.getPlantingDate()));
            }
        }


    }

    Timer timer;
    TimerTask timerTask;
    String url = "";

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    int[] value = (int[]) message.obj;
                    tvDetailTemp.setText("温度：" + value[0] + "℃");
                    tvDetailHum.setText("湿度：" + value[1] + "%");
                    tvDetailLig.setText("光照：" + value[2] + "lx");
            }
            return false;
        }
    });

    @Override
    protected void onStart() {
        super.onStart();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                HttpClient.getRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = new JSONObject(new JSONObject(response.body().string()).getString("serverInfo"));
                            int[] values = new int[3];
                            values[0] = jsonObject.getInt("temperature");
                            values[1] = jsonObject.getInt("humidity");
                            values[2] = jsonObject.getInt("LightIntensity");
                            Message message = new Message();
                            message.what = 1;
                            message.obj = values;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 500, 15000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timerTask.cancel();
            timerTask = null;
            timer = null;
        }
    }
}
