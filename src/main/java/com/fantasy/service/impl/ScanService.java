package com.fantasy.service.impl;

import com.fantasy.entity.Mater;
import com.fantasy.entity.Order;
import com.fantasy.entity.OrderBK;
import com.fantasy.model.Result.Result;
import com.fantasy.model.vo.MpordeVO;
import com.fantasy.model.vo.MporintpVO;
import com.fantasy.util.DBFSqlUtils;
import com.fantasy.util.DBFUtil;
import com.fantasy.util.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScanService {

    public Result scanOrder(OrderBK orderBK) throws IOException {
        if (orderBK == null || orderBK.getDono().isEmpty()) {
            return Result.error("扫码失败");
        }
        //1 检查该二维码是否扫过了
        List<LinkedHashMap<String, Object>> orderbkLinkedHashMaps = DBFUtil.readDbfToList(orderBK.getDbfPath() + File.separator + "orderbk.DBF");
        for (LinkedHashMap<String, Object> linkedHashMap : orderbkLinkedHashMaps) {
            String dono = (String) linkedHashMap.getOrDefault("DONO",(String) linkedHashMap.getOrDefault("dono",""));
            if (orderBK.getDono().equals(dono)){
                return Result.error("不能重复扫码");
            }
        }
        //2 读取order数据
        Order order = null;
        List<LinkedHashMap<String, Object>> orderLinkedHashMaps = DBFUtil.readDbfToList(orderBK.getDbfPath()  + File.separator + "order.DBF");
        for (LinkedHashMap<String, Object> orderLinkedHashMap : orderLinkedHashMaps) {
            String dono = (String) orderLinkedHashMap.getOrDefault("DONO",(String) orderLinkedHashMap.getOrDefault("dono",""));
            if (orderBK.getDono().equals(dono)){
                order = new Order();
                order.setDono(dono);
                order.setCuno((String) orderLinkedHashMap.getOrDefault("CUNO",(String) orderLinkedHashMap.getOrDefault("cuno","")));
                order.setCupdno((String) orderLinkedHashMap.getOrDefault("CUPDNO",(String) orderLinkedHashMap.getOrDefault("cupdno","")));
                order.setVol((String) orderLinkedHashMap.getOrDefault("VOL",(String) orderLinkedHashMap.getOrDefault("vol","")));
                order.setNopy((String) orderLinkedHashMap.getOrDefault("NOPY",(String) orderLinkedHashMap.getOrDefault("nopy","")));
                String okvol = (String) orderLinkedHashMap.getOrDefault("OKVOL", (String) orderLinkedHashMap.getOrDefault("okvol", "0"));
                //更新采购单尾箱数量
                if (okvol.isEmpty()) {
                    okvol = "0";
                }
                Integer ok = Integer.valueOf(okvol);
                Integer i = Integer.valueOf(orderBK.getOkvol());
                ok = ok + i;
                orderLinkedHashMap.put("OKVOL",ok.toString());
                order.setOkvol(ok.toString());
            }
        }
        DBFUtil.exportdbf(orderBK.getDbfPath()  + File.separator + "order.DBF",orderLinkedHashMaps);
        if (order == null) {
            return Result.error("找不到该采购单");
        }
        //3 保存二维码记录
        LinkedHashMap<String, Object> saveScanRecord = saveScanRecord(orderBK);
        orderbkLinkedHashMaps.add(saveScanRecord);
        //4 写入扫码记录表
        DBFUtil.exportdbf(orderBK.getDbfPath()  + File.separator + "orderbk.DBF",orderbkLinkedHashMaps);
        return Result.ok("扫码成功",order);
    }

    private LinkedHashMap<String,Object> saveScanRecord(OrderBK orderBK){
        LinkedHashMap<String,Object> scanBk = new LinkedHashMap<>();
        scanBk.put("DONO",orderBK.getDono());
        scanBk.put("XH",orderBK.getXh());
        String type = orderBK.getType();
        if (type.isEmpty()) {
            type = "1";
        }
        scanBk.put("TYPE",type);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        scanBk.put("DATE",simpleDateFormat.format(date));
        scanBk.put("TIME",simpleTimeFormat.format(date));
        scanBk.put("PDUSERNAME",orderBK.getPdusername());
        return scanBk;
    }

    public Result scanPurchase(MpordeVO purchase) {
        String ordeno = purchase.getOrdeno();
        if (StringUtils.isEmpty()) {
            return Result.error("采购单号不能为空");
        }
        List<Object> params = new ArrayList<>();
        params.add(ordeno);
        //查询采购单
//        params.add(purchase.getMkcuno());
        String charset = purchase.getCharset();
        if (charset == null) {
            purchase.setCharset("GBK");
            charset = "GBK";
        }
        List<Map<String, String>> maps = DBFSqlUtils.executeQuerySqlStringResult(purchase.getDbfPath(), "select * from MPORDE where ORDENO = ?", params,charset);
        if (maps.isEmpty()) {
            return Result.error("查询不到此单号");
        }
        Map<String, String> orderResult = maps.get(0);
        //厂商编号
        String mkcuno = orderResult.getOrDefault("MKCUNO", orderResult.getOrDefault("mkcuno", ""));
        if (!mkcuno.equals(purchase.getMkcuno())){
            return Result.error("当前用户代号与厂商代号不符，您无法进行采购入库");
        }
        //封装其他信息
        purchase.setMkcuno(mkcuno);
        String mpdno1 = orderResult.getOrDefault("MPDNO", orderResult.getOrDefault("mpdno", ""));
        purchase.setMpdno(mpdno1);
        if (!StringUtils.isEmpty(mpdno1)) {
            //查询加工信息
            List<Object> materParams = new ArrayList<>();
            materParams.add(mpdno1);
            //文件读写
//            long l = System.currentTimeMillis();
//            List<HashMap<String, String>> linkedHashMaps = DBFUtil.readDbfToStringListOptimized(purchase.getDbfPath() + File.separator + "MATER.DBF");
//            for (HashMap<String, String> linkedHashMap : linkedHashMaps) {
//                String mpdno = linkedHashMap.getOrDefault("MPDNO", linkedHashMap.getOrDefault("mpdno", ""));
//                if (mpdno1.equals(mpdno)){
//                    String mpdne = linkedHashMap.getOrDefault("MPDNE", linkedHashMap.getOrDefault("mpdne", ""));
//                    String mpdne1 = linkedHashMap.getOrDefault("MPDNE1", linkedHashMap.getOrDefault("mpdne1", ""));
//                    purchase.setMpdne(mpdne);
//                    purchase.setMpdne1(mpdne1);
//                }
//            }
//            System.out.println("dbf文件读写耗时");
//            System.out.println(System.currentTimeMillis()-l);
            //sql读写(出现乱码)
            List<Map<String, String>> materMaps = DBFSqlUtils.executeQuerySqlStringResult(purchase.getDbfPath(), "select * from MATER where MPDNO = ?", materParams,charset);
            if (materMaps != null && !materMaps.isEmpty()) {
                Map<String, String> materResult = materMaps.get(0);
                String mpdne =  materResult.getOrDefault("MPDNE", materResult.getOrDefault("mpdne",""));
                purchase.setMpdne(mpdne);
                purchase.setMpdne1( materResult.getOrDefault("MPDNE1", materResult.getOrDefault("mpdne1","")));
            }
        }
        //仓库号
        purchase.setPartno(orderResult.getOrDefault("PARTNO",orderResult.getOrDefault("partno","")));
        //采购数量
        purchase.setVol(removeFractionalPart(orderResult.getOrDefault("VOL",orderResult.getOrDefault("vol",""))));
        //未交数量
        String nopy = removeFractionalPart(orderResult.getOrDefault("NOPY", orderResult.getOrDefault("nopy", "")));
        purchase.setNopy(nopy);
        //本次交货，默认未交数量
//        purchase.setOutvol(nopy);
        //币别
        purchase.setDollno(orderResult.getOrDefault("DOLLNO",orderResult.getOrDefault("dollno","")));
        //单价
        purchase.setPrice(orderResult.getOrDefault("PRICE",orderResult.getOrDefault("price","")));
        //备品数量
        purchase.setFlowvol("0");
        //本次交货，默认未交数量减去总入库量
        if (purchase.getNopy() != null) {
            String sql = "select sum(OUTVOL) as os from MPORINTP where ORDENO = ?";
            List<Object> params2 = new ArrayList<>();
            params2.add(purchase.getOrdeno());
            List<Map<String, String>> maps1 = DBFSqlUtils.executeQuerySqlStringResult(purchase.getDbfPath(), sql, params2,charset);
            if (!maps1.isEmpty()) {
                String os = maps1.get(0).getOrDefault("os", maps1.get(0).getOrDefault("OS", "0"));
                if (os == null){
                    os = "0";
                }
                int i = Integer.parseInt(numberHandler(os));
                int i1 = Integer.parseInt(purchase.getNopy());
                int sub = i1 - i;
                if (sub <= 0){
                    return Result.error("未交数量为0，不需要交货");
                }
                purchase.setOutvol(String.valueOf(sub));
            }
        }
        return Result.ok("扫码成功",purchase);
    }

    /**
     * 去除小数部分
     * @param input
     * @return
     */
    public static String removeFractionalPart(String input) {
        if (input != null && input.contains(".")) {
            int decimalIndex = input.indexOf(".");
            return input.substring(0, decimalIndex);
        }
        return input;
    }

    public Result savePurchase(MporintpVO purchase) {
        if (StringUtils.isEmpty(purchase.getOrdeno())) {
            return Result.error("采购单号不能为空");
        }
        if (purchase.getNopy().equals("0")){
            return Result.error("未交数量为0，不需要交货");
        }
        String charset = purchase.getCharset();
        if (charset == null) {
            purchase.setCharset("GBK");
            charset = "GBK";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String date = format.format(new Date());
        //9:同一采购单，同一天交货数量相同，加总小于未交量，提示是否重复录单
        if (purchase.getIsRepeated() == null || purchase.getIsRepeated() == 0){
            //判断是否重复录入
            String sql = "select * from MPORINTP where ORDENO = ? and OUTVOL = ? and INDAY = ?";
            List<Object> params = new ArrayList<>();
            params.add(purchase.getOrdeno());
            params.add(purchase.getOutvol());
            params.add(date);
            List<Map<String, String>> maps1 = DBFSqlUtils.executeQuerySqlStringResult(purchase.getDbfPath(), sql, params,charset);
            if (!maps1.isEmpty()){
                return new Result(3,"是否要选择重复录入?",purchase);
            }
        }
        //8:同一采购单验收数量加总走超过未交数量有提示，不能保存
        //判断是否验收数量超过未交数量
        if (purchase.getNopy() != null) {
            String sql = "select sum(OUTVOL) as os from MPORINTP where ORDENO = ?";
            List<Object> params = new ArrayList<>();
            params.add(purchase.getOrdeno());
            List<Map<String, String>> maps1 = DBFSqlUtils.executeQuerySqlStringResult(purchase.getDbfPath(), sql, params,charset);
            if (!maps1.isEmpty()) {
                String os = maps1.get(0).getOrDefault("os", maps1.get(0).getOrDefault("OS", "0"));
                if (os == null){
                    os = "0";
                }
                int i = Integer.parseInt(numberHandler(os));
                int i1 = Integer.parseInt(purchase.getNopy());
                int i2 = Integer.parseInt(purchase.getOutvol());
                int i3 = i2 + i;
                if ( i3 > i1){
                    return Result.error("同一采购单验收数量加总="+ i3 +"超过未交数量="+ i1 +"不能保存");
                }
            }
        }
        //生成验收单号
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMM");
        String recno = simpleDateFormat.format(new Date());
        String sql1 = "select recno from MPORINTP where recno like ? order by recno desc limit 1";
        String sql2 = "select recno from MPORINHI where recno like ? order by recno desc limit 1";
        List<Object> params = new ArrayList<>();
        params.add(recno + "%");
        List<Map<String, String>> maps1 = DBFSqlUtils.executeQuerySqlStringResult(purchase.getDbfPath(), sql1, params,charset);
        List<Map<String, String>> maps2 = DBFSqlUtils.executeQuerySqlStringResult(purchase.getDbfPath(), sql2, params,charset);
        //recno取两者最大值再加一
        if (maps1.isEmpty() && maps2.isEmpty()) {
            recno = recno + "0000";
        }else if (maps1.isEmpty()){
            recno = maps2.get(0).getOrDefault("RECNO",maps2.get(0).getOrDefault("recno",""));
        }else if (maps2.isEmpty()){
            recno = maps1.get(0).getOrDefault("RECNO",maps1.get(0).getOrDefault("recno",""));
        }else if (Integer.parseInt(maps2.get(0).getOrDefault("RECNO",maps2.get(0).getOrDefault("recno",""))) >= Integer.parseInt(maps1.get(0).getOrDefault("RECNO",maps1.get(0).getOrDefault("recno","")))){
            recno = maps2.get(0).getOrDefault("RECNO",maps2.get(0).getOrDefault("recno",""));
        }else{
            recno = maps1.get(0).getOrDefault("RECNO",maps1.get(0).getOrDefault("recno",""));
        }
        int recnoInt = Integer.parseInt(recno) + 1;
        System.out.println("recnoInt = " + recnoInt);
        //插入采购入库表
        insertT(recnoInt,purchase,date);
        return Result.ok("采购入库成功!");
    }

    private void insertT(int recnoInt,MporintpVO purchase,String date){
        String sql3 = "insert into MPORINTP (RECNO,ORDENO,DOLLNO,PRICE,MKCUNO,OUTVOL,INDAY,MPDNO,PARTNO,FLOWVOL,RECDAY,QCTYPE,YESNO,MKOUTNO,PUCHNAME,PUCHDAY)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        List<Object> insertParams = new ArrayList<>();
        insertParams.add(recnoInt);
        insertParams.add(purchase.getOrdeno());
        insertParams.add(purchase.getDollno());
        insertParams.add(numberHandler(purchase.getPrice()));
        insertParams.add(purchase.getMkcuno());
        insertParams.add(purchase.getOutvol());
        insertParams.add(date);
        insertParams.add(purchase.getMpdno());
        insertParams.add(purchase.getPartno());
        insertParams.add(purchase.getFlowvol());
        insertParams.add(null);
        insertParams.add("Y");
        insertParams.add(null);
        insertParams.add(purchase.getMkoutno());
        insertParams.add(null);
        insertParams.add(null);
        try {
            DBFSqlUtils.executeInsertSql(purchase.getDbfPath(),sql3, insertParams);
        }catch (RuntimeException e){
            if (e.getMessage().contains("duplicate key")){
                //加一重试
                recnoInt = recnoInt + 1;
                insertT(recnoInt,purchase,date);
            }else {
                throw e;
            }
        }
    }


    private LinkedHashMap<String,String> saveScanOrder(MporintpVO purchase){
        LinkedHashMap<String,String> scanBk = new LinkedHashMap<>();
        scanBk.put("RECNO",purchase.getRecno());
        scanBk.put("ORDENO",purchase.getOrdeno());
        scanBk.put("DOLLNO",purchase.getDollno());
        scanBk.put("PRICE",numberHandler(purchase.getPrice()));
        scanBk.put("MKCUNO",purchase.getMkcuno());
        scanBk.put("OUTVOL",purchase.getOutvol());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        scanBk.put("INDAY",simpleDateFormat.format(new Date()));
        scanBk.put("MPDNO",purchase.getMpdno());
        scanBk.put("PARTNO",purchase.getPartno());
        scanBk.put("MKOUTNO",purchase.getMkoutno());
        scanBk.put("FLOWVOL",purchase.getFlowvol());
        scanBk.put("QCTYPE","Y");
        return scanBk;
    }

    private static String dateHandler(String inputDateString){
        String outputFormat = "yyyy/MM/dd";
        String formattedDate = "";
        try {
            // 创建一个 SimpleDateFormat 实例来解析输入日期字符串
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

            // 解析输入字符串为 Date 对象
            Date date = inputFormat.parse(inputDateString);

            // 创建另一个 SimpleDateFormat 实例来格式化输出日期字符串
            SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);

            // 格式化 Date 对象为所需格式的字符串
            formattedDate = outputFormatter.format(date);
        } catch (ParseException e) {
            System.err.println("Error parsing the input date string: " + e.getMessage());
        }
        return formattedDate;
    }


    private static String numberHandler(String inputString){
        double inputNumber = Double.parseDouble(inputString);
        DecimalFormat df = new DecimalFormat("#.##"); // 指定保留两位小数的格式

        return df.format(inputNumber);
    }

    public Result scanMater(Mater mater) {
        return null;
    }

    public Result saveMater(Mater mater) {
        return null;
    }
}
