package person.jack.plant.utils;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import person.jack.plant.R;
import person.jack.plant.activity.MainActivity;
import person.jack.plant.activity.PlantInfoActivity;
import person.jack.plant.common.AppContext;
import person.jack.plant.http.HttpClient;

/**
 * Created by Service_User on 2018/7/27.
 */

public class PlantInfoDialog {
    static List<String> plantList = new ArrayList<String>();
    private static ListView lv_planList;
    private static EditText et_plantName;
    private static Button btn_plantSearch;
     public static void showDialog(Context context){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_plantinfo,null);
          lv_planList = (ListView) view.findViewById(R.id.lv_planList);
        lv_planList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

         et_plantName = (EditText) view.findViewById(R.id.et_plantName);

       btn_plantSearch = (Button) view.findViewById(R.id.btn_plantSearch);

          et_plantName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    plantList.clear();
                    httpPlantInfo();
                }

            }
        });





    AlertDialog alertDialog=builder.create();
        alertDialog.setMessage("type");
        alertDialog.show();
    }
    public static void httpPlantInfo() {
        HttpClient.getRequest("https://api.apishop.net/common/plantFamily/queryPlantList" +
                "?apiKey=laUuwV4e99fe7400a5ea670e5c6cb78b74c84eeccbe3af4&pageSize=608", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("addPlant", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("addPlant", result);
                jsonPlantInfo(result);
            }
        });
    }

    public static void jsonPlantInfo(String text) {
        try {
            JSONObject jsonObject = new JSONObject(text);
            String result = jsonObject.getString("result");
            JSONObject jsonObject1 = new JSONObject(result);
            String plants = jsonObject1.getString("plantList");
            JSONArray array = new JSONArray(plants);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("name");
                plantList.add(name);
            }
          new Thread(new Runnable() {
              @Override
              public void run() {
                  ArrayAdapter<String> arrayAdapter =
                          new ArrayAdapter<String>(AppContext.getInstance(),
                                  android.R.layout.simple_list_item_1, plantList);
                  lv_planList.setAdapter(arrayAdapter);

              }
          }).start();




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }






}
