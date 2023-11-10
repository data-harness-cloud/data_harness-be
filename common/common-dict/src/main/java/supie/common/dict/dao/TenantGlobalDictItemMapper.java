package supie.common.dict.dao;

import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.dict.model.TenantGlobalDictItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 租户全局字典项目数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface TenantGlobalDictItemMapper extends BaseDaoMapper<TenantGlobalDictItem> {

    /**
     * 批量插入。
     *
     * @param dictItemList 字典条目列表。
     */
    @Insert("<script>"
            + "INSERT INTO zz_tenant_global_dict_item "
            + "    (id,"
            + "    dict_code,"
            + "    tenant_id,"
            + "    item_id,"
            + "    item_name,"
            + "    show_order,"
            + "    status,"
            + "    create_time,"
            + "    create_user_id,"
            + "    update_time,"
            + "    update_user_id,"
            + "    deleted_flag)"
            + "VALUES "
            + "<foreach collection=\"dictItemList\" index=\"index\" item=\"item\" separator=\",\" >"
            + "    (#{item.id},"
            + "    #{item.dictCode},"
            + "    #{item.tenantId},"
            + "    #{item.itemId},"
            + "    #{item.itemName},"
            + "    #{item.showOrder},"
            + "    #{item.status},"
            + "    #{item.createTime},"
            + "    #{item.createUserId},"
            + "    #{item.updateTime},"
            + "    #{item.updateUserId},"
            + "    #{item.deletedFlag})"
            + "</foreach>"
            + "</script>")
    void insertList(@Param("dictItemList") List<TenantGlobalDictItem> dictItemList);
}
