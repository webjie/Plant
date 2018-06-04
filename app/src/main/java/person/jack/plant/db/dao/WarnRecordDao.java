package person.jack.plant.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import person.jack.plant.db.DatabaseHelper;
import person.jack.plant.db.entity.ValueSet;
import person.jack.plant.db.entity.WarnRecord;

/**
 * Created by yanxu on 2018/6/4.
 */

public class WarnRecordDao {
    private Context context;
    private Dao<WarnRecord,Integer> warnDao;
    private DatabaseHelper helper;
    public WarnRecordDao(Context context){
        this.context=context;
        try{
            helper=DatabaseHelper.getHelper(context);
            warnDao=helper.getDao(WarnRecord.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void add(WarnRecord warnRecord){
        try {
          warnDao.create(warnRecord);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
