package com.itheima.health;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.helpers.XSSFXmlColumnPr;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ClassName:TestPOI
 * @Author：Mr.lee
 * @DATE：2019/11/08
 * @TIME： 15:16
 * @Description: TODO
 */
public class TestPOI {

    /**
     * 1、获取工作簿
     * 2、获取表名
     * 3、获取行
     * 4、获取单元格
     */
    @Test
    public void exportExcel() throws IOException {

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("d:/hay.xlsx");

        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        //遍历工作表获取行对象
        for (Row row : sheetAt) {
            //遍历行对象获取单元格对象
            for (Cell cell : row) {
                //获取单元格中的值
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
            System.out.println("============");
        }
    }

    // 导出excel，获取最后一行
    @Test
    public void exportExcel_lastRow() throws IOException {
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("D:/hay.xlsx");
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取当前工作表最后一行的行号，行号从0开始
        int lastRowNum = sheet.getLastRowNum();
        //遍历这一行
        for(int i=0;i<lastRowNum;i++){
            //根据行号获取行对象
            XSSFRow row = sheet.getRow(i);
            //根据行对象获取单元格，列号
            short lastCellNum = row.getLastCellNum();
            //遍历这一列
            for(short j=0;j<lastCellNum;j++){
                //获取单元格数据
                String value = row.getCell(j).getStringCellValue();
                System.out.println(value);
            }
        }
        workbook.close();
    }

    // 导入excel
    @Test
    public void importExcel() throws IOException {
        //在内存中创建一个Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表，指定工作表名称
        XSSFSheet sheet = workbook.createSheet("西交铁发");

        //创建行，0表示第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格，0表示第一个单元格
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("小明");
        row1.createCell(2).setCellValue("10");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("小王");
        row2.createCell(2).setCellValue("20");

        //通过输出流将workbook对象下载到磁盘
        FileOutputStream out = new FileOutputStream("D:/xjtf.xlsx");
        workbook.write(out);
//        out.flush();
        out.close();
        workbook.close();
    }


}
