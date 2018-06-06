package person.jack.plant.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.UserDao;
import person.jack.plant.db.entity.User;
import person.jack.plant.ui.UIHelper;

public class RePwdActivity extends BaseFragmentActivity {
    EditText name, oldPwd, newPwd, rePwd;
    Button back, btnOk;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_pwd);
        name = (EditText) findViewById(R.id.ed_re_name);
        oldPwd = (EditText) findViewById(R.id.ed_old_pwd);
        newPwd = (EditText) findViewById(R.id.ed_new_pwd);
        rePwd = (EditText) findViewById(R.id.ed_re_true);
        back = (Button) findViewById(R.id.btn_rePwd_back);
        btnOk = (Button) findViewById(R.id.btn_ok);
        userDao = new UserDao(AppContext.getInstance());

        final String userName = name.getText().toString();
        final String userOldPwd = oldPwd.getText().toString();
        final String userNewPwd = newPwd.getText().toString();
        final String reTrue = rePwd.getText().toString();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showMember(RePwdActivity.this);
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = userDao.findByName("aa");

                if (user != null) {
                    if ("123".equals(user.getPwd())) {
                        if ("1234".equals("1234")) {
                            user.setPwd("1234");
                            userDao.update(user);
                            Log.d("user",userDao.findAll().get(0).getPwd().toString());
                            Toast.makeText(RePwdActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RePwdActivity.this, "两次密码不匹配！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RePwdActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RePwdActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
