package person.jack.plant.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import person.jack.plant.R;
import person.jack.plant.db.dao.UserDao;
import person.jack.plant.db.entity.User;
import person.jack.plant.ui.UIHelper;

public class ResActivity extends BaseFragmentActivity {
    EditText name, pwd, phone;
    Button back, res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        name = (EditText) findViewById(R.id.ed_user_name);
        pwd = (EditText) findViewById(R.id.ed_user_pwd);
        phone = (EditText) findViewById(R.id.ed_user_phone);
        back = (Button) findViewById(R.id.btn_res_back);
        res = (Button) findViewById(R.id.btn_res);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showMember(ResActivity.this);
            }
        });

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userPwd = pwd.getText().toString();
                String userPhone = phone.getText().toString();
                if (isPhone(userPhone)){
                    User user = new User(1, userName, userPwd, userPhone);
                    UserDao userDao = new UserDao(ResActivity.this);
                    userDao.add(user);
                    Toast.makeText(ResActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    List<User> list = userDao.findAll();
                    Log.d("user", list.get(0).getName());
                    UIHelper.showMember(ResActivity.this);
                }else{
                    Toast.makeText(ResActivity.this, "手机号码不规范！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean isPhone(String phone) {
        String num = "[1][35789]\\d{9}";
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return phone.matches(num);
        }
    }

}
