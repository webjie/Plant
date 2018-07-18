package person.jack.plant.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import person.jack.plant.R;
import person.jack.plant.db.entity.Plants;
import person.jack.plant.http.HttpClient;
import person.jack.plant.http.JsonAnalysis;
import person.jack.plant.model.SerializableMap;

import static person.jack.plant.activity.PlantsDetailActivity.TAG;

/**
 * 实时获取环境信息服务
 */
public class MyEnvStatusService extends Service {
    public MyEnvStatusService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    Timer timer;
    TimerTask timerTask;
    String url=HttpClient.DEV_ADDRESS;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Map<String, int[]> map = (Map<String, int[]>) message.obj;
                    SerializableMap serializableMap=new SerializableMap();
                    serializableMap.setMap(map);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("map",serializableMap);
                    Intent intent=new Intent("plants.chart.update");
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                    Log.d(TAG, "handleMessage: 发送广播");
                    break;
            }
            return false;
        }
    });

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");


                HttpClient.getRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Map<String, int[]> map = null;
                        String jsonString =response.body().string();
                        Log.d(TAG, "onResponse: "+jsonString);
                        Message message = new Message();
                        try {
                            map = JsonAnalysis.getEnv(jsonString,getBaseContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (map != null && map.size() != 0) {
                            message.what = 1;
                            message.obj = map;
                        } else {
                            message.what = 2;
                        }
                        handler.sendMessage(message);

                    }
                });

            }
        };
        timer.schedule(timerTask, 500, 5000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timerTask.cancel();
            timerTask = null;
            timer = null;
        }
    }
}
