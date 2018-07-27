package person.jack.plant.activity;

import android.content.Intent;
import android.os.Bundle;
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
import person.jack.plant.utils.SharedPreferences;

/**
 * 5.用户登录功能
 *  author : cl
 */
public class LoginActivity extends BaseFragmentActivity {
    EditText name, pwd;
    Button btnLogin;
    UserDao userDao;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = new SharedPreferences();

        btnLogin = (Button) findViewById(R.id.btnSure);
        name = (EditText) findViewById(R.id.login_name);
        pwd = (EditText) findViewById(R.id.login_pwd);
        userDao = new UserDao(AppContext.getInstance());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = name.getText().toString();
                final String password = pwd.getText().toString();

                User user = userDao.findByName(userName);
                if (user != null) {
                    if ((password).equals(user.getPwd())) {
                        sharedPreferences.putString("userName", user.getName());
                        sharedPreferences.putBoolean("isLogin", true);
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
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
              intent.putExtra("result",4);
                startActivity(intent);
                finish();
            }
        });
    }

}
