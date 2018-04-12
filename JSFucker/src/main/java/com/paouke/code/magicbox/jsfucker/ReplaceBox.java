package com.paouke.code.magicbox.jsfucker;

import java.io.*;

public class ReplaceBox {
    public static String readFileByChars(String fileName, String charsetName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), charsetName == null ? "UTF-8" : charsetName);
            int tempChar;
            StringBuilder sb = new StringBuilder();
            while ((tempChar = reader.read()) != -1) {
                sb.append((char)tempChar);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static void writeFile(String fileName, String content, boolean isAppend) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, isAppend);
            fileWriter.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        String jsCode = readFileByChars("C:\\Users\\paouke.NCF-3952952\\Desktop\\g.js", "UTF-8");
        for(int i = 16; i < 178;i++) {
            jsCode = jsCode.replaceAll("\\\\x" + Integer.toHexString(i), String.valueOf((char)i));
        }
        writeFile("C:\\Users\\paouke.NCF-3952952\\Desktop\\h.js", jsCode, false);
        System.out.println(Integer.toHexString(12));
    }
}
