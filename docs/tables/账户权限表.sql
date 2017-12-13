create table cpdb_ds.t_sys_acct_prods
(
  id            VARCHAR2(32) not null,
  acct_id       VARCHAR2(32),
  prod_limit    VARCHAR2(100),
  price         number,
  test_num      number,
  status        VARCHAR2(10),
  update_time       DATE default sysdate not null,
  create_time       DATE default sysdate not null
);
comment on table cpdb_ds.t_sys_acct_prods
  is '账户产品授权表';
-- Add comments to the columns 
comment on column cpdb_ds.t_sys_acct_prods.acct_id
  is '账户ID';
comment on column cpdb_ds.t_sys_acct_prods.prod_limit
  is '授权产品';
comment on column cpdb_ds.t_sys_acct_prods.price
  is '产品价格';
comment on column cpdb_ds.t_sys_acct_prods.test_num
  is '测试条数';
comment on column cpdb_ds.t_sys_acct_prods.status
  is '状态:1产品可用,2产品冻结,3产品下线';
comment on column cpdb_ds.t_sys_acct_prods.update_time
  is '更新时间';
create index cpdb_ds.idx_t_sys_acct_prods01 on cpdb_ds.t_sys_acct_prods (acct_id);


-- Create table
create table cpdb_ds.t_sys_acct_ip_limits
(
  id  NUMBER NOT NULL,
  acct_id       VARCHAR2(32),
  ip_limit   VARCHAR2(100),
  status       VARCHAR2(10),
  update_time       DATE default sysdate not null,
  create_time       DATE default sysdate not null
);
comment on table cpdb_ds.t_sys_acct_ip_limits
  is '账户ip白名单授权表';
-- Add comments to the columns 
comment on column cpdb_ds.t_sys_acct_ip_limits.acct_id
  is '账户ID';
comment on column cpdb_ds.t_sys_acct_ip_limits.ip_limit
  is '授权ip';
comment on column cpdb_ds.t_sys_acct_ip_limits.status
  is '状态:1 ip可用,2 ip不可用';
comment on column cpdb_ds.t_sys_acct_ip_limits.update_time
  is '更新时间';
create index cpdb_ds.idx_t_sys_acct_ip01 on cpdb_ds.t_sys_acct_ip_limits (acct_id);

-- Create table
create table T_SYS_ACCOUNT
(
  id            VARCHAR2(32) not null,
  acct_id       VARCHAR2(32),
  org_code      VARCHAR2(32),
  org_name      VARCHAR2(200),
  org_full_name VARCHAR2(400),
  api_key       VARCHAR2(32),
  outer_flag    VARCHAR2(1),
  balance       NUMBER,
  status        VARCHAR2(2),
  enddate       DATE,
  note          VARCHAR2(400),
  mobiles       VARCHAR2(100),
  acct_name     VARCHAR2(400),
  ip_valid      VARCHAR2(1) default '1',
  emails        VARCHAR2(100),
  isfee         VARCHAR2(20) default '免费客户'
);
-- Add comments to the columns 
comment on column T_SYS_ACCOUNT.acct_id
  is '账户ID';
comment on column T_SYS_ACCOUNT.org_name
  is '客户单位名称';
comment on column T_SYS_ACCOUNT.org_full_name
  is '客户单位全名';
comment on column T_SYS_ACCOUNT.api_key
  is '分配的API KEY';
comment on column T_SYS_ACCOUNT.outer_flag
  is '内外部客户标识';
comment on column T_SYS_ACCOUNT.balance
  is '最新余额';
comment on column T_SYS_ACCOUNT.status
  is '当前状态(0:正常,-1:锁定,-2:注销,-3:销户)';
comment on column T_SYS_ACCOUNT.enddate
  is '终止时间';
comment on column T_SYS_ACCOUNT.note
  is '备注';
comment on column T_SYS_ACCOUNT.acct_name
  is '账户名称';
comment on column T_SYS_ACCOUNT.isfee
  is '1测试客户,2正式客户';
-- Create/Recreate indexes 
create index IDX_T_SYS_CUSTOMER1 on T_SYS_ACCOUNT (ACCT_ID);

create table cpdb_ds.t_sys_account_log
(
  id            varchar2(32) not null,
  acct_id       varchar2(32), 
  cost          number,
  prods          varchar2(100),
  ip_valid      varchar2(100),
  create_time       DATE default sysdate not null
);
comment on table cpdb_ds.t_sys_account_log
  is '账户授权日志表';
-- add comments to the columns 
comment on column cpdb_ds.t_sys_account_log.acct_id
  is '账户id';
comment on column cpdb_ds.t_sys_account_log.cost
  is '充值金额';
comment on column cpdb_ds.t_sys_account_log.prods
  is '授权产品';
comment on column cpdb_ds.t_sys_account_log.ip_valid
  is '授权ip';
-- create/recreate indexes 
create index idx_t_sys_account_log1 on cpdb_ds.t_sys_account_log (acct_id);
