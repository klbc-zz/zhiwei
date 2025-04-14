package com.fantasy.entity;

import lombok.Data;

/**
 * 扫码明细
 */
@Data
public class OrderBK extends Connect{

    //二维码前9位
    private String dono;
    //二维码11-15位
    private String xh;
    //入库1，出库2
    private String type;
    //扫码日期
    private String date;
    //扫码时间
    private String time;
    //取登录时newview.cname
    private String pdusername;
    private String okvol;

}
