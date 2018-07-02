package person.jack.plant.http;

import android.content.Context;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import person.jack.plant.R;

/**
 * json解析类
 * Created by kakee on 2018/6/9.
 */

public class JsonAnalysis {

    /**
     * 解析环境信息json
     *
     * @param jsonString
     * @return
     * @throws JSONException
     */
    public static Map<String, int[]> getEnv(String jsonString,Context context) throws JSONException {
        Map<String, int[]> map = new HashMap<>();
        int valueTem = 0;
        int valueHum = 0;
        int valueLig = 0;
        JSONArray array = new JSONArray(jsonString);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String name = object.getString("DevName");
            if (name.equals(context.getResources().getString(R.string.dev_soil))) {
                valueTem = object.getInt("DevTempValue");
                valueHum = object.getInt("DevHumiValue");

            } else if (name.equals(context.getResources().getString(R.string.dev_light))) {
                valueLig = object.getInt("DevHumiValue");
            }
        }
        int[] temp = new int[3];
        temp[0] = valueTem;
        temp[1] = valueHum;
        temp[2] = valueLig;


        map.put("values", temp);

        return map;

    }

    /**
     * @param json
     * @return
     */
    public static List<String> getPlantNameByImage(String json) throws JSONException {
        List<String> plantsName = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        String result = jsonObject.getString("msg");

        return plantsName;
    }
}
