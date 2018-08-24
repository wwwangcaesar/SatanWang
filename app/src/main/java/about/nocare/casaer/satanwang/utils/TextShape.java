package about.nocare.casaer.satanwang.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
*    变色的textview   继承的AppCompatTextView
* @author Satan Wang
* created at 2018/6/20 16:41
*/

public class TextShape extends AppCompatTextView {

    int mViewWidth = 0; //设置View的宽度变量
    Paint mPaint;       //获得TextView的画笔
    LinearGradient mLinearGradient; //渐变渲染器
    Matrix mGradientMatrix;//为了处理平移转换
    int mTranslate = 0;//平移转换量

    public TextShape(Context context) {
        super(context);
    }

    public TextShape(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextShape(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();//View全部的宽度，包括隐藏的
            if (mViewWidth > 0) {
                mPaint =getPaint();
                mLinearGradient = new LinearGradient(0, 0,
                        mViewWidth, 0,
                        new int[]{Color.GREEN, Color.RED,
                                Color.GREEN}, null
                        , Shader.TileMode.CLAMP);
                //Gradient是基于Shader类，所以我们通过Paint的setShader方法来设置这个渐变
                mPaint.setShader(mLinearGradient);
                //初始化Matrix，为绘制做准备
                mGradientMatrix = new Matrix();
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGradientMatrix != null) {
            mTranslate += mViewWidth / 5;
            if (mTranslate > 2 * mViewWidth) {
                mTranslate = -mViewWidth;
            }
            //设置平移转换量
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            //延迟刷新界面
            postInvalidateDelayed(100);
        }
    }
    //重写OnMeasure方法实现wrap_content的适配
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //必须调用这个方法，来改变view的显示大小
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }
    /**
     * UNSPECIFIED
     * 不指定view的显示大小，常用于自定义view中。
     EXACTLY
     精确模式，就是我们常常定义的match_parent，或者layout_width="100dp"。告诉view是一个精确的显示。
     AT_MOST
     我们定义的wrap_content，没有具体的大小。
     */
    private int measureWidth(int measureSpc){
        int result ;
        //通过measureSpe来获取view的显示模式（这里有三种显示模式）
        int specMode = MeasureSpec.getMode(measureSpc);
        int specSize = MeasureSpec.getSize(measureSpc);
        Log.i("获取模式","模式："+specMode+" 尺寸："+specSize);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = 200;
            if(result == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }
    private int measureHeight(int measureSpc){
        int result ;
        //通过measureSpe来获取view的显示模式（这里有三种显示模式）
        int specMode = MeasureSpec.getMode(measureSpc);
        int specSize = MeasureSpec.getSize(measureSpc);
//        Log.i("获取模式","模式："+specMode+" 尺寸："+specSize);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = 200;
            if(result == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }
}
