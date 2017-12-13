insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000001,'sys_public_logprint_switch','1','是否打印日志:1打印,0不打印','0753513de038453c820cf6869w3we342','sys_public_code','系统参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000002,'sys_public_mock_switch','1','是否走mock接口:1走,0不走','0753513de038453c820cf6869w3we342','sys_public_code','系统参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000003,'sys_ds_max_error_num','30','ds应用数据源error熔断阈值','0753513de038453c820cf6869w3we342','sys_ds_code','系统ds应用参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000004,'sys_ds_error_expire_sec','300','ds应用数据源error统计器失效时间 单位秒','0753513de038453c820cf6869w3we342','sys_ds_code','系统ds应用参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000005,'sys_ds_sms_time_rate','5','ds应用sms频率，单位分','0753513de038453c820cf6869w3we342','sys_ds_code','系统ds应用参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000006,'sys_ds_jixin_url','http://121.201.18.234:9000/jxdata/api/auth/execute.do','吉信-银行卡鉴权调用url','0753513de038453c820cf6869w3we342','sys_ds_code','系统ds应用参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000007,'sys_ds_jixin_merchkey','z5wJ3Ezk3rO7vmUaCLHnRwvopUhzL240','吉信-商户key','0753513de038453c820cf6869w3we342','sys_ds_code','系统ds应用参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000008,'sys_ds_jixin_merchno','0000000000000003','吉信-商户号','0753513de038453c820cf6869w3we342','sys_ds_code','系统ds应用参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000009,'sys_ds_jixin_transcode','106','吉信-调用接口号','0753513de038453c820cf6869w3we342','sys_ds_code','系统ds应用参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000010,'sys_ds_jixin_version','0100','吉信-调用版本号','0753513de038453c820cf6869w3we342','sys_ds_code','系统ds应用参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000011,'sys_public_env_timeout','5000','超时时间:单位秒','0753513de038453c820cf6869w3we342','sys_public_code','系统参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000012,'sys_public_des_key','ctesrece','加密key','0753513de038453c820cf6869w3we342','sys_public_code','系统参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000013,'sys_public_encode_keys','cardNo,cardId','加密code','0753513de038453c820cf6869w3we342','sys_public_code','系统参数-公用','3');

insert into cpdb_ds.t_sys_property_param(id,key_code,key_value,key_desc,ownerid,child_code,child_name,status)
values (1000016,'sys_public_gw_test_mock','0','是否走mock:1走,0不走','1q53513de038453c820cf6869w3we342','sys_gw_code','系统参数-gw','3');

commit;


update cpdb_ds.t_sys_property_model d set d.ownerid='1q53513de038453c820cf6869w3we342';
update cpdb_ds.t_sys_property_param m set m.ownerid='1q53513de038453c820cf6869w3we342';
commit;