package com.generate.framework.demo.controller;

import com.generate.framework.demo.service.TableColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/11
 */
@RestController
@RequestMapping(value = "/generate/code")
public class TableColumnController {

    @Autowired
    private TableColumnService tableColumnService;

    @PostMapping(value = "/")
    public boolean generateCode(String tableName){
        return tableColumnService.generateBaseCode(tableName);
    }


}
