package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.upms.vo.SysPostVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 岗位实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_sys_post")
public class SysPost extends BaseModel {

    /**
     * 岗位Id。
     */
    @TableId(value = "post_id")
    private Long postId;

    /**
     * 岗位名称。
     */
    private String postName;

    /**
     * 岗位层级，数值越小级别越高。
     */
    @TableField(value = "post_level")
    private Integer postLevel;

    /**
     * 是否领导岗位。
     */
    private Boolean leaderPost;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    /**
     * postId 的多对多关联表数据对象。
     */
    @TableField(exist = false)
    private SysDeptPost sysDeptPost;

    @Mapper
    public interface SysPostModelMapper extends BaseModelMapper<SysPostVo, SysPost> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param sysPostVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "sysDeptPost", expression = "java(mapToBean(sysPostVo.getSysDeptPost(), supie.webadmin.upms.model.SysDeptPost.class))")
        @Override
        SysPost toModel(SysPostVo sysPostVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param sysPost 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "sysDeptPost", expression = "java(beanToMap(sysPost.getSysDeptPost(), false))")
        @Override
        SysPostVo fromModel(SysPost sysPost);
    }
    public static final SysPostModelMapper INSTANCE = Mappers.getMapper(SysPostModelMapper.class);
}
