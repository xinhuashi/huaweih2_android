package com.palmap.demo.huaweih2.http;

import android.os.Handler;
import android.os.Looper;

import com.palmap.demo.huaweih2.other.Constant;

import org.apache.http.NameValuePair;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2015/10/12.
 * 将接口和具体请求分离，使回调工作在主线程
 */
public class DataProvider {
  private Handler mHandler = new Handler(Looper.getMainLooper());

  /*
  * get请求
  * */
  public void getProvider(String url, Map<String, String> heads, final HttpDataCallBack<Object> callBack) {
    if (Constant.useOkHttp) {
      OkHttpUtils.get(url, new HttpDataCallBack<Object>() {
        @Override
        public void onError(final int errorCode) {
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              callBack.onError(errorCode);
            }
          });
        }

        @Override
        public void onComplete(final Object content) {
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              callBack.onComplete(content);
            }
          });
        }
      });
    } else {

      AsyncHttp.getInstance().getRequest(url, heads, new HttpDataCallBack<Object>() {
        @Override
        public void onError(final int errorCode) {
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              callBack.onError(errorCode);
            }
          });
        }

        @Override
        public void onComplete(final Object content) {
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              callBack.onComplete(content);
            }
          });
        }
      });
    }
  }

  /*
  * post请求
  * */
  public void postProvider(String url, Map<String, String> heads, List<NameValuePair> pairList, final HttpDataCallBack callBack) {
    AsyncHttp.getInstance().postRequest(url, heads, pairList, new HttpDataCallBack<Object>() {
      @Override
      public void onError(final int errorCode) {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            callBack.onError(errorCode);
          }
        });
      }

      @Override
      public void onComplete(final Object content) {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            callBack.onComplete(content);
          }
        });
      }
    });
  }

  /*
  * post请求传递数据
  * */
  public void postDataProvider(String url, Map<String, String> heads, byte[] data, final HttpDataCallBack callBack) {
    AsyncHttp.getInstance().sendDataByPost(url, data, heads, new HttpDataCallBack() {
      @Override
      public void onError(final int errorCode) {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            callBack.onError(errorCode);
          }
        });
      }

      @Override
      public void onComplete(final Object content) {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            callBack.onComplete(content);
          }
        });
      }
    });
  }

  /*
  * post formdata数据
  * */
  public void postFormDataProvider(String url, final File file, final String jsonData, final HttpDataCallBack callBack) {
    AsyncHttp.getInstance().uploadFile(url, file, jsonData, new HttpDataCallBack() {
      @Override
      public void onError(final int errorCode) {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            callBack.onError(errorCode);
          }
        });
      }

      @Override
      public void onComplete(final Object content) {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            callBack.onComplete(content);
          }
        });
      }
    });
  }




}