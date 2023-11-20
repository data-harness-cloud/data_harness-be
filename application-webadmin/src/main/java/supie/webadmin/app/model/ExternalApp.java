package supie.webadmin.app.model;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.constant.ApplicationConstant;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.common.core.util.RsaUtil;
import supie.webadmin.app.vo.ExternalAppVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.nio.charset.StandardCharsets;

/**
 * ExternalApp实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_external_app")
public class ExternalApp extends BaseModel implements Cloneable {

    @TableField(exist = false)
    public static final String APP_KEY_PREFIX = "data_harness_cloud:";

    /**
     * 主键ID。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 应用名称。
     */
    private String appName;

    /**
     * App描述。
     */
    private String appDescribe;

    /**
     * AppKey。
     */
    private String appKey;

    /**
     * 认证方式（1：key认证。2：无认证）。
     */
    private Integer authenticationMethod;

    /**
     * 业务过程ID
     */
    private Long processId;

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
     * app_name / app_describe / app_key LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    @RelationOneToOne(
            masterIdField = "processId",
            slaveModelClass = PlanningProcess.class,
            slaveIdField = "id")
    @TableField(exist = false)
    private PlanningProcess planningProcess;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface ExternalAppModelMapper extends BaseModelMapper<ExternalAppVo, ExternalApp> {
    }
    public static final ExternalAppModelMapper INSTANCE = Mappers.getMapper(ExternalAppModelMapper.class);

    /**
     * 生成 AppKey 并且加密
     *
     * @param keySuffix 应用密钥
     * @return 字符串
     * @author 王立宏
     * @date 2023/11/20 03:35
     */
    public static String generateAppKey(String keySuffix) {
        if (keySuffix == null) {
            keySuffix = new String(RandomUtil.randomBytes(10), StandardCharsets.UTF_8);
        }
        String appKey = ExternalApp.APP_KEY_PREFIX + keySuffix;
        appKey = RsaUtil.encrypt(appKey, ApplicationConstant.PUBLIC_KEY);
        return appKey;
    }

    /**
     * 验证 AppKey 是否为本平台发放的 Key
     * @return 合法返回 true
     */
    public static boolean verifyAppKey(String appKey) {
        // 解密
        try {
            appKey = RsaUtil.decrypt(appKey, ApplicationConstant.PRIVATE_KEY);
            return appKey.startsWith(ExternalApp.APP_KEY_PREFIX);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ExternalApp clone() {
        try {
            return (ExternalApp) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("对象克隆失败！" + e);
        }
    }
}
