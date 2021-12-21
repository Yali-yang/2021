package com.xunce.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
public class ExcelExportUtils {
    private final XSSFWorkbook workbook;

    private String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    public ExcelExportUtils() {
        // 声明一个工作薄
        workbook = new XSSFWorkbook();
    }

    public XSSFWorkbook getWorkbook(){
        return workbook;
    }

    /**
     * 创建多个sheet
     * 一个List<List<Object>>是代表一个sheet
     * 一个List<Object>是代表一个sheet中的数据，List<Object>中的第一个数据是标题
     * @param filename
     * @param title
     * @param exportDataList
     * @param response
     */
    public void exportMultiSheet(String filename, String title, List<List<List<Object>>> exportDataList,
                                 HttpServletResponse response) {
        OutputStream out = null;

        try {
            out = response.getOutputStream();
            response.reset();
            /*response.setHeader("Content-Disposition", "attachment;filename=\""
                    + new String(filename.getBytes(StandardCharsets.UTF_8), "iso8859-1")
                    + "\"");
            response.setContentType("application/octet-stream; charset=utf-8");*/

            response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(System.currentTimeMillis() + ".xlsx", "utf-8"));
            createMultiSheet(title, exportDataList, null, null);

            workbook.write(out);

        } catch (Exception e) {
            log.error("export data failed.", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    log.error("close stream error.", e);
                }
            }

        }
    }

    public void createMultiSheet(String title, List<List<List<Object>>> exportDataList,
                                 HSSFCellStyle headCellStyle, HSSFCellStyle contentCellStyle) {

        for (int i = 0; i < exportDataList.size(); i++) {

            List<List<Object>> sheetData = exportDataList.get(i);

            // 生成一个表格
            Sheet sheet = workbook.createSheet();
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth(15);
//            sheet.setDefaultColumnWidth((short) 15);

            if (null == headCellStyle) {
//                headCellStyle = getDefaultHeadCellStyle();
            }

            if (null == contentCellStyle) {
//                contentCellStyle = getDefaultContentCellStyle();
            }

            // 产生表格标题行
            Row row = sheet.createRow(0);

            List<Object> titleList = sheetData.get(0);
            for (int j = 0; j < titleList.size(); j++) {
                // 添加表头
                Cell cell = row.createCell(j);
                cell.setCellStyle(headCellStyle);
                XSSFRichTextString text = new XSSFRichTextString(titleList.get(j).toString());
                cell.setCellValue(text);
            }

            for (int j = 1; j < sheetData.size(); j++) {
                try {
                    row = sheet.createRow(j);
                    for (int k = 0; k < sheetData.get(j).size(); k++) {
                        // 添加数据内容
                        Cell cell = row.createCell(k);
                        cell.setCellStyle(contentCellStyle);
                        Object textValue = sheetData.get(j).get(k);
                        if (textValue != null) {
                            XSSFRichTextString richString = new XSSFRichTextString(
                                    textValue.toString());
                            cell.setCellValue(richString);
                        }
                    }
                } catch (Exception e) {
                    log.error("create sheet error.", e);
                }
            }
        }
    }

}
