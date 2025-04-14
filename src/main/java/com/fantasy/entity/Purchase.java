package com.fantasy.entity;

import lombok.Data;

@Data
public class Purchase extends Connect{

    //采购单号
    private String pon;
    //送货单号
    private String dnn;
    //物料编号
    private String materialNumber;
    //物料名称
    private String materialName;
    //采购数量
    private String purchaseQuantity;
    //未交数量
    private String unpaidQuantity;
    //本次交货
    private String thisDelivery;

}
