package person.jack.plant.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * json解析类
 * Created by kakee on 2018/6/9.
 */

public class JsonAnalysis {

    /**
     * 解析环境信息json
     * @param jsonString
     * @return
     * @throws JSONException
     */
    public static Map<String,int[]> getEnv(String jsonString) throws JSONException {
        Map<String,int[]> map=new HashMap<>();
        JSONArray jsonArray=new JSONArray(jsonString);
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            int [] tmpArray=new int[3];
            tmpArray[0]=jsonObject.getInt("DevTempValue");
            tmpArray[1]=jsonObject.getInt("DevHumiValue");
            tmpArray[2]=1200;

            map.put(i+"",tmpArray);
        }

        return map;

    }
}
