package about.nocare.casaer.satanwang.widget.video;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SquareGLSurfaceView extends GLSurfaceView {
    private static final String TAG = "SquareGLSurfaceView";
    DemoRenderer mRenderer;// 渲染器  不进行初始化 GLThread 渲染就会崩溃
    public SquareGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //如果是单独继承GLSurfaceView，需要初始化 Renderer  本项目部用，因为已经初始化了
//        mRenderer = new DemoRenderer();
//        this.setRenderer(mRenderer);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        Log.i(TAG, "specify width mode:" + MeasureSpec.toString(widthMeasureSpec) + " size:" + width);
        Log.i(TAG, "specify height mode:" + MeasureSpec.toString(heightMeasureSpec) + " size:" + height);

        setMeasuredDimension(width, width);
    }

    class DemoRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onDrawFrame(GL10 gl) {
            //每帧都需要调用该方法进行绘制。绘制时通常先调用glClear来清空framebuffer。
            //然后调用OpenGL ES其他接口进行绘制
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int w, int h) {
            //当surface的尺寸发生改变时，该方法被调用，。往往在这里设置ViewPort。或者Camara等。
            gl.glViewport(0, 0, w, h);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // 该方法在渲染开始前调用，OpenGL ES的绘制上下文被重建时也会调用。
            //当Activity暂停时，绘制上下文会丢失，当Activity恢复时，绘制上下文会重建。
        }
    }
}
