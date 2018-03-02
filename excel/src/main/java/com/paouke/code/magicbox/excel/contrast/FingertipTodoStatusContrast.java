package com.paouke.code.magicbox.excel.contrast;

import com.sun.media.sound.InvalidFormatException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version : 1.0
 * @Project : MagicBox
 * @Program Name  : com.paouke.code.magicbox.excel.contrast.FingertipTodoStatusContrast
 * @Class Name    : FingertipTodoStatusContrast
 * @Copyright : Copyright (c)2017-2015
 * @Company : CreditEase
 * @Description :
 * @Author : tongwei
 * @Creation Date : 2018/3/1 17:51
 * @ModificationHistory Date         Author      Version            Description
 * ------------------------------------------------------------------
 * 2018/3/1      tongwei       1.0
 * 1.0 Version
 */
public class FingertipTodoStatusContrast {
    public Map<String, String> fingertipTodoMap;

    private boolean isHead = true;

    public FingertipTodoStatusContrast() {
        fingertipTodoMap = new HashMap<>();
    }

    private String getStringCell(Row row, int index) {
        Cell cell = row.getCell(index);
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    private void readFingertipTodoXls(File fingertipXlsFile) throws Exception {
        // 获得工作簿
        Workbook workbook = WorkbookFactory.create(fingertipXlsFile);
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getLastRowNum() + 1;
        Row tmp = sheet.getRow(0);
        if (tmp == null) {
            return;
        }
        for (int row = 0; row < rows; row++) {
            Row r = sheet.getRow(row);
            if (tmp.getPhysicalNumberOfCells() < 3) {
                continue;
            }
            fingertipTodoMap.put(getStringCell(r, 0), getStringCell(r, 2) + ":" + getStringCell(r, 1));
        }
    }

    private boolean isEqual(String todoSign, String processorEmail) {
        String statusData = fingertipTodoMap.get(todoSign);
        if (statusData == null) return false;
        String[] statusDatas = statusData.split(":");
        System.out.print("单子状态: " + statusDatas[1] + ", ");
        System.out.print("单子审批人: " + statusDatas[0] + ", ");
        if (!("11".equals(statusDatas[1]) || "10".equals(statusDatas[1]))) {
            return false;
        }
        try {
            String peHead = processorEmail.substring(0, 3);
            String fpeHead = statusDatas[0].substring(0, 3);
            String peTail = processorEmail.split("@")[1];
            String fpeTail = statusDatas[0].split("@")[1];
            return peHead.equals(fpeHead) && peTail.equals(fpeTail);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getFingertipPe(String todoSign) {
        return fingertipTodoMap.get(todoSign) == null ? "" : fingertipTodoMap.get(todoSign).split(":")[0];
    }

    private String getFingertipStatus(String todoSign) {
        return fingertipTodoMap.get(todoSign) == null ? "" : fingertipTodoMap.get(todoSign).split(":")[1];
    }

    private void writeDiffTodo(HSSFSheet sheet, String todoSign, String pe, String fpe, String fs) {
        HSSFRow rows = isHead ?
                (sheet.createRow(0)) : sheet.createRow(sheet.getLastRowNum() + 1);
        isHead = false;
        rows.createCell(0).setCellValue(todoSign);
        rows.createCell(1).setCellValue(pe);
        rows.createCell(2).setCellValue(fpe);
        rows.createCell(3).setCellValue(fs);
    }

    private void contrast(File moaXlsFile, File fingertipXlsFile, File diffXlsFile) throws Exception {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("sheet1");
        writeDiffTodo(hssfSheet, "报销单id", "MOA处理人", "指尖处理人", "status");
        readFingertipTodoXls(fingertipXlsFile);
        Workbook workbook = WorkbookFactory.create(moaXlsFile);
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getLastRowNum() + 1;
        Row tmp = sheet.getRow(0);
        if (tmp == null) {
            return;
        }
        for (int row = 0; row < rows; row++) {
            Row r = sheet.getRow(row);
            if (tmp.getPhysicalNumberOfCells() < 2) {
                continue;
            }
            String todoSign = getStringCell(r, 0);
            String pe = getStringCell(r, 1);
            System.out.print("检查TODO: " + todoSign + ", ");
            System.out.print("PE: " + pe + ", ");
            if (!isEqual(todoSign, pe)) {
                System.out.println("检查结果为双方不同！");
                writeDiffTodo(hssfSheet, todoSign, pe, getFingertipPe(todoSign), getFingertipStatus(todoSign));
            } else {
                System.out.println("检查结果为双方相同！");
            }
        }
        FileOutputStream xlsStream = new FileOutputStream(diffXlsFile);
        hssfWorkbook.write(xlsStream);
    }


    public static void main(String[] args) throws Exception {
        File moaXlsFile = new File("D:\\doc\\fmc\\moa.xlsx");
        File fingertipXlsFile = new File("D:\\doc\\fmc\\fing.xlsx");
        File diffXlsFile = new File("D:\\doc\\fmc\\diff.xls");
        FingertipTodoStatusContrast fc = new FingertipTodoStatusContrast();
        //a,b -> c
        fc.contrast(moaXlsFile, fingertipXlsFile, diffXlsFile);
    }
}
