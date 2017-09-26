spring boot with mybatis

1. 测试了一下mybatis中三种ExecutorType: batch, simple, reuse，结论：batch最优。
2. 测试用例在CityMapperTest中，mybatis配置在application.properties中，日志配置在logback.xml中
3. City表DDL在resource/schema/City.sql中
