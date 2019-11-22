package com.javayh.service;

import com.javayh.common.entity.Order;
import com.javayh.conf.dao.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName javayh-middleware → com.javayh.service → DruidService
 * @Description
 * @Author Dylan
 * @Date 2019/11/22 9:53
 * @Version
 */
@Service
public class DruidService {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * @Description
     * @UserModule:
     * @author Dylan
     * @date 2019/11/22
     * @param
     * @return com.javayh.common.entity.Order
     */
    public Order get(){
       return orderMapper.selectByPrimaryKey(-2141346672);
    }
}
