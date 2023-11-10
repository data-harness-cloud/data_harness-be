DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
                                    `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                    `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                    `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                    `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                    `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                    `IS_DURABLE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                    `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                    `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                    `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                    `JOB_DATA` blob,
                                    PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
                                    KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
                                    KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
                                 `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                 `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                 `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                 `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                 `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                 `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                 `NEXT_FIRE_TIME` bigint DEFAULT NULL,
                                 `PREV_FIRE_TIME` bigint DEFAULT NULL,
                                 `PRIORITY` int DEFAULT NULL,
                                 `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                 `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                 `START_TIME` bigint NOT NULL,
                                 `END_TIME` bigint DEFAULT NULL,
                                 `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                 `MISFIRE_INSTR` smallint DEFAULT NULL,
                                 `JOB_DATA` blob,
                                 PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                 KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
                                 KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
                                 KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE,
                                 KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                 KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`) USING BTREE,
                                 KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
                                 KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
                                 KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`) USING BTREE,
                                 KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`) USING BTREE,
                                 KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`) USING BTREE,
                                 KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`) USING BTREE,
                                 KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
                                 CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
                                      `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                      `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                      `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                      `BLOB_DATA` blob,
                                      PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                      KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                      CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
                                  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                  `CALENDAR` blob NOT NULL,
                                  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
                                      `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                      `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                      `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                      `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                      `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                      PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                      CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
                                       `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                       `ENTRY_ID` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                       `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                       `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                       `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                       `FIRED_TIME` bigint NOT NULL,
                                       `SCHED_TIME` bigint NOT NULL,
                                       `PRIORITY` int NOT NULL,
                                       `STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                       `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                       `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                       `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                       `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                       PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`) USING BTREE,
                                       KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE,
                                       KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
                                       KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
                                       KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
                                       KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                       KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
                              `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                              `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                              PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
                                            `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                            `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                            PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
                                        `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                        `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                        `LAST_CHECKIN_TIME` bigint NOT NULL,
                                        `CHECKIN_INTERVAL` bigint NOT NULL,
                                        PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
                                        `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                        `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                        `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                        `REPEAT_COUNT` bigint NOT NULL,
                                        `REPEAT_INTERVAL` bigint NOT NULL,
                                        `TIMES_TRIGGERED` bigint NOT NULL,
                                        PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                        CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
                                         `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                         `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                         `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                         `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                         `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                         `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                         `INT_PROP_1` int DEFAULT NULL,
                                         `INT_PROP_2` int DEFAULT NULL,
                                         `LONG_PROP_1` bigint DEFAULT NULL,
                                         `LONG_PROP_2` bigint DEFAULT NULL,
                                         `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
                                         `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
                                         `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                         `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                                         PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                         CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

