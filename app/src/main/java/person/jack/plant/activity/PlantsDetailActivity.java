package person.jack.plant.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    public static final String TAG="Kaa";

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
        plant=DemoPtrFragment.curPlant;

        if(plant!=null){
            Log.d(TAG, "initView: "+plant.getName());
            //先使用默认图片
            Log.d(TAG, "initView: "+plant.getImage());
            ivPlant.setImageResource(R.drawable.default_image);
        tvName.setText(plant.getName());
//        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        tvDate.setText(dateFormat.format(plant.getPlantingDate()));

            //目前是写死的生长状态
        tvState.setText(plant.getGrowthStage());
        }



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
                    tvDetailTemp.setText("温度：："+value[0]+"℃");
                    tvDetailHum.setText("湿度："+value[1]+"%");
                    tvDetailLig.setText("光照："+value[2]+"lx");
            }
            return false;
        }
    });

    @Override
    protected void onStart() {
        super.onStart();
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                HttpClient.getRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject=new JSONObject(new JSONObject(response.body().string()).getString("serverInfo"));
                            int [] values=new int[3];
                            values[0]=jsonObject.getInt("temperature");
                            values[1]=jsonObject.getInt("humidity");
                            values[2]=jsonObject.getInt("LightIntensity");
                            Message message=new Message();
                            message.what=1;
                            message.obj=values;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask,500,15000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer!=null){
            timer.cancel();
            timerTask.cancel();
            timerTask=null;
            timer=null;
        }
    }
}
