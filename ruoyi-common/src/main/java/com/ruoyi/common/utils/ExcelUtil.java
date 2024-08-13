package com.ruoyi.common.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description: TODO
 * @Author: jzy
 * @Date: 2022/6/16
 * @Version:v1.0
 */
public class ExcelUtil {
    private static final int BYTES_DEFAULT_LENGTH = 10240;
    private static final int IMG_HEIGHT = 30;
    private static final int IMG_WIDTH = 30;
    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    public static void export(HttpServletResponse response, List<String> title, List<List<Object>> data, String fileName, String sheetName, Map<Integer, Integer> merge, HttpServletRequest request, String path) throws Exception {

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet(sheetName);
        Drawing<?> patriarch = sheet.createDrawingPatriarch();


        //设置标题样式
        CellStyle headStyle = wb.createCellStyle();
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //行样式
        CellStyle rowStyle = wb.createCellStyle();
        rowStyle.setAlignment(HorizontalAlignment.CENTER);
        rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置标题
        SXSSFRow titleRow = sheet.createRow(0);
        for (int i = 0; i < title.size(); i++) {
            SXSSFCell cell = titleRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(title.get(i));
        }


        //设置内容
        for (int i = 0; i < data.size(); i++) {
            List<Object> objects = data.get(i);
            SXSSFRow row = sheet.createRow(i + 1);
            SXSSFCell indexCell = row.createCell(0);
            setCellValue(indexCell, i + 1, rowStyle);

            for (int i1 = 0; i1 < objects.size(); i1++) {
                String res = String.valueOf(objects.get(i1)) ;
                if (res.endsWith(".jpg") || res.endsWith(".png") || res.endsWith(".jpeg") ) {
                    String[] split = res.split(",");
                    for (int i2 = 0; i2 < split.length; i2++) {
                       String pic = split[i2].replace("/profile","");
                        setCellPicture(wb, row, patriarch,i , i1 , new FileInputStream(path + pic));
                    }

                } else {
                    SXSSFCell cell = row.createCell(i1 + 1);
                    setCellValue(cell, res, rowStyle);
                }

            }
        }

//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
//        String name = new String(fileName.getBytes("GBK"), "ISO8859_1") + XLSX;
//        response.addHeader("Content-Disposition", "attachment;filename=" + name);
        ServletOutputStream outputStream = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String(fileName.getBytes(), "iso-8859-1") + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     *
     * @param wb wb
     * @param sr row
     * @param patriarch
     * @param x
     * @param y
     * @param
     */

    private static void setCellPicture(SXSSFWorkbook wb, Row sr, Drawing<?> patriarch, int x, int y, FileInputStream fileInputStream) {
        // 设置图片宽高
        sr.setHeight((short) (IMG_WIDTH * IMG_HEIGHT));
        // （jdk1.7版本try中定义流可自动关闭）
        try (InputStream is = fileInputStream; ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buff = new byte[BYTES_DEFAULT_LENGTH];
            int rc;
            while ((rc = is.read(buff, 0, BYTES_DEFAULT_LENGTH)) > 0) {
                outputStream.write(buff, 0, rc);
            }
            // 设置图片位置
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, y, x, y + 1, x + 1);
            // 设置这个，图片会自动填满单元格的长宽
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
            patriarch.createPicture(anchor, wb.addPicture(outputStream.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setCellValue(Cell cell, Object o, CellStyle cellStyle) {
        cell.setCellStyle(cellStyle);
        if (o == null) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue("");
            return;
        }

        if (o instanceof String) {
            String s = o.toString();
            cell.setCellType(CellType.STRING);
            cell.setCellValue(s);
            return;
        }

        if (o instanceof Integer || o instanceof Double || o instanceof Float || o instanceof Long) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Double.parseDouble(o.toString()));
            return;
        }
        if (o instanceof Boolean) {
            cell.setCellType(CellType.BOOLEAN);
            cell.setCellValue((Boolean) o);
            return;
        }
        if (o instanceof Date) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(formatDate((Date) o));
            return;
        }

        if (o instanceof LocalDateTime) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(formatDate((LocalDateTime) o));
            return;
        }
        if (o instanceof LocalDate) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(formatDate((LocalDate) o));
            return;
        }
        cell.setCellType(CellType.STRING);
        cell.setCellValue(o.toString());
    }


    static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(date);
    }

    static String formatDate(LocalDateTime date) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


    ////////////////////////////////import///////////////////////////////////

    /**
     * 导入流程
     * 1、根据文件名称获取对应Workbook
     * 2、获取标题行数据 封装成map  key 是列坐标  value 是标题
     * 3、循环除标题每一行
     * ）1 一行对应一个map  key 是标题    value 是列值
     * ）2 循环class对象的所有字段，只要字段带有指定注解，取出注解中的标题
     * ）3 用map根据字段注解中的标题取出对应的value
     * ）4 根据字段类型判断将value转换成字段类型
     * ）5 返回   ok
     *
     * @param multipartFile
     * @param c
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readMultipartFile(MultipartFile multipartFile, Class<T> c) throws Exception {
        Workbook wb = getWorkbook(multipartFile);

        if (wb == null) return null;
        Sheet sheet = wb.getSheetAt(0);

        int titleRow = sheet.getFirstRowNum();  //首行下标
        int lastRow = sheet.getLastRowNum();    //尾行下标

        Row firstRow = sheet.getRow(titleRow);
        int firstCellNum = firstRow.getFirstCellNum();  //第一列   不是下标
        int lastCellNum = firstRow.getLastCellNum();    //最后一列  不是下标

        Map<Integer, String> keyMap = new HashMap<>();
        for (int i = firstCellNum; i < lastCellNum; i++) {
            String val = getCellValue(firstRow.getCell(i));
            if (val != null && val.trim().length() != 0) {
                keyMap.put(i, val);
            }
        }
        if (firstCellNum == lastCellNum || keyMap.isEmpty()) { // 只有一列 且第一列的值为空
            return null;
        }


        List<T> data = new ArrayList<>();// 返回集合
        Field[] fields = c.getDeclaredFields(); // 获取所有字段
        //循环行
        for (int i = titleRow + 1; i <= lastRow; i++) {  //去除标题行
            T t = c.newInstance();  //一行对应一个对象
            Map<String, String> map = new HashMap<>(); //接收每列数据，不然要在列循环里嵌套循环
            Row row = sheet.getRow(i);
            for (int j = firstCellNum; j < lastCellNum; j++) {
                String val = getCellValue(row.getCell(j));  //列值
                map.put(keyMap.get(j), val);
            }

            // 一行数据循环完在给对象赋值
            for (Field field : fields) {
                ExcelAnnotation annotation = field.getAnnotation(ExcelAnnotation.class);
                if (annotation == null) {
                    continue;
                }
                String value = annotation.value(); //注解值 对应excel表头
                String className = field.getType().getSimpleName(); //属性类型名称
                String s = map.get(value);  //获取属性对应数据的值
                field.setAccessible(true);  //
                try {
                    if ("int".equalsIgnoreCase(className) || "Integer".equals(className)) {
                        field.set(t, Integer.parseInt(s));
                    } else if ("String".equalsIgnoreCase(className)) {
                        field.set(t, s);
                    } else if ("Date".equalsIgnoreCase(className)) {
                        field.set(t, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s));
                    } else if ("boolean".equalsIgnoreCase(className)) {
                        field.set(t, Boolean.valueOf(s));
                    } else if ("double".equalsIgnoreCase(className)) {
                        field.set(t, Double.valueOf(s));
                    } else if ("long".equalsIgnoreCase(className)) {
                        field.set(t, Long.valueOf(s));
                    } else if ("BigDecimal".equalsIgnoreCase(className)) {
                        field.set(t, new BigDecimal(s));
                    } else if ("LocalDateTime".equalsIgnoreCase(className)) {
                        field.set(t, LocalDateTime.parse(s, DateTimeFormatter.ofPattern(DATE_FORMAT)));
                    } else if ("LocalDate".equalsIgnoreCase(className)) {
                        field.set(t, LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            data.add(t);
        }
        return data;
    }

    private static Workbook getWorkbook(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            return null;
        }
        String fileName = getString(multipartFile.getOriginalFilename()).toLowerCase();
        InputStream in = multipartFile.getInputStream();
        Workbook wb;
        if (fileName.endsWith(XLSX)) {
            wb = new XSSFWorkbook(in);
        } else if (fileName.endsWith(XLS)) {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
            wb = new HSSFWorkbook(poifsFileSystem);
        } else {
            return null;
        }
        return wb;
    }

    static String getCellValue(Cell cell) {
        if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
            return "";
        }
        if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue() + "";
        }
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            SimpleDateFormat sdf = null;
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                if (cell.getCellStyle().getDataFormat() == 14) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                } else if (cell.getCellStyle().getDataFormat() == 21) {
                    sdf = new SimpleDateFormat("HH:mm:ss");
                } else if (cell.getCellStyle().getDataFormat() == 22) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    throw new RuntimeException("日期格式错误!!!");
                }
                Date date = cell.getDateCellValue();
                return sdf.format(date);
            } else {
                return NUMBER_FORMAT.format(cell.getNumericCellValue()) + "";
            }
        }
        if (cell.getCellTypeEnum() == CellType.STRING) {
            String val = cell.getStringCellValue();
            if (val == null || val.trim().length() == 0) {
                return "";
            }
            return val.trim();
        }
        return cell.getCellFormula();
    }

    static String getString(String s) {
        if (null == s) {
            return "";
        }
        if (s.isEmpty()) {
            return s;
        }
        return s.trim();
    }
}
