package com.sonos.youngjoopark.prep3d;

import android.content.Context;
import android.opengl.GLES20;

import com.sonos.youngjoopark.prep3d.utils.GLSLReader;
import com.sonos.youngjoopark.prep3d.utils.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {
    // each vertex has three entities
    public static int COORDS_PER_VERTEX = 3;
    public static float[] triangleCoords = { // counterclockwise
           0.0f,  0.622008459f, 0.0f, // top
          -0.5f, -0.311004243f, 0.0f, // bottom left
           0.5f, -0.311004243f, 0.0f  // bottom right
    };
    public float[] color = {
         //   R     G    B    alpha
            0.6f, 0.7f, 0.2f, 1f
    };

    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_VERTEX = 4;
    private static final String V_POSITION = "vPosition"; // in glsl
    private static final String V_COLOR = "vColor"; // in glsl
    private static final String U_MATRIX = "u_MVPMatrix";
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX *  BYTES_PER_VERTEX;
    private final Context mContext;
    private final int mProgramId;
    private FloatBuffer mVertexBuffer;
    private String mVertexShaderCode;
    private String mFragmentShaderCode;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    public Triangle(Context context) {
        mContext = context;

        // vertex buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                triangleCoords.length * BYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());

        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(triangleCoords);
        mVertexBuffer.position(0);

        mVertexShaderCode = GLSLReader.readFileFromResource(context, R.raw.my_vertex_shader);
        mFragmentShaderCode = GLSLReader.readFileFromResource(context, R.raw.my_fragment_shader);

        // upload and compile shader code
        int vertexShader = ShaderHelper.loadShader(GLES20.GL_VERTEX_SHADER, mVertexShaderCode);
        int fragmentShader = ShaderHelper.loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShaderCode);

        mProgramId = ShaderHelper.linkProgram(vertexShader, fragmentShader);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgramId);

        // get location of attribute to tell where to find the data
        mPositionHandle = GLES20.glGetAttribLocation(mProgramId, V_POSITION);

        mVertexBuffer.position(0); // make sure to read from the beginning

        // link the data to the attribute and enable the attribute before drawing
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, vertexStride, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // set the color and draw the triangle
        mColorHandle = GLES20.glGetUniformLocation(mProgramId, V_COLOR);
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramId, U_MATRIX);

        // send project matrix to shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // disable vertex attribute after drawing
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
