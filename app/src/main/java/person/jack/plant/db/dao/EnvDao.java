package person.jack.plant.db.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import person.jack.plant.db.DatabaseHelper;
import person.jack.plant.db.entity.Env;

/**
 * Created by yanxu on 2018/6/6.
 */

public class EnvDao {
    private Context context;
    private DatabaseHelper helper;
    private Dao<Env,Integer> envDao;
    public EnvDao(Context context){
        this.context=context;

        try {
            helper=DatabaseHelper.getHelper(context);

            envDao=helper.getDao(Env.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void add(Env env) {


        try {
            envDao.create(env);
            Log.d("student3","环境添加成功");
        } catch (SQLException e) {
            Log.d("student3","环境添加失败");
            e.printStackTrace();
        }

    }
    public List<Env> findAll() {
        List<Env> list=null;
        try {
            list=envDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
