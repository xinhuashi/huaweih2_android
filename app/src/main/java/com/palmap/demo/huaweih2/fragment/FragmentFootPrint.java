package com.palmap.demo.huaweih2.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.palmap.demo.huaweih2.ActivityUploadCom;
import com.palmap.demo.huaweih2.ImageAlbumActivity;
import com.palmap.demo.huaweih2.LocateTimerService;
import com.palmap.demo.huaweih2.PoiInfoActivity;
import com.palmap.demo.huaweih2.R;
import com.palmap.demo.huaweih2.adapter.FootComListAdapter;
import com.palmap.demo.huaweih2.http.DataProviderCenter;
import com.palmap.demo.huaweih2.http.ErrorCode;
import com.palmap.demo.huaweih2.http.HttpDataCallBack;
import com.palmap.demo.huaweih2.json.CommentDown;
import com.palmap.demo.huaweih2.json.PictureModelSum;
import com.palmap.demo.huaweih2.other.Constant;
import com.palmap.demo.huaweih2.util.DialogUtils;
import com.palmap.demo.huaweih2.util.JsonUtils;
import com.palmap.demo.huaweih2.view.FootPrintItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric3 on 2016/10/8.
 */

public class FragmentFootPrint extends BaseFragment implements View.OnClickListener {

    FootPrintItemView footprintView_office;
    FootPrintItemView footprintView_lab;
    FootPrintItemView footprintView_h2;
    FootPrintItemView footprintView_meetingRoom;
    FootPrintItemView footprintView_h2_hall;
    Activity mContext;
    public LinearLayout commentList;
    RelativeLayout write;
    public int start = 0;//开始加载
    public static boolean hasShowGide = false;//

    private PullToRefreshScrollView refreshScrollView;

    private ListView com_listView;
    private FootComListAdapter footComListAdapter;


    public View getBitMapView(){
        return refreshScrollView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View fragmentView = inflater.inflate(R.layout.foot_print, container, false);
        commentList = (LinearLayout) fragmentView.findViewById(R.id.com_list);
        mContext = getActivity();
        write = (RelativeLayout) fragmentView.findViewById(R.id.btn_com);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityUploadCom.class);
                intent.putExtra("location", LocateTimerService.getCurrentLocationArea());
                startActivityForResult(intent, Constant.startUploadText);
            }
        });
//    getMainActivity().titleBar.setRightIcoImageRes(R.drawable.ico_nav_camera);
//    getMainActivity().titleBar.setRightIcoClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        getMainActivity().openCameraActivity();
//      }
//    });
//    getMainActivity().titleBar.setOnTitleClickListener(new TitleBar.OnTitleClickListener() {
//      @Override
//      public void onLeft() {
//        getMainActivity().showFragmentMap();
//      }
//
//      @Override
//      public void onRight() {
//
//      }
//    });

        com_listView = (ListView) fragmentView.findViewById(R.id.com_listView);
        footComListAdapter = new FootComListAdapter(getActivity());
        com_listView.setAdapter(footComListAdapter);

        loadComments();

        loadPicNum();

        checkFirst();

        return fragmentView;
    }

    private void checkFirst() {
        SharedPreferences setting = mContext.getSharedPreferences(Constant.IS_FIRST_RUN, 0);
        int time = setting.getInt("time", 2);
        if (time == 2 && !hasShowGide) {//第一次显示
            hasShowGide = true;
            Intent intent = new Intent(mContext, PoiInfoActivity.class);
            intent.putExtra("type", PoiInfoActivity.POI_FOOT);
            mContext.startActivity(intent);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.startUploadText && resultCode == Activity.RESULT_OK) {
            footComListAdapter.clear();
            start = 0;
            loadComments();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
    }

    private void bindView(View view) {
        footprintView_office = (FootPrintItemView) view.findViewById(R.id.footprintView_office);
        footprintView_lab = (FootPrintItemView) view.findViewById(R.id.footprintView_lab);
        footprintView_h2 = (FootPrintItemView) view.findViewById(R.id.footprintView_h2);
        footprintView_meetingRoom = (FootPrintItemView) view.findViewById(R.id.footprintView_meetingRoom);
        footprintView_h2_hall = (FootPrintItemView) view.findViewById(R.id.footprintView_h2_hall);

        footprintView_office.setImageResource(R.drawable.icon_foot_office);
//    footprintView_office.setName("ICS办公区");
        footprintView_lab.setImageResource(R.drawable.icon_foot_board);
//    footprintView_lab.setName("ICS实验室");
        footprintView_h2.setImageResource(R.drawable.icon_foot_build);
//    footprintView_h2.setName("H2大楼");
        footprintView_meetingRoom.setImageResource(R.drawable.icon_foot_meet);
//    footprintView_meetingRoom.setName("会议室");
        footprintView_h2_hall.setImageResource(R.drawable.icon_foot_hall);
//    footprintView_h2_hall.setName("ICS大厅");


        registerFootPrintItemViewClickEvent(footprintView_office
                , footprintView_lab
                , footprintView_h2
                , footprintView_meetingRoom
                , footprintView_h2_hall
        );

        refreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.refreshScrollView);

        refreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        refreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                refreshView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //网络请求
                loadComments();
//                String js = JsonUtils.getCommentsDown("",start,Constant.EACH_TIME_COMMENT_NUM);
//                DataProviderCenter.getInstance().getComments(js, new HttpDataCallBack() {
//                    @Override
//                    public void onError(int errorCode) {
//                        DialogUtils.showShortToast(errorCode+"");
//                    }
//
//                    @Override
//                    public void onComplete(Object content) {
//
//                    }
//                });


            }
        });

    }

    private void registerFootPrintItemViewClickEvent(FootPrintItemView... views) {
        for (FootPrintItemView v : views) {
            v.setOnClickListener(new FootPrintItemViewClickEvent(v));
        }
    }

    //从服务器获取某位置的所有图片列表
    private void getImageListByPosition() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//      case R.id.image_foot_print:
//        startActivity(new Intent(getMainActivity(), ImageListActivity.class));
//        break;

            default:
                break;
        }
    }

    private class FootPrintItemViewClickEvent implements View.OnClickListener {
        FootPrintItemView view;

        FootPrintItemViewClickEvent(FootPrintItemView view) {
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            if (v == null) return;

            Intent intent = new Intent(getActivity(), ImageAlbumActivity.class);
            switch (v.getId()) {
                case R.id.footprintView_office:
                    intent.putExtra("location", Constant.ICS办公区);
                    break;
                case R.id.footprintView_h2:
                    intent.putExtra("location", Constant.H2大楼);
                    break;
                case R.id.footprintView_lab:
                    intent.putExtra("location", Constant.ICS实验室);
                    break;
                case R.id.footprintView_h2_hall:
                    intent.putExtra("location", Constant.H2大厅);
                    break;
                case R.id.footprintView_meetingRoom:
                    intent.putExtra("location", Constant.会议室);
                    break;

            }


            startActivity(intent);
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


                /*for (int i = 0; i < list.size(); i++) {
                    //显示评论
                    // TODO 动态添加布局(xml方式)
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View view = inflater.inflate(R.layout.com_list_item, commentList, false);
                    view.setLayoutParams(lp);
                    TextView tn = (TextView) view.findViewById(R.id.com_name);
//              tn.setText(list.get(i).getUserId());
                    tn.setText("访客" + list.get(i).getId());
                    TextView tc = (TextView) view.findViewById(R.id.com_text);
                    tc.setText(list.get(i).getComment());
                    TextView tt = (TextView) view.findViewById(R.id.com_time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(list.get(i).getComTime());
                    tt.setText(sdf.format(date));
                    TextView tl = (TextView) view.findViewById(R.id.loc);
                    tl.setText(list.get(i).startGetLocation());

//          view.setBackgroundResource(R.drawable.commentbar_short);

                    start++;
                    commentList.addView(view);
                }*/
                start += list.size();
                footComListAdapter.addAll(list);
                refreshScrollView.onRefreshComplete();
            }
        });
    }

    public void loadPicNum() {
        //加载总数
        DataProviderCenter.getInstance().getAllLocationPicNum(new HttpDataCallBack() {
            @Override
            public void onError(int errorCode) {
                ErrorCode.showError(errorCode);
            }

            @Override
            public void onComplete(Object content) {
                List<PictureModelSum> pictureModelSums = JsonUtils.getPictureModelSum(content);
                if (pictureModelSums == null) {
                    footprintView_meetingRoom.setCount(0);
                    footprintView_lab.setCount(0);
                    footprintView_office.setCount(0);
                    footprintView_h2.setCount(0);
                    footprintView_h2_hall.setCount(0);

                    return;
                }
                for (int i = 0; i < pictureModelSums.size(); i++) {
                    String location = pictureModelSums.get(i).getLocation();
                    if (location == null)
                        location = "";

                    if (location.contains(Constant.H2大厅)) {
                        footprintView_h2_hall.setCount(pictureModelSums.get(i).getCount());
                    } else if (location.contains(Constant.ICS办公区)) {
                        footprintView_office.setCount(pictureModelSums.get(i).getCount());
                    } else if (location.contains(Constant.ICS实验室)) {
                        footprintView_lab.setCount(pictureModelSums.get(i).getCount());
                    } else if (location.contains(Constant.会议室)) {
                        footprintView_meetingRoom.setCount(pictureModelSums.get(i).getCount());
                    } else if (location.contains(Constant.H2大楼)) {
                        footprintView_h2.setCount(pictureModelSums.get(i).getCount());
                    }
                }
            }
        });
    }

}
