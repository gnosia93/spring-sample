#### 유저 생성 ####
```
create database sample;
create user 'sample'@'%' identified by 'sample';
grant all privileges on sample.* to 'sample'@'%';
alter database sample default character set utf8;
status
```

