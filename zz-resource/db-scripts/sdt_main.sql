SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for zz_area_code
-- ----------------------------
DROP TABLE IF EXISTS `zz_area_code`;
CREATE TABLE `zz_area_code`  (
  `area_id` bigint UNSIGNED NOT NULL COMMENT '行政区划主键Id',
  `area_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '行政区划名称',
  `area_level` int NOT NULL COMMENT '行政区划级别 (1: 省级别 2: 市级别 3: 区级别)',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级行政区划Id',
  PRIMARY KEY (`area_id`) USING BTREE,
  INDEX `idx_level`(`area_level` ASC) USING BTREE,
  INDEX `idx_area_name`(`area_name` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '行政区划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_base_business_dict
-- ----------------------------
DROP TABLE IF EXISTS `sdt_base_business_dict`;
CREATE TABLE `sdt_base_business_dict`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级id',
  `bind_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定类型',
  `dict_name` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_desc` varchar(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典描述',
  `show_order` int NULL DEFAULT NULL COMMENT '显示顺序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据项目-基础附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_base_business_file
-- ----------------------------
DROP TABLE IF EXISTS `sdt_base_business_file`;
CREATE TABLE `sdt_base_business_file`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `bind_id` bigint NULL DEFAULT NULL COMMENT '绑定id',
  `bind_str_id` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定字符id',
  `bind_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定类型',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_json` varchar(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'json字段',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小',
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据项目-基础附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_customize_route
-- ----------------------------
DROP TABLE IF EXISTS `sdt_customize_route`;
CREATE TABLE `sdt_customize_route`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `route_describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址（不可重复）',
  `request_type` int NULL DEFAULT NULL COMMENT '请求类型（1：GET。2：POST。默认为POST）',
  `state` int NULL DEFAULT NULL COMMENT '状态（1：上线。-1：下线）',
  `project_id` bigint NULL DEFAULT NULL COMMENT '存算引擎项目ID',
  `database_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '目标数据库名称',
  `sql_script` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'SQL语句',
  `parameter` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '参数集（JSON字符串形式存储）',
  `process_id` bigint NULL DEFAULT NULL COMMENT '业务规程ID',
  `definition_index_id` bigint NULL DEFAULT NULL COMMENT '指标ID（一对一，如果没有表示不为指标的API）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '自定义动态路由表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_definition_dimension
-- ----------------------------
DROP TABLE IF EXISTS `sdt_definition_dimension`;
CREATE TABLE `sdt_definition_dimension`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '分层关联项目id',
  `process_id` bigint NULL DEFAULT NULL COMMENT '关联业务过程id',
  `dimension_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度类型',
  `dimension_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度名称',
  `dimension_en_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度英文名称',
  `dimension_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度编码',
  `dimension_describe` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度描述',
  `dimension_directory_id` bigint NULL DEFAULT NULL COMMENT '维度目录id',
  `is_auto_create_table` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自动建表',
  `dimension_period_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '周期类型',
  `dimension_period_start_date` datetime NULL DEFAULT NULL COMMENT '周期开始日期',
  `dimension_period_end_date` datetime NULL DEFAULT NULL COMMENT '周期结束日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-数据标准-维度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_definition_dimension_level
-- ----------------------------
DROP TABLE IF EXISTS `sdt_definition_dimension_level`;
CREATE TABLE `sdt_definition_dimension_level`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `dimension_id` bigint NULL DEFAULT NULL COMMENT '维度表id',
  `level_number` int NULL DEFAULT NULL COMMENT '层级数字',
  `level_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '层级名称',
  `level_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '层级编码',
  `level_scale` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预计规模',
  `level_enable` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划_数据标准-维度层级' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_definition_dimension_property_column
-- ----------------------------
DROP TABLE IF EXISTS `sdt_definition_dimension_property_column`;
CREATE TABLE `sdt_definition_dimension_property_column`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `dimension_id` bigint NULL DEFAULT NULL COMMENT '维度表id',
  `property_name` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性名称',
  `property_en_name` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性英文名称',
  `property_data_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据类型',
  `property_description` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务描述',
  `property_decimal_length` int NULL DEFAULT NULL COMMENT '小数点',
  `is_primary` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划_数据标准-维度属性字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_definition_dimension_publish_record
-- ----------------------------
DROP TABLE IF EXISTS `sdt_definition_dimension_publish_record`;
CREATE TABLE `sdt_definition_dimension_publish_record`  (
  `id` bigint NOT NULL COMMENT '编号',
  `str_id` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字符编号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `dimension_id` bigint NULL DEFAULT NULL COMMENT '维度表id',
  `publish_database` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库',
  `publish_database_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `publish_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布类型',
  `physics_table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物理表名',
  `publish_description` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '业务定义-数据维度发布记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_definition_index
-- ----------------------------
DROP TABLE IF EXISTS `sdt_definition_index`;
CREATE TABLE `sdt_definition_index`  (
  `id` bigint NOT NULL COMMENT '编号',
  `str_id` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字符编号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `index_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指标类型',
  `index_name` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指标名称',
  `index_en_name` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指标英文名称',
  `index_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指标编码',
  `index_level` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指标等级;核心、重要、一般、临时',
  `process_id` bigint NULL DEFAULT NULL COMMENT '业务过程id',
  `index_description` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务描述',
  `customize_route_id` bigint NOT NULL COMMENT '动态路由ID（一对一）',
  `model_desgin_field_id` bigint NULL DEFAULT NULL COMMENT '关联字段',
  `data_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据类型',
  `product_period` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生产周期',
  `project_id` bigint NULL DEFAULT NULL COMMENT '所属项目id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '业务定义-指标管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_definition_index_model_field_relation
-- ----------------------------
DROP TABLE IF EXISTS `sdt_definition_index_model_field_relation`;
CREATE TABLE `sdt_definition_index_model_field_relation`  (
  `id` bigint NOT NULL COMMENT '编号',
  `str_id` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字符编号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `index_id` bigint NULL DEFAULT NULL COMMENT '指标id',
  `index_process_id` bigint NULL DEFAULT NULL COMMENT '指标业务过程id',
  `model_id` bigint NULL DEFAULT NULL COMMENT '模型id',
  `model_field_id` bigint NULL DEFAULT NULL COMMENT '模型字段id',
  `model_process_id` bigint NULL DEFAULT NULL COMMENT '模型业务过程id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '业务定义-指标管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_dev_ai_chat_dialogue
-- ----------------------------
DROP TABLE IF EXISTS `sdt_dev_ai_chat_dialogue`;
CREATE TABLE `sdt_dev_ai_chat_dialogue`  (
  `id` bigint NOT NULL COMMENT '主表控制台id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `dialogue_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对话名称',
  `dialogue_question` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '对话问题',
  `dialogue_answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '相应答案',
  `dialogue_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '问答类型：对话、工具调用、代码执行',
  `dialogue_data_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据应用类型：数据探源、生成图表、归因总结等等',
  `dialogue_role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '问答角色',
  `dialogue_prompt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '问答预设提示语',
  `dialogue_str_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对话标识ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '查询控制台子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_dev_console
-- ----------------------------
DROP TABLE IF EXISTS `sdt_dev_console`;
CREATE TABLE `sdt_dev_console`  (
  `id` bigint NOT NULL COMMENT '主表控制台id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `query_console_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '查询控制台名称',
  `query_statements` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '查询语句',
  `response_results` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '响应结果',
  `query_database` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所选数据库',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '查询控制台子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_dev_liteflow_log
-- ----------------------------
DROP TABLE IF EXISTS `sdt_dev_liteflow_log`;
CREATE TABLE `sdt_dev_liteflow_log`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `ruler_id` bigint NOT NULL COMMENT '规则链ID',
  `scheduling_tasks_id` bigint NOT NULL COMMENT '任务ID',
  `run_version` int NULL DEFAULT NULL COMMENT '运行版本',
  `run_time` datetime NULL DEFAULT NULL COMMENT '运行时间',
  `run_result` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '运行结果',
  `run_result_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '运行结果信息',
  `log_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '日志文件名称（rulerId_version_nowDate.log）',
  `log_file_json` varchar(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '日志文件Json字段',
  `log_file_size` bigint NULL DEFAULT NULL COMMENT '日志文件大小',
  `is_delete` int NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '修改者ID',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'LiteFlow流程执行日志（任务执行日志）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_dev_liteflow_node
-- ----------------------------
DROP TABLE IF EXISTS `sdt_dev_liteflow_node`;
CREATE TABLE `sdt_dev_liteflow_node`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `ruler_id` bigint NOT NULL COMMENT '规则链ID（EL-ID）',
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组件ID',
  `node_tag` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件标签',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '节点名称',
  `status` int NULL DEFAULT NULL COMMENT '节点状态（1：启用。0：停用）',
  `field_json_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '字段值数据（JSON数据）',
  `execution_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '上一次执行结果信息',
  `is_delete` int NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '修改者ID',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人ID',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '流程规则组件属性值存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_dev_liteflow_ruler
-- ----------------------------
DROP TABLE IF EXISTS `sdt_dev_liteflow_ruler`;
CREATE TABLE `sdt_dev_liteflow_ruler`  (
  `id` bigint NOT NULL COMMENT '编号',
  `str_id` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '字符编号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `application_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '应用名称',
  `chain_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '流程chain名称',
  `chain_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '流程chain描述',
  `el_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '规则表达式',
  `chain_structure_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '流程结构JSON数据',
  `online_type` int NULL DEFAULT NULL COMMENT '上线类型（0：开发中。1：发布上线）',
  `previous_version_id` bigint NULL DEFAULT NULL COMMENT '上一个版本的ID',
  `process_id` bigint NULL DEFAULT NULL COMMENT '过程ID',
  `project_id` bigint NULL DEFAULT NULL COMMENT '所属项目ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '数据开发-流程编排-liteflow规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_dev_liteflow_script
-- ----------------------------
DROP TABLE IF EXISTS `sdt_dev_liteflow_script`;
CREATE TABLE `sdt_dev_liteflow_script`  (
  `id` bigint NOT NULL COMMENT '编号',
  `str_id` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '字符编号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `application_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '应用名称',
  `script_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '脚本id',
  `script_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '脚本名称',
  `script_data` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '脚本内容',
  `script_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '脚本种类',
  `script_language` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '脚本语言',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '数据开发-流程编排-liteflow脚本表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_external_app
-- ----------------------------
DROP TABLE IF EXISTS `sdt_external_app`;
CREATE TABLE `sdt_external_app`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '应用名称',
  `app_describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'App描述',
  `app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'AppKey',
  `authentication_method` int NOT NULL COMMENT '认证方式（1：key认证。2：无认证）',
  `process_id` bigint NULL DEFAULT NULL COMMENT '业务过程ID',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '外部App表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_external_app_customize_route
-- ----------------------------
DROP TABLE IF EXISTS `sdt_external_app_customize_route`;
CREATE TABLE `sdt_external_app_customize_route`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `customize_route_id` bigint NOT NULL COMMENT '动态路由ID',
  `external_app_id` bigint NOT NULL COMMENT '外部APP ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '外部APP与动态路由对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_model_desgin_field
-- ----------------------------
DROP TABLE IF EXISTS `sdt_model_desgin_field`;
CREATE TABLE `sdt_model_desgin_field`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `model_field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段名称',
  `model_field_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段代码',
  `model_field_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段类型',
  `model_field_index` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段指标',
  `model_field_meta_standard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联元数据标准',
  `model_field_value_standard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联值域校验',
  `model_field_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段描述',
  `model_field_length` int NULL DEFAULT NULL COMMENT '长度',
  `model_field_decimal_point` int NULL DEFAULT NULL COMMENT '小数点（小数点<=长度）',
  `model_field_is_null` int NULL DEFAULT NULL COMMENT '是否为空（0：可为空。1：不为空）',
  `model_field_default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认值',
  `model_field_key` int NULL DEFAULT 0 COMMENT '主键字段（0：非主键。1：主键）',
  `model_field_ppartition` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段分区',
  `model_field_source_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型表来源字段名',
  `model_field_source_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型表来源字段类型',
  `model_field_source_id` bigint NULL DEFAULT NULL COMMENT '模型表来源字段ID',
  `model_field_source_table` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段来源表',
  `model_field_mapping` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '业务映射字段Json数据',
  `model_id` bigint NULL DEFAULT NULL COMMENT '模型id',
  `process_id` bigint NULL DEFAULT NULL COMMENT '业务过程id',
  `model_quote_standard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型引用的标准',
  `model_field_script` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '字段计算脚本，仅展示关系',
  `show_order` int NULL DEFAULT NULL COMMENT '显示顺序',
  `standard_main_id` bigint NULL DEFAULT NULL COMMENT '数据标准主表ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-模型设计-模型字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_model_logical_main
-- ----------------------------
DROP TABLE IF EXISTS `sdt_model_logical_main`;
CREATE TABLE `sdt_model_logical_main`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '分层关联项目id',
  `process_id` bigint NULL DEFAULT NULL COMMENT '关联业务过程id',
  `model_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型名称',
  `model_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型代码',
  `model_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型状态',
  `model_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型描述',
  `model_datasource_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型数据源类型',
  `model_class_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型业务分类',
  `model_theme_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型主题域类型',
  `model_layer_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型分层类型',
  `model_table_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型表类型，字典表',
  `model_update_cycle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型更新周期，字典类型，对应命名',
  `model_warehouse_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型数仓表类型',
  `model_table_permissions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型表权限类型，个人还是项目共享',
  `model_table_alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型表别名',
  `model_table_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型表等级，字典表',
  `model_life_cycle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型表生命周期，字典',
  `model_publish_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型发布状态',
  `model_sql_script` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '模型SQL脚本',
  `model_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型版本',
  `model_physical_database` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型发布物理库',
  `model_datasource_id` bigint NULL DEFAULT NULL COMMENT '模型数据源',
  `model_public_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型发布类型，新建表、覆盖表',
  `model_public_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型发布描述',
  `warehouse_layer_id` bigint NULL DEFAULT NULL COMMENT '数据分层id',
  `model_table_custom_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '自定义表名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-模型设计-模型逻辑表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_model_physics_script
-- ----------------------------
DROP TABLE IF EXISTS `sdt_model_physics_script`;
CREATE TABLE `sdt_model_physics_script`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '分层关联项目id',
  `model_id` bigint NULL DEFAULT NULL COMMENT '模型id',
  `model_physics_database` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '物理模型建库语句',
  `model_physics_table_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物理模型表名称',
  `model_physics_table` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '物理模型建表语句',
  `model_physics_state` int NULL DEFAULT NULL COMMENT '物理模型状态（1、未建表。2、已建表）',
  `model_physics_datasource_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物理模型数据源',
  `model_physics_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '物理模型描述',
  `model_physics_premise` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型权限：是否项目共享',
  `model_physics_header_id` bigint NULL DEFAULT NULL COMMENT '负责人id',
  `show_order` int NULL DEFAULT NULL COMMENT '显示顺序',
  `planning_warehouse_layer_id` bigint NOT NULL COMMENT '分层id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-模型设计-模型物理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_planning_classification
-- ----------------------------
DROP TABLE IF EXISTS `sdt_planning_classification`;
CREATE TABLE `sdt_planning_classification`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '关联项目id',
  `classification_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类名称',
  `classification_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类代码',
  `classification_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类状态',
  `classification_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-数据架构-业务分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_planning_process
-- ----------------------------
DROP TABLE IF EXISTS `sdt_planning_process`;
CREATE TABLE `sdt_planning_process`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '分层关联项目id',
  `classification_id` bigint NULL DEFAULT NULL COMMENT '关联分类id',
  `process_theme_id` bigint NULL DEFAULT NULL COMMENT '关联主题域id',
  `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务过程名称',
  `process_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务过程代码',
  `process_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务过程状态',
  `process_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务过程描述',
  `process_parent_id` bigint NULL DEFAULT NULL COMMENT '父过程id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-数据架构-业务过程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_planning_theme
-- ----------------------------
DROP TABLE IF EXISTS `sdt_planning_theme`;
CREATE TABLE `sdt_planning_theme`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '分层关联项目id',
  `classification_id` bigint NULL DEFAULT NULL COMMENT '关联分类id',
  `theme_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主题名称',
  `theme_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主题代码',
  `theme_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主题状态',
  `theme_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主题描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-数据架构-主题域表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_planning_warehouse_layer
-- ----------------------------
DROP TABLE IF EXISTS `sdt_planning_warehouse_layer`;
CREATE TABLE `sdt_planning_warehouse_layer`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '分层关联项目id',
  `house_layer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分层名称',
  `house_layer_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分层编码',
  `house_layer_status` int NULL DEFAULT NULL COMMENT '分层状态',
  `house_layer_database` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分层关联库',
  `house_layer_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分层描述',
  `house_layer_header_user_id` bigint NULL DEFAULT NULL COMMENT '分层负责人id',
  `house_layer_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分层类型：是否为维度层',
  `house_layer_datasource_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分层数据源类型',
  `datasource_id` bigint NULL DEFAULT NULL COMMENT '数据源id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-数据仓库分层表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_project_datasource
-- ----------------------------
DROP TABLE IF EXISTS `sdt_project_datasource`;
CREATE TABLE `sdt_project_datasource`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `datasource_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据源名称',
  `datasource_type` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据源类型',
  `datasource_show_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据源显示名称',
  `datasource_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '数据源描述',
  `datasource_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '数据源连接信息存储为json字段',
  `datasource_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据源图标',
  `datasource_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据源分组',
  `datasource_connect` int NULL DEFAULT NULL COMMENT '数据源连通性',
  `is_meta_collect` int NULL DEFAULT NULL COMMENT '是否采集元数据',
  `project_id` bigint NULL DEFAULT NULL COMMENT '所属项目ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据项目-数据源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_project_datasource_relation
-- ----------------------------
DROP TABLE IF EXISTS `sdt_project_datasource_relation`;
CREATE TABLE `sdt_project_datasource_relation`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '项目id',
  `datasource_id` bigint NULL DEFAULT NULL COMMENT '数据源id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据项目-项目数据源关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_project_datasource_template_dict
-- ----------------------------
DROP TABLE IF EXISTS `sdt_project_datasource_template_dict`;
CREATE TABLE `sdt_project_datasource_template_dict`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `template_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板类型',
  `template_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板名称',
  `template_source` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'source配置',
  `template_sink` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'sink配置',
  `template_trans` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '转换配置',
  `template_icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据项目-项目数据源模板字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_project_engine
-- ----------------------------
DROP TABLE IF EXISTS `sdt_project_engine`;
CREATE TABLE `sdt_project_engine`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `engine_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引擎名称',
  `engine_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引擎类型',
  `engine_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引擎地址',
  `engine_port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引擎端口',
  `engine_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引擎用户名',
  `engine_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引擎密码',
  `engine_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引擎位置',
  `engine_config` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引擎配置i文件',
  `engine_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引擎状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据项目-存算引擎表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_project_host_relation
-- ----------------------------
DROP TABLE IF EXISTS `sdt_project_host_relation`;
CREATE TABLE `sdt_project_host_relation`  (
  `id` bigint NOT NULL COMMENT '主键',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '项目id',
  `host_id` bigint NULL DEFAULT NULL COMMENT '主机id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据项目-项目数据源关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_project_main
-- ----------------------------
DROP TABLE IF EXISTS `sdt_project_main`;
CREATE TABLE `sdt_project_main`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目名称',
  `project_description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目描述',
  `project_engine_id` bigint NULL DEFAULT NULL COMMENT '项目存算引擎',
  `project_currents_status` int NULL DEFAULT NULL COMMENT '项目状态',
  `project_group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目组名称',
  `project_header_id` bigint NULL DEFAULT NULL COMMENT '项目负责人',
  `project_flow_status` bigint NULL DEFAULT NULL COMMENT '项目流程状态',
  `project_flow_approve_status` bigint NULL DEFAULT NULL COMMENT '项目审批状态字段',
  `project_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目英文名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据项目—项目管理主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_project_member
-- ----------------------------
DROP TABLE IF EXISTS `sdt_project_member`;
CREATE TABLE `sdt_project_member`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `member_user_id` bigint NULL DEFAULT NULL COMMENT '成员关联用户表id',
  `member_project_id` bigint NULL DEFAULT NULL COMMENT '关联项目id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据项目-项目成员关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_remote_host
-- ----------------------------
DROP TABLE IF EXISTS `sdt_remote_host`;
CREATE TABLE `sdt_remote_host`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `show_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '显示名称',
  `host_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'SSH-IP地址',
  `host_port` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '端口',
  `host_key_file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ssh-key文件存储路径',
  `login_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '修改者ID',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人ID',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门ID',
  `project_id` bigint NULL DEFAULT NULL COMMENT '项目关联id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '远程主机配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_scheduling_tasks
-- ----------------------------
DROP TABLE IF EXISTS `sdt_scheduling_tasks`;
CREATE TABLE `sdt_scheduling_tasks`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '修改者ID',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门ID',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人ID',
  `is_delete` int NOT NULL DEFAULT 1 COMMENT '逻辑删除',
  `task_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务名称',
  `task_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务所属分组',
  `task_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务描述',
  `task_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务状态（未上线、正常、暂停、出错、阻塞、已下线）',
  `task_class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务执行类',
  `configuration_type` int NULL DEFAULT NULL COMMENT '配置类型（1：常规，2：crontab\n）',
  `cron_expression` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `cron_analytic_information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'cron解析信息',
  `start_time` datetime NULL DEFAULT NULL COMMENT '起始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '截止时间',
  `task_data_map` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务参数Map类型数据',
  `ruler_id` bigint NULL DEFAULT NULL COMMENT '任务规则链ID',
  `run_type` int NOT NULL COMMENT '任务执行类型（1：单次执行、2：周期执行）',
  `run_number` int NULL DEFAULT NULL COMMENT '任务运行次数',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  `error_number` int NULL DEFAULT NULL COMMENT '运行失败次数',
  `success_number` int NULL DEFAULT NULL COMMENT '运行成功次数',
  `process_id` bigint NULL DEFAULT NULL COMMENT '过程ID',
  `project_id` bigint NULL DEFAULT NULL COMMENT '所属项目ID',
  `classification_id` bigint NULL DEFAULT NULL COMMENT '业务分类ID',
  `process_theme_id` bigint NULL DEFAULT NULL COMMENT '主题域ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_seatunnel_config
-- ----------------------------
DROP TABLE IF EXISTS `sdt_seatunnel_config`;
CREATE TABLE `sdt_seatunnel_config`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `show_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '显示名称',
  `call_mode` int NULL DEFAULT NULL COMMENT 'Seatunnel 的调用方式（1：接口。2：ssh。默认为1）',
  `localhost_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Seatunnel 根地址',
  `submit_job_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Seatunnel 的提交job的地址（默认为版本2.3.3的Submit Job地址：/hazelcast/rest/maps/submit-job）',
  `remote_host_id` bigint NULL DEFAULT NULL COMMENT '远程主机ID',
  `seatunnel_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'seatunnel安装路径',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '修改者ID',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人ID',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门ID',
  `project_id` bigint NULL DEFAULT NULL COMMENT '关联项目id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'seatunnel配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_standard_directory
-- ----------------------------
DROP TABLE IF EXISTS `sdt_standard_directory`;
CREATE TABLE `sdt_standard_directory`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '分层关联项目id',
  `directory_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标准目录名称',
  `directory_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标准目录编码',
  `directory_parent_id` bigint NULL DEFAULT NULL COMMENT '父目录id',
  `directory_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-数据标准-目录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_standard_field
-- ----------------------------
DROP TABLE IF EXISTS `sdt_standard_field`;
CREATE TABLE `sdt_standard_field`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `staidard_id` bigint NULL DEFAULT NULL COMMENT '标准主表id',
  `standard_field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段标准名称',
  `standard_field_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段标准编码',
  `standard_field_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-数据标准-数据字段标准' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_standard_field_quality
-- ----------------------------
DROP TABLE IF EXISTS `sdt_standard_field_quality`;
CREATE TABLE `sdt_standard_field_quality`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `staidard_id` bigint NULL DEFAULT NULL COMMENT '标准主表id',
  `staidard_field_id` bigint NULL DEFAULT NULL COMMENT '标准字段id',
  `staidard_quality_id` bigint NULL DEFAULT NULL COMMENT '质量校验id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-数据标准-数据字段质量关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_standard_main
-- ----------------------------
DROP TABLE IF EXISTS `sdt_standard_main`;
CREATE TABLE `sdt_standard_main`  (
  `id` bigint NOT NULL COMMENT '主键',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `project_id` bigint NULL DEFAULT NULL COMMENT '分层关联项目id',
  `standard_directory_id` bigint NULL DEFAULT NULL COMMENT '标准目录id',
  `standard_header_id` bigint NULL DEFAULT NULL COMMENT '标准负责人id',
  `standard_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标准名称',
  `standard_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标准编码',
  `standard_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标准分类',
  `standard_english` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标准英语名称',
  `standard_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标准描述',
  `standard_input_mode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标准录入方式',
  `standard_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标准状态',
  `standard_regular` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '正则表达式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-数据标准-主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_standard_quality
-- ----------------------------
DROP TABLE IF EXISTS `sdt_standard_quality`;
CREATE TABLE `sdt_standard_quality`  (
  `id` bigint NOT NULL COMMENT '租户号',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `data_user_id` bigint NULL DEFAULT NULL COMMENT '数据所属人',
  `data_dept_id` bigint NULL DEFAULT NULL COMMENT '数据所属部门',
  `is_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `standard_quality_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '质量标准名称',
  `standard_quality_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '质量标准编码',
  `staidard_quality_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '质量标准分类',
  `standard_quality_parent_id` bigint NULL DEFAULT NULL COMMENT '质量标准父id',
  `standard_qaulity_re` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '质量校验正则',
  `standard_quality_lang` int NULL DEFAULT NULL COMMENT '质量校验长度限制（正数->大等于。负数->小等于）',
  `standard_quality_not_null` int NULL DEFAULT NULL COMMENT '质量校验不为空（1：不为空。0：可为空）',
  `custom_judgment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SQL条件',
  `standard_quality_quality_center_id` bigint NULL DEFAULT NULL COMMENT '质量校验中心关联规则',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据规划-数据标准-数据质量表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_sys_data_perm
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_data_perm`;
CREATE TABLE `sdt_sys_data_perm`  (
  `data_perm_id` bigint NOT NULL COMMENT '主键',
  `data_perm_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '显示名称',
  `rule_type` tinyint NOT NULL COMMENT '数据权限规则类型(0: 全部可见 1: 只看自己 2: 只看本部门 3: 本部门及子部门 4: 多部门及子部门 5: 自定义部门列表)。',
  `create_user_id` bigint NOT NULL COMMENT '创建者Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新者Id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`data_perm_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_sys_data_perm_dept
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_data_perm_dept`;
CREATE TABLE `sdt_sys_data_perm_dept`  (
  `data_perm_id` bigint NOT NULL COMMENT '数据权限Id',
  `dept_id` bigint NOT NULL COMMENT '部门Id',
  PRIMARY KEY (`data_perm_id`, `dept_id`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据权限和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_sys_data_perm_menu
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_data_perm_menu`;
CREATE TABLE `sdt_sys_data_perm_menu`  (
  `data_perm_id` bigint NOT NULL COMMENT '数据权限Id',
  `menu_id` bigint NOT NULL COMMENT '菜单Id',
  PRIMARY KEY (`data_perm_id`, `menu_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据权限和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_sys_data_perm_user
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_data_perm_user`;
CREATE TABLE `sdt_sys_data_perm_user`  (
  `data_perm_id` bigint NOT NULL COMMENT '数据权限Id',
  `user_id` bigint NOT NULL COMMENT '用户Id',
  PRIMARY KEY (`data_perm_id`, `user_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据权限和用户关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_dept`;
CREATE TABLE `sdt_sys_dept`  (
  `dept_id` bigint NOT NULL COMMENT '部门Id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父部门Id',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '部门名称',
  `show_order` int NOT NULL COMMENT '兄弟部分之间的显示顺序，数字越小越靠前',
  `mark` bigint NOT NULL COMMENT '测试字段',
  `create_user_id` bigint NOT NULL COMMENT '创建者Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新者Id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int NOT NULL COMMENT '删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`dept_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_show_order`(`show_order` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '部门管理表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_dept_post
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_dept_post`;
CREATE TABLE `sdt_sys_dept_post`  (
  `dept_post_id` bigint NOT NULL COMMENT '主键Id',
  `dept_id` bigint NOT NULL COMMENT '部门Id',
  `post_id` bigint NOT NULL COMMENT '岗位Id',
  `post_show_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '部门岗位显示名称',
  PRIMARY KEY (`dept_post_id`) USING BTREE,
  INDEX `idx_post_id`(`post_id` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_sys_dept_relation
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_dept_relation`;
CREATE TABLE `sdt_sys_dept_relation`  (
  `parent_dept_id` bigint NOT NULL COMMENT '父部门Id',
  `dept_id` bigint NOT NULL COMMENT '部门Id',
  PRIMARY KEY (`parent_dept_id`, `dept_id`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '部门关联关系表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_menu`;
CREATE TABLE `sdt_sys_menu`  (
  `menu_id` bigint NOT NULL COMMENT '主键Id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父菜单Id，目录菜单的父菜单为null',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '菜单显示名称',
  `str_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '字符id',
  `menu_type` int NOT NULL COMMENT '(0: 目录 1: 菜单 2: 按钮 3: UI片段)',
  `form_router_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '前端表单路由名称，仅用于menu_type为1的菜单类型',
  `online_form_id` bigint NULL DEFAULT NULL COMMENT '在线表单主键Id',
  `online_menu_perm_type` int NULL DEFAULT NULL COMMENT '在线表单菜单的权限控制类型',
  `report_page_id` bigint NULL DEFAULT NULL COMMENT '统计页面主键Id',
  `online_flow_entry_id` bigint NULL DEFAULT NULL COMMENT '仅用于在线表单的流程Id',
  `show_order` int NOT NULL COMMENT '菜单显示顺序 (值越小，排序越靠前)',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单图标',
  `create_user_id` bigint NOT NULL COMMENT '创建者Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新者Id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除标记(1: 正常 -1: 已删除)',
  `extra_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '附加信息',
  PRIMARY KEY (`menu_id`) USING BTREE,
  INDEX `idx_show_order`(`show_order` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_menu_type`(`menu_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '菜单和操作权限管理表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_menu_perm_code
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_menu_perm_code`;
CREATE TABLE `sdt_sys_menu_perm_code`  (
  `menu_id` bigint NOT NULL COMMENT '关联菜单Id',
  `perm_code_id` bigint NOT NULL COMMENT '关联权限字Id',
  PRIMARY KEY (`menu_id`, `perm_code_id`) USING BTREE,
  INDEX `idx_perm_code_id`(`perm_code_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '菜单和权限关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_sys_perm
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_perm`;
CREATE TABLE `sdt_sys_perm`  (
  `perm_id` bigint NOT NULL COMMENT '权限id',
  `module_id` bigint NOT NULL DEFAULT 0 COMMENT '权限所在的权限模块id',
  `perm_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '权限名称',
  `url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '关联的url',
  `show_order` int NOT NULL DEFAULT 0 COMMENT '权限在当前模块下的顺序，由小到大',
  `create_user_id` bigint NOT NULL COMMENT '创建者Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新者Id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`perm_id`) USING BTREE,
  INDEX `idx_show_order`(`show_order` ASC) USING BTREE,
  INDEX `idx_module_id`(`module_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统权限表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_perm_code
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_perm_code`;
CREATE TABLE `sdt_sys_perm_code`  (
  `perm_code_id` bigint NOT NULL COMMENT '主键Id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '上级权限字Id',
  `perm_code` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '权限字标识(一般为有含义的英文字符串)',
  `perm_code_type` int NOT NULL COMMENT '类型(0: 表单 1: UI片段 2: 操作)',
  `show_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '显示名称',
  `show_order` int NOT NULL COMMENT '显示顺序(数值越小，越靠前)',
  `create_user_id` bigint NOT NULL COMMENT '创建者Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新者Id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`perm_code_id`) USING BTREE,
  UNIQUE INDEX `uk_perm_code`(`perm_code` ASC, `deleted_flag` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_show_order`(`show_order` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统权限资源表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_perm_code_perm
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_perm_code_perm`;
CREATE TABLE `sdt_sys_perm_code_perm`  (
  `perm_code_id` bigint NOT NULL COMMENT '权限字Id',
  `perm_id` bigint NOT NULL COMMENT '权限id',
  PRIMARY KEY (`perm_code_id`, `perm_id`) USING BTREE,
  INDEX `idx_perm_id`(`perm_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统权限字和权限资源关联表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_perm_module
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_perm_module`;
CREATE TABLE `sdt_sys_perm_module`  (
  `module_id` bigint NOT NULL COMMENT '权限模块id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '上级权限模块id',
  `module_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '权限模块名称',
  `module_type` int NOT NULL COMMENT '模块类型(0: 普通模块 1: Controller模块)',
  `show_order` int NOT NULL DEFAULT 0 COMMENT '权限模块在当前层级下的顺序，由小到大',
  `create_user_id` bigint NOT NULL COMMENT '创建者Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新者Id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`module_id`) USING BTREE,
  INDEX `idx_show_order`(`show_order` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_module_type`(`module_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统权限模块表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_perm_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_perm_whitelist`;
CREATE TABLE `sdt_sys_perm_whitelist`  (
  `perm_url` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '权限资源的url',
  `module_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '权限资源所属模块名字(通常是Controller的名字)',
  `perm_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '权限的名称',
  PRIMARY KEY (`perm_url`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '权限资源白名单表(认证用户均可访问的url资源)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_post`;
CREATE TABLE `sdt_sys_post`  (
  `post_id` bigint NOT NULL COMMENT '岗位Id',
  `post_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '岗位名称',
  `post_level` int NOT NULL COMMENT '岗位层级，数值越小级别越高',
  `leader_post` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否领导岗位',
  `create_user_id` bigint NOT NULL COMMENT '创建者Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新者Id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_role`;
CREATE TABLE `sdt_sys_role`  (
  `role_id` bigint NOT NULL COMMENT '主键Id',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `create_user_id` bigint NOT NULL COMMENT '创建者Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新者Id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统角色表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_role_menu`;
CREATE TABLE `sdt_sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色Id',
  `menu_id` bigint NOT NULL COMMENT '菜单Id',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色与菜单对应关系表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_user`;
CREATE TABLE `sdt_sys_user`  (
  `user_id` bigint NOT NULL COMMENT '主键Id',
  `login_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户登录名称',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `show_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户显示名称',
  `dept_id` bigint NOT NULL COMMENT '用户所在部门Id',
  `head_image_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户头像的Url',
  `user_type` int NOT NULL COMMENT '用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)',
  `user_status` int NOT NULL COMMENT '状态(0: 正常 1: 锁定)',
  `create_user_id` bigint NOT NULL COMMENT '创建者Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新者Id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int NOT NULL COMMENT '删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_login_name`(`login_name` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE,
  INDEX `idx_status`(`user_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统用户表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_user`;
CREATE TABLE `sdt_sys_user`  (
  `user_id` bigint NOT NULL COMMENT '主键Id',
  `login_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户登录名称',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `show_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户显示名称',
  `dept_id` bigint NOT NULL COMMENT '用户所在部门Id',
  `head_image_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户头像的Url',
  `user_type` int NOT NULL COMMENT '用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)',
  `user_status` int NOT NULL COMMENT '状态(0: 正常 1: 锁定)',
  `create_user_id` bigint NOT NULL COMMENT '创建者Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新者Id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int NOT NULL COMMENT '删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_login_name`(`login_name` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE,
  INDEX `idx_status`(`user_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统用户表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sdt_sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_user_post`;
CREATE TABLE `sdt_sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户Id',
  `dept_post_id` bigint NOT NULL COMMENT '部门岗位Id',
  `post_id` bigint NOT NULL COMMENT '岗位Id',
  PRIMARY KEY (`user_id`, `dept_post_id`) USING BTREE,
  INDEX `idx_post_id`(`post_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sdt_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sdt_sys_user_role`;
CREATE TABLE `sdt_sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户Id',
  `role_id` bigint NOT NULL COMMENT '角色Id',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户与角色对应关系表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for zz_sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `zz_sys_operation_log`;
CREATE TABLE `zz_sys_operation_log`  (
  `log_id` bigint NOT NULL COMMENT '主键Id',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '日志描述',
  `operation_type` int NULL DEFAULT NULL COMMENT '操作类型',
  `service_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '接口所在服务名称',
  `api_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '调用的controller全类名',
  `api_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '调用的controller中的方法',
  `session_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户会话sessionId',
  `trace_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '每次请求的Id',
  `elapse` int NULL DEFAULT NULL COMMENT '调用时长',
  `request_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'HTTP 请求方法，如GET',
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'HTTP 请求地址',
  `request_arguments` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'controller接口参数',
  `response_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'controller应答结果',
  `request_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '请求IP',
  `success` bit(1) NULL DEFAULT NULL COMMENT '应答状态',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '错误信息',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户Id',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '操作员Id',
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '操作员名称',
  `operation_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_trace_id_idx`(`trace_id` ASC) USING BTREE,
  INDEX `idx_operation_type_idx`(`operation_type` ASC) USING BTREE,
  INDEX `idx_operation_time_idx`(`operation_time` ASC) USING BTREE,
  INDEX `idx_success`(`success` ASC) USING BTREE,
  INDEX `idx_elapse`(`elapse` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
-- Table structure for zz_global_dict
-- ----------------------------
DROP TABLE IF EXISTS `zz_global_dict`;
CREATE TABLE `zz_global_dict`  (
  `dict_id` bigint NOT NULL COMMENT '主键Id',
  `dict_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典编码',
  `dict_name` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典中文名称',
  `create_user_id` bigint NOT NULL COMMENT '创建用户Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新用户名',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除字段',
  PRIMARY KEY (`dict_id`) USING BTREE,
  INDEX `idx_dict_code`(`dict_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '全局字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zz_global_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `zz_global_dict_item`;
CREATE TABLE `zz_global_dict_item`  (
  `id` bigint NOT NULL COMMENT '主键Id',
  `dict_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典编码',
  `item_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典数据项Id',
  `item_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典数据项名称',
  `show_order` int NOT NULL COMMENT '显示顺序',
  `status` int NOT NULL COMMENT '字典状态',
  `create_user_id` bigint NOT NULL COMMENT '创建用户Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新用户名',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除字段',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_show_order`(`show_order` ASC) USING BTREE,
  INDEX `idx_dict_code_item_id`(`dict_code` ASC, `item_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '全局字典项目表' ROW_FORMAT = Dynamic;
