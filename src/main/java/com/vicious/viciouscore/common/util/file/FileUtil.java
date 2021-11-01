package com.vicious.viciouscore.common.util.file;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileUtil {
    public static Path createDirectoryIfDNE(Path p){
        if(!Files.isDirectory(p)){
            try {
                Files.createDirectory(p);
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        return p;
    }

    public static void createOrWipe(Path p){
        try {
            Files.write(p, "".getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e){
            try {
                Files.createFile(p);
            } catch(IOException ex){
                System.out.println("I'm not sure how we got here, but somehow the file you have created both exists and doesn't exist at the same time. Is this God?");
                ex.printStackTrace();
            }
        }
    }
    public static JSONObject loadJSON(Path p) throws JSONException,IOException{
        try{
            InputStream is = new FileInputStream(p.toFile());
            String jsonTxt = IOUtils.toString(is, "UTF-8");
            is.close();
            return new JSONObject(jsonTxt);

        } catch (Exception ex){
            System.out.println("Could not load, probably doesn't actually exist. " + p.toString() + " caused by: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }
}
