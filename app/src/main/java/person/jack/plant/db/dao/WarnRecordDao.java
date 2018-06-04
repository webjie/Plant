package person.jack.plant.db.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import person.jack.plant.db.DatabaseHelper;
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

    /**
     * 添加报警记录
     * @param warnRecord
     */
    public void add(WarnRecord warnRecord){
        try {
          warnDao.create(warnRecord);
            Log.d("student3","警告添加成功");
        } catch (SQLException e) {
            Log.d("student3","警告添加失败");
            e.printStackTrace();
        }
    }

    /**
     * 查询所有的报警记录
     * @return
     */
    public List<WarnRecord> findAll(){
        List<WarnRecord>list=null;
        try {
            list=warnDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }
}
