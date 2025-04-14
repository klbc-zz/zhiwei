package com.fantasy.model;

import com.fantasy.entity.Connect;
import com.fantasy.enums.StatEnum;
import com.fantasy.model.dto.UserDTO;
import com.fantasy.util.FileBasedCodeGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cash extends Connect {
    private String no;
    private String partno	;//部门代号
    private String cname	;//申请人
    private String day	    ;//申请日期
    private String telno	;//电话号码
    private BigDecimal totm	    ;//申请金额
    private String mone	    ;//申请原因
    private String bankno	;//银行帐号
    private String bankname	;//银行名称
    private String skname	;//收款人
    private String class_	;//申请类型
    private BigDecimal ytotm	;// 实际用款
    private BigDecimal htotm	;// 退回金额
    private Boolean yesno= false;//
    private Boolean mark=false	    ;// 付款是否到位
    private Boolean ok=false	    ;// 还款是否到位
    private String pict1;//
    private String pict2;//
    private String pict3;//
    private String pict4;//
    private String pict5;//
    private String pict6;//
    private String pict7;//
    private String pict8	;// 最多8张附件，在服务器的位置
    private String sname1	;// 初审
    private String sday1	;// 日期
    private Integer stat1= StatEnum.UNREAD.getCode()	;// 状态
    private String smone1	;// 退回原因

    private String sname2	;// 复审
    private String sday2	;// 日期
    private Integer stat2= StatEnum.UNREAD.getCode();
    private String smone2	;// 退回原因

    private String sname3	;// 批准
    private String sday3	;// 日期
    private Integer stat3= StatEnum.UNREAD.getCode();
    private String smone3	;// 退回原因

    private String sname4	;// 出纳
    private String sday4	;// 日期
    private Integer stat4= StatEnum.UNREAD.getCode();
    private String smone4	;// 退回原因
    @JsonIgnore
    public List<Object> getObjectList(UserDTO user) throws Exception {
        // 准备参数列表
        List<Object> insertParams = new ArrayList<>();

        insertParams.add(FileBasedCodeGenerator.generateCode(user.getCompany()));
        insertParams.add(partno);
        insertParams.add(user.getCname());
        insertParams.add(day);
        insertParams.add(telno);
        insertParams.add(totm);
        insertParams.add(mone);
        insertParams.add(bankno);
        insertParams.add(bankname);
        insertParams.add(skname);
        insertParams.add(class_);
        insertParams.add(ytotm);
        insertParams.add(htotm);
        insertParams.add(yesno);
//        insertParams.add(mark);
        insertParams.add(ok);
        insertParams.add(pict1);
        insertParams.add(pict2);
        insertParams.add(pict3);
        insertParams.add(pict4);
        insertParams.add(pict5);
        insertParams.add(pict6);
        insertParams.add(pict7);
        insertParams.add(pict8);
        insertParams.add(user.getNextname());
        insertParams.add(sday1);
        insertParams.add(stat1);
        insertParams.add(smone1);
        insertParams.add(sname2);
        insertParams.add(sday2);
        insertParams.add(stat2);
        insertParams.add(smone2);
        insertParams.add(sname3);
        insertParams.add(sday3);
        insertParams.add(stat3);
        insertParams.add(smone3);
        insertParams.add(sname4);
        insertParams.add(sday4);
        insertParams.add(stat4);
        insertParams.add(smone4);
        return insertParams;
    }
    @JsonIgnore
    public List<Object> getObjectListUpdate(){
        // 准备参数列表（注意WHERE条件的NO参数放在最后）
        List<Object> updateParams = new ArrayList<>();
        updateParams.add(partno);
        updateParams.add(cname);
        updateParams.add(day);
        updateParams.add(telno);
        updateParams.add(totm);
        updateParams.add(mone);
        updateParams.add(bankno);
        updateParams.add(bankname);
        updateParams.add(skname);
        updateParams.add(class_);
        updateParams.add(ytotm);
        updateParams.add(htotm);
        updateParams.add(yesno);
//        updateParams.add(mark);
        updateParams.add(ok);
        updateParams.add(pict1);
        updateParams.add(pict2);
        updateParams.add(pict3);
        updateParams.add(pict4);
        updateParams.add(pict5);
        updateParams.add(pict6);
        updateParams.add(pict7);
        updateParams.add(pict8);
        updateParams.add(sname1);
        updateParams.add(sday1);
        updateParams.add(stat1);
        updateParams.add(smone1);
        updateParams.add(sname2);
        updateParams.add(sday2);
        updateParams.add(stat2);
        updateParams.add(smone2);
        updateParams.add(sname3);
        updateParams.add(sday3);
        updateParams.add(stat3);
        updateParams.add(smone3);
        updateParams.add(sname4);
        updateParams.add(sday4);
        updateParams.add(stat4);
        updateParams.add(smone4);
        updateParams.add(no);  // WHERE条件参数放在最后
        return updateParams;
    }

}
