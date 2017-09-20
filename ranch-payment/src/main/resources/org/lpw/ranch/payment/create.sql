DROP TABLE IF EXISTS t_payment;
CREATE TABLE t_payment
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_type VARCHAR(255) NOT NULL COMMENT '类型',
  c_user CHAR(36) NOT NULL COMMENT '用户ID',
  c_amount INT DEFAULT 0 COMMENT '金额，单位：分',
  c_order_no CHAR(21) NOT NULL COMMENT '订单号',
  c_trade_no VARCHAR(255) NOT NULL COMMENT '网关订单号',
  c_state INT DEFAULT 0 COMMENT '状态：0-新建；1-成功；2-失败',
  c_notice TEXT DEFAULT NULL COMMENT '通知配置',
  c_start DATETIME DEFAULT NULL COMMENT '开始时间',
  c_end DATETIME DEFAULT NULL COMMENT '结束时间',
  c_json TEXT DEFAULT NULL COMMENT '扩展数据',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_user_start(c_user,c_start) USING BTREE,
  UNIQUE KEY uk_order_no(c_order_no) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
