package person.jack.plant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * 修改密码界面 ，chenle
 */
public class PersonChangePwdActivity extends BaseFragmentActivity {
    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;

    EditText oldPwd, newPwd, rePwd;
    Button btnOk;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_change_pwd);
        ButterKnife.bind(this);

        textHeadTitle.setText("修改密码");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        oldPwd = (EditText) findViewById(R.id.ed_old_pwd);
        newPwd = (EditText) findViewById(R.id.ed_new_pwd);
        rePwd = (EditText) findViewById(R.id.ed_re_true);
        btnOk = (Button) findViewById(R.id.btn_ok);
        userDao = new UserDao(AppContext.getInstance());

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = new SharedPreferences();
                final String userName = sharedPreferences.getString("userName","");
                if (userName == null || userName.length() == 0){
                    UIHelper.ToastMessage(getApplicationContext(), "无法获取用户信息，请重新登录！");
                    UIHelper.showLogin(PersonChangePwdActivity.this);
                    finish();
                }
                final String userOldPwd = oldPwd.getText().toString();
                final String userNewPwd = newPwd.getText().toString();
                final String reTrue = rePwd.getText().toString();

                User user = userDao.findByName(userName);

                String msg;
                if (userOldPwd.equals(user.getPwd())) {
                    if (userNewPwd.equals(reTrue)) {
                        user.setPwd(userNewPwd);
                        userDao.update(user);
                        msg = "密码修改成功！";
                    } else {
                        msg = "两次密码不匹配";
                    }
                } else {
                    msg = "密码错误";
                }
                UIHelper.ToastMessage(getApplicationContext(),msg);
            }
        });
    }
}
