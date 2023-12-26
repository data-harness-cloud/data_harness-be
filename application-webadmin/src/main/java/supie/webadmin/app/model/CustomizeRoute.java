package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.CustomizeRouteVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * CustomizeRoute实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_customize_route")
public class CustomizeRoute extends BaseModel {

    /**
     * 主键ID。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 名称。
     */
    private String name;

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
     * 描述。
     */
    private String routeDescribe;

    /**
     * 地址（不可重复）。
     */
    private String url;

    /**
     * 请求类型（1：GET。2：POST。默认为POST）。
     */
    private Integer requestType;

    /**
     * 状态（1：上线。-1：下线）。
     */
    private Integer state;

    /**
     * 存算引擎项目ID。
     */
    private Long projectId;

    /**
     * 目标数据库名称。
     */
    private String databaseName;

    /**
     * SQL语句。
     */
    private String sqlScript;

    /**
     * 参数集（JSON字符串形式存储）。
     */
    private String parameter;

    /**
     * 业务规程ID。
     */
    private Long processId;

    /**
     * 指标ID。
     */
    private Long definitionIndexId;

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

    /**
     * name / routeDescribe / url / database_name / sql_script / parameter LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface CustomizeRouteModelMapper extends BaseModelMapper<CustomizeRouteVo, CustomizeRoute> {
    }
    public static final CustomizeRouteModelMapper INSTANCE = Mappers.getMapper(CustomizeRouteModelMapper.class);
}
