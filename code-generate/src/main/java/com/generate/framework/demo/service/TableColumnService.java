package com.generate.framework.demo.service;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/11
 */
public interface TableColumnService {

    /**
     * 根据表名生成基础类
     * @param tableName
     * @return
     */
    boolean generateBaseCode(String tableName);

}
