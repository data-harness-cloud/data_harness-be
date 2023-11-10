package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.StandardQuatityVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * StandardQuatity实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_standard_quatity")
public class StandardQuatity extends BaseModel {

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
     * 质量标准名称。
     */
    private String standardQualityName;

    /**
     * 质量标准编码。
     */
    private String standardQualityCode;

    /**
     * 质量标准分类。
     */
    private String staidardQualityType;

    /**
     * 质量标准父id。
     */
    private Long standardQualityParentId;

    /**
     * 质量校验正则。
     */
    private String standardQaulityRe;

    /**
     * 质量校验长度限制。
     */
    private String standardQualityLang;

    /**
     * 质量校验不为空。
     */
    private String standardQualityNotNull;

    /**
     * 质量校验中心关联规则。
     */
    private Long standardQualityQualityCenterId;

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
     * standard_quality_name / standard_quality_code / staidard_quality_type / standard_qaulity_re / standard_quality_lang / standard_quality_not_null LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    /**
     * id 的多对多关联表数据对象。
     */
    @TableField(exist = false)
    private StandardFieldQuatity standardFieldQuatity;

    @Mapper
    public interface StandardQuatityModelMapper extends BaseModelMapper<StandardQuatityVo, StandardQuatity> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param standardQuatityVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "standardFieldQuatity", expression = "java(mapToBean(standardQuatityVo.getStandardFieldQuatity(), supie.webadmin.app.model.StandardFieldQuatity.class))")
        @Override
        StandardQuatity toModel(StandardQuatityVo standardQuatityVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param standardQuatity 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "standardFieldQuatity", expression = "java(beanToMap(standardQuatity.getStandardFieldQuatity(), false))")
        @Override
        StandardQuatityVo fromModel(StandardQuatity standardQuatity);
    }
    public static final StandardQuatityModelMapper INSTANCE = Mappers.getMapper(StandardQuatityModelMapper.class);
}
