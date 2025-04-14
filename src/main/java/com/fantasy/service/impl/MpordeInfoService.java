package com.fantasy.service.impl;

import com.fantasy.model.Result.Result;
import com.fantasy.model.vo.MpordeVO;
import com.fantasy.model.vo.MporintpVO;
import com.fantasy.util.DBFSqlUtils;
import com.fantasy.util.PageUtil;
import com.fantasy.util.StringUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MpordeInfoService {


    /**
     * 查询未交采购单
     * @param mpordeVO
     * @return
     */
    public Result getUnPayPurchaseOrders(MpordeVO mpordeVO) {
        String sql = "select ordeno ,mpdno ,mater.mpdne as MPDNE ,mater.mpdne1 as MPDNE1 ,vol ,dono ,outvol ,bavol ,nopy ,orday ,pyday  " +
                "from mporde left join mater on mporde.mpdno=mater.mpdno " +
                "where mkcuno = ? and nopy>0 and yesno=true ";
        List<Object> params = new ArrayList<>();
        params.add(mpordeVO.getMkcuno());
        if (StringUtils.isEmpty(mpordeVO.getCharset())) {
            mpordeVO.setCharset("GBK");
        }
        if (!StringUtils.isEmpty(mpordeVO.getOrdeno())) {
            sql = sql + " and ordeno = ? ";
            params.add(mpordeVO.getOrdeno());
        }
        if (!StringUtils.isEmpty(mpordeVO.getMpdno())) {
            sql = sql + " and mpdno = ? ";
            params.add(mpordeVO.getMpdno());
        }
        if (!StringUtils.isEmpty(mpordeVO.getPyday())) {
            sql = sql + " and pyday = ? ";
            params.add(mpordeVO.getPyday());
        }
        if (!StringUtils.isEmpty(mpordeVO.getDono())) {
            sql = sql + " and dono = ? ";
            params.add(mpordeVO.getDono());
        }
        sql = sql + " order by orday desc ";
        List<Map<String, String>> maps = DBFSqlUtils.executeQuerySqlStringResult(mpordeVO.getDbfPath(), sql, params,mpordeVO.getCharset());
        List<Map<String,String>> UpMaps = new ArrayList<>();
        for (Map<String, String> map : maps) {
            Map<String, String> upperCaseMap = map.entrySet().stream()
                    .filter(entry -> entry.getValue() != null) // 过滤掉value为null的entry
                    .collect(Collectors.toMap(
                            entry -> entry.getKey().toUpperCase(),
                            entry -> {
                                return removeFractionalPart(entry.getValue());
                            }
                    ));
            String s = upperCaseMap.get("PYDAY");
            if (!StringUtils.isEmpty(s)) {
                int index = s.indexOf('-');
                s = s.substring(index + 1);
                upperCaseMap.put("PYDAY",s);
            }
            String MPDNE1 = upperCaseMap.get("MPDNE1");
            String MPDNE = upperCaseMap.get("MPDNE");
            String ORDENO = upperCaseMap.get("ORDENO");
            String MPDNO = upperCaseMap.get("MPDNO");
            String DONO = upperCaseMap.get("DONO");
            if (!StringUtils.isEmpty(MPDNE1)) {
                upperCaseMap.put("MPDNE",MPDNE + " " + MPDNE1);
            }
            if (!StringUtils.isEmpty(MPDNO)) {
                upperCaseMap.put("MPDNO",insertSpacesEveryTwoChars3(MPDNO));
            }
            if (!StringUtils.isEmpty(ORDENO)) {
                upperCaseMap.put("ORDENO",insertSpacesEveryTwoChars(ORDENO));
            }
            if (!StringUtils.isEmpty(DONO)) {
                upperCaseMap.put("DONO",insertSpacesEveryTwoChars(DONO));
            }
            UpMaps.add(upperCaseMap);
        }
        PageInfo<Map<String, String>> maps1 = PageUtil.manualPage(UpMaps, mpordeVO.getPageInfo().getCurrent(), mpordeVO.getPageInfo().getSize());
        return Result.ok("查询成功",maps1);
    }

    private static String insertSpacesEveryTwoChars(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            sb.append(str.charAt(i));
            // 在除了最后一个和倒数第二个字符外的每个字符后添加空格
            if (i % 2 == 1 && i < str.length() - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private static String insertSpacesEveryTwoChars3(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            sb.append(str.charAt(i));
            // 在除了最后一个、倒数第二个和倒数第三个字符外的每个字符后添加空格
            if (i % 3 == 2 && i < str.length() - 2) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private static String numberHandler(String inputString){
        // 检查输入是否为空或者只包含空白字符
        if (inputString == null || inputString.trim().isEmpty()) {
            return inputString; // 直接返回原字符串
        }

        try {
            // 尝试将输入转换为Double
            double inputNumber = Double.parseDouble(inputString);
            DecimalFormat df = new DecimalFormat("#.##"); // 指定保留两位小数的格式

            return df.format(inputNumber);
        } catch (NumberFormatException e) {
            // 如果转换失败（即输入并非数字），直接返回原字符串
            return inputString;
        }
    }

    /**
     * 去除小数部分
     * @param input
     * @return
     */
    public static String removeFractionalPart(String input) {
        if (input != null && input.contains(".")) {
            // 尝试将输入转换为Double
            try {
                double inputNumber = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                // 如果转换失败（即输入并非数字），直接返回原字符串
                return input;
            }
            int decimalIndex = input.indexOf(".");
            return input.substring(0, decimalIndex);
        }
        return input;
    }


    public Result getSubmitMporintps(MporintpVO mporintpVO) {
//        PageHelper.startPage(mporintpVO.getPageInfo().getCurrent(),mporintpVO.getPageInfo().getSize(),true);
        //验收单号，采购单号，物料编号，物料名称，交货数量，交货日期
        String sql = "select recno as RECNO,ordeno as ORDENO,mpdno as MPDNO,mater.mpdne as MPDNE,mater.mpdne1 as MPDNE1,outvol as OUTVOL,inday  " +
                "from MPORINTP left join mater on MPORINTP.mpdno=mater.mpdno " +
                "where mkcuno = ? ";
        List<Object> params = new ArrayList<>();
        params.add(mporintpVO.getMkcuno());
        if (!StringUtils.isEmpty(mporintpVO.getRecno())) {
            sql = sql + "and recno = ? ";
            params.add(mporintpVO.getRecno());
        }
        if (!StringUtils.isEmpty(mporintpVO.getOrdeno())) {
            sql = sql + "and ordeno = ? ";
            params.add(mporintpVO.getOrdeno());
        }
        if (!StringUtils.isEmpty(mporintpVO.getMpdno())) {
            sql = sql + "and mpdno = ? ";
            params.add(mporintpVO.getMpdno());
        }
        if (!StringUtils.isEmpty(mporintpVO.getInday())) {
            sql = sql + "and inday = ? ";
            params.add(mporintpVO.getInday());
        }
        sql = sql + " order by inday desc ";
        if (StringUtils.isEmpty(mporintpVO.getCharset())) {
            mporintpVO.setCharset("GBK");
        }
        List<Map<String, String>> maps = DBFSqlUtils.executeQuerySqlStringResult(mporintpVO.getDbfPath(), sql, params,mporintpVO.getCharset());
//        PageInfo<Map<String, String>> mapPageInfo = PageInfo.of(maps);
        List<Map<String,String>> UpMaps = new ArrayList<>();
        for (Map<String, String> map : maps) {
            Map<String, String> upperCaseMap = map.entrySet().stream()
                    .filter(entry -> entry.getValue() != null) // 过滤掉value为null的entry
                    .collect(Collectors.toMap(
                            entry -> entry.getKey().toUpperCase(),
                            entry -> {
                                return removeFractionalPart(entry.getValue());
                            }
                    ));
            String s = upperCaseMap.get("INDAY");
            if (!StringUtils.isEmpty(s)) {
                int index = s.indexOf('/');
                s = s.substring(index + 1);
                upperCaseMap.put("INDAY",s);
            }
            String MPDNE1 = upperCaseMap.get("MPDNE1");
            String MPDNE = upperCaseMap.get("MPDNE");
            if (!StringUtils.isEmpty(MPDNE1)) {
                upperCaseMap.put("MPDNE",MPDNE + " " + MPDNE1);
            }
            String RECNO = upperCaseMap.get("RECNO");
            String ORDENO = upperCaseMap.get("ORDENO");
            if (!StringUtils.isEmpty(RECNO)) {
                upperCaseMap.put("RECNO",insertSpacesEveryTwoChars3(RECNO));
            }
            if (!StringUtils.isEmpty(ORDENO)) {
                upperCaseMap.put("ORDENO",insertSpacesEveryTwoChars3(ORDENO));
            }
            UpMaps.add(upperCaseMap);
        }
        PageInfo<Map<String, String>> maps1 = PageUtil.manualPage(UpMaps, mporintpVO.getPageInfo().getCurrent(), mporintpVO.getPageInfo().getSize());
        return Result.ok("查询成功",maps1);
    }

    public Result deleteMporintp(MporintpVO mporintpVO){
        String recno = mporintpVO.getRecno();
        if (StringUtils.isEmpty(recno)) {
            return Result.error("验收单号不能为空");
        }
        recno = recno.replaceAll("\\s", "");
        String sql = "delete from mporintp where recno = ?";
        List<Object> params = new ArrayList<>();
        params.add(recno);
        DBFSqlUtils.executeInsertSql(mporintpVO.getDbfPath(), sql, params);
        return Result.ok("删除成功");
    }
}
