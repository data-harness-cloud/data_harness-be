package supie.webadmin.app.service.databasemanagement;

import supie.common.core.object.MyPageParam;

import java.util.List;
import java.util.Map;

/**
 * 为策略对象定义一个接口
 */
public interface Strategy {

    /**
     * 初始化连接信息，并连接数据库
     *
     * @param hostIp       主机 IP
     * @param hostPort     主机端口
     * @param databaseName 数据库名称
     * @param userName     用户名
     * @param password     密码
     * @param type     0为不需要校验是否需要创建库的权限  1为需要
     * @author 王立宏
     * @date 2023/11/06 04:51
     */
    void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password,int type);

    /**
     * 关闭连接
     */
    void closeAll();

    /**
     * 查询数据库数据表名及类型
     */
    List<Map<String,Object>> queryDatabaseTable(String databaseName);

    /**
     * 查询数据库数据的字段名及类型
     */
    List<Map<String,Object>> queryTableFields(String databaseName, String tableName);

    /**
     * 执行单条SQL语句。
     * @param sqlScript SQL语句。
     * @param pageParam 分页对象。如果为 null 则不进行分页处理。
     * @return
     */
    Map<String, Object> executeSql(String sqlScript, MyPageParam pageParam);

    /**
     * 执行位置数量的SQL
     * @param sql 字符（会将SQl以“;”切开成List来执行，结果也会按照语句的顺序来显示）
     * @return [
     *   {
     *     "isSuccess": true,
     *     "sql": "该信息所属的SQL语句。",
     *     "message": "执行信息，主要为错误时的错误消息。",
     *     "queryResultData": "查询语句查询到的数据集",
     *     "updateResultData": "修改语句影响的数据的条数"
     *   }
     * ]
     */
    List<Map<String, Object>> executeSqlList(String sql);

    /**
     * 查询可操作的所有数据库名称
     *
     * @return 该账户可操作的所有数据库集
     * @author 王立宏
     * @date 2023/11/14 03:57
     */
    List<String> queryAllDatabaseName();

    /**
     * 创建数据库
     *
     * @param databaseName 创建的数据库的名称
     * @author 王立宏
     * @date 2023/11/02 04:30
     */
    void createDatabase(String databaseName);

    /**
     * 获取表结构
     * @param tableName 表名
     */
    List<Map<String, Object>> queryTableStructure(String tableName);

}
