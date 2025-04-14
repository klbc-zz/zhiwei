package com.fantasy.model.dto;

import com.fantasy.entity.Connect;
import com.fantasy.enums.StatEnum;
import com.fantasy.model.Result.PageVO;
import lombok.Data;

@Data
public class CashDTO extends Connect {
    private Integer stat;
    private String cname;
    PageVO page = new PageVO();
}
