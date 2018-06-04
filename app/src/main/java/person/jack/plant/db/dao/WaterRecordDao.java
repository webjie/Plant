package person.jack.plant.db.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import person.jack.plant.db.DatabaseHelper;
import person.jack.plant.db.entity.WaterRecord;

/**
 * Created by yanxu on 2018/6/4.
 */

public class WaterRecordDao {
    private Context context;
    private Dao<WaterRecord,Integer> waterDao;
    private DatabaseHelper helper;
    public WaterRecordDao(Context context){
        this.context=context;
        try{
            helper=DatabaseHelper.getHelper(context);
            waterDao=helper.getDao(WaterRecord.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 添加灌溉记录
     * @param waterRecord
     */
    public void add(WaterRecord waterRecord){
        try {
            waterDao.create(waterRecord);
            Log.d("student3","waterRecord添加成功");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("student3","waterRecord添加失败");
        }
    }
    /**
     * 查询所有的灌溉记录
     * @return
     */
    public List<WaterRecord> findAll(){
        List<WaterRecord>list=null;
        try {
            list=waterDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }
}
