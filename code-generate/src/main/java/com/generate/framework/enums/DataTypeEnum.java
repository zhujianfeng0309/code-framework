package com.generate.framework.enums;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/12
 */
public enum  DataTypeEnum {

    BIGINT("Long", "bigint"),
    VARCHAR("String", "varchar"),
    TINYINT("Integer", "tinyint"),
    TIMESTAMP("Date", "timestamp"),
    DATE("Date", "date");

    private String javaType;

    private String dbType;


    DataTypeEnum(String javaType, String dbType) {
        this.dbType = dbType;
        this.javaType = javaType;
    }

    public static String getByDbType(String dbType){
        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()){
            if (dbType.equals(dataTypeEnum.getDbType())){
                return dataTypeEnum.getJavaType();
            }
        }
        return DataTypeEnum.VARCHAR.getJavaType();
    }

    public String getDbType() {
        return dbType;
    }

    public String getJavaType() {
        return javaType;
    }
}
