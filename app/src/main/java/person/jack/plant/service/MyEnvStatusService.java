package person.jack.plant.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import person.jack.plant.db.entity.Plants;
import person.jack.plant.http.JsonAnalysis;
import person.jack.plant.model.SerializableMap;

import static person.jack.plant.activity.PlantsDetailActivity.TAG;

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
    String url;

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

                String jsonString = "[\n" +
                        "    {\n" +
                        "        \"DevKey\": \"105\",\n" +
                        "        \"DevName\": \"1号主机\",\n" +
                        "        \"DevType\": \"0\",\n" +
                        "        \"DevAddr\": \"10000043\",\n" +
                        "        \"DevTempName\": \"温度(℃)\",\n" +
                        "        \"DevTempValue\": \"23\",\n" +
                        "        \"DevHumiName\": \"湿度(%RH)\",\n" +
                        "        \"DevHumiValue\": \"60\",\n" +
                        "        \"DevStatus\": \"false\",\n" +
                        "        \"DevLng\": \"0.0\",\n" +
                        "        \"DevLat\": \"0.0\",\n" +
                        "        \"TempStatus\": \"0\",\n" +
                        "        \"HumiStatus\": \"0\",\n" +
                        "        \"devDataType1\": \"0\",\n" +
                        "        \"devDataType2\": \"0\",\n" +
                        "        \"devPos\": \"1\"\n" +
                        "    }]\n";


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
                Map<String, int[]> map = null;
                Message message = new Message();
                try {
                    map = JsonAnalysis.getEnv(jsonString);
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
