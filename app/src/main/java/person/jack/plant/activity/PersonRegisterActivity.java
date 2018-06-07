package person.jack.plant.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.UserDao;
import person.jack.plant.db.entity.User;
import person.jack.plant.ui.UIHelper;

public class PersonRegisterActivity extends BaseFragmentActivity {
    EditText name, pwd, phone;
    Button back, res;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_register);
        name = (EditText) findViewById(R.id.ed_user_name);
        pwd = (EditText) findViewById(R.id.ed_user_pwd);
        phone = (EditText) findViewById(R.id.ed_user_phone);
        back = (Button) findViewById(R.id.btn_res_back);
        res = (Button) findViewById(R.id.btn_res);
        userDao = new UserDao(AppContext.getInstance());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showMember(PersonRegisterActivity.this);
                finish();
            }
        });

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userPwd = pwd.getText().toString();
                String userPhone = phone.getText().toString();
                User user1 = userDao.findByName(userName);
                if (user1 == null) {
                    if (isPhone(userPhone)) {
                        User user = new User(1, userName, userPwd, userPhone);
                        userDao.add(user);
                        Toast.makeText(PersonRegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();

                        UIHelper.showMember(PersonRegisterActivity.this);
                        finish();
                    } else {
                        Toast.makeText(PersonRegisterActivity.this, "手机号码不规范！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PersonRegisterActivity.this, "已存在该用户！", Toast.LENGTH_SHORT).show();
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
