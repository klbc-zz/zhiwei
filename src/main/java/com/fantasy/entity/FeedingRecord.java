package com.fantasy.entity;



import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Fantasy0521
 * @since 2024-03-26
 */
@ApiModel(value = "FeedingRecord对象", description = "")
@Data
public class FeedingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("线号")
    private String lineNumber;

    @ApiModelProperty("机台")
    private String machineNumber;

    private String customer;

    @ApiModelProperty("品名")
    private String productName;

    @ApiModelProperty("素材批号")
    private String materialNumber;

    private BigDecimal number;

    private Date uploadTime;

    private Date downloadTime;

    @ApiModelProperty("断料卡料")
    private Integer brokenMaterial;

    private Integer comeMaterial;

    private Integer product;

    @ApiModelProperty("卷绕")
    private Integer winding;

    private Date createTime;

}
