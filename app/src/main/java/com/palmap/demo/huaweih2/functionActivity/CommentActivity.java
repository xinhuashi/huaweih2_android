package com.palmap.demo.huaweih2.functionActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.palmap.demo.huaweih2.ActivityUploadCom;
import com.palmap.demo.huaweih2.BaseActivity;
import com.palmap.demo.huaweih2.LocateTimerService;
import com.palmap.demo.huaweih2.R;
import com.palmap.demo.huaweih2.adapter.FootComListAdapter;
import com.palmap.demo.huaweih2.http.DataProviderCenter;
import com.palmap.demo.huaweih2.http.ErrorCode;
import com.palmap.demo.huaweih2.http.HttpDataCallBack;
import com.palmap.demo.huaweih2.json.CommentDown;
import com.palmap.demo.huaweih2.other.Constant;
import com.palmap.demo.huaweih2.util.DialogUtils;
import com.palmap.demo.huaweih2.util.JsonUtils;
import com.palmap.demo.huaweih2.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import static com.palmap.demo.huaweih2.R.id.btn_com;

public class CommentActivity extends BaseActivity {

//    private PullToRefreshScrollView refreshScrollView;
    public LinearLayout commentList;
    public int start = 0;//开始加载
    RelativeLayout btn_write;

    private FootComListAdapter adapter;
    private PullToRefreshListView refreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        btn_write = (RelativeLayout) findViewById(btn_com);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentActivity.this, ActivityUploadCom.class);
                intent.putExtra("location", LocateTimerService.getCurrentLocationArea());
                startActivityForResult(intent, Constant.startUploadText);
            }
        });

        TitleBar titleBar = (TitleBar) findViewById(R.id.titleBar);

        titleBar.show(null,"留言板",null);

        titleBar.setOnTitleClickListener(new TitleBar.OnTitleClickListener() {
            @Override
            public void onLeft() {
                onBackPressed();
            }

            @Override
            public void onRight() {

            }
        });

        commentList = (LinearLayout) findViewById(R.id.com_list);

        refreshListView = (PullToRefreshListView) findViewById(R.id.refreshListView);
        refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //网络请求
                loadComments();
                refreshView.onRefreshComplete();
            }
        });
        loadComments();


        adapter = new FootComListAdapter(this);
        refreshListView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.startUploadText && resultCode == Activity.RESULT_OK) {
            adapter.clear();
            start = 0;
            loadComments();
        }
    }
    public void loadComments() {
        //加载评论

        String js = JsonUtils.getCommentsDown("", start, Constant.EACH_TIME_COMMENT_NUM);
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

                adapter.addAll(list);
                start+=list.size();

                /*
                for (int i = 0; i < list.size(); i++) {
                    //显示评论
                    // TODO 动态添加布局(xml方式)
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LayoutInflater inflater = LayoutInflater.from(CommentActivity.this);
                    View view = inflater.inflate(R.layout.com_list_item, commentList, false);
                    view.setLayoutParams(lp);
                    TextView tn = (TextView) view.findViewById(R.id.com_name);
                    tn.setText("访客" + list.get(i).getId());
                    TextView tc = (TextView) view.findViewById(R.id.com_text);
                    tc.setText(list.get(i).getComment());
                    TextView tt = (TextView) view.findViewById(R.id.com_time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(list.get(i).getComTime());
                    tt.setText(sdf.format(date));
                    TextView tl = (TextView) view.findViewById(R.id.loc);
                    start++;
                    commentList.addView(view);
                }*/
            }
        });
    }
}
