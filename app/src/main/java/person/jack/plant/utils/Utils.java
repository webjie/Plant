package person.jack.plant.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import person.jack.plant.common.AppContext;
import person.jack.plant.http.HttpClient;
import person.jack.plant.http.HttpResponseHandler;
import person.jack.plant.http.RestApiResponse;
import person.jack.plant.model.MyAppContants;

import static person.jack.plant.activity.PlantsDetailActivity.TAG;


/**
 * Created by tiansj on 15/7/29.
 */
public class Utils {

    private static final String TAG = "Utils";

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }


    /**
     * 设置手机网络类型，wifi，cmwap，ctwap，用于联网参数选择
     * @return
     */
    static String getNetworkType() {
        String networkType = "wifi";
        ConnectivityManager manager = (ConnectivityManager) AppContext.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
        if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
            return ""; // 当前网络不可用
        }

        String info = netWrokInfo.getExtraInfo();
        if ((info != null)
                && ((info.trim().toLowerCase().equals("cmwap"))
                || (info.trim().toLowerCase().equals("uniwap"))
                || (info.trim().toLowerCase().equals("3gwap")) || (info
                .trim().toLowerCase().equals("ctwap")))) {
            // 上网方式为wap
            if (info.trim().toLowerCase().equals("ctwap")) {
                // 电信
                networkType = "ctwap";
            } else {
                networkType = "cmwap";
            }
        }
        return networkType;
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static void getPlantTypeByImage(String path){
        String time_stamp = System.currentTimeMillis() / 1000 + "";
        String nonce_str= TencentAISign.getRandomString(10);

        Handler handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                return false;
            }
        });

        //网络图片
        try {
            //本地图片
        byte[] imageData= UrlMethodUtil.local2byte(path);
//            byte[] imageData= UrlMethodUtil.url2byte("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3495450057,3472067227&fm=5");
            String img64= Base64Util.encode(imageData);
            final Map<String,String> person_Id_body = new HashMap<>();
            person_Id_body.put("app_id", String.valueOf(MyAppContants.APP_ID_AI));
            person_Id_body.put("time_stamp",time_stamp);
            person_Id_body.put("nonce_str", nonce_str);
            person_Id_body.put("image", img64);
            person_Id_body.put("scene","2");

            String sign=TencentAISignSort.getSignature(person_Id_body);
            person_Id_body.put("sign",sign);
            final Map<String,String> headers=new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");

            if(HttpClient.isNetworkAvailable()){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpResponse responseBD = null;
                        try {
                            responseBD = HttpsUtil4Tencent.doPostTencentAI(MyAppContants.IMAGE_LABEL_PLANTS, headers, person_Id_body);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String json = null;
                        try {
                            json = EntityUtils.toString(responseBD.getEntity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(json);  //这个就是我们的要的数据了
                    }
                }).start();


            }else{


            }

            Log.d(TAG, "getPlantTypeByImage: "+sign);
            Log.d(TAG, "getPlantTypeByImage: "+img64);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    static String getString(Context context, int resId){
        return context.getResources().getString(resId);
    }
}
