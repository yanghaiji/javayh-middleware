package com.javayh.mongo.controller;

import com.javayh.common.entity.SysMenu;
import com.javayh.common.id.Uid;
import com.javayh.common.result.Result;
import com.javayh.mongo.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dylan Yang
 * @Description: SysMenuCtr
 * @Title: SysMenuCtr
 * @ProjectName javayh-cloud
 * @date 2019/7/21 0:47
 */
@RestController
public class SysMenuCtr {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 新增
     *
     * @param sysMenu
     * @return
     */
    @GetMapping(value = "save")
    public Result save(/*@RequestBody SysMenu sysMenu*/) {
        SysMenu sysMenu = SysMenu.builder().id(Uid.getUid()).pcode(Uid.getUid()).code(Uid.getUid()).build();

        return Result.javaYhInsertSuccess(sysMenuService.save(sysMenu));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping(value = "findAll")
    public Result findAll() {
        Pageable pageable = new PageRequest(1, 4);
        return Result.javaYhQuerySuccess(sysMenuService.findAll(pageable));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @GetMapping(value = "delete/{id}")
    public Result delete(@PathVariable String id) {
        String exists = sysMenuService.delete(id);
        return "1".equals(exists) ?
                Result.javaYhDeleteSuccess(" delete success") :
                Result.javaYhResultFailed("delete failed");
    }

    /**
     * 利用jpa自带的方法名进行sql映射
     *
     * @param code
     * @return
     */
    @GetMapping(value = "findByCodeStartsWith/{code}")
    public Result findByCodeStartsWith(@PathVariable String code) {
        return Result.javaYhQuerySuccess(sysMenuService.findByCodeStartsWith(code));
    }


    /**
     * 模糊查询所有
     *
     * @return
     */
    @GetMapping(value = "findPage")
    public Result findPage() {
        Pageable pageable = new PageRequest(1, 4);
        return Result.javaYhQuerySuccess(sysMenuService.findPag(pageable));
    }

    @GetMapping(value = "findIs/{id}")
    public Result findIs(@PathVariable String id) {
        return Result.javaYhQuerySuccess(sysMenuService.find(id));
    }
}

