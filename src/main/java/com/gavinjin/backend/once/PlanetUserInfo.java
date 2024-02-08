package com.gavinjin.backend.once;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Planet user information
 */
@Data
public class PlanetUserInfo {
    /**
     * Planet code
     */
    @ExcelProperty("成员编号")
    private String planetCode;

    /**
     * Planet username
     */
    @ExcelProperty("成员昵称")
    private String username;
}
