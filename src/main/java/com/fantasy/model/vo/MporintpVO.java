package com.fantasy.model.vo;

import com.fantasy.entity.Mporintp;
import com.fantasy.model.Result.PageVO;
import lombok.Data;

@Data
public class MporintpVO extends Mporintp {

    //物料名称(mater)
    private String MPDNE;
    //采购数量
    private String vol;
    //未交数量
    private String nopy;

    private String dbfPath;
    private String charset;
    //是否重复录入
    private Integer isRepeated;

    private PageVO pageInfo;

}
