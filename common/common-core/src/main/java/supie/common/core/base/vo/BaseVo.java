package supie.common.core.base.vo;

import lombok.Data;

import java.util.Date;

/**
 * VO对象的公共基类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class BaseVo {

    /**
     * 创建者Id。
     */
    private Long createUserId;

    /**
     * 创建时间。
     */
    private Date createTime;

    /**
     * 更新者Id。
     */
    private Long updateUserId;

    /**
     * 更新时间。
     */
    private Date updateTime;
}
