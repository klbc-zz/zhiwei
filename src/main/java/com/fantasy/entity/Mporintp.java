package com.fantasy.entity;

import lombok.Data;

@Data
public class Mporintp {

    //验收单号
    private String recno;
    //采购单号
    private String ordeno;
    //币别
    private String dollno;
    //单价
    private String price;
    //用户代号(mporde)
    private String mkcuno;
    //本次交货
    private String outvol;
    //当天日期
    private String inday;
    //物料编号
    private String mpdno;
    //仓库代号
    private String partno;
    //备品数量
    private String flowvol;
    private String recday;
    private String qctype;
    private String yesno;
    //送货单号(人工录入)
    private String mkoutno;
    private String puchname;
    private String puchday;

}
