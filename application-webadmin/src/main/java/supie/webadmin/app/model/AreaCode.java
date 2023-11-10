package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 行政区划实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "zz_area_code")
public class AreaCode {

    /**
     * 行政区划主键Id
     */
    @TableId(value = "area_id")
    private Long areaId;

    /**
     * 行政区划名称
     */
    private String areaName;

    /**
     * 行政区划级别 (1: 省级别 2: 市级别 3: 区级别)
     */
    private Integer areaLevel;

    /**
     * 父级行政区划Id
     */
    private Long parentId;
}