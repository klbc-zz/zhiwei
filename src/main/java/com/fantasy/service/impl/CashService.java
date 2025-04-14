package com.fantasy.service.impl;

import com.fantasy.enums.StatEnum;
import com.fantasy.model.Cash;
import com.fantasy.model.Result.PageResult;
import com.fantasy.model.Result.PageVO;
import com.fantasy.model.dto.CashDTO;
import com.fantasy.model.dto.UserDTO;
import com.fantasy.util.DBFSqlUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CashService {
    public int addCash(Cash cash,UserDTO user) throws Exception {
        String dbfPath = cash.getDbfPath();
        // 定义SQL插入语句（全大写）
        String sql2 = "INSERT INTO CASH (" +
                "No, PARTNO, CNAME, DAY, TELNO, TOTM, MONE, BANKNO, BANKNAME, SKNAME, [CLASS], " +
                "YTOTM, HTOTM, YESNO, MARK, OK, PICT1, PICT2, PICT3, PICT4, PICT5, PICT6, PICT7, PICT8, " +
                "SNAME1, SDAY1, STAT1, SMONE1, SNAME2, SDAY2, STAT2, SMONE2, " +
                "SNAME3, SDAY3, STAT3, SMONE3, SNAME4, SDAY4, STAT4, SMONE4) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sql1 = "INSERT INTO CASH (" +
                "NO, PARTNO, CNAME, DAY, TELNO, TOTM, MONE, BANKNO, BANKNAME, " +
                "SKNAME, [CLASS], YTOTM, HTOTM, YESNO, OK, PICT1, PICT2, PICT3, " +
                "PICT4, PICT5, PICT6, PICT7, PICT8, SNAME1, SDAY1, SMONE1, " +
                "STAT1, SNAME2, SDAY2, SMONE2, STAT2, SNAME3, SDAY3, SMONE3, " +
                "STAT3, SNAME4, SDAY4, SMONE4, STAT4" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?)";
        String sql = "INSERT INTO CASH (" +
                "[NO], [PARTNO], [CNAME], [DAY], [TELNO], [TOTM], [MONE], [BANKNO], [BANKNAME], " +
                "[SKNAME], [CLASS], [YTOTM], [HTOTM], [YESNO], [OK], [PICT1], [PICT2], [PICT3], " +
                "[PICT4], [PICT5], [PICT6], [PICT7], [PICT8], [SNAME1], [SDAY1], [SMONE1], " +
                "[STAT1], [SNAME2], [SDAY2], [SMONE2], [STAT2], [SNAME3], [SDAY3], [SMONE3], " +
                "[STAT3], [SNAME4], [SDAY4], [SMONE4], [STAT4]" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?)";
        List<Object> insertParams = cash.getObjectList(user);

        return DBFSqlUtils.executeInsertSql(dbfPath, sql, insertParams);
    }
    public int updateCash(Cash cash) {
        String dbfPath = cash.getDbfPath();
        List<Object> updateParams = cash.getObjectListUpdate();
        String updateSql = "UPDATE CASH SET " +
                "[PARTNO] = ?, [CNAME] = ?, [DAY] = ?, [TELNO] = ?, " +
                "[TOTM] = ?, [MONE] = ?, [BANKNO] = ?, [BANKNAME] = ?, " +
                "[SKNAME] = ?, [CLASS] = ?, [YTOTM] = ?, [HTOTM] = ?, " +
                "[YESNO] = ?, [OK] = ?, [PICT1] = ?, " +
                "[PICT2] = ?, [PICT3] = ?, [PICT4] = ?, [PICT5] = ?, " +
                "[PICT6] = ?, [PICT7] = ?, [PICT8] = ?, [SNAME1] = ?, " +
                "[SDAY1] = ?, [STAT1] = ?, [SMONE1] = ?, [SNAME2] = ?, " +
                "[SDAY2] = ?, [STAT2] = ?, [SMONE2] = ?, [SNAME3] = ?, " +
                "[SDAY3] = ?, [STAT3] = ?, [SMONE3] = ?, [SNAME4] = ?, " +
                "[SDAY4] = ?, [STAT4] = ?, [SMONE4] = ? " +
                "WHERE [NO] = ?";
        return DBFSqlUtils.executeInsertSql(dbfPath, updateSql, updateParams);
    }

    public PageResult<Cash> list(CashDTO cashDTO) {
        List<Object> params = new ArrayList<>();
        String querySql = "SELECT * FROM CASH WHERE " +
                "(CNAME = ? OR SNAME1 = ? OR SNAME2 = ? OR SNAME3 = ? OR SNAME4 = ?) ";
       if(cashDTO.getStat()!=null){
           querySql = "SELECT * FROM CASH WHERE (" +
                   "(CNAME = ? AND STAT1 = ?) " +
                   "OR (CNAME = ? AND STAT2 = ?) " +
                   "OR (CNAME = ? AND STAT3 = ?) " +
                   "OR (CNAME = ? AND STAT4 = ?) " +
                   "OR (SNAME1 = ? AND STAT1 = ?) " +
                   "OR (SNAME2 = ? AND STAT2 = ?) " +
                   "OR (SNAME3 = ? AND STAT3 = ?) " +
                   "OR (SNAME4 = ? AND STAT4 = ?)) ";
           params.add(cashDTO.getCname());params.add(cashDTO.getStat());params.add(cashDTO.getCname());params.add(cashDTO.getStat());
           params.add(cashDTO.getCname());params.add(cashDTO.getStat());params.add(cashDTO.getCname());params.add(cashDTO.getStat());
           params.add(cashDTO.getCname());params.add(cashDTO.getStat());params.add(cashDTO.getCname());params.add(cashDTO.getStat());
           params.add(cashDTO.getCname());params.add(cashDTO.getStat());params.add(cashDTO.getCname());params.add(cashDTO.getStat());
       }else {
           params.add(cashDTO.getCname()); params.add(cashDTO.getCname()); params.add(cashDTO.getCname()); params.add(cashDTO.getCname());
           params.add(cashDTO.getCname());
       }

        List<Map<String, Object>> oblist = DBFSqlUtils.executeQuerySqlListResult(cashDTO.getDbfPath(), querySql, params);
        List<Cash> cashList = new ArrayList<>();
        for (Map<String, Object> map : oblist) {
            Cash cash = new Cash();
            beanMapper(map,cash);
            cashList.add(cash);
        }
        // 内存分页
        PageVO pageVO = cashDTO.getPage();

        int start = (pageVO.getCurrent() - 1) * pageVO.getSize();
        if (start >= cashList.size()) {
            cashList = new ArrayList<>();
        }
        int end = Math.min(start +  pageVO.getSize(), cashList.size());
        cashList = cashList.subList(start, end);
        PageResult<Cash> rs = new PageResult<>();
        rs.setList(cashList);
        return rs;


    }
    public Cash getByNo(String dbfPath,String no) {
        List<Object> params = new ArrayList<>();
        params.add(no);
        String querySql = "SELECT * FROM CASH WHERE " +
                "[NO] = ? ";
        List<Map<String, Object>> oblist = DBFSqlUtils.executeQuerySqlListResult(dbfPath, querySql, params);
        if(oblist.isEmpty()){
            return null;
        }
        Map<String, Object> map = oblist.get(0);
        Cash cash = new Cash();
        beanMapper(map,cash);
        boolean isR = false;
        if(cash.getSname1()!=null && !cash.getSname1().isEmpty() && cash.getStat1()==0){
            cash.setStat1(StatEnum.UNREVIEW.getCode());
            isR = true;
        }
        if(cash.getSname2()!=null && !cash.getSname2().isEmpty() && cash.getStat1()==0){
            cash.setStat2(StatEnum.UNREVIEW.getCode());
            isR = true;
        }
        if(cash.getSname3()!=null && !cash.getSname3().isEmpty() && cash.getStat1()==0){
            cash.setStat3(StatEnum.UNREVIEW.getCode());
            isR = true;
        }
        if(cash.getSname4()!=null && !cash.getSname4().isEmpty() && cash.getStat1()==0){
            cash.setStat4(StatEnum.UNREVIEW.getCode());
            isR = true;
        }
        cash.setDbfPath(dbfPath);
        if(isR){
            updateCash(cash);
        }
        return cash;

    }
    private void beanMapper(Map<String, Object> map,Cash cash){
        cash.setNo((String) map.get("NO"));
        // 使用工具方法安全获取值
        cash.setNo(getStringValue(map, "NO"));
        cash.setPartno(getStringValue(map, "PARTNO"));
        cash.setCname(getStringValue(map, "CNAME"));
        cash.setDay(getStringValue(map, "DAY"));
        cash.setTelno(getStringValue(map, "TELNO"));
        cash.setTotm(getDecimalValue(map, "TOTM"));
        cash.setMone(getStringValue(map, "MONE"));
        cash.setBankno(getStringValue(map, "BANKNO"));
        cash.setBankname(getStringValue(map, "BANKNAME"));
        cash.setSkname(getStringValue(map, "SKNAME"));
        cash.setClass_(getStringValue(map, "CLASS"));
        cash.setYtotm(getDecimalValue(map, "YTOTM"));
        cash.setHtotm(getDecimalValue(map, "HTOTM"));
        cash.setYesno(getBooleanValue(map, "YESNO"));
        cash.setMark(getBooleanValue(map, "MARK"));
        cash.setOk(getBooleanValue(map, "OK"));
        cash.setPict1(getStringValue(map, "PICT1"));
        cash.setPict2(getStringValue(map, "PICT2"));
        cash.setPict3(getStringValue(map, "PICT3"));
        cash.setPict4(getStringValue(map, "PICT4"));
        cash.setPict5(getStringValue(map, "PICT5"));
        cash.setPict6(getStringValue(map, "PICT6"));
        cash.setPict7(getStringValue(map, "PICT7"));
        cash.setPict8(getStringValue(map, "PICT8"));
        cash.setSname1(getStringValue(map, "SNAME1"));
        cash.setSday1(getStringValue(map, "SDAY1"));
        cash.setStat1(getIntValue(map, "STAT1"));
        cash.setSmone1(getStringValue(map, "SMONE1"));
        cash.setSname2(getStringValue(map, "SNAME2"));
        cash.setSday2(getStringValue(map, "SDAY2"));
        cash.setStat2(getIntValue(map, "STAT2"));
        cash.setSmone2(getStringValue(map, "SMONE2"));
        cash.setSname3(getStringValue(map, "SNAME3"));
        cash.setSday3(getStringValue(map, "SDAY3"));
        cash.setStat3(getIntValue(map, "STAT3"));
        cash.setSmone3(getStringValue(map, "SMONE3"));
        cash.setSname4(getStringValue(map, "SNAME4"));
        cash.setSday4(getStringValue(map, "SDAY4"));
        cash.setStat4(getIntValue(map, "STAT4"));
        cash.setSmone4(getStringValue(map, "SMONE4"));
    }
    // 安全获取String值的工具方法
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }
    private boolean getBooleanValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null && (boolean) value;
    }
    private int getIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);

        try {
            String v = (String) value;
            return value != null ? Integer.parseInt(v) : null;
        }catch (Exception e) {
            return 0;
        }
    }
    private BigDecimal getDecimalValue(Map<String, Object> map, String key) {
        BigDecimal defaultValue = new BigDecimal(0);
        try {
            Object value = map.get(key);
            if (value == null) {
                return defaultValue;
            }

            if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            }
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }


    public List<Cash> send(UserDTO user) {
        List<Object> params = new ArrayList<>();
        String querySql = "SELECT * FROM CASH WHERE (" +
                " (SNAME1 = ? AND STAT1 <= 1) " +
                "OR (SNAME2 = ? AND STAT2 <= 1) " +
                "OR (SNAME3 = ? AND STAT3 <= 1) " +
                "OR (SNAME4 = ? AND STAT4 <= 1) "+
                "OR (CNAME = ? AND (STAT1 = 3 OR STAT2 = 3 OR STAT3 = 3 OR STAT4 = 3))) ";

        params.add(user.getCname());
        params.add(user.getCname());
        params.add(user.getCname());
        params.add(user.getCname());
        params.add(user.getCname());


        List<Map<String, Object>> oblist = DBFSqlUtils.executeQuerySqlListResult(user.getDbfPath(), querySql, params);
        List<Cash> cashList = new ArrayList<>();
        for (Map<String, Object> map : oblist) {
            Cash cash = new Cash();
            beanMapper(map,cash);
            cashList.add(cash);
        }
        // 内存分页
//        PageVO pageVO = cashDTO.getPage();
//
//        int start = (pageVO.getCurrent() - 1) * pageVO.getSize();
//        if (start >= cashList.size()) {
//            cashList = new ArrayList<>();
//        }
//        int end = Math.min(start +  pageVO.getSize(), cashList.size());
//        cashList = cashList.subList(start, end);
//        PageResult<Cash> rs = new PageResult<>();
//        rs.setList(cashList);
        return cashList;

    }
}
