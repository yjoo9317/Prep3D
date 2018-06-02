package com.sonos.youngjoopark.prep3d.utils;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static final String POSITION_LOCATION = "vPosition";
    public static final String COLOR_LOCATION = "vColor";

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programId = GLES20.glCreateProgram();

        if(programId == 0){
            Log.w(TAG, "Could not create new OpenGL program.");
            return 0;
        }

        // attach both vertex and fragment shaders to the program
        GLES20.glAttachShader(programId, vertexShaderId);
        GLES20.glAttachShader(programId, fragmentShaderId);

        GLES20.glLinkProgram(programId);

        // retrieve linking of program result
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);

        if(linkStatus[0] == 0){
            Log.w(TAG, "Failed to link program.");
            return 0;
        }
        return programId;
    }

    public static int loadShader(int type, String shaderCode) {
        final int shader = GLES20.glCreateShader(type);
        if (shader == 0) {
            Log.w(TAG, "Couldn't create new shader.");
            return 0;
        }

        //upload shader code and link to shader id
        GLES20.glShaderSource(shader, shaderCode);

        //compile the code uploaded under shader id.
        GLES20.glCompileShader(shader);

        // retrieve compile status
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shader);
            return 0;
        }

        return shader;
    }

}
