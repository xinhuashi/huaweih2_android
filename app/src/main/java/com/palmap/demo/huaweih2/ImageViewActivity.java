package com.palmap.demo.huaweih2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.palmap.demo.huaweih2.json.PictureModel;
import com.palmap.demo.huaweih2.other.Constant;
import com.palmap.demo.huaweih2.util.JsonUtils;
import com.palmap.demo.huaweih2.util.ShareUtils;
import com.palmap.demo.huaweih2.view.CustomerViewPager;
import com.palmap.demo.huaweih2.view.MyProgressDialog;
import com.palmap.demo.huaweih2.view.RecyclableImageView;
import com.palmap.demo.huaweih2.view.SharePopView;
import com.palmap.demo.huaweih2.view.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by 王天明 on 2016/10/22
 */
public class ImageViewActivity extends Activity {

//    private ArrayList<String> imgList = new ArrayList<String>();
    private List<PictureModel> pictureModelList = new ArrayList<PictureModel>();
    private CustomerViewPager imgPager;
    private PagerAdapter mAdapter;
    Handler myHandler = new Handler();
    private MyProgressDialog progressDialog;
    private TitleBar titleBar;

    private int currentPos = 0;
    private TextView tvTime,tvDetails,tvLocation;
//    private Bitmap currentBmp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDetails = (TextView) findViewById(R.id.tv_details);
        tvLocation = (TextView) findViewById(R.id.tv_location);

        Intent intent = super.getIntent();
//        imgList = intent.getStringArrayListExtra("imgList");
        String content = getIntent().getStringExtra("imgListJson");
        pictureModelList = JsonUtils.getPictureModel(content);
        currentPos = intent.getIntExtra("itemIndex", 0);

        titleBar=(TitleBar)findViewById(R.id.title_bar);
        titleBar.show(null,pictureModelList.get(0).getLocation(),null);
        titleBar.setOnTitleClickListener(new TitleBar.OnTitleClickListener() {
            @Override
            public void onLeft() {
                finish();
            }

            @Override
            public void onRight() {

            }
        });
        imgPager = (CustomerViewPager) findViewById(R.id.imgPager);
        mAdapter = new ImageAdapter();
        imgPager.setAdapter(mAdapter);
        imgPager.setCurrentItem(currentPos);
        imgPager.setOnPageChangeListener(new CustomerViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPos = position;
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy年MM月dd日 HH:mm"
                );
                tvTime.setText(dateFormat.format(new Date(pictureModelList.get(position).getUpdtime())));
                tvDetails.setText(pictureModelList.get(position).getAppendix());
                tvLocation.setText(pictureModelList.get(position).getLocation());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgPager.setCurrentItem(0);
        titleBar.setRightIcoImageRes(R.drawable.ico_tab_share);

        titleBar.setRightIcoClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();

                        if (Constant.useSystemShare){
                            ShareUtils.shareText(ImageViewActivity.this,"我在华为ICS实验室参观："+pictureModelList.get(currentPos).getAppendix()+"  快来看看我拍的照片吧："+
                                pictureModelList.get(currentPos).getPhoto());
                        }else {
                            SharePopView.ShareModel shareModel = new SharePopView.ShareModel();

                            shareModel.imgUrl = pictureModelList.get(imgPager.getCurrentItem()).getPhoto();
                            Bitmap myBitmap = null;
                            try {
                                myBitmap = Glide.with(ImageViewActivity.this)
                                    .load(shareModel.imgUrl)
                                    .asBitmap() //必须
                                    .centerCrop()
                                    .into(500, 500)
                                    .get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            shareModel.urlBmp = myBitmap;
                            shareModel.title = "ICS足迹分享";
                            shareModel.text = pictureModelList.get(currentPos).getAppendix();

                            SharePopView.showSharePop(ImageViewActivity.this, shareModel);
                        }
                        Looper.loop();
                    }
                }).start();


            }
        });
    }

    private class ImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
//            return imgList.size();
            return pictureModelList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            String text = imgList.get(position);
            String url = pictureModelList.get(position).getPhoto();
            RecyclableImageView img = new RecyclableImageView(ImageViewActivity.this);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            getBM(url, img, container, position);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private void getBM(final String url, final RecyclableImageView img, final ViewGroup container, final int position) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //开始加载的时候执行
                            starProgressDialog();

                            Glide.with(ImageViewActivity.this).load(url).centerCrop()
                                    .into(new SimpleTarget<GlideDrawable>() {
                                        @Override
                                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                            img.setImageDrawable(resource);
                                            closeProgressDialog();
                                        }

                                        @Override
                                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                            super.onLoadFailed(e, errorDrawable);
                                            closeProgressDialog();
                                        }
                                    });
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ImageViewActivity.this.finish();
                                }
                            });
                            container.addView(img, 0);
                        }
                    });
                    Looper.loop();
                }
            }).start();
        }

        private void starProgressDialog() {
            //开始加载的时候执行
            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = MyProgressDialog.createDialog(ImageViewActivity.this);
                progressDialog.show();
                progressDialog.setCancelable(true);
            }
        }

        private void closeProgressDialog() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }
}
