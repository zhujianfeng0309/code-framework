package com.generate.framework.demo.service.impl;

import com.generate.framework.demo.dao.TableColumnDAO;
import com.generate.framework.demo.dao.TableSchemaDAO;
import com.generate.framework.demo.entity.TableColumn;
import com.generate.framework.demo.entity.TableSchema;
import com.generate.framework.demo.service.TableColumnService;
import com.generate.framework.util.GenerateDirectoryAndJavaFileUtil;
import com.generate.framework.util.GenerateDirectoryUtil;
import com.generate.framework.util.StringReplaceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/11
 */
@Service
public class TableColumnServiceImpl implements TableColumnService {

    @Value("${generate.code.database}")
    private String tableSchema;
    @Value("${file.output.path}")
    private String targetOutputPath;

    @Autowired
    private TableColumnDAO tableColumnDAO;
    @Autowired
    private TableSchemaDAO tableSchemaDAO;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean generateBaseCode(String tableName) {
        if (StringUtils.isEmpty(tableName)){
            throw new RuntimeException("tableName不能为空");
        }
        List<TableSchema> tableSchemaList = tableSchemaDAO.findByTableSchema(tableSchema);
        if (CollectionUtils.isEmpty(tableSchemaList)){
            throw new RuntimeException("数据库不存在");
        }
        Map<String, List<TableSchema>> tableNameMap = tableSchemaList.stream().collect(Collectors.groupingBy(TableSchema::getTableName));
        List<TableSchema> schemaList = tableNameMap.get(tableName);
        if (CollectionUtils.isEmpty(schemaList)){
            throw new RuntimeException("tableName不存在");
        }
        List<TableColumn> tableColumnList = tableColumnDAO.findByTableSchemaAndTableName(this.tableSchema, tableName);
        try {
            String tableFileName = StringReplaceUtil.replaceUnderlineToUpperCase(tableName);
            doGenerateCode(tableColumnList, StringReplaceUtil.firstLetterToUpperCase(tableFileName.substring(1)));
        }catch (Exception e){
            throw new RuntimeException("自动生产文件代码失败", e);
        }
        return true;
    }

    /**
     * 创建目录及文件
     * --tableName
     *      --domain
     *          --tableName.java
     *      --dao
     *          --tableNameDAO.java
     *      --dto
     *          --tableNameDTO.java
     *      --service
     *          --tableNameService.java
     *          --impl
     *              --tableNameServiceImpl.java
     *      --controller
     *          --tableNameController.java
     *      --facade
     *          --tableNameFacade.java
     *          --impl
     *              --tableNameFacadeImpl.java
     *       --form
     *          --tableNameForm.java
     *       --vo
     *           --tableNameVO.java
     *
     * @param tableColumnList
     * @param tableName
     * @throws IOException
     */
     private void doGenerateCode(List<TableColumn> tableColumnList, String tableName) throws IOException {
         //创建目录
         GenerateDirectoryUtil.createDirectory(targetOutputPath, tableName);
         //创建domain
         GenerateDirectoryAndJavaFileUtil.createDomainFile(targetOutputPath, tableName, tableColumnList);
         //创建dao
         GenerateDirectoryAndJavaFileUtil.createDaoFile(targetOutputPath, tableName);
         //创建dto
         GenerateDirectoryAndJavaFileUtil.createDtoFile(targetOutputPath, tableName, tableColumnList);
         //创建service
         GenerateDirectoryAndJavaFileUtil.createServiceFile(targetOutputPath, tableName);
         ////创建service/impl
         GenerateDirectoryAndJavaFileUtil.createServiceImplFile(targetOutputPath, tableName);
         //创建VO
         GenerateDirectoryAndJavaFileUtil.createVOFile(targetOutputPath, tableName, tableColumnList);
         //创建facade
         GenerateDirectoryAndJavaFileUtil.createFacadeFile(targetOutputPath, tableName);
         //创建facade/impl
         GenerateDirectoryAndJavaFileUtil.createFacadeImplFile(targetOutputPath, tableName);
         //创建form
         GenerateDirectoryAndJavaFileUtil.createFormFile(targetOutputPath, tableName, tableColumnList);
         //创建controller
         GenerateDirectoryAndJavaFileUtil.createControllerFile(targetOutputPath, tableName);
     }


}
