package com.sonos.youngjoopark.prep3d.utils;

import android.content.Context;
import android.content.res.Resources;

import com.sonos.youngjoopark.prep3d.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GLSLReader {

    public static String readFileFromResource(Context context,int rID) {
        StringBuilder sb = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(rID);
            InputStreamReader isReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(isReader);
            String str;
            while((str = bufferedReader.readLine()) != null) {
                sb.append(str);
                sb.append('\n');
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Could not open resource: "+rID, ioe);
        } catch (Resources.NotFoundException nfe) {
            throw new RuntimeException("Resource not found:"+rID, nfe);
        }
        return sb.toString();
    }
}
