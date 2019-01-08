package com.example.project.myproject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopBar extends RelativeLayout {
    // 定义显示的控件
    private ImageView leftButton;
    private ImageView rightButton;
    private TextView tvTittle;

    // 左边Button的属性
    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    // 右边Button的属性
    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    // 中间Title的属性
    private String tittle;
    private float tittleTextSize;
    private int tittleTextColor;
    // 设置三个控件的格式 或者说是布局  LayoutParams可以设置左右按钮和中间文本的布局
    private LayoutParams leftParams, rightParams, tittleParams;
    //定义接口
    private TopBarListener topBarListener;

    /**
     * 在构造函数中完成控件的初始化 获取xml中的属性  并将xml中的属性赋值给我们在类中定义的与之对应的变量
     *
     * @param context
     * @param attrs
     */
    @SuppressLint("NewApi")
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //重要的数据结构   通过TypedArray我们可以获取到atts.xml文件中的属性内容
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.TopBar);
        leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor, 0);
        leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);
        leftText = ta.getString(R.styleable.TopBar_leftText);

        rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor, 0);
        rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);
        rightText = ta.getString(R.styleable.TopBar_rightText);

        tittle = ta.getString(R.styleable.TopBar_tittle);
        tittleTextColor = ta.getColor(R.styleable.TopBar_tittleTextColor, 0);
        tittleTextSize = ta.getDimension(R.styleable.TopBar_tittleTextSize, 0);

        ta.recycle();//回收系统资源

        leftButton = new ImageView(context);
        rightButton = new ImageView(context);
        tvTittle = new TextView(context);

        //为左边Button设置我们在xml文件中自定义的属性
//        leftButton.setTextColor(leftTextColor);
//        leftButton.setBackground(leftBackground);
//        leftButton.setText(leftText);
//        leftButton.setPadding(15,0,0,0);
//        leftButton.setGravity(Gravity.CENTER);
        leftButton.setImageResource(R.mipmap.help);
        leftButton.setPadding(20,0,0,0);

        //为右边Button设置我们在xml文件中自定义的属性
//        rightButton.setTextColor(rightTextColor);
//        rightButton.setBackground(rightBackground);
//        rightButton.setText(rightText);
//        rightButton.setPadding(0,0,15,0);
//        rightButton.setGravity(Gravity.CENTER);
        rightButton.setImageResource(R.mipmap.menu);
        rightButton.setPadding(0,0,25,0);


        //为中间Tittle设置我们在xml文件中自定义的属性
        tvTittle.setText(tittle);
        tvTittle.setTextColor(tittleTextColor);
        tvTittle.setTextSize(tittleTextSize);
        tvTittle.setGravity(Gravity.CENTER);    //布局为居中

        // 设置左边Button的布局
        // 宽：WRAP_CONTENT 高：WRAP_CONTENT 布局：居左对齐
        //addRule()方法可以设置Button的布局
        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        leftParams.width = 88;
        leftParams.height = 88;
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(leftButton, leftParams);

        // 右边按钮的属性
        // 宽：WRAP_CONTENT 高：WRAP_CONTENT 布局：居右对齐
        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rightParams.width = 88;
        rightParams.height = 88;
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(rightButton, rightParams);

        // 中间Tittle的属性
        tittleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        tittleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTittle, tittleParams);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBarListener.leftButtonClick();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBarListener.rightButtonClick();
            }
        });
    }
    //定义方法使用接口机制   该方法的主要目的是暴露给使用者   使用者通过重写topBarListener的方法   实现onClick的功能
    public void setOnLeftButtonClickListener(TopBarListener topBarListener){
        this.topBarListener = topBarListener;
    }
    public void setTitle(String title){
        tvTittle.setText(title);
    }
    public void setVisiable(Boolean isVisiable){
        if(isVisiable){
            rightButton.setVisibility(View.VISIBLE);
            leftButton.setVisibility(View.VISIBLE);
        }else{
            rightButton.setVisibility(View.INVISIBLE);
            leftButton.setVisibility(View.INVISIBLE);
        }

    }

}