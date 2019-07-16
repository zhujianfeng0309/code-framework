# code-generate
功能：根据数据库表自动生成代码，包含简单的增删查改, Controller/facade/facadeImpl/VO/Form/DTO/Entity/service/serviceImpl/jpa DAO等；  
在application.properties文件中配置数据源，generate.code.database配置数据库的名称，用于校验传入的数据库表名称是否在这个库中；  
file.output.path配置代码生成后的路径
