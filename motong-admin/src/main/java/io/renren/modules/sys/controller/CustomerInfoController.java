package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import io.renren.common.utils.JsonUtils;
import io.renren.common.validator.ValidatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.sys.entity.CustomerInfoEntity;
import io.renren.modules.sys.service.CustomerInfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import sun.rmi.runtime.Log;


/**
 * 客户信息表
 *
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2020-05-31 21:41:20
 */
@Slf4j
@RestController
@RequestMapping("sys/customerinfo")
public class CustomerInfoController {
    @Autowired
    private CustomerInfoService customerInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:customerinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = customerInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:customerinfo:info")
    public R info(@PathVariable("id") Long id) {
        CustomerInfoEntity customerInfo = customerInfoService.getById(id);

        return R.ok().put("customerInfo", customerInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:customerinfo:save")
    public R save(@RequestBody CustomerInfoEntity customerInfo) {
        customerInfo.setBindeStatus(0);
        customerInfo.setValidStatus(0);
        customerInfo.setStatus(1);
        customerInfo.setGmtCreate(new Date());
        customerInfo.setGmtUpdate(new Date());
        customerInfoService.save(customerInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:customerinfo:update")
    public R update(@RequestBody CustomerInfoEntity customerInfo) {
        ValidatorUtils.validateEntity(customerInfo);
        customerInfoService.updateById(customerInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:customerinfo:delete")
    public R delete(@RequestBody Long[] ids) {
        customerInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/waitConfirmlist")
    @RequiresPermissions("sys:customerinfo:list")
    public R waitConfirmlist(@RequestParam Map<String, Object> params) {
        params.put("validStatus", 0);
        params.put("bindeStatus", 1);
        PageUtils page = customerInfoService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 确认
     */
    @RequestMapping("/conf")
    @RequiresPermissions("sys:customerinfo:delete")
    public R confirm(@RequestBody Long[] ids) {
        log.info("用户确认请求,[{}]", JsonUtils.toJson(ids));
        R result = customerInfoService.confirm(ids);
        log.info("用户确认结果,result:[{}]", JsonUtils.toJson(result));

        return R.ok();
    }

}
