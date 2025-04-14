package com.fantasy.util;

import com.linuxense.javadbf.*;

import javax.swing.text.StyleConstants;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DBFUtil {

    public static void main(String[] args) throws IOException {
//        String dbfFilePath = "E:\\dev_project\\WaiBao\\Feeding\\src\\main\\java\\com\\fantasy\\util\\mater.DBF"; // 替换为实际的 DBF 文件路径
        String dbfFilePath = "C:\\Users\\keda\\Desktop\\new1\\MATER.DBF"; // 替换为实际的 DBF 文件路径
        readDbf(dbfFilePath);
        List<LinkedHashMap<String, String>> linkedHashMaps = readDbfToStringList(dbfFilePath);
//        System.out.println(linkedHashMaps);
//        List<LinkedHashMap<String, Object>> linkedHashMaps = readDbfToList(dbfFilePath);
//        for (LinkedHashMap<String, Object> linkedHashMap : linkedHashMaps) {
//            String dono = (String) linkedHashMap.get("DONO");
//            if (dono.equals("000010201")){
//                linkedHashMap.put("OKVOL","1");
//            }
//        }
//        System.out.println(linkedHashMaps);
//        exportdbf(dbfFilePath,linkedHashMaps);
//        List<LinkedHashMap<String, Object>> linkedHashMaps = readDbfToList(dbfFilePath);
//        //新加一行
//        LinkedHashMap<String,Object> newLine = new LinkedHashMap<>();
//        newLine.put("MPDNO","1100");
//        newLine.put("MPDNE","芯线料");
//        newLine.put("MPDNE1","黑色");
//        newLine.put("UNIT","KG");
//        newLine.put("PUCHUNIT","KG");
//        linkedHashMaps.add(newLine);
//        exportdbf("E:\\dev_project\\WaiBao\\Feeding\\src\\main\\java\\com\\fantasy\\util\\create\\mater.DBF",linkedHashMaps);
    }

    /**
     * 读文件并打印
     * @param dbfFilePath
     */
    public static void readDbf(String dbfFilePath) throws FileNotFoundException {
        try (DBFReader reader = new DBFReader(new FileInputStream(dbfFilePath),Charset.forName("GBK"))) {
            for (int i = 0; i < reader.getFieldCount(); i++) {
                //打印表头名称
                DBFField field = reader.getField(i);
                System.out.println(field.getName());
            }
            List<String[]> records = new ArrayList<>();
//            while (reader.nextRecord()!=null) {
//                Object[] recordObjects = reader.nextRecord();
//                String[] recordStrings = convertObjectsToStrings(recordObjects);
//                records.add(recordStrings);
//            }
            Object[] recordObjects;
            while ((recordObjects = reader.nextRecord()) != null) {
                String[] recordStrings = convertObjectsToStrings(recordObjects);
                records.add(recordStrings);
            }

            // 处理或打印读取到的记录...
            for (String[] record : records) {
                System.out.println(Arrays.toString(record));
            }
        } catch (IOException | DBFException e) {
            System.err.println("Error reading DBF file: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * dbf 生成 list
     * @param dbfFilePath
     * @return
     */
    public static List<LinkedHashMap<String,Object>> readDbfToList(String dbfFilePath){
        List<LinkedHashMap<String,Object>> result = new ArrayList<>();
        try (DBFReader reader = new DBFReader(new FileInputStream(dbfFilePath), Charset.forName("GBK"))) {
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < reader.getFieldCount(); i++) {
                //记录表头名称
                DBFField field = reader.getField(i);
                headers.add(field.getName());
//                System.out.println(field.getName());
            }
            Object[] recordObjects;
            while ((recordObjects = reader.nextRecord()) != null) {
                LinkedHashMap<String,Object> line = new LinkedHashMap<>();
                String[] recordStrings = convertObjectsToStrings(recordObjects);
                for (int i = 0; i < recordStrings.length; i++) {
                    line.put(headers.get(i),recordStrings[i]);
                }
                result.add(line);
            }
//            for (HashMap<String, Object> stringObjectHashMap : result) {
//                System.out.println(stringObjectHashMap);
//            }
        } catch (IOException | DBFException e) {
            System.err.println("Error reading DBF file: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * dbf 生成 list
     * @param dbfFilePath
     * @return
     */
    public static List<LinkedHashMap<String,String>> readDbfToStringList(String dbfFilePath){
        List<LinkedHashMap<String,String>> result = new ArrayList<>();
        try (DBFReader reader = new DBFReader(new FileInputStream(dbfFilePath),Charset.forName("GBK"))) {
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < reader.getFieldCount(); i++) {
                //记录表头名称
                DBFField field = reader.getField(i);
                headers.add(field.getName());
//                System.out.println(field.getName());
            }
            Object[] recordObjects;
            while ((recordObjects = reader.nextRecord()) != null) {
                LinkedHashMap<String,String> line = new LinkedHashMap<>();
                String[] recordStrings = convertObjectsToStrings(recordObjects);
                for (int i = 0; i < recordStrings.length; i++) {
                    line.put(headers.get(i),recordStrings[i]);
                }
                result.add(line);
            }
//            for (HashMap<String, Object> stringObjectHashMap : result) {
//                System.out.println(stringObjectHashMap);
//            }
        } catch (IOException | DBFException e) {
            System.err.println("Error reading DBF file: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static List<HashMap<String,String>> readDbfToStringListOptimized(String dbfFilePath) {
        try (DBFReader reader = new DBFReader(new FileInputStream(dbfFilePath), Charset.forName("GBK"))) {
            // 预估结果大小以减少扩容操作，这里假设一个大致的记录数，实际应用中应根据文件大小或具体需求调整
            List<HashMap<String,String>> result = new ArrayList<>(reader.getRecordCount());

            // 使用数组存储表头，优化访问速度
            String[] headers = new String[reader.getFieldCount()];
            for (int i = 0; i < headers.length; i++) {
                headers[i] = reader.getField(i).getName();
            }

            Object[] recordObjects;
            while ((recordObjects = reader.nextRecord()) != null) {
                HashMap<String,String> line = new HashMap<>(headers.length);
                // 直接将对象数组转换为字符串数组，减少转换过程中的临时对象创建
                String[] recordStrings = convertObjectsToStrings(recordObjects);
                for (int i = 0; i < recordStrings.length; i++) {
                    line.put(headers[i], recordStrings[i]);
                }
                result.add(line);
            }
            return result;
        } catch (IOException | DBFException e) {
            System.err.println("Error reading DBF file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static String[] convertObjectsToStrings(Object[] objects) {
        String[] strings = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            strings[i] = objects[i] != null ? objects[i].toString() : "";
        }
        return strings;
    }

    /**
     * list 生成 dbf
     *
     * @param dbfname 文件 名
     * @param listdata	文件源数据
     *
     * @throws IOException
     */
    public static void exportdbf(String dbfname, List<LinkedHashMap<String,Object>> listdata) throws IOException {
        int i2 = 0;
        for(String key : listdata.get(0).keySet()) {
            i2++;
        }
        DBFField fields[] = new DBFField[i2];

        int i = 0;
        for(String key : listdata.get(0).keySet()) {
            fields[i] = new DBFField();
            fields[i].setName(key);
            fields[i].setType(DBFDataType.CHARACTER);
            fields[i].setLength(100);
            i++;
        }

        FileOutputStream fos = new FileOutputStream(dbfname);
        DBFWriter writer = new DBFWriter(fos,Charset.forName("GBK"));
//        System.out.println("编码集"+writer.getCharset());
        writer.setFields(fields);


        for (int j = 0; j < listdata.size(); j++) {
//			HashMap<String,Object> m3 = listdata.get(j);
            Object rowData[] = new Object[i];
            int i1 = 0;
            for(String key : listdata.get(j).keySet()) {
                rowData[i1] = listdata.get(j).get(key);
                i1++;
            }
            writer.addRecord(rowData);
        }

        writer.write(fos);
        fos.close();
        System.out.println("dbf文件生成！");
    }


    /**
     * list 生成 dbf
     *
     * @param dbfname 文件 名
     * @param listdata	文件源数据
     *
     * @throws IOException
     */
    public static void exportdbfString(String dbfname, List<LinkedHashMap<String,String>> listdata) throws IOException {
        int i2 = 0;
        for(String key : listdata.get(0).keySet()) {
            i2++;
        }
        DBFField fields[] = new DBFField[i2];

        int i = 0;
        for(String key : listdata.get(0).keySet()) {
            fields[i] = new DBFField();
            fields[i].setName(key);
            fields[i].setType(DBFDataType.CHARACTER);
            fields[i].setLength(100);
            i++;
        }

        FileOutputStream fos = new FileOutputStream(dbfname);
        DBFWriter writer = new DBFWriter(fos,Charset.forName("GBK"));
//        System.out.println("编码集"+writer.getCharset());
        writer.setFields(fields);


        for (int j = 0; j < listdata.size(); j++) {
//			HashMap<String,Object> m3 = listdata.get(j);
            String rowData[] = new String[i];
            int i1 = 0;
            for(String key : listdata.get(j).keySet()) {
                rowData[i1] = listdata.get(j).get(key);
                i1++;
            }
            writer.addRecord(rowData);
        }

        writer.write(fos);
        fos.close();
        System.out.println("dbf文件生成！");
    }



}
