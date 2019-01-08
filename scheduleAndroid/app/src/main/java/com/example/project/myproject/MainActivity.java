package com.example.project.myproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements LoadingView.LoadingViewListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String NEW_DB = "data/user/0/com.example.project.myproject/app_webview/IndexedDB";
    private WebView mWebView;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.permission.MOUNT_UNMOUNT_FILESYSTEM"};
    //第一次按返回键系统的时间戳，默认为0。
    private long firstTime = 0;
    private TopBar topBar;
    private ImageButton leftImageButton;
    private LoadingView mLoadingView;
    private boolean flag=false;
    private boolean isShow=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag=false;
        //startActivity(new Intent(this, Splash.class));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBlue));
        }
        setContentView(R.layout.activity_main);
        // 本示例是在界面一开始就要调用 Loading ，所以在 onCreate 里面做 Loading 加载，根据项目实际情况在适当的位置进行调用。
        mLoadingView = (LoadingView) findViewById(R.id.loading);
        mLoadingView.setListener(this);
        mLoadingView.showLoading();

        topBar = (TopBar) findViewById(R.id.topbar);
        leftImageButton=(ImageButton) findViewById(R.id.leftImageButton);
        leftImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();//返回上个页面
                }
            }
        });
        //对方法进行重写
        topBar.setOnLeftButtonClickListener(new TopBarListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void rightButtonClick() {
                // TODO Auto-generated method stub
//                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
//                mWebView.loadUrl("javascript:goStudent()");
//                 通过Handler发送消息
                if(flag){
                    receiveMsgFromNative();
                }
            }

            @Override
            public void leftButtonClick() {
                if(isShow){
                    receiveMsgFromNative();
                }else{
                    startActivity(new Intent(getApplication(), Explain.class));
                }

            }
        });
       verifyStoragePermissions(MainActivity.this);
//        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android
//                .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

    }
    public void receiveMsgFromNative(){
        mWebView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                // Android版本变量
                final int version = Build.VERSION.SDK_INT;
                // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
                if (version < 18) {
                    mWebView.loadUrl("javascript:receiveMsgFromNative()");
                } else {
                    mWebView.evaluateJavascript("javascript:receiveMsgFromNative()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                        }
                    });
                }
                isShow=!isShow;
            }
        });
    }

    @SuppressLint("AddJavascriptInterface")
    public void initWebView() {
        // 实例化
        mWebView = findViewById(R.id.mWebView);
        //文件权限
        mWebView.getSettings().setAllowFileAccess(true);
        //测试远程的mWebView.loadUrl("http://www.itxdl.cn");
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
//        mWebView.addJavascriptInterface(new AndroidtoJs(), "test");//AndroidtoJS类对象映射到js的test对象
        mWebView.addJavascriptInterface(this, "android");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
//        String cacheDirPath = getExternalCacheDir() + "";
////      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
//        String dbPath = this.getApplicationContext().getDir("database", this.MODE_PRIVATE).getPath();
//        Log.i(TAG, "cacheDirPath=" + getCacheDir().getAbsolutePath() + "/test");
//        //设置数据库缓存路径
//        mWebView.getSettings().setDatabasePath(cacheDirPath);
//        //设置  Application Caches 缓存目录
//        mWebView.getSettings().setAppCachePath(cacheDirPath);
        // 开启 DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        mWebView.getSettings().setDatabaseEnabled(true);
        //开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);
//        mWebView.getSettings().setAppCacheMaxSize(810241024); //缓存最多可以有8M
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                Log.i(TAG, "onLoadResource url=" + url);

                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "intercept url=" + url);
                view.loadUrl(url);
                return true;
            }

            // 页面开始时调用
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted");
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成调用
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "intercept url=" + url);
                if(url.contains("/students")){
                    //学员列表
                    topBar.setTitle("学员");
                    topBar.setVisiable(false);
                    leftImageButton.setVisibility(View.VISIBLE);
                }else if(url.contains("/detail")){
                    //详情页面
                    topBar.setTitle("排期");
                    topBar.setVisiable(false);
                    leftImageButton.setVisibility(View.VISIBLE);
                }else{
                    //主页
                    topBar.setTitle("主页");
                    topBar.setVisiable(true);
                    leftImageButton.setVisibility(View.INVISIBLE);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            }
        });
        mWebView.loadUrl("file:///android_asset/index.html");
        // 格式规定为:file:///android_asset/文件名.html
//       mWebView.loadUrl("file:///android_asset/test.html");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();//返回上个页面
            return true;
        } else {

            String newPath = Environment.getExternalStorageDirectory() + File.separator + "databackup" + File.separator; // storage/emulated/0/databackup
            copyFolder(NEW_DB, newPath);

            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);//退出整个应用程序
    }

    /**
     * JS调用android的方法
     *
     * @param str
     * @return
     */
    @JavascriptInterface //必不可少
    public void getClient() {
        flag=true;
        // 网络请求数据加载成功 Loading 页面关闭，同时显示数据内容的 View
        mLoadingView.showContentView();
        topBar.setVisiable(true);
        Splash.finishActivity();
    }
    @JavascriptInterface //必不可少
    public void setIsShow() {
        isShow=false;
    }
    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//            }
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String newPath = Environment.getExternalStorageDirectory() + File.separator + "databackup" + File.separator; // storage/emulated/0/TestDB
            if(copyFolder(newPath,NEW_DB)){
                initWebView();
            }else{
                initWebView();
            }
        }else{
            initWebView();
        }
    }

    /**
     * 复制文件夹及其中的文件
     *
     * @param oldPath String 原文件夹路径 如：data/user/0/com.test/files
     * @param newPath String 复制后的路径 如：data/user/0/com.test/cache
     * @return <code>true</code> if and only if the directory and files were copied;
     * <code>false</code> otherwise
     */
    public boolean copyFolder(String oldPath, String newPath) {
            try {

                File newFile = new File(newPath);
                if (!newFile.exists()) {
                    if (!newFile.mkdirs()) {
                        Log.e("--Method--", "copyFolder: cannot create directory.");
                        return false;
                    }
                }
                File oldFile = new File(oldPath);
                String[] files = oldFile.list();
                File temp;
                for (String file : files) {
                    if (oldPath.endsWith(File.separator)) {
                        temp = new File(oldPath + file);
                    } else {
                        temp = new File(oldPath + File.separator + file);
                    }

                    if (temp.isDirectory()) {   //如果是子文件夹
                        copyFolder(oldPath + "/" + file, newPath + "/" + file);
                    } else if (!temp.exists()) {
                        Log.e("--Method--", "copyFolder:  oldFile not exist.");
                        return false;
                    } else if (!temp.isFile()) {
                        Log.e("--Method--", "copyFolder:  oldFile not file.");
                        return false;
                    } else if (!temp.canRead()) {
                        Log.e("--Method--", "copyFolder:  oldFile cannot read.");
                        return false;
                    } else {
                        FileInputStream fileInputStream = new FileInputStream(temp);
                        FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                        byte[] buffer = new byte[1024];
                        int byteRead;
                        while ((byteRead = fileInputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, byteRead);
                        }
                        fileInputStream.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }

                /* 如果不需要打log，可以使用下面的语句
                if (temp.isDirectory()) {   //如果是子文件夹
                    copyFolder(oldPath + "/" + file, newPath + "/" + file);
                } else if (temp.exists() && temp.isFile() && temp.canRead()) {
                    FileInputStream fileInputStream = new FileInputStream(temp);
                    FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                    byte[] buffer = new byte[1024];
                    int byteRead;
                    while ((byteRead = fileInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, byteRead);
                    }
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                 */
                }
                return true;
            } catch (Exception e) {
                Log.e("--err--", e+"");
                e.printStackTrace();
                return false;
            }

    }

    @Override
    public void onFailedClickListener() {

    }
}
