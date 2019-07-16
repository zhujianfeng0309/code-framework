package com.generate.framework.demo.dao;

import com.generate.framework.demo.entity.TableColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/11
 */
@Repository
public interface TableColumnDAO extends JpaRepository<TableColumn, String>, JpaSpecificationExecutor<TableColumn> {

    /**
     * 查询表字段信息
     * @param tableSchema
     * @param tableName
     * @return
     */
    List<TableColumn> findByTableSchemaAndTableName(String tableSchema, String tableName);


}
