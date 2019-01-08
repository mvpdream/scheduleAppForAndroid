package com.example.project.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class Splash extends AppCompatActivity {
    public static ImageView welcomeImg = null;
    public static WeakReference<Splash> weak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weak = new WeakReference<>(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
//        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_splash);
        welcomeImg = (ImageView) this.findViewById(R.id.welcome_img);
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
        anima.setDuration(3000);// 设置动画显示时间
        welcomeImg.startAnimation(anima);
        anima.setAnimationListener(new AnimationImpl());
    }
    private class AnimationImpl implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            welcomeImg.setBackgroundResource(R.mipmap.splash);
        }
        @Override
        public void onAnimationEnd(Animation animation) {
            Log.i("ansen","onAnimationEnd");
             // 动画结束后跳转到别的页面
            skip();
        }
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
    private void skip() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    /**
     * 在别的Activity关闭自己的方法
     */
    public static void finishActivity() {
        if (weak!= null && weak.get() != null) {
            weak.get().finish();
            welcomeImg.clearAnimation();
        }
    }
}
