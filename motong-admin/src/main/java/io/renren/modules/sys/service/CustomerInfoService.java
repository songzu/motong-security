package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.CustomerInfoEntity;

import java.util.Map;

/**
 * 客户信息表
 *
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2020-05-31 21:41:20
 */
public interface CustomerInfoService extends IService<CustomerInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Integer confirm(Long[] ids);
}

