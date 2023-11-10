package supie.common.core.object;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 实体对象数据组装参数构建器。
 * BaseService中的实体对象数据组装函数，会根据该参数对象进行数据组装。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@Builder
public class MyRelationParam {

    /**
     * 是否组装字典关联的标记。
     * 组装RelationDict和RelationConstDict注解标记的字段。
     */
    private boolean buildDict;

    /**
     * 是否组装一对一关联的标记。
     * 组装RelationOneToOne注解标记的字段。
     */
    private boolean buildOneToOne;

    /**
     * 是否组装一对多关联的标记。
     * 组装RelationOneToMany注解标记的字段。
     */
    private boolean buildOneToMany;

    /**
     * 在组装一对一关联的同时，是否继续关联从表中的字典。
     * 从表中RelationDict和RelationConstDict注解标记的字段。
     * 该字段为true时，无需设置buildOneToOne了。
     */
    private boolean buildOneToOneWithDict;

    /**
     * 是否组装主表对多对多中间表关联的标记。
     * 组装RelationManyToMany注解标记的字段。
     */
    private boolean buildRelationManyToMany;

    /**
     * 是否组装聚合计算关联的标记。
     * 组装RelationOneToManyAggregation和RelationManyToManyAggregation注解标记的字段。
     */
    private boolean buildRelationAggregation;

    /**
     * 关联表中，需要忽略的脱敏字段名。key是关联表实体对象名，如SysUser，value是对象字段名的集合，如userId。
     */
    @Getter
    private Map<String, Set<String>> ignoreMaskFieldMap;

    /**
     * 关联表中需要忽略的脱敏字段结合。
     * @param ignoreRelationMaskFieldSet 数据项格式为"实体对象名.对象属性名"，如 sysUser.userId。
     */
    public void setIgnoreMaskFieldSet(Set<String> ignoreRelationMaskFieldSet) {
        if (CollUtil.isEmpty(ignoreRelationMaskFieldSet)) {
            return;
        }
        ignoreMaskFieldMap = MapUtil.newHashMap();
        for (String ignoreField : ignoreRelationMaskFieldSet) {
            String[] fullFieldName = StrUtil.splitToArray(ignoreField, ".");
            Set<String> ignoreMaskFieldSet =
                    ignoreMaskFieldMap.computeIfAbsent(fullFieldName[0], k -> new HashSet<>());
            ignoreMaskFieldSet.add(fullFieldName[1]);
        }
    }

    /**
     * 便捷方法，返回仅做字典关联的参数对象。
     *
     * @return 返回仅做字典关联的参数对象。
     */
    public static MyRelationParam dictOnly() {
        return MyRelationParam.builder().buildDict(true).build();
    }

    /**
     * 便捷方法，返回仅做字典关联、一对一从表及其字典和聚合计算的参数对象。
     * NOTE: 对于一对多和多对多，这种从表数据是列表结果的关联，均不返回。
     *
     * @return 返回仅做字典关联、一对一从表及其字典和聚合计算的参数对象。
     */
    public static MyRelationParam normal() {
        return MyRelationParam.builder()
                .buildDict(true)
                .buildOneToOneWithDict(true)
                .buildRelationAggregation(true)
                .build();
    }

    /**
     * 便捷方法，返回全部关联的参数对象。
     *
     * @return 返回全部关联的参数对象。
     */
    public static MyRelationParam full() {
        return MyRelationParam.builder()
                .buildDict(true)
                .buildOneToOneWithDict(true)
                .buildRelationAggregation(true)
                .buildRelationManyToMany(true)
                .buildOneToMany(true)
                .build();
    }
}
