package com.paouke.code.magicbox.filebatch;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenameBatch {
    Pattern pattern = Pattern.compile("(.jpg|.png|.gif)$");
    public boolean rename(String prefix, String suffix, String path) {
        File file = new File(path);
        int count = 0;
        if(file.exists() && file.isDirectory()) {
            File files[] = file.listFiles();
            for(File sonFile : files) {
                if(sonFile.isFile()) {
                    Matcher matcher = pattern.matcher(sonFile.getName());
                    String newFileName = path + "/" + prefix + count++;
                    String oldFileName = sonFile.getName();
                    File newFile = matcher.find() ? new File(newFileName + matcher.group()) : new File(newFileName + suffix);
                    boolean result = sonFile.renameTo(newFile);
                    System.out.println("文件：" + oldFileName + "->" + newFile.getName() + " is " + result);
                }
            }
        } else {
            return false;
        }
        return true;
    }
    public static void main(String[] args){
        RenameBatch renameBatch = new RenameBatch();
        renameBatch.rename("表情", ".gif", "C:\\Users\\admin\\Desktop\\CustomEmotions");
    }
}
