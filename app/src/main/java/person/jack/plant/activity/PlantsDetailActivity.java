package person.jack.plant.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import person.jack.plant.R;
import person.jack.plant.ui.swipebacklayout.SwipeBackActivity;

public class PlantsDetailActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_plants_detail);
            initView();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initView(){
        try{
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
