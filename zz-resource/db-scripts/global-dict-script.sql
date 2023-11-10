
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 全局字典表
-- ----------------------------
DROP TABLE IF EXISTS `zz_global_dict`;
CREATE TABLE `zz_global_dict` (
  `dict_id` bigint(20) NOT NULL COMMENT '主键Id',
  `dict_code` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '字典编码',
  `dict_name` varchar(2048) COLLATE utf8mb4_bin NOT NULL COMMENT '字典中文名称',
  `create_user_id` bigint NOT NULL COMMENT '创建用户Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新用户名',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除字段',
  PRIMARY KEY (`dict_id`),
  KEY `idx_dict_code` (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='全局字典表';

-- ----------------------------
-- 全局字典项目表
-- ----------------------------
DROP TABLE IF EXISTS `zz_global_dict_item`;
CREATE TABLE `zz_global_dict_item` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `dict_code` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '字典编码',
  `item_id` varchar(64) NOT NULL COMMENT '字典数据项Id',
  `item_name` varchar(1024) COLLATE utf8mb4_bin NOT NULL COMMENT '字典数据项名称',
  `show_order` int NOT NULL COMMENT '显示顺序',
  `status` int NOT NULL COMMENT '字典状态',
  `create_user_id` bigint NOT NULL COMMENT '创建用户Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint NOT NULL COMMENT '更新用户名',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted_flag` int NOT NULL COMMENT '逻辑删除字段',
  PRIMARY KEY (`id`),
  KEY `idx_show_order` (`show_order`) USING BTREE,
  KEY `idx_dict_code_item_id` (`dict_code`,`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='全局字典项目表';

INSERT INTO zz_global_dict VALUES (1658383345941221376,'ProjectStatus','项目状态字典',1326769583993917440,CURDATE(),1326769583993917440,CURDATE(),1);

SET FOREIGN_KEY_CHECKS = 1;
