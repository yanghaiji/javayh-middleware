package com.javayh.controller;

import com.javayh.common.entity.Order;
import com.javayh.service.DruidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName javayh-middleware → com.javayh.controller → DruidController
 * @Description
 * @Author Dylan
 * @Date 2019/11/22 9:52
 * @Version
 */
@RestController
@RequestMapping(value = "/test/")
public class DruidController {

    @Autowired
    private DruidService druidService;

    /**
     * @Description
     * @UserModule:
     * @author Dylan
     * @date 2019/11/22
     * @param
     * @return com.javayh.common.entity.Order
     */
    @GetMapping("get")
    public Order get(){
        return druidService.get();
    }
}
