package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

import javax.validation.constraints.*;

/**
 * BaseBusinessDictDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("BaseBusinessDictDto对象")
@Data
public class BaseBusinessDictDto {

    /**
     * 租户号。
     */
    @ApiModelProperty(value = "租户号", required = true)
    @NotNull(message = "数据验证失败，租户号不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 数据所属人。
     */
    @ApiModelProperty(value = "数据所属人")
    private Long dataUserId;

    /**
     * 数据所属部门。
     */
    @ApiModelProperty(value = "数据所属部门")
    private Long dataDeptId;

    /**
     * 父级id。
     */
    @ApiModelProperty(value = "父级id")
    private Long parentId;

    /**
     * 绑定类型。
     */
    @ApiModelProperty(value = "绑定类型")
    private String bindType;

    /**
     * 绑定类型列表。
     */
    @ApiModelProperty(value = "绑定类型列表")
    private List<String> bindTypeList;

    /**
     * 字典名称。
     */
    @ApiModelProperty(value = "字典名称")
    private String dictName;

    /**
     * 字典描述。
     */
    @ApiModelProperty(value = "字典描述")
    private String dictDesc;

    /**
     *　显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer showOrder;

    /**
     * 创建者ID
     */
    @ApiModelProperty(value = "创建者ID")
    private Long createUserId;

    /**
     * 修改者ID
     */
    @ApiModelProperty(value = "修改者ID")
    private Long updateUserId;

    /**
     * updateTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤起始值(>=)")
    private String updateTimeStart;

    /**
     * updateTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤结束值(<=)")
    private String updateTimeEnd;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤起始值(>=)")
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤结束值(<=)")
    private String createTimeEnd;

    /**
     * bind_type / dict_name / dict_desc LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
