package person.jack.plant.db.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;
import person.jack.plant.db.DatabaseHelper;
import person.jack.plant.db.entity.ValueSet;
/**
 * Created by yanxu on 2018/6/4.
 */
public class ValueSetDao {
    private Context context;
    private Dao<ValueSet, Integer> valueSetDao;
    private DatabaseHelper helper;

    public ValueSetDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            valueSetDao = helper.getDao(ValueSet.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加阈值
     *
     * @param valueSet
     */
    public void add(ValueSet valueSet) {
        try {
            valueSetDao.create(valueSet);
            Log.d("student3","阈值添加成功");
        } catch (SQLException e) {
            Log.d("student3","阈值添加失败");
            e.printStackTrace();
        }
    }

    /**
     * 查询所有阈值记录
     *
     * @return
     */
    public List<ValueSet> findAll() {
        List<ValueSet> list = null;
        try {
            list = valueSetDao.queryForAll();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据名称查询
     *
     * @param name
     * @return
     */
    public List<ValueSet> findValueName(String name) {
        List<ValueSet> list = null;

        try {
            list = valueSetDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }

}
