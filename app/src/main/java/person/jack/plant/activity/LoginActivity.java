package person.jack.plant.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.UserDao;
import person.jack.plant.db.entity.User;
import person.jack.plant.ui.UIHelper;


/**
 * 5.登录界面-没有实现远程登录功能-chenle
 */
public class LoginActivity extends BaseFragmentActivity {
    EditText name, pwd;
    Button btnLogin;
    UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnSure);
        name = (EditText) findViewById(R.id.login_name);
        pwd = (EditText) findViewById(R.id.login_pwd);
        userDao = new UserDao(AppContext.getInstance());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_name = name.getText().toString();
                final String user_pwd = pwd.getText().toString();

                List<User> list = userDao.findAll();
                for (User user2 : list
                        ) {
                    Log.d("user", user2.getName());
                }
                User user = userDao.findByName(user_name);
                if (user != null) {
                    if ((user_pwd).equals(user.getPwd())) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                        editor.putString("user_name", user.getName().toString());
                        editor.putBoolean("isLogin", true);
                        editor.commit();
                        UIHelper.showMember(LoginActivity.this);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "无该用户！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
