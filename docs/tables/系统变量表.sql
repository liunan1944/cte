create table cpdb_ds.t_sys_property_param
(
  id  number not null,
  key_code       VARCHAR2(200) not null,
  key_value      VARCHAR2(4000) not null,
  key_desc       VARCHAR2(200),
  ownerid        VARCHAR2(200),
  child_code     VARCHAR2(200),
  child_name     VARCHAR2(500),
  status         VARCHAR2(10),
  update_time    DATE default sysdate not null,
  create_time    DATE default sysdate not null
);
comment on table cpdb_ds.t_sys_property_param
  is '系统变量表';
-- Add comments to the columns 
comment on column cpdb_ds.t_sys_property_param.key_code
  is '变量key';
comment on column cpdb_ds.t_sys_property_param.key_value
  is '变量值';
 comment on column cpdb_ds.t_sys_property_param.key_desc
  is '变量说明';
comment on column cpdb_ds.t_sys_property_param.ownerid
  is '所属模型id';
comment on column cpdb_ds.t_sys_property_param.child_code
  is '子模型key';
comment on column cpdb_ds.t_sys_property_param.child_name
  is '子模型name';
comment on column cpdb_ds.t_sys_property_param.status
  is '状态:1草稿,2历史,3发布在用';
comment on column cpdb_ds.t_sys_property_param.create_time
  is '创建时间';
create index cpdb_ds.idx_t_sys_property_param01 on cpdb_ds.t_sys_property_param (ownerid);
create index cpdb_ds.idx_t_sys_property_param02 on cpdb_ds.t_sys_property_param (key_code,child_code);

create table cpdb_ds.t_sys_property_model
(
  id  number not null,
  model_id       VARCHAR2(200),
  ownerid        VARCHAR2(200),
  status         VARCHAR2(10),
  update_time    DATE default sysdate not null,
  create_time    DATE default sysdate not null
);
comment on table cpdb_ds.t_sys_property_model
  is '系统模型-变量表';
-- Add comments to the columns 
comment on column cpdb_ds.t_sys_property_model.model_id
  is '模型id';
comment on column cpdb_ds.t_sys_property_model.ownerid
  is '模型id';
comment on column cpdb_ds.t_sys_property_model.status
  is '状态:1草稿,2历史,3发布在用';
comment on column cpdb_ds.t_sys_property_model.create_time
  is '创建时间';
create index cpdb_ds.idx_t_sys_property_model01 on cpdb_ds.t_sys_property_model (ownerid);
create index cpdb_ds.idx_t_sys_property_model02 on cpdb_ds.t_sys_property_model (model_id);