-- Create table
create table CPDB_DS.T_DS_NCIIC_RESULT
(
  id          VARCHAR2(32) default sys_guid() not null,
  trade_id    VARCHAR2(32) not null,
  cardno      VARCHAR2(1000) not null,
  name        VARCHAR2(200),
  card_check  VARCHAR2(50),
  name_check  VARCHAR2(50),
  error_mesg  VARCHAR2(500),
  status      VARCHAR2(10),
  image_file  VARCHAR2(500),
  update_time DATE default sysdate not null,
  create_time DATE default sysdate not null,
  sourceid    VARCHAR2(10),
  sex         VARCHAR2(50),
  birth_day   VARCHAR2(50),
  police_addr VARCHAR2(500)
);
-- Add comments to the columns 
comment on column CPDB_DS.T_DS_NCIIC_RESULT.trade_id
  is '接口交易号';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.cardno
  is '身份证号';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.name
  is '姓名';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.card_check
  is '身份证查询结果';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.name_check
  is '姓名查询结果';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.error_mesg
  is '查询出错原因';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.status
  is '状态：00完全一致，01身份证号一致但姓名不一致，02库中无此号';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.image_file
  is '照片';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.create_time
  is '插入时间';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.sourceid
  is '数据源id:01公安;02爱金';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.sex
  is '性别';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.birth_day
  is '生日';
comment on column CPDB_DS.T_DS_NCIIC_RESULT.police_addr
  is '地址';
-- Create/Recreate indexes 
create index CPDB_DS.IDX_DS_NCIIC_CARDNO1 on CPDB_DS.T_DS_NCIIC_RESULT (CARDNO);
create index CPDB_DS.IDX_DS_NCIIC_TRADEID1 on CPDB_DS.T_DS_NCIIC_RESULT (TRADE_ID);
-- Create/Recreate primary, unique and foreign key constraints 
alter table CPDB_DS.T_DS_NCIIC_RESULT  add constraint PK_DS_NCIIC_ID1 primary key (ID) using index ;
