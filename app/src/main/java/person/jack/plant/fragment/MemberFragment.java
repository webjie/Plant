package person.jack.plant.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import person.jack.plant.R;
import person.jack.plant.ui.UIHelper;
import person.jack.plant.ui.pulltozoomview.PullToZoomScrollViewEx;
import person.jack.plant.utils.SharedPreferences;

/**
 * 我的信息界面，包括头部登录用户图片信息，可缩放背景图片，用户注册、登录，个人信息，修改密码，软件版本，，退出登录
 */
public class MemberFragment extends Fragment {

    private Activity context;
    private View root;
    private PullToZoomScrollViewEx scrollView;
    TextView name;
    ImageView logo;
    LinearLayout layoutPerson;
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
        layoutPerson =  (LinearLayout) headView.findViewById(R.id.layout_person);
        if (UIHelper.isLogin()){
            layoutPerson.setVisibility(View.GONE);
        }
        name = (TextView) headView.findViewById(R.id.tv_user_name);
        name.setText(sharedPreferences.getString("userName", "请登录"));
        logo = (ImageView) headView.findViewById(R.id.iv_user_head);

        View zoomView = LayoutInflater.from(context).inflate(R.layout.member_zoom_view, null, false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.member_content_view,
                null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);

        headView.findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showRegister(getActivity());
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
                UIHelper.showChangePwd(getActivity());
            }
        });
        scrollView.getPullRootView().findViewById(R.id.textVersion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showVersion(getActivity());
            }
        });
        scrollView.getPullRootView().findViewById(R.id.textValueSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UIHelper.isLogin()){
                    UIHelper.ToastMessage(getContext(), "请先登录！");
                    return;
                }
                UIHelper.showValueSet(getActivity());
            }
        });
        scrollView.getPullRootView().findViewById(R.id.textLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UIHelper.isLogin()){
                    UIHelper.ToastMessage(getContext(), "未登录！");
                    return;
                }

                try{
                    final SweetAlertDialog dlg = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                    dlg.setTitleText("提示");
                    dlg.showCancelButton(true);
                    dlg.setCancelText("取消");
                    dlg.setConfirmText("确定");
                    dlg.setContentText("确定要退出登录吗？");
                    dlg.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            name.setText("未登录");
                            logo.setImageResource(R.drawable.head);
                            sharedPreferences.putBoolean("isLogin", false);
                            sharedPreferences.putString("userName", null);
                            sweetAlertDialog.dismiss();
                        }
                    });

                    dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.cancel();
                        }
                    });

                    dlg.show();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("plant", e.getMessage());
                }

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