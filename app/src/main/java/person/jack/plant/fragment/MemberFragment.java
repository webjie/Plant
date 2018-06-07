package person.jack.plant.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import person.jack.plant.R;
import person.jack.plant.ui.UIHelper;
import person.jack.plant.ui.pulltozoomview.PullToZoomScrollViewEx;
import person.jack.plant.utils.SharedPreferences;

/**
 * 我的信息界面，包括头部登录用户图片信息，可缩放背景图片，导航菜单
 */
public class MemberFragment extends Fragment {

    private Activity context;
    private View root;
    private PullToZoomScrollViewEx scrollView;
    TextView name;
    ImageView logo;
    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.fragment_member, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        sharedPreferences = new SharedPreferences();
        initView();
    }

    void initView() {
        scrollView = (PullToZoomScrollViewEx) root.findViewById(R.id.scrollView);
        View headView = LayoutInflater.from(context).inflate(R.layout.member_head_view, null, false);
        name = (TextView) headView.findViewById(R.id.tv_user_name);
        name.setText(sharedPreferences.getString("userName", "请登录"));
        logo = (ImageView) headView.findViewById(R.id.iv_user_head);
        View zoomView = LayoutInflater.from(context).inflate(R.layout.member_zoom_view, null, false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.member_content_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);

        headView.findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showRes(getActivity());
            }
        });

        headView.findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
                if (isLogin) {
                    Toast.makeText(getActivity(), "请退出当前用户", Toast.LENGTH_SHORT).show();
                } else {
                    UIHelper.showLogin(getActivity());
                }

            }
        });


        scrollView.getPullRootView().findViewById(R.id.textPersonInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UIHelper.isLogin()){
                    UIHelper.ToastMessage(getContext(), "请先登录！");
                    return;
                }
                UIHelper.showPersonInfo(getActivity());
            }
        });
        scrollView.getPullRootView().findViewById(R.id.textChangePwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UIHelper.isLogin()){
                    UIHelper.ToastMessage(getContext(), "请先登录！");
                    return;
                }
                UIHelper.showRePwd(getActivity());
            }
        });
        scrollView.getPullRootView().findViewById(R.id.textVersion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showVersion(getActivity());
            }
        });

        scrollView.getPullRootView().findViewById(R.id.textLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UIHelper.isLogin()){
                    UIHelper.ToastMessage(getContext(), "未登录！");
                    return;
                }
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("确定退出登录吗？")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                name.setText("未登录");
                                logo.setImageResource(R.drawable.head);
                                sharedPreferences.putBoolean("isLogin", false);
                                sharedPreferences.putString("userName", null);
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .create();
                alertDialog.show();

            }
        });

//        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
//        context.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
//        int mScreenHeight = localDisplayMetrics.heightPixels;
//        int mScreenWidth = localDisplayMetrics.widthPixels;
//        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
//        scrollView.setHeaderLayoutParams(localObject);
    }

    private void initData() {

    }

}