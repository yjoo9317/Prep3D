package com.sonos.youngjoopark.prep3d;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "MyGLRenderer";
    private final Context mContext;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private Triangle mTriangle;
    private Square mSquare;

    public MyGLRenderer(Context context){
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // set background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1f);
        mTriangle = new Triangle(mContext);
        mSquare = new Square(mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = width > height ?
                (float)width / (float)height :
                (float)height / (float)width;
        if (width > height) {
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        } else {
            Matrix.frustumM(mProjectionMatrix, 0, -1f, 1f, -ratio, ratio, 3, 7);
        }
    }

    @Override
    public void onDrawFrame(GL10 unsed) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        mSquare.draw();
        /*
        // set the camera location
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // multiplication of projection and view matrix is stored in mMVPMatrix
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        mTriangle.draw(mMVPMatrix);
        */
    }

}
