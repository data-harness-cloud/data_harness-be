package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.StandardMainVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * StandardMain实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_standard_main")
public class StandardMain extends BaseModel {

    /**
     * 主键。
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
     * 分层关联项目id。
     */
    private Long projectId;

    /**
     * 标准目录id。
     */
    private Long standardDirectoryId;

    /**
     * 标准负责人id。
     */
    private Long standardHeaderId;

    /**
     * 标准名称。
     */
    private String standardName;

    /**
     * 标准编码。
     */
    private String standardCode;

    /**
     * 标准分类。
     */
    private String standardType;

    /**
     * 标准英语名称。
     */
    private String standardEnglish;

    /**
     * 标准描述。
     */
    private String standardDescription;

    /**
     * 标准录入方式。
     */
    private String standardInputMode;

    /**
     * 标准状态。
     */
    private String standardStatus;

    /**
     * 正则表达式。
     */
    private String standardRegular;

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
     * standard_name / standard_code / standard_type / standard_english / standard_description / standard_input_mode / standard_status LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface StandardMainModelMapper extends BaseModelMapper<StandardMainVo, StandardMain> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param standardMainVo 域对象。
         * @return 实体对象。
         */
        @Override
        StandardMain toModel(StandardMainVo standardMainVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param standardMain 实体对象。
         * @return 域对象。
         */
        @Override
        StandardMainVo fromModel(StandardMain standardMain);
    }
    public static final StandardMainModelMapper INSTANCE = Mappers.getMapper(StandardMainModelMapper.class);
}
