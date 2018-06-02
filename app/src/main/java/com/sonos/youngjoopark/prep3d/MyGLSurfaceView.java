package com.sonos.youngjoopark.prep3d;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {
    public static final String TAG = "MyGLSurfaceView";
    public static final int ESVersion = 2;
    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(ESVersion);
        mRenderer = new MyGLRenderer(context);

        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
