package com.fantasy.model.vo;

import com.fantasy.entity.Mporde;
import com.fantasy.model.Result.PageVO;
import lombok.Data;

@Data
public class MpordeVO extends Mporde {

    //物料名称(关联mater)
    private String mpdne;
    //附加说明(关联mater)
    private String mpdne1;
    //送货单号(人工录入)
    private String mkoutno;
    //用户名
    private String jmkcuna;
    //备品数量
    private String flowvol;

    private String dbfPath;
    private String charset;

    private PageVO pageInfo;


}
