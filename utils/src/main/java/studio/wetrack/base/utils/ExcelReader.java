package studio.wetrack.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by zhanghong on 16/12/28.
 */
public class ExcelReader {

    private static Logger logger = LoggerFactory.getLogger(ExcelReader.class);

    private XSSFWorkbook wb;
    private DateFormat dateFormat = DateFormat.getDateInstance();

    public ExcelReader(InputStream inputStream) throws IOException {
        wb = new XSSFWorkbook(inputStream);
    }

    public ExcelReader(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public int numberOfSheets(){
        return wb.getNumberOfSheets();
    }

    public void setDateFormat(DateFormat dateFormat){
        this.dateFormat = dateFormat;
    }

    List<String> firstRowValuesAtSheet(int sheetIndex){
        List list = new ArrayList();
        Sheet sheet = wb.getSheetAt(sheetIndex);
        if(sheet == null){
            return null;
        }
        Row row = sheet.getRow(sheet.getFirstRowNum());
        if(row == null){
            return null;
        }
        short minColIx = row.getFirstCellNum();
        short maxColIx = row.getLastCellNum();
        for(short colIx=minColIx; colIx < maxColIx; colIx++) {
            Cell cell = row.getCell(colIx);
            if (cell == null) {
                continue;
            }
            list.add(getCellFormatValue(cell).trim());
        }
        return list;
    }

    /**
     * 把表中数据每行转换成一个map，以第一行为标题
     * @param fieldToHeadMap
     * @return
     */
    public List<Map<String, String>> toListAsMapWithFirstRowAsHeader(Map<String, String> fieldToHeadMap){
        List list = new ArrayList<>();
        for(int i = 0; i < numberOfSheets(); i++){
            list.addAll(toListAsMapWithFirstRowAsHeaderAtSheet(i, fieldToHeadMap));
        }

        return list;
    }

    public List<Map<String, String>> toListAsMapWithFirstRowAsHeaderAtSheet(int sheetIndex, Map<String, String> fieldToHeadMap){
        List list = new ArrayList();

        List<String> headerValues = firstRowValuesAtSheet(sheetIndex);

        Sheet sheet = wb.getSheetAt(sheetIndex);
        int firstRowNum = sheet.getFirstRowNum() + 1;
        int lastRowNum = sheet.getLastRowNum();
        for(int i = firstRowNum; i <= lastRowNum; i++){
            Row row = sheet.getRow(i);
            List<String> values = new ArrayList<>();

            short minColIx = row.getFirstCellNum();
            short maxColIx = row.getLastCellNum();
            for(short colIx=minColIx; colIx < maxColIx; colIx++) {
                Cell cell = row.getCell(colIx);
                if (cell == null) {
                    values.add(null);
                    continue;
                }
                String value = getCellFormatValue(cell).trim();
                values.add(value);
            }

            list.add(rowToMap(headerValues, values, fieldToHeadMap));
        }

        return list;
    }

    private Map<String, String> rowToMap(List<String> headerNames, List<String> values, Map<String, String> fieldToHeadMap){
        Map<String, String> obj = new HashMap<>();
        for(int i = 0; i < values.size(); i++){
            if(!StringUtils.isEmpty(values.get(i)) && i < headerNames.size()) {
                String key = headerNames.get(i);
                if (fieldToHeadMap != null && fieldToHeadMap.get(key) != null) {
                    key = fieldToHeadMap.get(key);
                }
                if(!StringUtils.isEmpty(key)) {
                    obj.put(key, values.get(i));
                }
            }

        }
        return obj;

    }

    /**
     * 把表中数据每行转换成一个类型为 type的，以第一行为标题
     * @param type
     * @param fieldToHeadMap
     * @return
     */
    public List toListAsTypeWithFirstRowAsHeader(Class type, Map<String, String> fieldToHeadMap){
        List list = new ArrayList();
        for(int i = 0; i < numberOfSheets(); i++){
            list.addAll(toListAsTypeWithFirstRowAsHeaderAtSheet(i, type, fieldToHeadMap));
        }

        return list;
    }

    public List toListAsTypeWithFirstRowAsHeaderAtSheet(int sheetIndex, Class type, Map<String, String> fieldToHeadMap){
        List list = new ArrayList();

        List<String> headerValues = firstRowValuesAtSheet(sheetIndex);

        Sheet sheet = wb.getSheetAt(sheetIndex);
        int firstRowNum = sheet.getFirstRowNum() + 1;
        int lastRowNum = sheet.getLastRowNum();
        for(int i = firstRowNum; i <= lastRowNum; i++){
            Row row = sheet.getRow(i);
            List<String> values = new ArrayList<>();

            short minColIx = row.getFirstCellNum();
            short maxColIx = row.getLastCellNum();
            for(short colIx=minColIx; colIx < maxColIx; colIx++) {
                Cell cell = row.getCell(colIx);
                if (cell == null) {
                    values.add(null);
                    continue;
                }
                String value = getCellFormatValue(cell).trim();
                values.add(value);
            }

            list.add(rowToObject(type, headerValues, values, fieldToHeadMap));
        }

        return list;
    }

    private Object rowToObject(Class type, List<String> headerNames, List<String> values, Map<String, String> fieldToHeadMap){
        try {
            Object obj = type.newInstance();
            for(int i = 0; i < values.size(); i++){
                if(values.get(i) != null) {
                    String fieldName = headerNames.get(i);
                    if (fieldToHeadMap != null && fieldToHeadMap.get(fieldName) != null) {
                        fieldName = fieldToHeadMap.get(fieldName);
                    }
                    if(!org.apache.commons.lang3.StringUtils.isEmpty(fieldName)) {
                        Field field = null;
                        try {
                            field = type.getDeclaredField(fieldName);
                            setFieldValue(field, values.get(i), obj);
                        }catch (NoSuchFieldException e) {
                                logger.warn("type {} field is not available: {}", type.getName(), e.getMessage());
                                e.printStackTrace();
                        }
                    }
                }

            }
            return obj;

        } catch (InstantiationException e) {
            logger.error("type {} might has no default constructor, failed to construct", type.getName());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error("type {} default constructor is not accessible, failed to construct", type.getName());
            e.printStackTrace();
        }
        return null;
    }

    private void setFieldValue(Field field, String value, Object obj){
        boolean access = field.isAccessible();
        field.setAccessible(true);
        try {
            if (field != null) {
                if (TypeUtil.isBoolean(field.getType())) {
                    field.setBoolean(obj, Boolean.parseBoolean(value));
                }else if(TypeUtil.isInt(field.getType())){
                    Double d = Double.parseDouble(value);
                    field.set(obj, Integer.valueOf(d.intValue()));
                }else if(TypeUtil.isLong(field.getType())){
                    Double d = Double.parseDouble(value);
                    field.set(obj, Long.valueOf(d.longValue()));
                }else if(TypeUtil.isDouble(field.getType())){
                    field.set(obj, Double.valueOf(Double.parseDouble(value)));
                }else if(TypeUtil.isFloat(field.getType())){
                    field.set(obj, Float.valueOf(Float.parseFloat(value)));
                }else if(TypeUtil.isString(field.getType())){
                    field.set(obj, value);
                }else if(TypeUtil.isDate(field.getType())){
                    field.set(obj, dateFormat.parse(value));
                }
            }
        } catch (ParseException e) {
            logger.error("type {} field {} parse error of value {}", obj.getClass().getName(), field.getName(), value);
            e.printStackTrace();
        } catch (Exception e){
            logger.error("type {} field {} set value {} error, {}", obj.getClass().getName(), field.getName(), value, e.getMessage());
        } finally {
            field.setAccessible(access);
        }
    }


    private String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case Cell.CELL_TYPE_NUMERIC:
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式

                        //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                        //cellvalue = cell.getDateCellValue().toLocaleString();

                        //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);

                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRING
                case Cell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }


    public static void main(String[] args) throws IOException, NoSuchFieldException, ParseException, IllegalAccessException {
        Map<String, String> m = new HashMap<>();
        m.put("分组名称", "group");
        m.put("地址", "modbusId");
        m.put("值名称", "name");
        m.put("位偏移", "bitIndex");
        m.put("等级", "maintenanceLevel");
        m.put("相关地址", "relatedModbusId");
        m.put("值含义", "valueNames");
        m.put("无效值名称", "invalidValueName");
        m.put("最小值", "minValue");
        m.put("最大值", "maxValue");
        m.put("默认值（初始值）", "defaultValue");
        m.put("单位", "unitName");
        m.put("除数因子", "divisor");
        m.put("地址类型", "type");
        m.put("是否生成状态曲线", "showChat");
        m.put("读取方式", "opType");
        m.put("设置前需清零", "clearBeforeSet");
        ExcelReader reader = new ExcelReader(new File("/Users/zhanghong/Desktop/doc/爱空调/远程集控子系统/干式螺杆水冷机组.xlsx"));
//        List<Test> ts =  reader.toListAsTypeWithFirstRowAsHeader(Test.class, m);
        List ts =  reader.toListAsMapWithFirstRowAsHeader(m);
//

//        Test t = new Test();
//        Field f = Test.class.getDeclaredField("modbusId");
//        f.setAccessible(true);
//        f.set(t, Long.valueOf("41991"));
//        f.setAccessible(false);
        DateFormat df = DateFormat.getDateInstance();
        System.out.println("date is " + df.parse("2016-05-10"));
    }


    public static class Test{
        String group;
        long modbusId;
        String name;
    }

}
