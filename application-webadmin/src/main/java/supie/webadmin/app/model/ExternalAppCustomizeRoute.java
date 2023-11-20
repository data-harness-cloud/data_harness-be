package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.ExternalAppCustomizeRouteVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * ExternalAppCustomizeRoute实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_external_app_customize_route")
public class ExternalAppCustomizeRoute extends BaseModel {

    /**
     * 主键ID。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 数据所属人。
     */
    @UserFilterColumn
    private Long dataUserId;

    /**
     * 数据所属部门。
     */
    @DeptFilterColumn
    private Long dataDeptId;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 动态路由ID。
     */
    private Long customizeRouteId;

    /**
     * 外部APP ID。
     */
    private Long externalAppId;

    /**
     * updateTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String updateTimeStart;

    /**
     * updateTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String updateTimeEnd;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String createTimeEnd;

    @Mapper
    public interface ExternalAppCustomizeRouteModelMapper extends BaseModelMapper<ExternalAppCustomizeRouteVo, ExternalAppCustomizeRoute> {
    }
    public static final ExternalAppCustomizeRouteModelMapper INSTANCE = Mappers.getMapper(ExternalAppCustomizeRouteModelMapper.class);
}
