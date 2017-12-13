/*======================================
表类型：交易表
数据量预估：1000/T
数据日增量预估：0.1W
数据生命周期：永久
=======================================*/
-- create table
create table cpdb_ds.t_ds_jixin_result
(
  id              NUMBER NOT NULL,
  trade_id        varchar2(32) not null,
  cardno          varchar2(200) ,
  dsorderid       varchar2(100),
  merchno         varchar2(200),
  returncode      varchar2(100),
  errtext         varchar2(300),
  transcode        varchar2(100),
  ordersn        varchar2(100),
  orderid        varchar2(100),
  sign           varchar2(200),
  update_time     date default sysdate not null,
  create_time     date default sysdate not null
);
-- add comments to the table 
comment on table cpdb_ds.t_ds_jixin_result
  is '吉信银行卡3要素落地表';
-- add comments to the columns 
comment on column cpdb_ds.t_ds_jixin_result.trade_id
  is '交易号';
comment on column cpdb_ds.t_ds_jixin_result.cardno
  is '证件号';
comment on column cpdb_ds.t_ds_jixin_result.dsorderid
  is '商户订单号';
comment on column cpdb_ds.t_ds_jixin_result.merchno
  is '商户号';
comment on column cpdb_ds.t_ds_jixin_result.returncode
  is '返回码';
comment on column cpdb_ds.t_ds_jixin_result.errtext
  is '返回信息';
comment on column cpdb_ds.t_ds_jixin_result.ordersn
  is '商户请求流水号';
comment on column cpdb_ds.t_ds_jixin_result.orderid
  is '平台交易流水号';
comment on column cpdb_ds.t_ds_jixin_result.sign
  is '加密校验值';
comment on column cpdb_ds.t_ds_jixin_result.update_time
  is '更新时间';
comment on column cpdb_ds.t_ds_jixin_result.create_time
  is '创建时间';
-- create/recreate indexes 
alter table cpdb_ds.t_ds_jixin_result add constraint pk_ds_jixin_result_id primary key (id)  using index;
create  index idx_ds_jixin_result01 on cpdb_ds.t_ds_jixin_result (cardno);
create index idx_ds_jixin_result02 on cpdb_ds.t_ds_jixin_result (trade_id);

-- Create Seq  
Create sequence cpdb_ds.seq_t_ds_jixin_result
Start with 1
Increment by 1
Cache 20; 