insert into cpdb_ds.t_sys_account( 
       id,
  acct_id       ,
  org_code      ,
  org_name      ,
  api_key       ,
  balance       ,
  status        ,
  enddate    ,
  isfee    ) 
  values (
  sys_guid(),
  'lnzcp_user',
  '测试用户',
  '测试用户',
  sys_guid(),
  100000,
  0,
  to_date('2018-03-01 00:00:00','yyyy-mm-dd hh24:mi:ss'),
  '1'
  );
commit;

insert into cpdb_ds.t_sys_acct_prods( 
      id   ,
  acct_id    ,
  prod_limit ,
  price   ,
  test_num  ,
  status    ) 
  values (
  sys_guid(),
  'lnzcp_user',
  'P_C_B142',
  0.2,
  1000,
  '1'
  );