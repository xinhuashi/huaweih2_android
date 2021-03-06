package com.palmap.demo.huaweih2;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.palmap.demo.huaweih2.util.SystemBarTintManager;

import java.security.Policy;

/**
 * Created by eric3 on 2016/9/27.
 */
public class BaseActivity extends FragmentActivity {
    private static boolean isActivityVisible = false;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("BaseActivity", "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
//    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
//    initStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
    }
    /*
          *  显示进度条
          * */
    ScrollView scrollView;
    LinearLayout linearLayout;
    TextView textView;
    protected void showProgress(String title, String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
        }

        progressDialog.setTitle(title);
        if (scrollView == null) {
            scrollView = new ScrollView(this);
        }
        if (linearLayout == null) {
            linearLayout = new LinearLayout(this);
            scrollView.addView(linearLayout);
        }
        scrollToBottom(scrollView,linearLayout);
        if (textView == null) {
            textView = new TextView(this);
            linearLayout.addView(textView);
        }
        textView.setText(msg);
        progressDialog.setContentView(scrollView);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void scrollToBottom(final View scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }

                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        });
    }

    /*
    *  关闭进度条
    * */
    protected void closeProgress() {
        Log.e("exp", "BaseActivity->closeProgress()");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeProgress();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 如果是返回键，动画结束该activity
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        final int bits_nav = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |= bits;
            winParams.flags |= bits_nav;
        } else {
            winParams.flags &= ~bits;
            winParams.flags &= ~bits_nav;
        }
        window.setAttributes(winParams);
    }


    /**
     * 设置状态栏颜色
     * API >=19
     *
     * @param colorId
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void initStatusBar(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 19
            Window window = getWindow();
            WindowManager.LayoutParams winParams = window.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            window.setAttributes(winParams);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.getConfig().getNavigationBarHeight();
            try {
                tintManager.setTintResource(colorId);
                int height = tintManager.getConfig().getStatusBarHeight();
                getRootView().setPadding(
                        getRootView().getLeft(),
                        getRootView().getPaddingTop() + height,
                        getRootView().getRight(),
                        getRootView().getBottom());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取root节点
     *
     * @return
     */
    public View getRootView() {
        return findViewById(android.R.id.content);
    }


    public static boolean isVisible(){
        return isActivityVisible;
    }

}
