package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.ProjectMemberVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import supie.webadmin.upms.model.SysUser;
import supie.webadmin.upms.model.SysUserData;

/**
 * ProjectMember实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_project_member")
public class ProjectMember extends BaseModel {

    /**
     * 租户号。
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
     * 成员关联用户表数据。
     */
    @RelationOneToOne(
            masterIdField = "memberUserId",
            slaveModelClass = SysUser.class,
            slaveIdField = "userId")
    @TableField(exist = false)
    private SysUserData memberUser;

    /**
     * 成员关联用户表id。
     */
    private Long memberUserId;

    /**
     * 关联项目id。
     */
    private Long memberProjectId;

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
    public interface ProjectMemberModelMapper extends BaseModelMapper<ProjectMemberVo, ProjectMember> {
    }
    public static final ProjectMemberModelMapper INSTANCE = Mappers.getMapper(ProjectMemberModelMapper.class);
}
