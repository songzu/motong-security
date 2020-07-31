package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import io.renren.common.model.CustomerBindeModel;
import io.renren.common.utils.*;
import io.renren.modules.sys.dao.CustomerInfoDao;
import io.renren.modules.sys.entity.CustomerInfoEntity;
import io.renren.modules.sys.service.CustomerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
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
                new QueryWrapper<CustomerInfoEntity>().eq(validStatus != null, "valid_status", validStatus)
                        .eq(bindeStatus != null, "binde_status", bindeStatus)
        );

        for (CustomerInfoEntity customerInfoEntity : page.getRecords()) {
            customerInfoEntity.setValidStatusName(customerInfoEntity.getValidStatus() == 1 ? "生效" : "未生效");
            customerInfoEntity.setBindeStatusName(customerInfoEntity.getBindeStatus() == 1 ? "绑定" : "未绑定");
            customerInfoEntity.setWeChatNickName(StringUtils.isNotBlank(customerInfoEntity.getWeChatNickName()) ? customerInfoEntity.getWeChatNickName() : "NA");
        }

        return new PageUtils(page);
    }

    @Override
    public R confirm(Long[] ids) {
        try {
            Integer confirm = customerInfoDao.confirm(Lists.newArrayList(ids));
            return R.ok();
        } catch (Exception e) {
            log.warn("用户确认失败,请求参数[{}],[{}]", JsonUtils.toJson(ids), e);
            return R.error(100, "用户确认失败");
        }
    }

    @Override
    public R bindeCustomer(CustomerBindeModel bindeCustomerModel) {

        try {
            //1.查询
            List<CustomerInfoEntity> customerInfoList = customerInfoDao.query(bindeCustomerModel.getUserName(), bindeCustomerModel.getPassword());
            //2.存在性校验
            if (CollectionUtils.isEmpty(customerInfoList)) {
                return R.error(100, "用户信息不存在");
            }
            //3.绑定校验
            List<CustomerInfoEntity> usedCustomerInfos = FunctionUtil.filter(customerInfoList, data -> bindeCustomerModel.getWeChatOpenId().equals(data.getWeChatOpenId()));
            if (!CollectionUtils.isEmpty(usedCustomerInfos)) {
                return R.error(101, "当前微信已绑定");
            }
            //4.绑定
            List<CustomerInfoEntity> customerInfoFilter = FunctionUtil.filter(customerInfoList, data -> data.getBindeStatus() == 0);
            if (!CollectionUtils.isEmpty(customerInfoFilter)) {
                //4.1有未绑定记录-更新
                CustomerInfoEntity customerInfoEntity = customerInfoFilter.get(0);
                customerInfoEntity.setBindeStatus(1);
                customerInfoEntity.setWeChatNickName(bindeCustomerModel.getWeChatNickName());
                customerInfoEntity.setWeChatOpenId(bindeCustomerModel.getWeChatOpenId());
                this.updateById(customerInfoEntity);
            } else {
                //4.1无未绑定记录-新增
                CustomerInfoEntity customerInfoEntity = customerInfoList.get(0);
                customerInfoEntity.setId(null);
                customerInfoEntity.setBindeStatus(1);
                customerInfoEntity.setValidStatus(0);
                customerInfoEntity.setWeChatNickName(bindeCustomerModel.getWeChatNickName());
                customerInfoEntity.setWeChatOpenId(bindeCustomerModel.getWeChatOpenId());
                this.save(customerInfoEntity);
            }

            return R.ok();
        } catch (Exception e) {
            log.warn("绑定用户失败,请求参数[{}],[{}]", JsonUtils.toJson(bindeCustomerModel), e);
            return R.error(100, "用户绑定失败");
        }
    }

    @Override
    public R customerDetail(String wxOpenId) {

        try {
            //1.查询
            List<CustomerInfoEntity> customerInfoList = customerInfoDao.queryByWechatId(wxOpenId);
            if (CollectionUtils.isEmpty(customerInfoList)) {
                return R.error(100, "用户信息不存在");
            } else if (customerInfoList.size() != 1) {
                return R.error(101, "用户信息大于1条");
            }
            //2.状态判断
            CustomerInfoEntity customerInfoEntity = customerInfoList.get(0);
            if (customerInfoEntity.getBindeStatus() != 1) {
                return R.error(101, "用户信息未绑定");
            }
            if (customerInfoEntity.getValidStatus() != 1) {
                return R.error(101, "用户信息未生效");
            }

            return R.ok().put("data", customerInfoEntity);
        } catch (Exception e) {
            log.info("查询用户信息失败,wxOpenId[{}],[{}]", wxOpenId, e);
            return R.error(101, "查询用户信息失败");
        }

    }

}
