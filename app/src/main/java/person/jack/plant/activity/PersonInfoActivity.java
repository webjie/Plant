package person.jack.plant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.UserDao;
import person.jack.plant.db.entity.User;
import person.jack.plant.ui.UIHelper;
import person.jack.plant.utils.SharedPreferences;

/**
 * 我的-个人信息界面，chenle
 */
public class PersonInfoActivity extends BaseFragmentActivity {
    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;

    Button back;
    TextView name, phone;
    SharedPreferences sharedPreferences;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);

        textHeadTitle.setText("个人信息 ");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = new SharedPreferences();
        name = (TextView) findViewById(R.id.person_name);
        phone = (TextView) findViewById(R.id.person_phone);
        userDao = new UserDao(AppContext.getInstance());
        String userName = sharedPreferences.getString("userName", "");
        User user = userDao.findByName(userName);
        if(user != null) {
            name.setText(user.getName());
            phone.setText(user.getPhone());
        }
    }
}
