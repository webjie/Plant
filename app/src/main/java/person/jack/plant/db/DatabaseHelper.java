package person.jack.plant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import person.jack.plant.db.entity.Article;
import person.jack.plant.db.entity.Plants;
import person.jack.plant.db.entity.Student;
import person.jack.plant.db.entity.User;
import person.jack.plant.db.entity.ValueSet;
import person.jack.plant.db.entity.WarnRecord;
import person.jack.plant.db.entity.WaterRecord;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "sqlite-test.db";
    private static final int DATABASE_VERSION = 6;

	private Map<String, Dao> daos = new HashMap();

    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

	@Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Article.class);
            TableUtils.createTable(connectionSource, Student.class);
            TableUtils.createTable(connectionSource,ValueSet.class);
            TableUtils.createTable(connectionSource, WarnRecord.class);
            TableUtils.createTable(connectionSource, WaterRecord.class);
            TableUtils.createTable(connectionSource,Plants.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	@Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Article.class, true);
            TableUtils.dropTable(connectionSource, Student.class, true);
            TableUtils.dropTable(connectionSource, ValueSet.class, true);
            TableUtils.dropTable(connectionSource, WarnRecord.class, true);
                     TableUtils.dropTable(connectionSource, WaterRecord.class, true);
            TableUtils.dropTable(connectionSource, Plants.class, true);


            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	@Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}
