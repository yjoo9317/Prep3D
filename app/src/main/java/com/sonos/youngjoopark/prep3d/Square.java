package com.sonos.youngjoopark.prep3d;

import android.content.Context;
import android.opengl.GLES20;

import com.sonos.youngjoopark.prep3d.utils.GLSLReader;
import com.sonos.youngjoopark.prep3d.utils.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square {
    public static final int COORDS_PER_VERTEX = 3;
    public static float[] mSquareCoords = { // vertex for a square consists of two triangles
            -0.5f,  0.5f, 0.0f, // top left
            -0.5f, -0.5f, 0.0f, // bottom left
             0.5f, -0.5f, 0.0f, // bottom right
             0.5f,  0.5f, 0.0f  // top right
    };
    public float[] color = {
            //   R     G    B    alpha
            0.6f, 0.7f, 0.2f, 1f
    };


    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_SHORT = 2;

    private final int vertexCount = mSquareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * BYTES_PER_FLOAT;
    private final Context mContext;
    private final int mProgramId;
    private short[] mOrder = {0, 1, 2, 0, 2, 3};
    private String mVertexShaderCode;
    private String mFragmentShaderCode;
    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;
    private int mPositionHandle; // position location
    private int mColorHandle; // color location

    public Square(Context context) {
        mContext = context;
        ByteBuffer bb = ByteBuffer.allocateDirect(mSquareCoords.length * BYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(mSquareCoords);
        mVertexBuffer.position(0); // let it read from the beginning

        ByteBuffer dlb = ByteBuffer.allocateDirect(mOrder.length * BYTES_PER_SHORT);
        dlb.order(ByteOrder.nativeOrder());
        mDrawListBuffer = dlb.asShortBuffer();
        mDrawListBuffer.put(mOrder);
        mDrawListBuffer.position(0); // let it read from the beginning

        mVertexShaderCode = GLSLReader.readFileFromResource(context, R.raw.my_vertex_shader);
        mFragmentShaderCode = GLSLReader.readFileFromResource(context, R.raw.my_fragment_shader);

        // upload and compile shader code
        int vertexShader = ShaderHelper.loadShader(GLES20.GL_VERTEX_SHADER, mVertexShaderCode);
        int fragmentShader = ShaderHelper.loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShaderCode);

        // join shaders into GL program
        mProgramId = ShaderHelper.linkProgram(vertexShader, fragmentShader);

    }

    public void draw() {

        GLES20.glUseProgram(mProgramId);

        mPositionHandle = GLES20.glGetAttribLocation(mProgramId, ShaderHelper.POSITION_LOCATION);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        mVertexBuffer.position(0);
        // prepare coordinates for a square
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, vertexStride, mVertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgramId, ShaderHelper.COLOR_LOCATION);
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
