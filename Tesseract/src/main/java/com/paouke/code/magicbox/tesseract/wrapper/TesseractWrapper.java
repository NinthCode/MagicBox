package com.paouke.code.magicbox.tesseract.wrapper;

import com.paouke.code.magicbox.tesseract.util.FileUtils;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class TesseractWrapper {

    private String tesseractPath;

    private Language language;

    private static final String DO_OCR_CMD = "tesseract";

    private static final String TESS_DATA_PATH = "tessdata";

    public TesseractWrapper(String tesseractPath, Language language) {
        this.tesseractPath = tesseractPath;
        this.language = language;
    }

    public enum Language {
        ENG("eng");

        private String value;

        Language(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    public String doOCR(String imageFullName) {
        try {
            String[] cmd = new String[]{this.tesseractPath + DO_OCR_CMD, imageFullName, imageFullName, "-l", language.getValue(), "--psm", "7"};
            Process pb = Runtime.getRuntime().exec(cmd);
            int execRes = pb.waitFor();
            if (execRes != 0) {
                throw new RuntimeException("Command execution failed, return " + execRes);
            }
            return FileUtils.readFileByChars(imageFullName + ".txt", "UTF-8");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String doOCR(BufferedImage bufferedImage) {
        try {
            Tesseract instance = new Tesseract();
            instance.setLanguage(language.getValue());
            instance.setDatapath(this.tesseractPath + TESS_DATA_PATH);
            return instance.doOCR(bufferedImage);
        } catch (TesseractException te) {
            te.printStackTrace();
            return "";
        }
    }
}
