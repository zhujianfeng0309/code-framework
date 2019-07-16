package com.generate.framework.demo.dao;

import com.generate.framework.demo.entity.TableSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/11
 */
public interface TableSchemaDAO extends JpaRepository<TableSchema, String>, JpaSpecificationExecutor<TableSchema> {

    /**
     * 查询指定数据库中的所有表
     * @param tableSchema
     * @return
     */
    List<TableSchema> findByTableSchema(String tableSchema);

}
