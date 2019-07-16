package com.generate.framework.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/11
 */
@Entity
@Table(name = "information_schema.tables", schema = "information_schema")
public class TableSchema {


    @Column(name = "table_schema")
    private String tableSchema;

    @Id
    @Column(name = "table_name")
    private String tableName;

    @Column(name = "table_comment")
    private String tableComment;

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

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }
}
