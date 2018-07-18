package person.jack.plant.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import person.jack.plant.activity.EnvWarnActivity;
import person.jack.plant.activity.FailureWarnActivity;
import person.jack.plant.activity.HouseDetailActivity;
import person.jack.plant.activity.LoginActivity;
import person.jack.plant.activity.MainActivity;
import person.jack.plant.activity.PersonInfoActivity;
import person.jack.plant.activity.PlantsDetailActivity;
import person.jack.plant.activity.PlantsStatusActivity;
import person.jack.plant.activity.PersonChangePwdActivity;
import person.jack.plant.activity.PersonRegisterActivity;
import person.jack.plant.activity.VersionActivity;
import person.jack.plant.activity.WebViewActivity;
import person.jack.plant.utils.SharedPreferences;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 */
public class UIHelper {

    public final static String TAG = "UIHelper";

    public final static int RESULT_OK = 0x00;
    public final static int REQUEST_CODE = 0x01;

    public static void ToastMessage(Context cont, String msg) {
        if (cont == null || msg == null) {
            return;
        }
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, int msg) {
        if (cont == null || msg <= 0) {
            return;
        }
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, String msg, int time) {
        if (cont == null || msg == null) {
            return;
        }
        Toast.makeText(cont, msg, time).show();
    }

    /**
     * 调用WebView，显示Url地址内容
     * @param context
     * @param bundle
     */
    public static void showWeb(Activity context, Bundle bundle) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("navigate", bundle);
        context.startActivity(intent);
    }

    public static void showHome(Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showLogin(Activity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void showPersonInfo(Activity context) {
        Intent intent = new Intent(context, PersonInfoActivity.class);
        context.startActivity(intent);
    }

    public static void showRegister(Activity context) {
        Intent intent = new Intent(context, PersonRegisterActivity.class);
        context.startActivity(intent);
    }

    public static void showChangePwd(Activity context) {
        Intent intent = new Intent(context, PersonChangePwdActivity.class);
        context.startActivity(intent);
    }

    public static void showVersion(Activity context) {
        Intent intent = new Intent(context, VersionActivity.class);
        context.startActivity(intent);
    }

    public static void showHouseDetailActivity(Activity context) {
        Intent intent = new Intent(context, HouseDetailActivity.class);
        context.startActivity(intent);
    }

    public static void showPlantsDetailActivity(Activity context) {
        Intent intent = new Intent(context, PlantsDetailActivity.class);
        context.startActivity(intent);
    }

    public static void showChartActivity(Activity context, int position) {
        Intent intent = new Intent(context, PlantsStatusActivity.class);
        intent.putExtra("fragmentPosition", position);
        context.startActivity(intent);
    }

    public static void showMember(Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示故障报警界面
     * @param context
     */
    public static void showAlert(Activity context) {
        Intent intent = new Intent(context, EnvWarnActivity.class);
        context.startActivity(intent);
    }

    /**
     * 检查用户是否登录
     */
    public static boolean isLogin() {
        SharedPreferences sharedPreferences = new SharedPreferences();
        String userName = sharedPreferences.getString("userName", "");
        if (userName == null || userName.length() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
