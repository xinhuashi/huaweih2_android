package com.palmap.demo.huaweih2.other;

import com.palmap.demo.huaweih2.HuaWeiH2Application;

/**
 * Created by eric3 on 2016/10/7.
 */

public class Constant {
//

//
    public static final String APP_KEY = "30497ca93a3b47bd841db8dced24878f";//h2
  public static final long MAP_ID = 1430L;
  public static final long FLOOR_ID_F1 = 1262934L;
  public static final long FLOOR_ID_B1 = 1261980L;



//  public static final String APP_KEY = "19e7db5672824721a9e47ac84a8aa222";//图聚办公室的
//  public static final long FLOOR_ID_F1 = 1003497L;//21楼
//  public static final long FLOOR_ID_B1 = 1261980L;
//  public static final long MAP_ID = 6L;



//  public static final String APP_KEY = "543c0c5266fe4d30a561c3cc3d12ba97";//无锡海岸城
//  public static final long MAP_ID = 1444L;
//  public static final long FLOOR_ID_F1 = 1272847L;
//  public static final long FLOOR_ID_B1 = 0L;



  public static final String IS_FIRST_RUN = "isFirstRun";
  public static final String TAG_ACCESS_TOKEN = "access_token";

  public static final String LUR_NAME = "Nagrand/lua";
  public static final String FONT_NAME = "Nagrand/font";

//  public static final String APP_DATA_PATH = Environment.getExternalStorageDirectory()+"/huaweih2";
  public static final String APP_DATA_PATH = HuaWeiH2Application.instance.getExternalFilesDir(null)+"";
  public static final String DIR_PICTURE_UPLOAD = APP_DATA_PATH+"/temp";
  public static final String PATH_PICTURE_UPLOAD = DIR_PICTURE_UPLOAD+"/temp.jpg";
  public static final String PATH_MAP_CASH = APP_DATA_PATH+"/Nagrand/download/";

  //Environment.getExternalStorageDirectory()会有权限问题

//  public static final String DIR_PICTURE_UPLOAD = APP_DATA_PATH+"/temp";
//  public static final String PATH_PICTURE_UPLOAD = DIR_PICTURE_UPLOAD+"/temp.jpg";
//  public static final String PATH_MAP_CASH = APP_DATA_PATH+"/Nagrand/download/";

  public static String SERVER_URL = "https://api.ipalmap.com/";///huaweih2

  public static final long 电梯_ID = 24091000L;
  public static final long 无障碍电梯_ID = 24092000L;
  public static final long 男洗手间_ID = 23024000L;
  public static final long 女洗手间_ID = 23025000L;
  public static final long 残障洗手间_ID = 23059000L;
  public static final long 安全出口_ID = 23061000L;
  public static final long 门窗楼梯_ID = 13164000L;
  public static final long 盥洗室_ID = 23047000L;
  public static final long 茶水间_ID = 15000000L;//生活服务

  //四个poi详情
  public static final String H2大厅 = "H2大厅";//poi详情
  public static final String 会议室 = "会议室";//poi详情
  public static final String ICS实验室 = "ICS实验室";//poi详情
  public static final String ICS办公区 = "ICS办公区";//poi详情



  public static final String FACILITY_LAYER = "Facility";
  public static final String FACILITY_KEY = "category";

  public static final String AREA_LAYER = "Area";
  public static final String AREA_KEY = "display";
  //设置开关
  //使用新平台接口与服务器通讯
  public static Boolean useAPIV2 = true;


  //是否开启地图缓存模式
  public static Boolean useCash = true;

  //测试参数，控制
  public static Boolean isDebug = false;

  public static boolean onSingleTap_changeColor =true;//是否变色

  public static final int RESULT_PICTURE = 0;

  public static final int EACH_TIME_COMMENT_NUM = 5;

  //startActivityForResult
  public static final int startPay = 2;
  public static final int startWelcome = 1;
}
