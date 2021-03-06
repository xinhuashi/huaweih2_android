package com.palmap.demo.huaweih2;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.palmap.demo.huaweih2.http.DataProviderCenter;
import com.palmap.demo.huaweih2.http.ErrorCode;
import com.palmap.demo.huaweih2.http.HttpDataCallBack;
import com.palmap.demo.huaweih2.json.CommentDown;
import com.palmap.demo.huaweih2.other.Constant;
import com.palmap.demo.huaweih2.util.DialogUtils;
import com.palmap.demo.huaweih2.util.JsonUtils;
import com.palmap.demo.huaweih2.util.KeyBoardUtils;
import com.palmap.demo.huaweih2.util.VisitorHelper;
import com.palmap.demo.huaweih2.view.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityUploadCom extends BaseActivity {
    PullToRefreshScrollView refreshScrollView;
    String location;
    TitleBar titleBar;
    LinearLayout commentList;
    EditText textView;
    int start = 0;

    EditText edit_name;


    private final static String KEY_NAME = "ActivityUploadCom_Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_com);
        edit_name = (EditText) findViewById(R.id.edit_name);

        edit_name.setText(loadName());

        textView = (EditText) findViewById(R.id.text);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.show(null, "评论", "发表");
        titleBar.setOnTitleClickListener(new TitleBar.OnTitleClickListener() {
            @Override
            public void onLeft() {
                KeyBoardUtils.closeKeybord(textView, ActivityUploadCom.this);
                setResult(RESULT_CANCELED, getIntent());
                finish();
            }

            @Override
            public void onRight() {

                KeyBoardUtils.closeKeybord(textView, ActivityUploadCom.this);

                if (TextUtils.isEmpty(edit_name.getText().toString())) {
                    DialogUtils.showShortToast("昵称不能为空");
                    return;
                }
                if (textView.getText().toString().equals("")) {
                    DialogUtils.showShortToast("写点什么吧");
                    return;
                }

                titleBar.enableRight(false);

                String name = edit_name.getText().toString();
                saveName(name);

                String msg = textView.getText().toString() + Constant.commont_split + name;

                String s = JsonUtils.getPostComment(location, msg);
//                String s = JsonUtils.getPostComment(location, textView.getText().toString());
                DataProviderCenter.getInstance().postComments(s, new HttpDataCallBack() {
                    @Override
                    public void onError(int errorCode) {
                        ErrorCode.showError(errorCode);
                        titleBar.enableRight(true);
                    }

                    @Override
                    public void onComplete(Object content) {
                        DialogUtils.showShortToast("发表成功！");
                        textView.setText("");
                        setResult(RESULT_OK, getIntent());
                        finish();
                    }
                });

            }
        });


        commentList = (LinearLayout) findViewById(R.id.com_list);
        location = getIntent().getStringExtra("location");
        refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);

        refreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        refreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                refreshView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                loadComments();
                refreshView.onRefreshComplete();
            }
        });

//    loadComments();//加载评论

        KeyBoardUtils.openKeybord(textView, this);
        textView.requestFocus();
    }



    private void saveName(String name){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(KEY_NAME, name).commit();
    }

    private String loadName(){
        return PreferenceManager.getDefaultSharedPreferences(this).getString(KEY_NAME,"");
    }


    private void loadComments() {
        //加载评论
        String js = JsonUtils.getCommentsDown(location, start, Constant.EACH_TIME_COMMENT_NUM);
        DataProviderCenter.getInstance().getComments(js, new HttpDataCallBack() {
            @Override
            public void onError(int errorCode) {
                ErrorCode.showError(errorCode);
            }

            @Override
            public void onComplete(Object content) {
//            loadmore_view.setVisibility(View.INVISIBLE);
                Log.i("", content.toString());
                List<CommentDown> list = new ArrayList<>(JSONArray.parseArray(content.toString(), CommentDown.class));
                if (list == null) {
                    DialogUtils.showShortToast("没有更多评论");
                    return;
                }
                if (list.size() == 0) {
                    DialogUtils.showShortToast("没有更多评论");
                    return;
                }


                for (int i = 0; i < list.size(); i++) {
                    //显示评论
                    // TODO 动态添加布局(xml方式)
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View view = inflater.inflate(R.layout.com_list_item, commentList, false);
                    view.setLayoutParams(lp);
                    TextView tn = (TextView) view.findViewById(R.id.com_name);
//              tn.setText(list.get(i).getUserId());

//          tn.setText("访客"+list.get(i).getId());
                    tn.setText(VisitorHelper.createName(list.get(i).getId()));

                    TextView tc = (TextView) view.findViewById(R.id.com_text);
                    tc.setText(list.get(i).getComment());
                    TextView tt = (TextView) view.findViewById(R.id.com_time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(list.get(i).getComTime());
                    tt.setText(sdf.format(date));
                    TextView tl = (TextView) view.findViewById(R.id.loc);
                    tl.setText(list.get(i).getLocation());

                    start++;
                    commentList.addView(view);
                }
            }
        });
    }

}
