package com.generate.framework.util;



import com.generate.framework.demo.entity.TableColumn;
import com.generate.framework.enums.DataTypeEnum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
/**
 * @author SONGJIUHUA386
 * @since 2019/7/12
 */
public class GenerateDirectoryAndJavaFileUtil {



    /**
     * 创建domain文件，及domain.java实体类
     * @param basePath
     * @param entityName xxx.java
     * @param tableColumnList
     * @throws IOException
     */
    public static void createDomainFile(String basePath, String entityName, List<TableColumn> tableColumnList) throws IOException {
        //创建domain
        File contentFile = GenerateDirectoryUtil.createFile(basePath, entityName, "domain", "");
        FileOutputStream out = new FileOutputStream(contentFile, true);
        String importBasePath = basePath.substring(basePath.indexOf("java") + 5, basePath.length() - 1).replace("\\", ".");
        StringBuilder importPackage = new StringBuilder("package ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".domain;\n");
        //定义引入包、类等信息 StringJoiner
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(importPackage.toString())
                .append("import org.hibernate.annotations.DynamicInsert;\n")
                .append("import org.springframework.data.jpa.domain.support.AuditingEntityListener;\n")
                .append("import org.springframework.data.annotation.CreatedBy;\n")
                .append("import org.springframework.data.annotation.CreatedDate;\n")
                .append("import org.springframework.data.annotation.LastModifiedBy;\n")
                .append("import org.springframework.data.annotation.LastModifiedDate;\n")
                .append("import javax.persistence.Column;\n")
                .append("import javax.persistence.Entity;\n")
                .append("import javax.persistence.EntityListeners;\n")
                .append("import javax.persistence.GeneratedValue;\n")
                .append("import javax.persistence.GenerationType;\n")
                .append("import javax.persistence.Id;\n")
                .append("import javax.persistence.Table;\n")
                .append("import java.util.Date;\n")
                .append("/**\n")
                .append(" * @author ")
                .append(System.getProperty("user.name"))
                .append(" \n")
                .append(" * @since")
                .append(new Date())
                .append("\n")
                .append(" */\n")
                .append("@Entity\n")
                .append("@Table(name = \"")
                //数据库表名称 t_table_name
                .append(tableColumnList.get(0).getTableName())
                .append("\")\n")
                .append("@DynamicInsert\n")
                .append("@EntityListeners({ AuditingEntityListener.class })\n")
                .append("public class ")
                //类名称
                .append(entityName.split("\\.")[0])
                .append(" {\n");
        //定义类中的基本属性
        tableColumnList.forEach(tableColumn -> {
            StringBuilder note = new StringBuilder("    /**\n").append("    * ").append(tableColumn.getColumnComment()).append("\n").append("    */\n");
            String columnName = tableColumn.getColumnName();
            StringBuilder columnDefine = new StringBuilder("    @Column(name = \"").append(columnName).append("\")\n");
            StringBuilder specialColumn = new StringBuilder();
            if ("id".equals(columnName)){
                specialColumn.append("    @Id\n").append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
            }else if ("create_time".equals(columnName)){
                specialColumn.append("    @CreatedDate\n");
            }else if ("create_by".equals(columnName)){
                specialColumn.append("    @CreatedBy\n");
            }else if ("update_time".equals(columnName)){
                specialColumn.append("    @LastModifiedDate\n");
            }else if("update_by".equals(columnName)){
                specialColumn.append("    @LastModifiedBy\n");
            }
            String dataType = tableColumn.getDataType();
            String javaType = DataTypeEnum.getByDbType(dataType);
            StringBuilder attributeDefine = new StringBuilder("    private ").append(javaType).append(" ")
                         .append(StringReplaceUtil.replaceUnderlineToUpperCase(columnName)).append(";\n\n");
            stringBuilder.append(note.toString()).append(specialColumn.toString()).append(columnDefine.toString()).append(attributeDefine.toString());
        });
        //定义getter and setter
        String setterAndGetter = generateSetterAndGetter(tableColumnList);
        stringBuilder.append(setterAndGetter).append("}");
        out.write(stringBuilder.toString().getBytes("utf-8"));
    }

    /**
     * 创建dao目录以及xxxDAO.java类
     * @param basePath
     * @param entityName
     * @throws IOException
     */
    public static void createDaoFile(String basePath, String entityName) throws IOException {
        File contentFile = GenerateDirectoryUtil.createFile(basePath, entityName, "dao", "DAO");
        FileOutputStream out = new FileOutputStream(contentFile, true);
        String importBasePath = basePath.substring(basePath.indexOf("java") + 5, basePath.length() - 1).replace("\\", ".");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".dao;\n")
                .append("import org.springframework.data.jpa.repository.JpaRepository;\n")
                .append("import org.springframework.data.jpa.repository.JpaSpecificationExecutor;\n")
                .append("import org.springframework.stereotype.Repository;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase())
                .append(".domain.").append(entityName).append(";\n")
                .append("import java.util.List;\n")
                .append(generateClassNote())
                .append("@Repository\n")
                .append("public interface ").append(entityName).append("DAO")
                .append(" extends JpaRepository<").append(entityName).append(", Long>, JpaSpecificationExecutor<")
                .append(entityName).append("> {\n\n")
                .append(generateFindByIdAndDeleted(entityName))
                .append(generateFindByProjectIdAndDeleted(entityName))
                .append("}");
        out.write(stringBuilder.toString().getBytes("utf-8"));
    }

    /**
     * 创建dto目录以及xxxDTO.java类
     * @param basePath
     * @param entityName
     * @throws IOException
     */
    public static void createDtoFile(String basePath, String entityName, List<TableColumn> tableColumnList) throws IOException{
        File contentFile = GenerateDirectoryUtil.createFile(basePath, entityName, "dto", "DTO");
        FileOutputStream out = new FileOutputStream(contentFile, true);
        //com.generate.framework
        String importBasePath = basePath.substring(basePath.indexOf("java") + 5, basePath.length() - 1).replace("\\", ".");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".dto;\n")
                .append("import java.util.Date;\n")
                .append(generateClassNote())
                .append("public class ").append(entityName).append("DTO").append(" {\n\n");
        //设置属性
        tableColumnList.forEach(tableColumn -> {
            StringBuilder note = new StringBuilder("    /**\n").append("    * ").append(tableColumn.getColumnComment()).append("\n").append("    */\n");
            String columnName = tableColumn.getColumnName();
            String dataType = tableColumn.getDataType();
            String javaType = DataTypeEnum.getByDbType(dataType);
            StringBuilder attributeDefine = new StringBuilder("    private ").append(javaType).append(" ")
                                                    .append(StringReplaceUtil.replaceUnderlineToUpperCase(columnName)).append(";\n\n");
            stringBuilder.append(note.toString()).append(attributeDefine.toString());
        });
        //设置getter and setter
        String setterAndGetter = generateSetterAndGetter(tableColumnList);
        stringBuilder.append(setterAndGetter).append("}");
        out.write(stringBuilder.toString().getBytes("utf-8"));
    }

    /**
     * 新增service目录 以及xxxService.java接口
     * @param basePath
     * @param entityName
     * @throws IOException
     */
    public static void createServiceFile(String basePath, String entityName) throws IOException {
        File contentFile = GenerateDirectoryUtil.createFile(basePath, entityName, "service", "Service");
        FileOutputStream out = new FileOutputStream(contentFile, true);
        String importBasePath = basePath.substring(basePath.indexOf("java") + 5, basePath.length() - 1).replace("\\", ".");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".service;\n")
                //import entityNameDTO
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".dto.").append(entityName).append("DTO;\n")
                .append("import java.util.List;\n")
                .append(generateClassNote())
                .append("public interface ").append(entityName).append("Service {\n\n")
                .append(generateServiceInterface(entityName))
                .append("}");
        out.write(stringBuilder.toString().getBytes("utf-8"));
    }

    /**
     * 新增service/impl目录 以及xxxServiceImpl.java实现
     * @param basePath
     * @param entityName
     * @throws IOException
     */
    public static void createServiceImplFile(String basePath, String entityName) throws IOException {
        File contentFile = GenerateDirectoryUtil.createFile(basePath, entityName, "service\\impl", "ServiceImpl");
        FileOutputStream out = new FileOutputStream(contentFile, true);
        //获取到com.generate.framework
        String importBasePath = basePath.substring(basePath.indexOf("java") + 5, basePath.length() - 1).replace("\\", ".");
        String entityNameDTO = entityName + "DTO";
        String entityNameDAO = entityName + "DAO";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".service.impl;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".dto.").append(entityNameDTO).append(";\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".service.").append(entityName).append("Service;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".domain.").append(entityName).append(";\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".dao.").append(entityNameDAO).append(";\n")
                .append("import java.util.List;\n")
                .append("import org.springframework.beans.BeanUtils;\n")
                .append("import org.springframework.stereotype.Service;\n")
                .append("import org.springframework.beans.factory.annotation.Autowired;\n")
                .append(generateClassNote())
                .append(generateServiceImpl(entityName))
                .append("}");
        out.write(stringBuilder.toString().getBytes("utf-8"));
    }


    /**
     * 创建VO
     * @param basePath
     * @param entityName
     * @param tableColumnList
     * @throws IOException
     */
    public static void createVOFile(String basePath, String entityName, List<TableColumn> tableColumnList) throws IOException{
        File contentFile = GenerateDirectoryUtil.createFile(basePath, entityName, "vo", "VO");
        FileOutputStream out = new FileOutputStream(contentFile, true);
        //com.generate.framework
        String importBasePath = basePath.substring(basePath.indexOf("java") + 5, basePath.length() - 1).replace("\\", ".");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".vo;\n")
                .append("import java.util.Date;\n")
                .append(generateClassNote())
                .append("public class ").append(entityName).append("VO").append(" {\n\n");
        //设置属性
        tableColumnList.forEach(tableColumn -> {
            StringBuilder note = new StringBuilder("    /**\n").append("    * ").append(tableColumn.getColumnComment()).append("\n    */\n");
            String columnName = tableColumn.getColumnName();
            String dataType = tableColumn.getDataType();
            String javaType = DataTypeEnum.getByDbType(dataType);
            StringBuilder attributeDefine = new StringBuilder("    private ").append(javaType).append(" ")
                          .append(StringReplaceUtil.replaceUnderlineToUpperCase(columnName)).append(";\n\n");
            stringBuilder.append(note.toString()).append(attributeDefine.toString());
        });
        //设置getter and setter
        String setterAndGetter = generateSetterAndGetter(tableColumnList);
        stringBuilder.append(setterAndGetter);
        stringBuilder.append("}");
        out.write(stringBuilder.toString().getBytes("utf-8"));
    }

    /**
     * 创建facade
     * @param basePath
     * @param entityName
     * @throws IOException
     */
    public static void createFacadeFile(String basePath, String entityName) throws IOException {
        File contentFile = GenerateDirectoryUtil.createFile(basePath, entityName, "facade", "Facade");
        FileOutputStream out = new FileOutputStream(contentFile, true);
        String importBasePath = basePath.substring(basePath.indexOf("java") + 5, basePath.length() - 1).replace("\\", ".");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".facade;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".dto.").append(entityName).append("DTO;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".vo.").append(entityName).append("VO;\n")
                .append("import java.util.List;\n")
                .append(generateClassNote())
                .append("public interface ").append(entityName).append("Facade").append(" {\n\n")
                .append(generateFacadeInterface(entityName))
                .append("}");
        out.write(stringBuilder.toString().getBytes("utf-8"));
    }


    /**
     * 创建facade/impl
     * @param basePath
     * @param entityName
     * @throws IOException
     */
    public static void createFacadeImplFile(String basePath, String entityName) throws IOException{
        File contentFile = GenerateDirectoryUtil.createFile(basePath, entityName, "facade\\impl", "FacadeImpl");
        FileOutputStream out = new FileOutputStream(contentFile, true);
        String importBasePath = basePath.substring(basePath.indexOf("java") + 5, basePath.length() - 1).replace("\\", ".");
        String importPackage = "package " + importBasePath + "." + entityName.toLowerCase() + ".facade.impl;\n";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(importPackage)
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".dto.").append(entityName).append("DTO;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".service.").append(entityName).append("Service;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".facade.").append(entityName).append("Facade;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".vo.").append(entityName).append("VO;\n")
                .append("import java.util.List;\n")
                .append("import org.springframework.beans.BeanUtils;\n")
                .append("import org.springframework.stereotype.Component;\n")
                .append("import org.springframework.beans.factory.annotation.Autowired;\n")
                .append(generateClassNote())
                .append(generateFacadeImpl(entityName))
                .append("}");
        out.write(stringBuilder.toString().getBytes("utf-8"));
    }


    /**
     * 创建form表单
     * @param basePath
     * @param entityName
     * @param tableColumnList
     * @throws IOException
     */
    public static void createFormFile(String basePath, String entityName, List<TableColumn> tableColumnList) throws IOException{
        File contentFile = GenerateDirectoryUtil.createFile(basePath, entityName, "form", "Form");
        FileOutputStream out = new FileOutputStream(contentFile, true);
        //com.generate.framework
        String importBasePath = basePath.substring(basePath.indexOf("java") + 5, basePath.length() - 1).replace("\\", ".");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".form;\n")
                .append("import java.util.Date;\n")
                .append("import io.swagger.annotations.ApiModelProperty;\n\n")
                .append(generateClassNote())
                .append("public class ").append(entityName).append("Form").append(" {\n\n");
        //设置属性
        tableColumnList.forEach(tableColumn -> {
            StringBuilder note = new StringBuilder("    /**\n").append("    * ").append(tableColumn.getColumnComment()).append("\n    */\n");
            String columnName = tableColumn.getColumnName();
            String dataType = tableColumn.getDataType();
            String javaType = DataTypeEnum.getByDbType(dataType);
            String apiModelProperty = "    @ApiModelProperty(value = \"" + tableColumn.getColumnComment() + "\")\n";
            String attributeDefine = "    private " + javaType + " " + StringReplaceUtil.replaceUnderlineToUpperCase(columnName) + ";\n\n";
            stringBuilder.append(note.toString()).append(apiModelProperty).append(attributeDefine);
        });
        //设置getter and setter
        String setterAndGetter = generateSetterAndGetter(tableColumnList);
        stringBuilder.append(setterAndGetter);
        stringBuilder.append("}");
        out.write(stringBuilder.toString().getBytes("utf-8"));
    }

    /**
     * 创建controller
     * @param basePath
     * @param entityName
     * @throws IOException
     */
    public static void createControllerFile(String basePath, String entityName) throws IOException {
        File contentFile = GenerateDirectoryUtil.createFile(basePath, entityName, "controller", "Controller");
        FileOutputStream out = new FileOutputStream(contentFile, true);
        String importBasePath = basePath.substring(basePath.indexOf("java") + 5, basePath.length() - 1).replace("\\", ".");
        String facadeName = entityName + "Facade";
        String facadeNameLower = StringReplaceUtil.firstLetterToLowerCase(facadeName);
        String entityNameDTO = entityName + "DTO";
        String entityNameVO = entityName + "VO";
        String entityNameForm = entityName + "Form";
        String dtoEntityNameLowerCase = StringReplaceUtil.firstLetterToLowerCase(entityNameDTO);
        String voEntityNameLowerCase = StringReplaceUtil.firstLetterToLowerCase(entityNameVO);
        String formEntityNameLowerCase = StringReplaceUtil.firstLetterToLowerCase(entityNameForm);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".controller;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".dto.").append(entityName).append("DTO;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".facade.").append(entityName).append("Facade;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".form.").append(entityName).append("Form;\n")
                .append("import ").append(importBasePath).append(".").append(entityName.toLowerCase()).append(".vo.").append(entityName).append("VO;\n")
                .append("import io.swagger.annotations.Api;\n")
                .append("import io.swagger.annotations.ApiOperation;\n")
                .append("import org.springframework.beans.factory.annotation.Autowired;\n")
                .append("import org.springframework.web.bind.annotation.PathVariable;\n")
                .append("import org.springframework.web.bind.annotation.PostMapping;\n")
                .append("import org.springframework.web.bind.annotation.RequestBody;\n")
                .append("import org.springframework.web.bind.annotation.RequestMapping;\n")
                .append("import org.springframework.web.bind.annotation.RestController;\n")
                .append("import java.util.List;\n")
                .append(generateClassNote())
                .append("@RestController\n")
                .append("@Api(value = \"").append(entityName).append("\", description =\"\")\n")
                .append("@RequestMapping(value = \"/").append(entityName.toLowerCase()).append("\")\n")
                .append("public class ").append(entityName).append("Controller {\n\n")
                .append("    @Autowired\n")
                .append("    private ").append(facadeName).append(" ").append(facadeNameLower).append(";\n\n")
                //save
                .append("    @PostMapping(value = \"/save\")\n")
                .append("    @ApiOperation(value = \"\")\n")
                .append("    public ").append(entityNameVO).append(" save").append(entityName).append("(@RequestBody ")
                .append(entityNameForm).append(" ").append(formEntityNameLowerCase).append("){\n")
                .append("        ").append(entityNameDTO).append(" ").append(dtoEntityNameLowerCase).append(" = new ").append(entityNameDTO).append("();\n")
                .append("        ").append(entityNameVO).append(" ").append(voEntityNameLowerCase).append(" = ")
                .append(facadeNameLower).append(".").append("save").append(entityName).append("(").append(dtoEntityNameLowerCase).append(");\n")
                .append("        return ").append(voEntityNameLowerCase).append(";\n")
                .append("    }\n\n")
                //update
                .append("    @PostMapping(value = \"/update/{id}\")\n")
                .append("    @ApiOperation(value = \"\")\n")
                .append("    public ").append(entityNameVO).append(" update").append(entityName)
                .append("(@PathVariable(\"id\") Long id, @RequestBody ").append(entityNameForm).append(" ").append(formEntityNameLowerCase).append("){\n")
                .append("        ").append(entityNameDTO).append(" ").append(dtoEntityNameLowerCase).append(" = new ").append(entityNameDTO).append("();\n")
                .append("        ").append(entityNameVO).append(" ").append(voEntityNameLowerCase).append(" = ").append(facadeNameLower)
                .append(".update").append(entityName).append("ById(id, ").append(dtoEntityNameLowerCase).append(");\n")
                .append("        return ").append(voEntityNameLowerCase).append(";\n")
                .append("    }\n\n")
                //detail方法
                .append("    @PostMapping(value = \"/detail/{id}\")\n")
                .append("    @ApiOperation(value = \"\")\n")
                .append("    public ").append(entityNameVO).append(" query").append(entityName).append("Detail(@PathVariable(\"id\") Long id){\n")
                .append("        ").append(entityNameDTO).append(" ").append(dtoEntityNameLowerCase).append(" = new ").append(entityNameDTO).append("();\n")
                .append("        ").append(entityNameVO).append(" ").append(voEntityNameLowerCase).append(" = ").append(facadeNameLower)
                .append(".query").append(entityName).append("ById(id);\n")
                .append("        return ").append(voEntityNameLowerCase).append(";\n")
                .append("    }\n\n")
                //queryList
                .append("    @PostMapping(value = \"/list\")")
                .append("    @ApiOperation(value = \"\")\n")
                .append("    public List<").append(entityNameVO).append("> query").append(entityName).append("List(@RequestBody ").append(entityNameForm).append(" ")
                .append(formEntityNameLowerCase).append("){\n")
                .append("        ").append(entityNameDTO).append(" ").append(dtoEntityNameLowerCase).append(" = new ").append(entityNameDTO).append("();\n")
                .append("        List<").append(entityNameVO).append("> ").append(voEntityNameLowerCase).append("List = ").append(facadeNameLower)
                .append(".query").append(entityName).append("List(").append(dtoEntityNameLowerCase).append(");\n")
                .append("        return ").append(voEntityNameLowerCase).append("List;\n")
                .append("    }\n\n")
                .append("}\n");
        out.write(stringBuilder.toString().getBytes("utf-8"));
    }

    /**
     * 添加类上备注
     * @return
     */
    private static String generateClassNote(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/**\n")
                .append(" * @author ")
                .append(System.getProperty("user.name"))
                .append(" \n")
                .append(" * @since")
                .append(new Date())
                .append("\n")
                .append(" */\n");
        return stringBuilder.toString();
    }

    /**
     * 创建DAO findByIdAndDeleted方法
     * @param entityName
     * @return
     */
    private static String generateFindByIdAndDeleted(String entityName){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    /**\n")
         .append("     * 根据id和deleted查询详情\n")
         .append("     * @param id\n")
         .append("     * @param deleted\n")
         .append("     * @return\n")
         .append("     */\n")
         .append("     ")
         .append(entityName).append(" findByIdAndDeleted(Long id, Integer deleted);\n\n");
        return stringBuilder.toString();
    }

    /**
     * 创建DAO findByProjectIdAndDeleted方法
     * @param entityName
     * @return
     */
    private static String generateFindByProjectIdAndDeleted(String entityName){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    /**\n")
                .append("     * 根据projectId和deleted查询详情\n")
                .append("     * @param projectId\n")
                .append("     * @param deleted\n")
                .append("     * @return\n")
                .append("     */\n")
                .append("     ")
                .append("List<").append(entityName).append(">").append(" findByProjectIdAndDeleted(Long projectId, Integer deleted);\n\n");
        return stringBuilder.toString();
    }


    /**
     * 生成setter和getter方法
     * @param tableColumnList
     * @return
     */
    private static String generateSetterAndGetter(List<TableColumn> tableColumnList){
        StringBuilder stringBuilder = new StringBuilder();
        tableColumnList.forEach(tableColumn -> {
            StringBuilder getterBuilder = new StringBuilder();
            StringBuilder setterBuilder = new StringBuilder();
            String javaType = DataTypeEnum.getByDbType(tableColumn.getDataType());
            String javaColumnName = StringReplaceUtil.replaceUnderlineToUpperCase(tableColumn.getColumnName());
            String upperCaseColumnName = StringReplaceUtil.firstLetterToUpperCase(javaColumnName);
            getterBuilder.append("    public ").append(javaType).append(" get").append(upperCaseColumnName).append("(){\n");
            getterBuilder.append("        return this.").append(javaColumnName).append(";\n").append("    }\n\n");

            setterBuilder.append("    public ").append("void").append(" set").append(upperCaseColumnName).append("(").append(javaType).append(" ").append(javaColumnName).append("){\n");
            setterBuilder.append("         this.").append(javaColumnName).append(" = ").append(javaColumnName).append(";\n").append("    }\n\n");
            stringBuilder.append(getterBuilder.toString());
            stringBuilder.append(setterBuilder.toString());
        });
        return stringBuilder.toString();
    }


    /**
     * 新增service 增删查改接口
     * @param entityName
     * @return
     */
    private static String generateServiceInterface(String entityName){
        StringBuilder stringBuilder = new StringBuilder();
        String entityNameDTO = entityName + "DTO";
        //新增方法save
        stringBuilder.append("    /**\n")
                .append("     * 新增数据\n")
                .append("     * @param ").append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append("\n")
                .append("     * @return\n")
                .append("     */\n")
                //新增save接口
                .append("    ").append(entityNameDTO).append(" save").append(entityName).append("(").append(entityNameDTO).append(" ")
                .append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append(");\n\n")
                .append("    /**\n")
                .append("     * 更新数据\n")
                .append("     * @param id\n")
                .append("     * @param ").append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append("\n")
                .append("     * @return\n")
                .append("     */\n")
                //新增updateById(EntityNameDTO entityNameDTO, Long id)接口
                .append("    ").append(entityNameDTO).append(" update").append(entityName).append("ById(Long id, ").append(entityNameDTO).append(" ")
                .append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append(");\n\n")
                .append("    /**\n")
                .append("     * 根据id查询详情\n")
                .append("     * @param id\n")
                .append("     * @return\n")
                .append("     */\n")
                //新增queryById(Long id)
                .append("    ").append(entityNameDTO).append(" query").append(entityName).append("ById").append("(Long id);\n\n")
                //新增queryByEntityNameDTOList
                .append("    /**\n")
                .append("     * 查询列表\n")
                .append("     * @param ").append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append("\n")
                .append("     * @return\n")
                .append("     */\n")
                .append("    ").append("List<").append(entityNameDTO).append("> query").append(entityName).append("List(")
                .append(entityNameDTO).append(" ").append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append(");\n");
        return stringBuilder.toString();
    }

    /**
     * service 代码实现
     * @param entityName
     * @return
     */
    private static String generateServiceImpl(String entityName){
        String entityNameDTO = entityName + "DTO";
        String entityNameDAO = entityName + "DAO";
        String entityNameLowerCase = StringReplaceUtil.firstLetterToLowerCase(entityName);
        String entityNameDTOLowerCase = StringReplaceUtil.firstLetterToLowerCase(entityNameDTO);
        String entityNameDAOLoweCase = entityNameLowerCase + "DAO";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("@Service\n")
                 .append("public class ").append(entityName).append("ServiceImpl implements ").append(entityName).append("Service {\n\n")
                 .append("    @Autowired\n")
                 .append("    private ").append(entityNameDAO).append(" ").append(entityNameDAOLoweCase).append(";\n\n")
                 //save接口实现类
                 .append("    @Override\n")
                 .append("    public ").append(entityNameDTO).append(" save").append(entityName).append("(").append(entityNameDTO)
                 .append(" ").append(entityNameDTOLowerCase).append("){\n")
                 .append("        ").append(entityName).append(" ").append(entityNameLowerCase).append(" = new ").append(entityName).append("();\n")
                 .append("        ").append("BeanUtils.copyProperties(").append(entityNameDTOLowerCase).append(", ").append(entityNameLowerCase).append(");\n")
                 .append("        ").append(entityName).append(" save = ").append(entityNameDAOLoweCase).append(".save(").append(entityNameLowerCase).append(");\n")
                 .append("        ").append(entityNameDTO).append(" resultDTO = new ").append(entityNameDTO).append("();\n")
                 .append("        BeanUtils.copyProperties(save, resultDTO);\n")
                 .append("        return resultDTO;\n")
                 .append("    }\n\n")
                 //updateById实现类
                 .append("    @Override\n")
                 .append("    public ").append(entityNameDTO).append(" update").append(entityName).append("ById(Long id, ")
                 .append(entityNameDTO).append(" ").append(entityNameDTOLowerCase).append(") {\n")
                 .append("        if (null == id) {\n")
                 .append("            //TODO 抛出异常\n")
                 .append("        }\n")
                 .append("        ").append(entityName).append(" ").append(entityNameLowerCase).append(" = new ").append(entityName).append("();\n")
                 .append("        BeanUtils.copyProperties(").append(entityNameDTOLowerCase).append(", ").append(entityNameLowerCase).append(");\n")
                 .append("        ").append(entityNameLowerCase).append(".setId(id);\n")
                 .append("        ").append(entityName).append(" save = ").append(entityNameDAOLoweCase).append(".save(").append(entityNameLowerCase).append(");\n")
                 .append("        ").append(entityNameDTO).append(" resultDTO = new ").append(entityNameDTO).append("();\n")
                 .append("        BeanUtils.copyProperties(save, resultDTO);\n")
                 .append("        return resultDTO;\n")
                 .append("    }\n\n")
                 //queryCheckListDTOById实现方法
                 .append("    @Override\n")
                 .append("    public ").append(entityNameDTO).append(" query").append(entityName).append("ById(Long id) {\n")
                 .append("        if (null == id) {\n")
                 .append("            //TODO 抛出异常\n")
                 .append("        }\n")
                 .append("        ").append(entityName).append(" ").append(entityNameLowerCase).append(" = ").append(entityNameDAOLoweCase).append(".findByIdAndDeleted(id, 0);\n")
                 .append("        ").append(entityNameDTO).append(" resultDTO = new ").append(entityNameDTO).append("();\n")
                 .append("        BeanUtils.copyProperties(").append(entityNameLowerCase).append(", resultDTO);\n")
                 .append("        return resultDTO;\n")
                 .append("    }\n\n")
                 //queryCheckListDTOList实现
                 .append("    @Override\n")
                 .append("    public List<").append(entityNameDTO).append("> query").append(entityName).append("List(")
                 .append(entityNameDTO).append(" ").append(entityNameDTOLowerCase).append(") {\n")
                 .append("        return null;\n")
                 .append("    }\n\n");
        return stringBuilder.toString();
    }

    /**
     * 生成facade接口
     * @param entityName
     * @return
     */
    private static String generateFacadeInterface(String entityName){
        StringBuilder stringBuilder = new StringBuilder();
        String entityNameVO = entityName + "VO";
        String entityNameDTO = entityName + "DTO";
        //新增方法save
        stringBuilder.append("    /**\n")
                .append("     * 新增数据\n")
                .append("     * @param ").append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append("\n")
                .append("     * @return\n")
                .append("     */\n")
                //新增save接口
                .append("    ").append(entityNameVO).append(" save").append(entityName).append("(").append(entityNameDTO).append(" ")
                .append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append(");\n\n")
                .append("    /**\n")
                .append("     * 更新数据\n")
                .append("     * @param id\n")
                .append("     * @param ").append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append("\n")
                .append("     * @return\n")
                .append("     */\n")
                //新增updateById(EntityNameDTO entityNameVO, Long id)接口
                .append("    ").append(entityNameVO).append(" update").append(entityName).append("ById(Long id, ").append(entityNameDTO).append(" ")
                .append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append(");\n\n")
                .append("    /**\n")
                .append("     * 根据id查询详情\n")
                .append("     * @param id\n")
                .append("     * @return\n")
                .append("     */\n")
                //新增queryById(Long id)
                .append("    ").append(entityNameVO).append(" query").append(entityName).append("ById").append("(Long id);\n\n")
                //新增queryByEntityNameDTOList
                .append("    /**\n")
                .append("     * 查询列表\n")
                .append("     * @param ").append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append("\n")
                .append("     * @return\n")
                .append("     */\n")
                .append("    ").append("List<").append(entityNameVO).append("> query").append(entityName).append("List(")
                .append(entityNameDTO).append(" ").append(StringReplaceUtil.firstLetterToLowerCase(entityNameDTO)).append(");\n");
        return stringBuilder.toString();
    }

    /**
     * facade 代码实现
     * @param entityName
     * @return
     */
    private static String generateFacadeImpl(String entityName){
        String entityNameDTO = entityName + "DTO";
        String entityNameVO = entityName + "VO";
        String entityNameService = entityName + "Service";
        String ServiceEntityNameLowerCase = StringReplaceUtil.firstLetterToLowerCase(entityNameService);
        String dtoEntityNameLowerCase = StringReplaceUtil.firstLetterToLowerCase(entityNameDTO);
        String voEntityNameLowerCase = StringReplaceUtil.firstLetterToLowerCase(entityNameVO);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("@Component\n")
                .append("public class ").append(entityName).append("FacadeImpl implements ").append(entityName).append("Facade {\n\n")
                .append("    @Autowired\n")
                .append("    private ").append(entityNameService).append(" ").append(ServiceEntityNameLowerCase).append(";\n\n")
                //save接口实现类
                .append("    @Override\n")
                .append("    public ").append(entityNameVO).append(" save").append(entityName).append("(").append(entityNameDTO)
                .append(" ").append(dtoEntityNameLowerCase).append("){\n")
                .append("        ").append(entityNameVO).append(" ").append(voEntityNameLowerCase).append(" = new ").append(entityNameVO).append("();\n")
                .append("        ").append(entityNameDTO).append(" resultDTO = ").append(ServiceEntityNameLowerCase)
                .append(".save").append(entityName).append("(").append(dtoEntityNameLowerCase).append(");\n")
                .append("        BeanUtils.copyProperties(resultDTO, ").append(voEntityNameLowerCase).append(");\n")
                .append("        return ").append(voEntityNameLowerCase).append(";\n")
                .append("    }\n\n")
                //updateById实现类
                .append("    @Override\n")
                .append("    public ").append(entityNameVO).append(" update").append(entityName).append("ById(Long id, ")
                .append(entityNameDTO).append(" ").append(dtoEntityNameLowerCase).append(") {\n")
                .append("        if (null == id) {\n")
                .append("            //TODO 抛出异常\n")
                .append("        }\n")
                .append("        ").append(entityNameVO).append(" ").append(voEntityNameLowerCase).append(" = new ").append(entityNameVO).append("();\n")
                .append("        ").append(entityNameDTO).append(" resultDTO = ").append(ServiceEntityNameLowerCase)
                .append(".update").append(entityName).append("ById(id, ").append(dtoEntityNameLowerCase).append(");\n")
                .append("        BeanUtils.copyProperties(resultDTO, ").append(voEntityNameLowerCase).append(");\n")
                .append("        return ").append(voEntityNameLowerCase).append(";\n")
                .append("    }\n\n")
                //queryCheckListDTOById实现方法
                .append("    @Override\n")
                .append("    public ").append(entityNameVO).append(" query").append(entityName).append("ById(Long id) {\n")
                .append("        if (null == id) {\n")
                .append("            //TODO 抛出异常\n")
                .append("        }\n")
                .append("        ").append(entityNameVO).append(" ").append(voEntityNameLowerCase).append(" = new ").append(entityNameVO).append("();\n")
                .append("        ").append(entityNameDTO).append(" resultDTO = ").append(ServiceEntityNameLowerCase)
                .append(".query").append(entityName).append("ById(id);\n")
                .append("        BeanUtils.copyProperties(resultDTO, ").append(voEntityNameLowerCase).append(");\n")
                .append("        return ").append(voEntityNameLowerCase).append(";\n")
                .append("    }\n\n")
                //queryCheckListDTOList实现
                .append("    @Override\n")
                .append("    public List<").append(entityNameVO).append("> query").append(entityName).append("List(")
                .append(entityNameDTO).append(" ").append(dtoEntityNameLowerCase).append(") {\n")
                .append("        return null;\n")
                .append("    }\n\n");
        return stringBuilder.toString();
    }

}
