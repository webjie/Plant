package person.jack.plant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    Button back;
    TextView name, phone;
    SharedPreferences sharedPreferences;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        sharedPreferences = new SharedPreferences();

        name = (TextView) findViewById(R.id.person_name);
        phone = (TextView) findViewById(R.id.person_phone);
        back = (Button) findViewById(R.id.btn_person_back);
        userDao = new UserDao(AppContext.getInstance());

        String userName = sharedPreferences.getString("userName", "");
        User user = userDao.findByName(userName);
        name.setText(user.getName());
        phone.setText(user.getPhone());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showMember(PersonInfoActivity.this);
                finish();
            }
        });
    }
}
