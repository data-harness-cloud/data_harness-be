package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.BaseBusinessDictVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * BaseBusinessDict实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_base_business_dict")
public class BaseBusinessDict extends BaseModel {

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
     * 父级id。
     */
    private Long parentId;

    /**
     * 绑定类型。
     */
    private String bindType;

    /**
     * 绑定类型列表。
     */
    @TableField(exist = false)
    private List<String> bindTypeList;

    /**
     * 字典名称。
     */
    private String dictName;

    /**
     * 字典描述。
     */
    private String dictDesc;

    /**
     *　显示顺序
     */
    private Integer showOrder;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 修改者ID
     */
    private Long updateUserId;

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
     * bind_type / dict_name / dict_desc LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface BaseBusinessDictModelMapper extends BaseModelMapper<BaseBusinessDictVo, BaseBusinessDict> {
    }
    public static final BaseBusinessDictModelMapper INSTANCE = Mappers.getMapper(BaseBusinessDictModelMapper.class);
}
