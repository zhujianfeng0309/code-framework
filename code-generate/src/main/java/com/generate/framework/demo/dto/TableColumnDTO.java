package com.generate.framework.demo.dto;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/11
 */
public class TableColumnDTO {

    /**
     * 数据库名称
     */
    private String tableSchema;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 字段评论
     */
    private String columnComment;

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }
}
