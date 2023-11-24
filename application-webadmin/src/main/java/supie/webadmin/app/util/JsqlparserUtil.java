package supie.webadmin.app.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import supie.common.core.object.MyPageParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/21 14:35
 * @path SDT-supie.webadmin.app.util-JsqlparserUtil
 */
@Slf4j
public class JsqlparserUtil {

    /**
     * 判断是否为查询语句
     *
     * @param sqlScript SQL 脚本
     * @return 查询语句返回 true
     * @author 王立宏
     * @date 2023/11/21 02:49
     */
    public static Boolean isQuerySql(String sqlScript) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sqlScript);
            return statement instanceof Select;
        } catch (JSQLParserException e) {
            // 处理分析异常
            return false;
        }
    }

    /**
     * 构建分页查询语句
     *
     * @param originalSqlQueryScript 原始 SQL 查询脚本
     * @param pageParam              页面参数
     * @return 具备分页功能的SQL语句
     * @author 王立宏
     * @date 2023/11/21 02:42
     */
    public static String buildLimitSqlScript(
            String originalSqlQueryScript, MyPageParam pageParam) {
        try {
            // 解析 SQL 查询语句
            Statement jsqlparserStatement = CCJSqlParserUtil.parse(originalSqlQueryScript);
            // 判断是否为 SELECT 语句
            if (jsqlparserStatement instanceof Select) {
                // 如果是 SELECT 语句，进行进一步处理
                Select select = (Select) jsqlparserStatement;
                SelectBody selectBody = select.getSelectBody();

                // 判断 SELECT 语句的类型
                if (selectBody instanceof PlainSelect) {
                    // 如果是 PlainSelect，表示是普通的 SELECT 查询
                    PlainSelect plainSelect = (PlainSelect) selectBody;

                    // 判断是否已经存在分页功能
                    if (plainSelect.getLimit() == null) {
                        // 不存在分页功能，添加分页逻辑
                        Limit limit = new Limit();
                        limit.setRowCount(new LongValue(pageParam.getPageSize()));
                        limit.setOffset(new LongValue((long) (pageParam.getPageNum() - 1) * pageParam.getPageSize()));
                        plainSelect.setLimit(limit); // 假设每页显示 10 条数据
//                        return plainSelect.toString();
                    } else {
                        log.warn("分页已存在于 SQL 查询中");
                    }
                } else {
                    // 不支持的 SELECT 语句类型
                    throw new RuntimeException("不支持的 SELECT 语句类型");
                }
                return select.toString() + ";";
            } else {
                // 如果不是 SELECT 语句，则给出相应提示
                throw new RuntimeException("不是 SELECT 语句");
            }
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建总数查询语句
     *
     * @param originalSqlQueryScript 原始 SQL 查询脚本
     * @return 分页查询的总数查询语句
     * @author 王立宏
     * @date 2023/11/21 02:43
     */
    public static String buildCountSqlScript(String originalSqlQueryScript) {
        // 使用正则表达式构建总数查询SQL语句
//        return originalSqlQueryScript.replaceAll("(?i)SELECT[\\s\\S]*?FROM", "SELECT COUNT(0) FROM");
        // 使用JSQLParser构建总数查询SQL语句
        try {
            Select select = (Select) CCJSqlParserUtil.parse(originalSqlQueryScript);
            SelectBody selectBody = select.getSelectBody();
            if (selectBody instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) selectBody;
                Function function = new Function();
                function.setName("COUNT");
                ExpressionList expressionList = new ExpressionList();
                List<Expression> list = new ArrayList<>();
                list.add(new LongValue(0));
                expressionList.setExpressions(list);
                function.setParameters(expressionList);
                List<SelectItem> functionList = new ArrayList<>();
                functionList.add(new SelectExpressionItem(function));
                plainSelect.setSelectItems(functionList);
                return select.toString() + ";";
            } else {
                throw new RuntimeException("Unsupported SELECT statement type.");
            }
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }

}
