package com.epoint.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

/**
 * 表格导出工具类
 */
public class ExcelUtil
{

    private final XSSFWorkbook xssfWorkbook;


    private final HashMap<Integer, XSSFSheet> sheetMap = new HashMap<>();

    private XSSFCellStyle xssfCellStyle;


    private final HashMap<Integer,Integer> rowMap = new HashMap<>();

    private Integer row = 0;

    private Integer sheetNum = 0;

    private Integer totalSheet = 0;

    public ExcelUtil() {
        xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet();
        sheetMap.put(totalSheet++,sheet);
        rowMap.put(0,0);
    }

    public ExcelUtil(String sheetName) {
        xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet(sheetName);
        sheetMap.put(totalSheet++,sheet);
        rowMap.put(0,0);
    }

    public void addNewSheet(String sheetName){
        rowMap.put(sheetNum,row);
        XSSFSheet sheet = xssfWorkbook.createSheet(sheetName);
        sheetMap.put(totalSheet++,sheet);
        sheetNum = totalSheet - 1;
        rowMap.put(sheetNum,0);
        row = 0;
    }

    public void movePointer(Integer sheetNum){
        rowMap.put(this.sheetNum,row);
        this.sheetNum = sheetNum;
        if(sheetNum <= 0){
            this.sheetNum = 0;
        }
        if(sheetNum >= totalSheet){
            this.sheetNum = totalSheet - 1;
        }
        row = rowMap.get(this.sheetNum);
    }

    public void setRowData(String... data) {
        XSSFSheet xssfSheet = sheetMap.get(sheetNum);
        XSSFRow xssfRow = xssfSheet.createRow(row++);
        for (int i = 0; i < data.length; i++) {
            XSSFCell cell = xssfRow.createCell(i);
            cell.setCellValue(data[i]);
            if (xssfCellStyle != null) {
                cell.setCellStyle(xssfCellStyle);
            }
        }
    }

    public void setRowData(int rowNum, String... data) {
        XSSFSheet xssfSheet = sheetMap.get(sheetNum);
        XSSFRow xssfRow = xssfSheet.getRow(rowNum);
        if (xssfRow == null) {
            xssfRow = xssfSheet.createRow(rowNum);
        }
        for (int i = 0; i < data.length; i++) {
            XSSFCell cell = xssfRow.createCell(i);
            cell.setCellValue(data[i]);
            if (xssfCellStyle != null) {
                cell.setCellStyle(xssfCellStyle);
            }
        }
    }

    public void setCellData(int rowNum, int columnNum, String data) {
        XSSFSheet xssfSheet = sheetMap.get(sheetNum);
        XSSFRow xssfRow = xssfSheet.getRow(rowNum);
        if (xssfRow == null) {
            xssfRow = xssfSheet.createRow(rowNum);
        }
        XSSFCell cell = xssfRow.getCell(columnNum);
        if (cell == null) {
            cell = xssfRow.createCell(columnNum);
        }
        cell.setCellValue(data);
        if (xssfCellStyle != null) {
            cell.setCellStyle(xssfCellStyle);
        }
    }

    public void write(OutputStream fileOutputStream) throws IOException {
        xssfWorkbook.write(fileOutputStream);
        xssfWorkbook.close();
    }

    public void save(String path) {
        try {
            if (!path.endsWith(".xlsx")) {
                path += ".xlsx";
            }
            FileOutputStream out = new FileOutputStream(path);
            write(out);
            out.flush();
            out.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCellStyle(XSSFCellStyle xssfCellStyle) {
        this.xssfCellStyle = xssfCellStyle;
    }

    public void setCellStyle(short fontsize, boolean isBold, boolean isCenter) {

        xssfCellStyle = xssfWorkbook.createCellStyle();
        // 是否水平居中
        if (isCenter) {
            xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        }
//        xssfCellStyle.setWrapText(true);
        xssfCellStyle.setBorderBottom(BorderStyle.THIN);
        xssfCellStyle.setBorderTop(BorderStyle.THIN);
        xssfCellStyle.setBorderLeft(BorderStyle.THIN);
        xssfCellStyle.setBorderRight(BorderStyle.THIN);
        xssfCellStyle.setLocked(false);

        xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        // 创建字体
        XSSFFont font = xssfWorkbook.createFont();
        // 是否加粗字体
        if (isBold) {
            font.setBold(true);
        }
        font.setFontHeightInPoints(fontsize);
        font.setFontName("思源宋体");
        // 加载字体
        xssfCellStyle.setFont(font);
    }

}
