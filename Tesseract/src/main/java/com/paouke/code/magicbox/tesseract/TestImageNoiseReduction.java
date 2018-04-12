package com.paouke.code.magicbox.tesseract;

import com.google.common.annotations.VisibleForTesting;
import com.paouke.code.magicbox.tesseract.util.ImageUtils;
import com.paouke.code.magicbox.tesseract.util.StringUtils;
import com.paouke.code.magicbox.tesseract.wrapper.CaptchaWrapper;
import com.paouke.code.magicbox.tesseract.wrapper.TesseractWrapper;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class TestImageNoiseReduction {

    private static final String TESS_PATH = "C:\\Program Files (x86)\\Tesseract-OCR\\";
    private static final String TESS_PATH35 = "D:\\OCR\\";

    private static final String IMAGE_DIR_PATH = "E:\\pic\\";

    private static final String IMAGE_OUT_DIR_PATH = "E:\\pic\\tmp\\";

    public static void main(String[] args) throws Exception {
        TesseractWrapper tw = new TesseractWrapper(TESS_PATH, TesseractWrapper.Language.ENG);
        Random random = new Random(System.currentTimeMillis());
        File imageDataDir = new File(IMAGE_DIR_PATH);
        if(imageDataDir.exists() && imageDataDir.isDirectory()) {
            int ok = 0;
            int count = 0;
            for (File file : imageDataDir.listFiles())
            {
                if(file.isFile() && file.getAbsolutePath().toLowerCase().contains(".png")) {
                    String imageName = IMAGE_OUT_DIR_PATH + System.currentTimeMillis() + random.nextInt() % 541849 + ".bmp";
                    CaptchaWrapper cw = new CaptchaWrapper(file);
                    cw.adjustContrast(1.3)
                            .binarization(2)
                            .cleanByEightBitDomain(5, 5, 0)
                            .cleanIsland(7)
                            .cleanSide(3, 0)
                            .save(imageName);
                    String ocr = StringUtils.cleanToNumberOrLetterString(tw.doOCR(imageName).trim()).toLowerCase();
                    String really = file.getName().split("\\.")[0].toLowerCase();
                    ok = ok + (really.equals(ocr) ? 1 : 0);
                    count++;
                    System.out.println("really: " + really + ", ocr: " + ocr + ", res: " + (really.equals(ocr) ? "OK" : "FAILD"));
                }
            }
            System.out.println("Success rate: " + ok * 1.0 / count);
        }
    }

    @Test
    public void testImage() {
        try {
            CaptchaWrapper cw = new CaptchaWrapper("D:\\1.png");
            cw.adjustContrast(1)
                    .binarization(142)
                        .cleanByEightBitDomain(5, 5, 0)
                        .cleanIsland(7)
                        .cleanSide(3, 0)
                    .save("D:\\image\\1_n.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
