package person.jack.plant.db.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.sql.Wrapper;
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

    /**
     * 根据环境名查询
     * @param name
     * @return
     */
    public WarnRecord findByNameAndType(String name,String type){
        List<WarnRecord>list=null;
        QueryBuilder queryBuilder=warnDao.queryBuilder();
        //list=queryBuilder.query();
        WarnRecord warnRecord=null;
        try {
            list=warnDao.queryBuilder().where().eq("name",name).and().eq("type",type).query();
            Log.d("findByNameType","list长度"+list.size());
            if(list.size()==0){
                warnRecord=null;
            }else{
                warnRecord=list.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warnRecord;

    }

    /**
     * 更新
     * @param warnRecord
     */
    public void update(WarnRecord warnRecord){
        try {
            warnDao.update(warnRecord);
            Log.d("student3","警告更新成功");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("student3","警告更新失败");
        }

    }

    /**
     * 删除
     * @param warnRecord
     */
    public void delete(WarnRecord warnRecord){
        try {
            warnDao.delete(warnRecord);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
