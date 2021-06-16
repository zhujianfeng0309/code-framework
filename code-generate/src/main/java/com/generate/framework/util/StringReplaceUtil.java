package com.generate.framework.util;

import org.springframework.util.StringUtils;

/**
 * @since 2019/7/12
 */
public class StringReplaceUtil {

    /**
     * template_parent_type_name ==> templateParentTypeName
     * @param column
     * @return
     */
    public static String replaceUnderlineToUpperCase(String column){
        StringBuilder stringBuilder = new StringBuilder();
        String[] columnArr = column.split("_");
        for (int i = 0; i < columnArr.length; i++){
            String targetColumn = columnArr[i];
            if (i > 0){
                targetColumn = firstLetterToUpperCase(targetColumn);
            }
            stringBuilder.append(targetColumn);
        }
        return stringBuilder.toString();
    }

    /**
     * 首字母大写
     * @param target
     * @return
     */
    public static String firstLetterToUpperCase(String target){
        if (StringUtils.isEmpty(target)){
            return "";
        }
        String firstChar = target.substring(0, 1);
        String restChar = target.substring(1);
        return firstChar.toUpperCase() + restChar;
    }

    /**
     * 首字母小写
     * @param target
     * @return
     */
    public static String firstLetterToLowerCase(String target){
        if (StringUtils.isEmpty(target)){
            return "";
        }
        String firstChar = target.substring(0, 1);
        String restChar = target.substring(1);
        return firstChar.toLowerCase() + restChar;
    }

}
