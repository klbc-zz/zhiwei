package com.fantasy.entity;

import lombok.Data;

/**
 * 业务订单
 */
@Data
public class Order {

    //制造序号
    private String dono;
    //客户代号
    private String cuno;
    //客户品号
    private String cupdno;
    //订单数量
    private String vol;
    //未交数量
    private String nopy;
    //尾箱每箱数量
    private String okvol;

}
