package supie.webadmin.app.liteFlow.model;

import lombok.Data;

/**
 * 描述：sql组件的数据模型
 *
 * @author 王立宏
 * @date 2023/10/29 14:35
 * @path SDT-supie.webadmin.app.liteFlow.model-SqlAndShellModel
 */
@Data
public class SqlAndShellModel {

    /**
     * 数据源ID project_datasource_id
     */
    private Long sourceId;
    /**
     * SQL 脚本语句
     */
    private String script;

}
