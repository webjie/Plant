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
     * 更新植物
     * @param plants
     */
    public void updatePlant(Plants plants){
        try {
            plantDao.update(plants);
            Log.d("student3","植物更新成功");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("student3","植物更新失败");
        }
    }
    /**
     * 删除植物
     * @param plants
     */
    public void deletePlant(Plants plants){
        try {
            plantDao.delete(plants);
            Log.d("student3","植物删除成功");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("student3","植物删除失败");
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
            Log.d("student3","list长度:"+list.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Plants findByName(String name){
        List<Plants> list=null;
        Plants plants=null;
        try {
            list= plantDao.queryForEq("name",name);
            if(list.size()==0){
                plants=null;

            }else{
                plants=list.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plants;
    }


}
