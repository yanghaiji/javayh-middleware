package com.javayh.mongo.repository;

import com.javayh.common.entity.SysMenu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dylan Yang
 * @Description: MG
 * @Title: SysMenuRepository
 * @ProjectName javayh-middleware
 * @date 2019/7/21 0:28
 */
@Repository
public interface SysMenuRepository extends MongoRepository<SysMenu, String> {

    /**
     * 模糊查询
     * StartsWith    起始位置开始匹配
     * EndsWith      结束位置
     *
     * @param code 匹配内容
     * @return
     */
    List<SysMenu> findByCodeStartsWith(String code);

}

