package com.libii.sso.unity.service.impl;

import com.libii.sso.common.core.AbstractService;
import com.libii.sso.unity.dao.ProjectMapper;
import com.libii.sso.unity.domain.Project;
import com.libii.sso.unity.service.ProjectService;
import com.libii.sso.common.restResult.PageParam;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Generate
* @Description: // TODO 为类添加注释
* @date 2021-03-03 03:50:52
*/
@Slf4j
@Service
@Transactional
public class ProjectServiceImpl extends AbstractService<Project> implements ProjectService {
    @Resource
    private ProjectMapper projectMapper;

    /**
     * 根据分页、排序信息和检索条件查询 @size 条 字典表数据
     * @param pageParam 分页参数
     * @param query  查询关键字
     * @return
     */
    @Override
    public List<Project> list(PageParam pageParam, String query) {
        Example example = new Example(Project.class);
        if (StringUtils.isNotEmpty(query)){
            example.and()
                    .orLike("code", "%" + query + "%")
                    .orLike("name", "%" + query + "%");
        }

        PageHelper.startPage(pageParam.getPage(), pageParam.getSize(), pageParam.getOrderBy());
        return projectMapper.selectByExample(example);
    }
}
