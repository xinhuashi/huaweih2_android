package com.palmap.demo.huaweih2.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.palmap.demo.huaweih2.R;
import com.palmaplus.nagrand.view.overlay.OverlayCell;

import static com.palmap.demo.huaweih2.other.Constant.H2大厅;
import static com.palmap.demo.huaweih2.other.Constant.ICS办公区;
import static com.palmap.demo.huaweih2.other.Constant.ICS实验室;
import static com.palmap.demo.huaweih2.other.Constant.会议室;

/**
 * Created by eric3 on 2016/10/23.
 */

public class PoiRedMark extends LinearLayout implements OverlayCell{
  private ImageView mIconView;
  private String name;
  private Context context;
  private OnClickListenerForMark onClickListenerForMark;
  double[] x={12697150.114,
      12697129.354,
      12697098.453,
      12697087.313

  };
  double[] y={
      2588909.271,
      2588909.813,
      2588907.851,
      2588875.184

  };
//  public final static int NORMAL = 0;
//  public final static int START = 1;
//  public final static int END = 2;
//  private int type ;
//  private TextView mPosX;
//  private TextView mPosY;
//  private TextView mPosId;l

  private double[] mGeoCoordinate;
//  private int mId;

  public PoiRedMark(Context context) {
    super(context);

    this.context = context;
    initView();
  }

  public PoiRedMark(Context context, String name,OnClickListenerForMark onClickListenerForMark) {
    super(context);

    this.onClickListenerForMark = onClickListenerForMark;
    this.context=context;
    this.name = name;
    initView();
  }

  public String getName() {
    return name;
  }

  private void initView() {
    LayoutInflater.from(getContext()).inflate(R.layout.poiinfo_mark, this);
    mIconView = (ImageView) findViewById(R.id.poi_img);
    if (H2大厅.equals(name)) {
      mIconView.setBackgroundResource(R.drawable.ico_map_1_red);
    } else if (会议室.equals(name)) {
      mIconView.setBackgroundResource(R.drawable.ico_map_2_red);
    } else if (ICS办公区.contains(name)) {
      mIconView.setBackgroundResource(R.drawable.ico_map_4_red);
    } else if (name.contains(ICS实验室)) {
      mIconView.setBackgroundResource(R.drawable.ico_map_3_red);
    }

    mIconView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onClickListenerForMark.onMarkSelect(PoiRedMark.this);
      }
    });

  }

  public void setMark(int id, double x, double y){
//    mId = id;
//    mPosId.setText(String.valueOf(id));
//    mPosX.setText("x: " + x);
//    mPosY.setText("y: " + y);
  }

  public void setMark(int id, double x, double y, int resId) {
//    mId = id;
//    mPosId.setText(String.valueOf(id));
//    mPosX.setText("x: " + x);
//    mPosY.setText("y: " + y);
    mIconView.setBackgroundResource(resId);
  }

  @Override
  public void init(double[] doubles) { // 用于接受一个世界坐标，必须要有
    mGeoCoordinate = doubles;
  }

  @Override
  public double[] getGeoCoordinate() { // 用于返回世界坐标，必须要有
    return mGeoCoordinate;
  }


  //用于定位覆盖物位置，这个接口会由SDK调用，
//最终参数是覆盖物添加世界坐标转换后的屏幕坐标，
//这个接口在做地图交互是会一直调用，如果你想自己控制覆盖物的显示位置，可以自己自定义这个接口
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @Override
  public void position(double[] doubles) {
    setX((float) doubles[0] - getWidth() / 2);
    setY((float) doubles[1] - getHeight() );
  }


  public interface OnClickListenerForMark{
    void onMarkSelect(PoiRedMark mark);
  }

}
