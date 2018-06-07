package person.jack.plant.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.UserDao;
import person.jack.plant.db.entity.User;
import person.jack.plant.ui.UIHelper;

/**
 * 用户注册界面 chenle
 */
public class PersonRegisterActivity extends BaseFragmentActivity {
    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;

    EditText name, pwd, phone;
    Button back, res;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_register);
        ButterKnife.bind(this);

        textHeadTitle.setText("注册");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        name = (EditText) findViewById(R.id.ed_user_name);
        pwd = (EditText) findViewById(R.id.ed_user_pwd);
        phone = (EditText) findViewById(R.id.ed_user_phone);
        res = (Button) findViewById(R.id.btn_res);
        userDao = new UserDao(AppContext.getInstance());

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userPwd = pwd.getText().toString();
                String userPhone = phone.getText().toString();
                User user1 = userDao.findByName(userName);

                String msg;
                if (user1 == null) {
                    if (isPhone(userPhone)) {
                        User user = new User(1, userName, userPwd, userPhone);
                        userDao.add(user);
                        msg = "注册成功！";
                        UIHelper.showMember(PersonRegisterActivity.this);
                        finish();
                    } else {
                        msg = "手机号码不规范！";
                    }
                } else {
                    msg = "已存在该用户！";
                }
                UIHelper.ToastMessage(getApplicationContext(), msg);
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
