package com.javayh.mongo.repository;

import com.javayh.common.entity.SysMenu;
import com.javayh.conf.mongo.MongodbBaseDao;
import org.springframework.stereotype.Repository;

/**
 * @author Dylan Yang
 * @Description: MgRepository
 * @Title: MgRepository
 * @ProjectName javayh-middleware
 * @date 2019/7/21 17:25
 */
@Repository
public class MgRepository extends MongodbBaseDao<SysMenu> {

    @Override
    protected Class getEntityClass() {
        return SysMenu.class;
    }

}

