package com.libii.sso.unity.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.libii.sso.common.core.AbstractService;
import com.libii.sso.common.exception.CustomException;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.common.restResult.ResultCode;
import com.libii.sso.unity.dao.WhiteListMapper;
import com.libii.sso.unity.domain.WhiteListInfo;
import com.libii.sso.unity.dto.WhiteListConditionDTO;
import com.libii.sso.unity.dto.WhiteListInputDTO;
import com.libii.sso.unity.dto.WhiteListOutputDTO;
import com.libii.sso.unity.service.WhiteListService;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: fengchenxin
 * @ClassName: WhiteListServiceImpl
 * @date: 2023-04-20  10:34
 * @Description: TODO
 * @version: 1.0
 */
@Service
@Transactional
public class WhiteListServiceImpl extends AbstractService<WhiteListInfo> implements WhiteListService {

    @Resource
    private WhiteListMapper whiteListMapper;

    @Resource
    private Cache<String, Integer> whiteListCache;

    @Override
    public List<WhiteListOutputDTO> list(PageParam pageParam, WhiteListConditionDTO whiteListConditionDTO) {
        PageHelper.startPage(pageParam.getPage(), pageParam.getSize());
        return whiteListMapper.list(whiteListConditionDTO);
    }

    @Override
    public Map<String, Integer> verify(WhiteListInputDTO whiteListInputDTO) {
        String cacheKey = whiteListInputDTO.getGameId()+"-"+whiteListInputDTO.getDeviceId();
        Integer ifPresent = whiteListCache.getIfPresent(cacheKey);
        Map<String, Integer> resultMap = new HashMap<>();
        if(null!=ifPresent){
            resultMap.put("result",0);
        }else {
            resultMap.put("result",1);
        }
        return resultMap;
    }

    @Override
    public void add(WhiteListInputDTO inputDTO) {
        WhiteListInfo whiteListInfo = new WhiteListInfo();
        BeanUtils.copyProperties(inputDTO,whiteListInfo);
        whiteListInfo.setCreateTime(new Date());
        try {
            whiteListMapper.insert(whiteListInfo);
        } catch (DuplicateKeyException e) {
            throw new CustomException(ResultCode.GAME_DEVICE_EXIST);
        }
    }

    @Override
    public void deleteWhiteList(WhiteListInputDTO inputDTO) {
        whiteListMapper.deleteWhiteList(inputDTO);
    }

    @Scheduled(fixedRateString = "${timed-task.white-list-refresh-cycle}")
    public void whiteListCacheBuilding(){
        List<WhiteListInfo> whiteListInfos = whiteListMapper.selectAll();
        Map<String, Integer> collect = whiteListInfos.stream().collect(Collectors.toMap(w -> w.getGameId() + "-" + w.getDeviceId(), w -> w.hashCode()));
        whiteListCache.invalidateAll();
        whiteListCache.putAll(collect);
    }
}
