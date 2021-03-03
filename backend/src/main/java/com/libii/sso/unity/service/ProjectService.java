package com.libii.sso.unity.service;

import com.libii.sso.unity.domain.Project;
import com.libii.sso.common.core.Service;
import com.libii.sso.common.restResult.PageParam;
import java.util.List;

/**
* @author Generate
* @Description: // TODO 为类添加注释
* @date 2021-03-03 03:50:52
*/
public interface ProjectService extends Service<Project> {

    /**
     * 根据分页、排序信息和检索条件查询 @size 条 字典表数据
     * @param pageParam 分页参数
     * @param query  查询关键字
     * @return
     */
    List<Project> list(PageParam pageParam, String query);

}
