package person.jack.plant.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import person.jack.plant.db.DatabaseHelper;
import person.jack.plant.db.entity.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private Context context;
    private Dao<User, Integer> userDao;
    private DatabaseHelper helper;

    public UserDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            userDao = helper.getDao(User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @param user
     * @throws SQLException
     */
    public void add(User user) {
        try {
            userDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<User> findAll(){
        List<User>list=null;
        try {
            list=userDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }


}
