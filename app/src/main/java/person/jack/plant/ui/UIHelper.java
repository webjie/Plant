package person.jack.plant.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import person.jack.plant.activity.HouseDetailActivity;
import person.jack.plant.activity.LoginActivity;
import person.jack.plant.activity.MainActivity;
import person.jack.plant.activity.PersonInfoActivity;
import person.jack.plant.activity.PlantsDetailActivity;
import person.jack.plant.activity.PlantsStatusActivity;
import person.jack.plant.activity.RePwdActivity;
import person.jack.plant.activity.ResActivity;
import person.jack.plant.activity.VersionActivity;

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

    public static void showRes(Activity context) {
        Intent intent = new Intent(context, ResActivity.class);
        context.startActivity(intent);
    }

    public static void showRePwd(Activity context) {
        Intent intent = new Intent(context, RePwdActivity.class);
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

}
