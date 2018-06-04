package person.jack.plant.db.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import person.jack.plant.db.DatabaseHelper;
import person.jack.plant.db.entity.Plants;

/**
 * Created by yanxu on 2018/6/4.
 */

public class PlantsDao {
    private Context context;
    private Dao<Plants,Integer>plantDao;
    private DatabaseHelper helper;
    public PlantsDao(Context context){
        this.context=context;
        try{
            helper=DatabaseHelper.getHelper(context);
            plantDao=helper.getDao(Plants.class);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 添加植物
     * @param plants
     */
    public void add(Plants plants){
        try {
            plantDao.create(plants);
            Log.d("student3","植物添加成功");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("student3","植物添加失败");
        }
    }

    /**
     *
     * @return 返回所有植物信息
     */
    public List<Plants> findAll(){
        List<Plants> list=null;
        try {
           list= plantDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
