package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * PlanningClassificationVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("PlanningClassificationVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanningClassificationVo extends BaseVo {

    /**
     * 租户号。
     */
    @ApiModelProperty(value = "租户号")
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
     * 关联项目id。
     */
    @ApiModelProperty(value = "关联项目id")
    private Long projectId;

    /**
     * 分类名称。
     */
    @ApiModelProperty(value = "分类名称")
    private String classificationName;

    /**
     * 分类代码。
     */
    @ApiModelProperty(value = "分类代码")
    private String classificationCode;

    /**
     * 分类状态。
     */
    @ApiModelProperty(value = "分类状态")
    private String classificationStatus;

    /**
     * 分类描述。
     */
    @ApiModelProperty(value = "分类描述")
    private String classificationDescription;

    /**
     * createUserId 字典关联数据。
     */
    @ApiModelProperty(value = "createUserId 字典关联数据")
    private Map<String, Object> createUserIdDictMap;

    /**
     * updateUserId 字典关联数据。
     */
    @ApiModelProperty(value = "updateUserId 字典关联数据")
    private Map<String, Object> updateUserIdDictMap;

    /**
     * dataUserId 字典关联数据。
     */
    @ApiModelProperty(value = "dataUserId 字典关联数据")
    private Map<String, Object> dataUserIdDictMap;
}
