package com.lee;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ClassName:POITest02
 * @Author：Mr.lee
 * @DATE：2019/12/25
 * @TIME： 19:21
 * @Description: TODO
 */
public class POITest02 {
    public static void main(String[] args) {
        FileOutputStream fileOut = null;
        BufferedImage bufferImg = null;
        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
        try {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            bufferImg = ImageIO.read(new File("D:\\green.jpg"));
            ImageIO.write(bufferImg, "jpg", byteArrayOut);

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet1 = wb.createSheet("test picture");
            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
            //anchor主要用于设置图片的属性
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255,
                    255,(short) 1, 1, (short) 5, 8);
            //注意：这个方法在新版本的POI中参数类型改成了（AnchorType anchorType）　
            anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
            //插入图片
            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(),
                    HSSFWorkbook.PICTURE_TYPE_JPEG));
            fileOut = new FileOutputStream("D:\\test3.xls");
            // 写入excel文件
            wb.write(fileOut);
            System.out.println("----Excle文件已生成------");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(fileOut != null){
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //插入某个图片到指定索引的位置
    private static void insertImage(HSSFWorkbook wb,HSSFPatriarch pa,byte[] data,int row,int column,int index){
        //单元格为标,以左上为起点,向右移,范围0-1023  dx1 must be between 0 and 1023
        int x1=0;
        //单元格为标,以左上为起点,向下移,范围0-1023  dy1 must be between 0 and 255
        int y1=0;
        //单元格为标,以右上为起点,向右移,范围0-1023  dx1 must be between 0 and 1023
        int x2=100;
        //单元格为标,以右下为起点,向下移,范围0-1023  dy1 must be between 0 and 255
        int y2=22;
         //后面四个参数表示图片左上角和右下角的坐标
        //col1 图片的左上角放在第几个列cell，
        // row1 图片的左上角放在第几个行cell，
        // col2 图片的右下角放在第几个列cell，
        // row2 图片的右下角放在第几个行cell，

        HSSFClientAnchor anchor = new HSSFClientAnchor(x1,y1,x2,y2,(short)column,row,(short)column,row);

        anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
        pa.createPicture(anchor , wb.addPicture(data,HSSFWorkbook.PICTURE_TYPE_JPEG));
    }
}
