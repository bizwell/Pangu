package com.joindata.inf.common.util.major;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import com.joindata.inf.common.basic.errors.ParamErrors;
import com.joindata.inf.common.basic.errors.ResourceErrors;
import com.joindata.inf.common.basic.exceptions.InvalidParamException;
import com.joindata.inf.common.basic.exceptions.ResourceException;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.DateUtil;
import com.joindata.inf.common.util.basic.FileUtil;
import com.joindata.inf.common.util.basic.JsonUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.major.excel.cst.ExcelParseMode;
import com.joindata.inf.common.util.major.excel.entities.ExcelExportDataList;
import com.joindata.inf.common.util.major.excel.stereotype.ExcelExportable;
import com.joindata.inf.common.util.major.excel.stereotype.ExcelImportable;
import com.joindata.inf.common.util.major.excel.stereotype.annotations.ExportColumn;
import com.joindata.inf.common.util.major.excel.stereotype.annotations.ExportSheet;
import com.joindata.inf.common.util.major.excel.stereotype.annotations.ImportColumn;
import com.joindata.inf.common.util.major.excel.stereotype.annotations.ImportSheet;

/**
 * Excel 处理工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月10日 下午8:25:09
 */
public class ExcelDocumentUtil
{

    /**
     * 解析 Excel 文件中的数据到目标类型的对象
     * 
     * @param excelFile Excel 文件的路径
     * @param targetClass 所要生成对象的 Class
     * @return 解析出来的对象列表
     * @throws InvalidParamException 如果文件不是 Excel 文件、目标对象不包含解析所需的参数设置，将抛该异常
     * @throws ResourceException 文件不存在，或读取 Excel 数据或解析时发生错误，抛出该异常
     * @throws RuntimeException 如果不能创建目标对象或写入值，将抛异常
     */
    public static <T extends ExcelImportable> List<T> parseExcel(String excelFile, Class<T> targetClass)
    {
        if(!FileUtil.isExists(excelFile))
        {
            throw new ResourceException(ResourceErrors.NOT_FOUND, excelFile + " 文件不存在！");
        }
        try
        {
            InputStream is = new FileInputStream(new File(excelFile));
            return parseExcel(is, targetClass, FileUtil.getName(excelFile));
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析Excel中的数据到目标类型的对象
     * 
     * @param excel Excel文件的MultipartFile（从HTTP客户端传来的文件）
     * @param targetClass 所要生成对象的Class
     * @return 解析出来的对象列表
     * @throws InvalidParamException 如果文件不是Excel文件、目标对象不包含解析所需的参数设置，将抛该异常
     * @throws ResourceException 读取 Excel 数据或解析时发生错误，抛出该异常
     * @throws RuntimeException 如果不能创建目标对象或写入值，将抛异常
     */
    @SuppressWarnings("resource")
    public static <T extends ExcelImportable> List<T> parseExcel(InputStream excel, Class<T> targetClass, String fileName) throws InvalidParamException, ResourceException, RuntimeException
    {
        if(!StringUtil.endsWithIgnoreCase(fileName, ExcelParseMode.XLS, ExcelParseMode.XLSX))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FILE_TYPE_ERROR, "文件类型不正确，请提供正确的 Excel 文件！");
        }

        List<T> objectList = new ArrayList<T>();
        ImportSheet sheetParams = targetClass.getAnnotation(ImportSheet.class);
        if(sheetParams == null)
        {
            throw new InvalidParamException(ParamErrors.INVALID_PARAM_CONFIG_ERROR, "未指定工作簿名称，请检查代码！");
        }
        // 获取对象注解中设置的最大行列范围，以免遍历过多浪费资源
        int minRow = sheetParams.startRow();
        int maxRow = sheetParams.endRow();
        int minColumn = 1;
        int maxColumn = 1;

        // 创建一个Map来存储行数和Field的对应关系
        Map<Integer, Field> fieldMap = new HashMap<Integer, Field>();

        Field[] flds = targetClass.getDeclaredFields();
        for(Field fld: flds)
        {
            ImportColumn columnIndex = fld.getAnnotation(ImportColumn.class);
            if(columnIndex == null)
            {
                continue;
            }
            // 设置最小列和最大列
            minColumn = columnIndex.value() < minColumn ? columnIndex.value() : minColumn;
            maxColumn = columnIndex.value() > maxColumn ? columnIndex.value() : maxColumn;
            // 减1，因为注解设置的是从1开始，而解析时从0开始
            fieldMap.put(columnIndex.value() - 1, fld);
        }

        Field[] supFields = targetClass.getSuperclass().getDeclaredFields();
        for(Field fld: supFields)
        {
            ImportColumn columnIndex = fld.getAnnotation(ImportColumn.class);
            if(columnIndex == null)
            {
                continue;
            }
            // 设置最小列和最大列
            minColumn = columnIndex.value() < minColumn ? columnIndex.value() : minColumn;
            maxColumn = columnIndex.value() > maxColumn ? columnIndex.value() : maxColumn;
            // 减1，因为注解设置的是从1开始，而解析时从0开始
            fieldMap.put(columnIndex.value() - 1, fld);
        }

        // 创建对Excel工作簿文件的引用
        Workbook workbook;
        try
        {
            if(StringUtil.endsWithIgnoreCase(fileName, ExcelParseMode.XLS))
            {
                workbook = new HSSFWorkbook(excel);
            }
            else
            {
                workbook = new XSSFWorkbook(excel);
            }

        }
        catch(IOException e)
        {
            throw new ResourceException(ResourceErrors.UNREADABLE, e.getMessage());
        }

        // 获取工作表
        Sheet sheet = workbook.getSheet(sheetParams.sheetName());

        // 如果读取不到表格，取当前激活的工作表
        if(sheet == null)
        {
            sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        }

        if(sheet == null)
        {
            throw new ResourceException(ResourceErrors.NOT_FOUND, "该工作表中没有任何工作表，无法导入");
        }

        // 如果最小行大于实际行数，则最小行使用实际行数
        // minRow = sheet.getLastRowNum() + 1 < minRow ? sheet.getLastRowNum() + 1 : minRow;
        // 如果最大行大于实际行数或小于0（小于0表示使用实际行数），则最大行使用实际行数
        maxRow = sheet.getLastRowNum() + 1 < maxRow || maxRow < 0 ? sheet.getLastRowNum() + 1 : maxRow;

        // 遍历行
        for(int i = minRow - 1; i < maxRow; i++)
        {
            // 读取行
            Row row = sheet.getRow(i);

            if(row == null)
            {
                // 遇到空行，如果声明以空行结束，就这么做，否则只跳过本次循环
                if(sheetParams.breakByEmptyRow())
                {
                    break;
                }
                continue;
            }

            // 如果声明以空行结束，就这么做（与上面判断null不同的是，这里可能遇到合并的单元格，而合并的单元格不是null，所以需要逐单元格判断）
            if(sheetParams.breakByEmptyRow())
            {
                boolean isEmpty = true;
                for(int ii = 0; ii < row.getLastCellNum(); ii++)
                {
                    // 获取单元格
                    Cell cell = row.getCell(ii);

                    if(cell == null)
                    {
                        continue;
                    }

                    // 统统设为String
                    cell.setCellType(CellType.STRING);

                    // 判断是否空单元格
                    if(cell.getStringCellValue() != null && !"".equals(StringUtil.trimAllWhitespace((cell.getStringCellValue()))))
                    {
                        isEmpty = false;
                        break;
                    }
                }

                if(isEmpty)
                {
                    break;
                }
            }

            // 创建新对象
            T obj;
            try
            {
                obj = targetClass.newInstance();
                objectList.add(obj);
            }
            catch(Exception e)
            {
                throw new RuntimeException("创建对象时发生异常，" + e.getMessage());
            }

            // 遍历列
            for(int j = minColumn - 1; j < maxColumn; j++)
            {
                // 获取单元格
                Cell cell = row.getCell(j);

                // 如果是空的格子，跳过不处理
                if(cell == null)
                {
                    continue;
                }

                String cellValue = null;

                // 如果是时间型，当做时间处理
                if(org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell))
                {
                    cellValue = DateUtil.formatDate(cell.getDateCellValue());
                }
                // 否则统统设为String
                else
                {
                    cell.setCellType(CellType.STRING);
                    cellValue = cell.getStringCellValue();
                }

                // 读取单元格数据并设置到新对象的对应属性中
                if(fieldMap.containsKey(cell.getColumnIndex()))
                {
                    Field fld = fieldMap.get(cell.getColumnIndex());
                    fld.setAccessible(true);
                    try
                    {
                        fld.set(obj, cellValue);
                    }
                    catch(Exception e)
                    {
                        throw new RuntimeException("设置对象属性值时发生错误，" + e.getMessage());
                    }
                }
            }
        }

        return objectList;
    }

    /**
     * 解析 CSV 中的数据到目标类型的对象
     * 
     * @param inputCsv CSV 文件的 MultipartFile（从HTTP客户端传来的文件）
     * @param targetClass 所要生成对象的Class
     * @param fileName 这个 CSV 的文件名，用于判断是否 CSV 文件
     * @return 解析出来的对象列表
     * @throws InvalidParamException 如果文件不是 Csv 文件、目标对象不包含解析所需的参数设置，将抛该异常
     * @throws ResourceException 读取 Csv 数据或解析时发生错误，抛出该异常
     * @throws RuntimeException 如果不能创建目标对象或写入值，将抛异常
     */
    public static <T extends ExcelImportable> List<T> parseCsv(InputStream inputCsv, Class<T> targetClass, String fileName) throws InvalidParamException, ResourceException, RuntimeException
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(inputCsv, "GBK");
            BufferedReader sr = new BufferedReader(isr);
            return parseCsv(sr, targetClass, fileName);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析 CSV 文件中的数据到目标类型的对象
     * 
     * @param csvFile CSV 文件的路径
     * @param targetClass 所要生成对象的 Class
     * @return 解析出来的对象列表
     * @throws InvalidParamException 如果文件不是 CSV 文件、目标对象不包含解析所需的参数设置，将抛该异常
     * @throws ResourceException 文件不存在，或读取 CSV 数据或解析时发生错误，抛出该异常
     * @throws RuntimeException 如果不能创建目标对象或写入值，将抛异常
     */
    public static <T extends ExcelImportable> List<T> parseCsv(String csvFile, Class<T> targetClass) throws InvalidParamException, ResourceException, RuntimeException
    {
        if(!FileUtil.isExists(csvFile))
        {
            throw new ResourceException(ResourceErrors.NOT_FOUND, csvFile + " 文件不存在！");
        }
        try
        {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(csvFile), "GBK");
            BufferedReader sr = new BufferedReader(isr);
            return parseCsv(sr, targetClass, FileUtil.getName(csvFile));
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析 CSV 中的数据到目标类型的对象
     * 
     * @param Csv Csv文件的MultipartFile（从HTTP客户端传来的文件）
     * @param targetClass 所要生成对象的Class
     * @param fileName 这个 CSV 的文件名，用于判断是否 CSV 文件
     * @return 解析出来的对象列表
     * @throws InvalidParamException 如果文件不是Csv文件、目标对象不包含解析所需的参数设置，将抛该异常
     * @throws ResourceException 读取 Csv 数据或解析时发生错误，抛出该异常
     * @throws RuntimeException 如果不能创建目标对象或写入值，将抛异常
     */
    @SuppressWarnings("resource")
    public static <T extends ExcelImportable> List<T> parseCsv(Reader inputCsv, Class<T> targetClass, String fileName) throws InvalidParamException, ResourceException, RuntimeException
    {
        if(!StringUtils.endsWithIgnoreCase(fileName, ExcelParseMode.CSV))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FILE_TYPE_ERROR, "文件类型不正确，请提供正确的 CSV 文件！");
        }

        List<T> objectList = new ArrayList<T>();
        ImportSheet sheetParams = targetClass.getAnnotation(ImportSheet.class);
        if(sheetParams == null)
        {
            throw new InvalidParamException(ParamErrors.INVALID_PARAM_CONFIG_ERROR, "未指定工作簿名称，请检查代码！");
        }
        // 获取对象注解中设置的最大行列范围，以免遍历过多浪费资源
        int minRow = sheetParams.startRow();
        int maxRow = sheetParams.endRow();
        int minColumn = 1;
        int maxColumn = 1;

        // 创建一个Map来存储行数和Field的对应关系
        Map<Integer, Field> fieldMap = new HashMap<Integer, Field>();

        Field[] flds = targetClass.getDeclaredFields();
        for(Field fld: flds)
        {
            ImportColumn columnIndex = fld.getAnnotation(ImportColumn.class);
            if(columnIndex == null)
            {
                continue;
            }
            // 设置最小列和最大列
            minColumn = columnIndex.value() < minColumn ? columnIndex.value() : minColumn;
            maxColumn = columnIndex.value() > maxColumn ? columnIndex.value() : maxColumn;
            // 减1，因为注解设置的是从1开始，而解析时从0开始
            fieldMap.put(columnIndex.value() - 1, fld);
        }

        Field[] supFields = targetClass.getSuperclass().getDeclaredFields();
        for(Field fld: supFields)
        {
            ImportColumn columnIndex = fld.getAnnotation(ImportColumn.class);
            if(columnIndex == null)
            {
                continue;
            }
            // 设置最小列和最大列
            minColumn = columnIndex.value() < minColumn ? columnIndex.value() : minColumn;
            maxColumn = columnIndex.value() > maxColumn ? columnIndex.value() : maxColumn;
            // 减1，因为注解设置的是从1开始，而解析时从0开始
            fieldMap.put(columnIndex.value() - 1, fld);
        }

        CSVParser parser;
        try
        {
            parser = new CSVParser(inputCsv, CSVFormat.EXCEL);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        Iterator<CSVRecord> iter = parser.iterator();

        // 遍历行
        while(iter.hasNext())
        {
            // 读取行
            CSVRecord row = iter.next();

            if(row == null)
            {
                // 遇到空行，如果声明以空行结束，就这么做，否则只跳过本次循环
                if(sheetParams.breakByEmptyRow() || parser.getCurrentLineNumber() >= maxRow)
                {
                    break;
                }
                continue;
            }

            if(minRow > 0)
            {
                // 小于最小行数，不读取
                if(row.getRecordNumber() < minRow)
                {
                    continue;
                }
            }

            if(maxRow > 0)
            {
                // 超过指定解析的最大行数，结束
                if(row.getRecordNumber() > maxRow)
                {
                    break;
                }
            }

            // 创建新对象
            T obj;
            try
            {
                obj = targetClass.newInstance();
                objectList.add(obj);
            }
            catch(Exception e)
            {
                throw new RuntimeException("创建对象时发生异常，" + e.getMessage());
            }

            if(maxColumn > 0)
            {

            }

            int itemMinColumn = minColumn < 0 ? 1 : minColumn;
            int itemMaxColumn = maxColumn < 0 ? row.size() : maxColumn;

            // 遍历列
            for(int j = itemMinColumn - 1; j < itemMaxColumn; j++)
            {
                // 获取单元格
                String cell = row.get(j);

                // 如果是空的格子，跳过不处理
                if(cell == null)
                {
                    continue;
                }

                // 读取单元格数据并设置到新对象的对应属性中
                if(fieldMap.containsKey(j))
                {
                    Field fld = fieldMap.get(j);
                    fld.setAccessible(true);
                    try
                    {
                        fld.set(obj, cell);
                    }
                    catch(Exception e)
                    {
                        throw new RuntimeException("设置对象属性值时发生错误，" + e.getMessage());
                    }
                }
            }
        }

        return objectList;
    }

    /**
     * 导出数据表到文件
     * 
     * @param data 要导出的数据对象列表
     * @param savePath 导出文件路径
     * @return 导出后的文件路径
     * @throws RuntimeException 任何未知错误，均抛出该异常
     * @throws ResourceException 文件无法访问，或无权限写入，抛出该异常
     */
    public static String exportExcel(ExcelExportDataList data, String savePath) throws RuntimeException, ResourceException
    {
        /**
         * 写入到文件
         */
        OutputStream out;
        try
        {
            out = new FileOutputStream(new File(savePath));
        }
        catch(FileNotFoundException e)
        {
            throw new ResourceException(ResourceErrors.CANNOT_ACCESS, e.toString());
        }

        exportExcel(data, out);

        return savePath;
    }

    /**
     * 导出数据表
     * 
     * @param data 要导出的数据对象列表
     * @param out 数据输出的流
     * @throws RuntimeException 任何未知错误，均抛出该异常
     * @throws ResourceException 输出时发生 IO 错误，抛出该异常
     */
    @SuppressWarnings("resource")
    public static void exportExcel(ExcelExportDataList data, OutputStream out) throws RuntimeException, ResourceException
    {
        HSSFWorkbook wb = new HSSFWorkbook();

        // 定义颜色
        HSSFPalette palette = wb.getCustomPalette();
        palette.setColorAtIndex(HSSFColor.GREY_40_PERCENT.index, (byte)230, (byte)230, (byte)230);
        palette.setColorAtIndex(HSSFColor.BLUE_GREY.index, (byte)48, (byte)84, (byte)150);
        palette.setColorAtIndex(HSSFColor.BLUE.index, (byte)91, (byte)155, (byte)213);
        palette.setColorAtIndex(HSSFColor.DARK_TEAL.index, (byte)31, (byte)78, (byte)120);
        palette.setColorAtIndex(HSSFColor.LIGHT_YELLOW.index, (byte)255, (byte)242, (byte)204);
        palette.setColorAtIndex(HSSFColor.GREY_80_PERCENT.index, (byte)58, (byte)56, (byte)56);
        palette.setColorAtIndex(HSSFColor.ORANGE.index, (byte)198, (byte)89, (byte)17);
        palette.setColorAtIndex(HSSFColor.LIGHT_ORANGE.index, (byte)252, (byte)228, (byte)214);
        palette.setColorAtIndex(HSSFColor.GREEN.index, (byte)84, (byte)130, (byte)53);
        palette.setColorAtIndex(HSSFColor.LIGHT_GREEN.index, (byte)226, (byte)239, (byte)218);

        // 定义表格标题单元格样式
        HSSFCellStyle captionStyle = wb.createCellStyle();
        {
            HSSFFont font = wb.createFont();
            font.setFontName("微软雅黑");
            font.setBold(true);
            font.setFontHeight((short)300);
            font.setColor(palette.getColor(HSSFColor.BLUE_GREY.index).getIndex());
            captionStyle.setFont(font);
            captionStyle.setAlignment(HorizontalAlignment.CENTER);
            captionStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            captionStyle.setFillForegroundColor(palette.getColor(HSSFColor.WHITE.index).getIndex());
            captionStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        // 表头最左侧单元格样式
        HSSFCellStyle firstCellStyle = wb.createCellStyle();
        {
            HSSFFont font = wb.createFont();
            font.setFontName("微软雅黑");
            font.setBold(true);
            font.setFontHeight((short)200);
            font.setColor(HSSFColor.WHITE.index);
            firstCellStyle.setFont(font);
            firstCellStyle.setAlignment(HorizontalAlignment.CENTER);
            firstCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            firstCellStyle.setFillForegroundColor(palette.getColor(HSSFColor.DARK_TEAL.index).getIndex());
            firstCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        // 表头单元格样式
        HSSFCellStyle headerStyle = wb.createCellStyle();
        {
            HSSFFont font = wb.createFont();
            font.setFontName("微软雅黑");
            font.setColor(HSSFColor.WHITE.index);
            headerStyle.setFont(font);
            headerStyle.setBorderBottom(BorderStyle.THIN);// 下边框
            headerStyle.setBorderLeft(BorderStyle.THIN);// 左边框
            headerStyle.setBorderRight(BorderStyle.THIN);// 右边框
            headerStyle.setBorderTop(BorderStyle.THIN);// 上边框
            headerStyle.setTopBorderColor(palette.getColor(HSSFColor.BLUE.index).getIndex());
            headerStyle.setRightBorderColor(palette.getColor(HSSFColor.BLUE.index).getIndex());
            headerStyle.setBottomBorderColor(palette.getColor(HSSFColor.BLUE.index).getIndex());
            headerStyle.setLeftBorderColor(palette.getColor(HSSFColor.BLUE.index).getIndex());
            headerStyle.setAlignment(HorizontalAlignment.LEFT);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(palette.getColor(HSSFColor.BLUE.index).getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        // 数据单元格样式
        HSSFCellStyle dataStyle = wb.createCellStyle();
        {
            HSSFFont font = wb.createFont();
            font.setFontName("微软雅黑");
            font.setColor(HSSFColor.GREY_80_PERCENT.index);
            dataStyle.setFont(font);
            dataStyle.setBorderBottom(BorderStyle.THIN);// 下边框
            dataStyle.setBorderLeft(BorderStyle.THIN);// 左边框
            dataStyle.setBorderRight(BorderStyle.THIN);// 右边框
            dataStyle.setBorderTop(BorderStyle.THIN);// 上边框
            dataStyle.setTopBorderColor(palette.getColor(HSSFColor.BLUE.index).getIndex());
            dataStyle.setRightBorderColor(palette.getColor(HSSFColor.BLUE.index).getIndex());
            dataStyle.setBottomBorderColor(palette.getColor(HSSFColor.BLUE.index).getIndex());
            dataStyle.setLeftBorderColor(palette.getColor(HSSFColor.BLUE.index).getIndex());
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setFillForegroundColor(palette.getColor(HSSFColor.WHITE.index).getIndex());
            dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        // 创建后的工作表放在这里
        Map<String, HSSFSheet> sheetMap = new HashMap<String, HSSFSheet>();

        for(ExcelExportable item: data)
        {
            // 对象属性和注解的 Map
            Map<Field, ExportColumn> fldAnnoMap = ClassUtil.getFieldAnnotationMap(item.getClass(), ExportColumn.class);

            // 取到记录对象中的注解配置
            ExportSheet sheetConfig = item.getClass().getAnnotation(ExportSheet.class);

            // 本条记录要操作的工作表
            HSSFSheet sheet = sheetMap.get(sheetConfig.value() + sheetConfig.sheetName());

            // 如果还没有这个工作表，那就创建
            if(sheet == null)
            {
                // 创建工作表
                sheet = wb.createSheet(sheetConfig.value() + sheetConfig.sheetName());

                // 新工作表注册到已创建目录中
                sheetMap.put(sheetConfig.value() + sheetConfig.sheetName(), sheet);

                /*
                 * 创建标题
                 */
                {
                    HSSFRow row = sheet.createRow(0);
                    row.setHeight((short)500);

                    HSSFCell cell = row.createCell(0);
                    // 合并单元格(startRow，endRow，startColumn，endColumn)
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fldAnnoMap.size()));

                    // 给标题单元格设置样式和赋值
                    cell.setCellStyle(captionStyle);
                    cell.setCellValue(sheetConfig.caption());
                }

                /*
                 * 创建表头
                 */
                {
                    HSSFRow row = sheet.createRow(1);

                    // 最左侧单元格
                    HSSFCell firstCell = row.createCell(0);
                    firstCell.setCellValue("#");
                    firstCell.setCellStyle(firstCellStyle);

                    int cellIndex = 1;
                    Iterator<Field> fldIter = CollectionUtil.iteratorMapKey(fldAnnoMap);
                    while(fldIter.hasNext())
                    {
                        ExportColumn columnConfig = fldAnnoMap.get(fldIter.next());

                        HSSFCell cell = row.createCell(cellIndex);
                        cell.setCellStyle(headerStyle);
                        cell.setCellValue(columnConfig.title());
                        sheet.setColumnWidth(cellIndex, columnConfig.cellWidth());

                        cellIndex++;
                    }
                }
            }

            // 写到最后一行
            int rowIndex = sheet.getLastRowNum() + 1;

            /*
             * 填充数据
             */
            {
                HSSFRow row = sheet.createRow(rowIndex);

                // 序号
                HSSFCell firstCell = row.createCell(0);
                firstCell.setCellValue(rowIndex - 1);
                firstCell.setCellStyle(firstCellStyle);

                int cellIndex = 1;
                Iterator<Field> fldIter = CollectionUtil.iteratorMapKey(fldAnnoMap);
                while(fldIter.hasNext())
                {
                    HSSFCell cell = row.createCell(cellIndex);
                    cell.setCellStyle(dataStyle);
                    try
                    {
                        cell.setCellValue(String.valueOf(fldIter.next().get(item)));
                    }
                    catch(IllegalArgumentException | IllegalAccessException e)
                    {
                        throw new RuntimeException("获取字段值时发生错误，" + e.toString());
                    }

                    cellIndex++;
                }
            }
        }

        try
        {
            wb.write(out);
            out.close();
            wb.close();
        }
        catch(IOException e)
        {
            throw new ResourceException(ResourceErrors.CANNOT_WRITE, "输出数据错误：" + e.toString());
        }
    }

    public static void main(String[] args) throws InvalidParamException, ResourceException, RuntimeException
    {
        @ImportSheet
        @SuppressWarnings("unused")
        class Dog implements ExcelImportable
        {
            @ImportColumn(1)
            private String name;

            @ImportColumn(2)
            private String gender;

            @ImportColumn(3)
            private String breed;

            @ImportColumn(4)
            private String age;

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getGender()
            {
                return gender;
            }

            public void setGender(String gender)
            {
                this.gender = gender;
            }

            public String getAge()
            {
                return age;
            }

            public void setAge(String age)
            {
                this.age = age;
            }

            public String getBreed()
            {
                return breed;
            }

            public void setBreed(String breed)
            {
                this.breed = breed;
            }
        }

        // List<Dog> dogs = parseExcel("/temp/test/excel/dog.xlsx", Dog.class);
        // System.out.println(JsonUtil.toJSON(dogs));

        @ImportSheet(sheetName = "NationData", startRow = 3)
        @ExportSheet(sheetName = "NationData", caption = "国家列表数据")
        @SuppressWarnings("unused")
        class Nation implements ExcelExportable, ExcelImportable
        {
            @ExportColumn(title = "国家", cellWidth = 3000)
            @ImportColumn(2)
            private String name;

            @ExportColumn(title = "缩写", cellWidth = 2000)
            @ImportColumn(3)
            private String shortName;

            @ExportColumn(title = "国旗", cellWidth = 7000)
            @ImportColumn(4)
            private String flag;

            @ExportColumn(title = "货币代码", cellWidth = 4000)
            @ImportColumn(5)
            private String moneyName;

            public Nation()
            {
            }

            public Nation(String name, String shortName, String flag, String moneyName)
            {
                this.name = name;
                this.shortName = shortName;
                this.flag = flag;
                this.moneyName = moneyName;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getShortName()
            {
                return shortName;
            }

            public void setShortName(String shortName)
            {
                this.shortName = shortName;
            }

            public String getFlag()
            {
                return flag;
            }

            public void setFlag(String flag)
            {
                this.flag = flag;
            }

            public String getMoneyName()
            {
                return moneyName;
            }

            public void setMoneyName(String moneyName)
            {
                this.moneyName = moneyName;
            }
        }

        @ImportSheet(sheetName = "Citizen Z Statistics", startRow = 3)
        @ExportSheet(value = "Citizen Z Statistics", caption = "丧尸国度数据")
        @SuppressWarnings("unused")
        class StateZ implements ExcelExportable
        {
            @ExportColumn(title = "州", cellWidth = 4000)
            @ImportColumn(2)

            private String name;

            @ExportColumn(title = "英文", cellWidth = 4500)
            @ImportColumn(3)

            private String engName;

            @ExportColumn(title = "僵尸类型", cellWidth = 7000)
            @ImportColumn(4)

            private String typeZ;

            @ExportColumn(title = "僵尸数量", cellWidth = 4000)
            @ImportColumn(5)

            private int count;

            public StateZ()
            {
            }

            public StateZ(String name, String engName, String typeZ, int count)
            {
                this.name = name;
                this.engName = engName;
                this.typeZ = typeZ;
                this.count = count;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getEngName()
            {
                return engName;
            }

            public void setEngName(String engName)
            {
                this.engName = engName;
            }

            public String getTypeZ()
            {
                return typeZ;
            }

            public void setTypeZ(String typeZ)
            {
                this.typeZ = typeZ;
            }

            public int getCount()
            {
                return count;
            }

            public void setCount(int count)
            {
                this.count = count;
            }

        }

        ExcelExportDataList dataList = new ExcelExportDataList();
        dataList.add(new Nation("中国", "CN", "Red Five Star Flag", "CNY"));
        dataList.add(new Nation("美国", "US", "Stars and Stripes", "USD"));
        dataList.add(new Nation("日本", "JP", "Red Sun Flag", "JPY"));
        dataList.add(new Nation("英国", "EN", "the Union Flag", "GBP"));
        dataList.add(new StateZ("普通行尸", "-Regular-", "walker", 414748328));
        dataList.add(new StateZ("密歇根", "Michigan", "Voodoo esque", 32142));
        dataList.add(new StateZ("爱达荷", "Idaho", "Mutant", 52214));
        dataList.add(new StateZ("加利福尼亚", "California", "Super soldier/anti-zombie", 5312));

        System.out.println(JsonUtil.toJSON(parseCsv("/temp/test/csv/nation.CSV", Nation.class)));

        // XXX 不执行后面的
        if(System.currentTimeMillis() > 0)
        {
            return;
        }

        System.out.println(exportExcel(dataList, "/temp/test/excel/export-nation.xls"));

        System.out.println(JsonUtil.toJSON(parseExcel("/temp/test/excel/export-nation.xls", Nation.class)));
        // TODO 后续要解决字段类型的问题
        // System.out.println(JsonUtil.toJSON(parseExcel("/temp/test/excel/export-nation.xls", StateZ.class)));

    }
}