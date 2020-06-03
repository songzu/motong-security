package io.renren.modules.sys.service.impl;

import com.google.common.collect.Lists;
import io.renren.common.utils.Constant;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.CustomerInfoDao;
import io.renren.modules.sys.entity.CustomerInfoEntity;
import io.renren.modules.sys.service.CustomerInfoService;

import javax.annotation.Resource;


@Service("customerInfoService")
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoDao, CustomerInfoEntity> implements CustomerInfoService {

    @Resource
    private CustomerInfoDao customerInfoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Object validStatus = params.get("validStatus");
        Object bindeStatus = params.get("bindeStatus");

        IPage<CustomerInfoEntity> page = this.page(
                new Query<CustomerInfoEntity>().getPage(params),
                new QueryWrapper<CustomerInfoEntity>().eq(validStatus != null,"valid_status", validStatus)
                        .eq(bindeStatus != null,"binde_status", bindeStatus)
        );

        for (CustomerInfoEntity customerInfoEntity : page.getRecords()) {
            customerInfoEntity.setValidStatusName(customerInfoEntity.getValidStatus() == 1 ? "生效" : "未生效");
            customerInfoEntity.setBindeStatusName(customerInfoEntity.getBindeStatus() == 1 ? "绑定" : "未绑定");
            customerInfoEntity.setWeChatNickName(StringUtils.isNotBlank(customerInfoEntity.getWeChatNickName()) ? customerInfoEntity.getWeChatNickName() : "NA");
        }

        return new PageUtils(page);
    }

    @Override
    public Integer confirm(Long[] ids) {
        return customerInfoDao.confirm(Lists.newArrayList(ids));
    }

}
