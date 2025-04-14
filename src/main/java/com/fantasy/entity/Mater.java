package com.fantasy.entity;

import lombok.Data;

@Data
public class Mater extends Connect{

    //物料编号
    private String mpdno;
    //物料名称
    private String mpdne;
    //附加说明
    private String mpdne1;
    private String unit;
    private String puchunit;
    //仓库代号
    private String partno;

}
