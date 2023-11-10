package supie.webadmin.app.liteFlow.model;

import lombok.Data;

/**
 * 描述：数据源连接对象
 *
 * @author 王立宏
 * @date 2023/11/9 9:51
 * @path SDT-supie.webadmin.app.liteFlow.model-DatasourceContentModel
 */
@Data
public class DatasourceContentModel {

    /**
     * 数据源类型
     */
    private String databaseType;
    /**
     * 主机地址
     */
    private String ip;
    /**
     * 主机端口
     */
    private String port;
    /**
     * 数据库名
     */
    private String databaseName;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

}
