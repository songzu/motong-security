package io.renren.modules.wechat.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import io.renren.common.config.WxMaConfiguration;
import io.renren.common.model.BindeCustomerModel;
import io.renren.common.model.CodeModel;
import io.renren.common.utils.FunctionUtil;
import io.renren.common.utils.R;
import io.renren.modules.sys.dao.CustomerInfoDao;
import io.renren.modules.sys.entity.CustomerInfoEntity;
import io.renren.modules.sys.service.CustomerInfoService;
import io.renren.common.utils.JsonUtils;
import io.renren.modules.wechat.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;

/**
 * <p></p>
 *
 * @author 松竹
 * @version $$ Id: WeChatController.java, V 0.1 2020/5/31 12:04 gengen.wang Exp $$
 **/
@Slf4j
@RestController
@RequestMapping("/wechat")
public class WeChatController {

    @Resource
    private CustomerInfoService customerInfoService;

    @Resource
    private WeChatService weChatService;

    @Resource
    private CustomerInfoDao customerInfoDao;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest req, HttpServletResponse res) {
        this.request = req;
        this.response = res;
        this.session = req.getSession();
    }

//    @Resource
//    private WeChatService weChatService;
//
//    @RequestMapping(value = "/getsessionid", method = RequestMethod.POST)
//    public R getSessionId(@RequestParam("code") String code) {
//        log.info("授权码,code:[{}]",code);
//        try {
//            if (StringUtils.isBlank(code)) {
//                return R.error("参数不能为空");
//            }
//
//            Object object = weChatService.getSessionIdByCode(code);
//            log.info("/sesion/getsessionid object{}", object);
//            return R.ok().put("object", object);
//        } catch (Exception e) {
//            log.error("/sesion/getsessionid 异常", e);
//            return R.error("服务异常");
//        }
//    }

    /**
     * 登陆接口
     */
    @RequestMapping(value = "/wechat", method = RequestMethod.POST)
    public R login(@RequestBody CodeModel codeModel) {
        log.info("微信登陆,请求参数[{}]", JsonUtils.toJson(codeModel));
        //1.参数校验
        if (codeModel == null || StringUtils.isBlank(codeModel.getCode())) {
            return R.error(100, "请求参数不能为空");
        }
        R result = weChatService.login(codeModel.getCode());
        log.info("微信登陆,结果[{}]", JsonUtils.toJson(result));
        return result;
    }

    /**
     * 用户绑定
     *
     * @param bindeCustomerModel
     * @return
     */
    @RequestMapping(value = "/binde", method = RequestMethod.POST)
    public R bindeCustomer(@RequestBody BindeCustomerModel bindeCustomerModel) {
        //1.参数校验
        if (bindeCustomerModel == null) {
            return R.error(99, "请求参数不能为空");
        }
        if (StringUtils.isBlank(bindeCustomerModel.getUserName())) {
            return R.error(99, "用户名不能为空");
        }
        if (StringUtils.isBlank(bindeCustomerModel.getPassword())) {
            return R.error(99, "密码不能为空");
        }
        if (StringUtils.isBlank(bindeCustomerModel.getWeChatOpenId())) {
            return R.error(99, "微信id不能为空");
        }
        if (StringUtils.isBlank(bindeCustomerModel.getWeChatNickName())) {
            return R.error(99, "微信昵称不能为空");
        }

        //2.查询
        List<CustomerInfoEntity> customerInfoList = customerInfoDao.query(bindeCustomerModel.getUserName(), bindeCustomerModel.getPassword());
        //3.存在性校验
        if (CollectionUtils.isEmpty(customerInfoList)) {
            return R.error(100, "用户信息不存在");
        }
        //4.绑定校验
        List<CustomerInfoEntity> usedCustomerInfos = FunctionUtil.filter(customerInfoList, data -> data.getWeChatOpenId().equals(bindeCustomerModel.getWeChatOpenId()));
        if (!CollectionUtils.isEmpty(usedCustomerInfos)) {
            return R.error(101, "当前微信已绑定");
        }
        //5.绑定
        List<CustomerInfoEntity> customerInfoFilter = FunctionUtil.filter(customerInfoList, data -> data.getBindeStatus() == 0);
        if (!CollectionUtils.isEmpty(customerInfoFilter)) {
            //5.1有未绑定记录-更新
            CustomerInfoEntity customerInfoEntity = customerInfoFilter.get(0);
            customerInfoEntity.setBindeStatus(1);
            customerInfoEntity.setWeChatNickName(bindeCustomerModel.getWeChatNickName());
            customerInfoEntity.setWeChatOpenId(bindeCustomerModel.getWeChatOpenId());
            customerInfoService.updateById(customerInfoEntity);
        } else {
            //5.1无未绑定记录-新增
            CustomerInfoEntity customerInfoEntity = customerInfoList.get(0);
            customerInfoEntity.setId(null);
            customerInfoEntity.setBindeStatus(1);
            customerInfoEntity.setWeChatNickName(bindeCustomerModel.getWeChatNickName());
            customerInfoEntity.setWeChatOpenId(bindeCustomerModel.getWeChatOpenId());
            customerInfoService.save(customerInfoEntity);
        }

        return R.ok();
    }

    /**
     * 用户详情
     *
     * @param wechaId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public R customerDetail(String wechaId) {

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + "===========" + value);
        }


        //1.参数校验
        if (StringUtils.isBlank(wechaId)) {
            return R.error(99, "请求参数不能为空");
        }
        //2.查询
        List<CustomerInfoEntity> customerInfoList = customerInfoDao.queryByWechatId(wechaId);
        if (CollectionUtils.isEmpty(customerInfoList)) {
            return R.error(100, "用户信息不存在");
        } else if (customerInfoList.size() != 1) {
            return R.error(101, "用户信息大于1条");
        }
        //3.状态判断
        CustomerInfoEntity customerInfoEntity = customerInfoList.get(0);
        if (customerInfoEntity.getBindeStatus() != 1) {
            return R.error(101, "用户信息未绑定");
        }
        if (customerInfoEntity.getValidStatus() != 1) {
            return R.error(101, "用户信息未生效");
        }

        log.info("用户详情,wechaId[{}],result[{}]", wechaId, JsonUtils.toJson(customerInfoEntity));
        return R.ok().put("data", customerInfoEntity);
    }


}
