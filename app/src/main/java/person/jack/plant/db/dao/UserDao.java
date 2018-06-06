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

    /**
     * 根据姓名查找
     * @param name
     * @return User对象
     */
    public  User findByName(String name){
        User user=null;
        List<User> list=null;
        try {
            list=userDao.queryForEq("name",name);
            if(list.size()==0){
                user=null;
            }else{
                user=list.get(0);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;

    }

    /**
     * 更新
     * @param user
     */
    public  void update(User user){
        try {
            userDao.update(user);
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

    /**
     * 删除
     * @param user
     */
    public  void delete(User user){
        try {
            userDao.delete(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
