package com.example.skymail.Data;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.ControllableTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class io {
    private static final String FILE_NAME = "localdata";

    public static void store(String txt, Context ctx){
        //String text = load("")+";"+txt;
        FileOutputStream fos = null;
        try {
            fos = ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE );
            fos.write( txt.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String access (String find,Context ctx){
        String content = null;
        int i = 0;
        String[] multi;
        try {
            FileInputStream fis = ctx.openFileInput(FILE_NAME);
            Scanner scanner = new Scanner(fis);
            scanner.useDelimiter("//Z");
            content = scanner.next();
            scanner.close();
            if(content.contains(";")) {
                multi = content.split(";");
                while (i < multi.length) {
                    if (multi[i].startsWith(find + ":")) {
                        multi = multi[i].split(":");
                        content = multi[1];
                        break;
                    } else i++;
                }
                if (i >= multi.length && !(find.equals(""))) content = null;
            }
            else {
                content=null;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return content;
    }


}