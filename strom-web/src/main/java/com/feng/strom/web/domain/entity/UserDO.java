package com.feng.strom.web.domain.entity;
/**
 * @ProjectName
 * @Description:
 * @Date 2020/1/16 14:02
 * @author
 * @version 1.0
 */
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class UserDO implements Serializable {

    private static final long serialVersionUID = -6031200295962660624L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ExcelProperty(value = "姓名")
    private String name;
    @ExcelProperty(value= "呢称")
    private String nickName;

    private String passwd;
    @ExcelProperty(value = "设备编码")
    private String imei;

}
