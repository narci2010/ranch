DROP TABLE IF EXISTS t_push;
CREATE TABLE t_push
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_key VARCHAR(255) NOT NULL COMMENT '引用键',
  c_sender VARCHAR(255) DEFAULT NULL COMMENT '推送器',
  c_subject VARCHAR(255) DEFAULT NULL COMMENT '标题',
  c_content TEXT DEFAULT NULL COMMENT '内容',
  c_template VARCHAR(255) DEFAULT NULL COMMENT '模板',
  c_name VARCHAR(255) DEFAULT NULL COMMENT '发送者名称',
  c_state INT DEFAULT 0 COMMENT '状态：0-待审核；1-使用中',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_key_state(c_key,c_state) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;