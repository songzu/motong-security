package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.model.CustomerBindeModel;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.CustomerInfoEntity;
import org.springframework.web.bind.annotation.RequestBody;

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

    R confirm(Long[] ids);

    R bindeCustomer(CustomerBindeModel bindeCustomerModel);

    R customerDetail(String wechaId);
}

