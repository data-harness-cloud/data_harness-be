package supie.webadmin.app.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将数据库表的创建语句复制到指定文件中。
 */
public class CopyTableCreateStatements {
    // 输入和输出文件路径
    private static final String INPUT_FILE_PATH = "/sdt_main.sql";
    private static final String OUTPUT_FILE_PATH = "/output.sql";

    public static void main(String[] args) {
        // 指定表名列表
        List<String> tableNameList = Arrays.asList("zz_area_code","sdt_base_business_dict","sdt_base_business_file","sdt_customize_route","sdt_definition_dimension","sdt_definition_dimension_level","sdt_definition_dimension_property_column","sdt_definition_dimension_publish_record","sdt_definition_index","sdt_definition_index_model_field_relation","sdt_dev_ai_chat_dialogue","sdt_dev_console","sdt_dev_liteflow_log","sdt_dev_liteflow_node","sdt_dev_liteflow_ruler","sdt_dev_liteflow_script","sdt_external_app","sdt_external_app_customize_route","sdt_model_desgin_field","sdt_model_logical_main","sdt_model_physics_script","sdt_planning_classification","sdt_planning_process","sdt_planning_theme","sdt_planning_warehouse_layer","sdt_project_datasource","sdt_project_datasource_relation","sdt_project_datasource_template_dict","sdt_project_engine","sdt_project_host_relation","sdt_project_main","sdt_project_member","sdt_remote_host","sdt_scheduling_tasks","sdt_seatunnel_config","sdt_standard_directory","sdt_standard_field","sdt_standard_field_quality","sdt_standard_main","sdt_standard_quality",
                "sdt_sys_data_perm","sdt_sys_data_perm_dept","sdt_sys_data_perm_menu","sdt_sys_data_perm_user","sdt_sys_dept","sdt_sys_dept_post","sdt_sys_dept_relation","sdt_sys_menu","sdt_sys_menu_perm_code","sdt_sys_perm","sdt_sys_perm_code","sdt_sys_perm_code_perm","sdt_sys_perm_module","sdt_sys_perm_whitelist","sdt_sys_post","sdt_sys_role","sdt_sys_role_menu","sdt_sys_user","sdt_sys_user","sdt_sys_user_post","sdt_sys_user_role",
                "zz_sys_operation_log",
                "zz_global_dict","zz_global_dict_item","zz_tenant_global_dict","zz_tenant_global_dict_item");

        Map<String, String> stringStringMap = parseSQLFile(INPUT_FILE_PATH);
        StringBuilder createAllTableSql = new StringBuilder();
        for (String tableName : tableNameList) {
//            System.out.println("==============================================》" + tableName + "《==============================================");
            String cheateTableSql = stringStringMap.get(tableName);
            if (cheateTableSql == null) cheateTableSql = tableName + " ----> null\n";
            createAllTableSql.append(cheateTableSql).append("\n");
            System.out.println(cheateTableSql);
        }
        // 调用方法将 StringBuilder 内容写入新文件
        writeStringBuilderToFile(createAllTableSql, OUTPUT_FILE_PATH);
    }
    public static void writeStringBuilderToFile(StringBuilder content, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.append(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Map<String, String> parseSQLFile(String filePath) {
        Map<String, String> tableMap = new LinkedHashMap<>();

        // 读取文件内容并匹配表结构
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            StringBuilder currentTableContent = new StringBuilder();
            String currentTableName = null;

            while ((line = reader.readLine()) != null) {
                // 匹配表名
                Matcher matcher = Pattern.compile("--[\\s-]*Table structure for (\\w+)[\\s-]*").matcher(line);
                if (matcher.matches()) {
                    // 保存上一个表的内容
                    if (currentTableName != null) {
                        tableMap.put(currentTableName, currentTableContent.toString().trim());
                    }

                    // 开始新表的解析
                    currentTableName = matcher.group(1);
                    currentTableContent = new StringBuilder();
                }

                // 添加当前行到表内容
                currentTableContent.append(line).append("\n");
            }

            // 保存最后一个表的内容
            if (currentTableName != null) {
                tableMap.put(currentTableName, currentTableContent.toString().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tableMap;
    }

}
