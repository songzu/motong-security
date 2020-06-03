package io.renren.modules.wechat.controller;

import io.renren.common.config.JwtConfig;
import io.renren.common.exception.RRException;
import io.renren.common.model.CodeModel;
import io.renren.common.model.CustomerBindeModel;
import io.renren.common.utils.JsonUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.service.CustomerInfoService;
import io.renren.modules.wechat.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    private JwtConfig jwtConfig;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest req, HttpServletResponse res) {
        this.request = req;
        this.response = res;
        this.session = req.getSession();
    }

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
    public R bindeCustomer(@RequestBody CustomerBindeModel bindeCustomerModel) {
        log.info("用户绑定请求,[{}]", JsonUtils.toJson(bindeCustomerModel));

        //1.token校验
        try {
            String wxOpenId = checkToken();
            bindeCustomerModel.setWeChatOpenId(wxOpenId);
        } catch (RRException e) {
            log.warn("用户未登陆,[{}]", e);
            return R.error(99, "用户未登陆");
        } catch (Exception e) {
            log.warn("用户未登陆,[{}]", e);
            return R.error(99, "用户未登陆");
        }

        //2.参数校验
        if (bindeCustomerModel == null) {
            return R.error(99, "请求参数不能为空");
        }
        if (StringUtils.isBlank(bindeCustomerModel.getUserName())) {
            return R.error(99, "用户名不能为空");
        }
        if (StringUtils.isBlank(bindeCustomerModel.getPassword())) {
            return R.error(99, "密码不能为空");
        }
        if (StringUtils.isBlank(bindeCustomerModel.getWeChatNickName())) {
            return R.error(99, "微信昵称不能为空");
        }

        R result = customerInfoService.bindeCustomer(bindeCustomerModel);
        log.info("用户绑定结果,[{}]", JsonUtils.toJson(result));

        return result;
    }

    /**
     * 用户详情
     *
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public R customerDetail() {
        //1.token校验
        String wxOpenId;
        try {
            wxOpenId = checkToken();
        } catch (RRException e) {
            log.warn("用户未登陆,[{}]", e);
            return R.error(99, "用户未登陆");
        } catch (Exception e) {
            log.warn("用户未登陆,[{}]", e);
            return R.error(99, "用户未登陆");
        }

        R result = customerInfoService.customerDetail(wxOpenId);
        log.info("用户详情,wxOpenId[{}],result[{}]", wxOpenId, JsonUtils.toJson(result));
        return result;
    }

    private String checkToken() {
        String jwtToken = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwtToken)) {
            log.warn("token is invalid , please check your token");
            throw new RRException("token is invalid , please check your token");
        }
        log.info("token[{}]", jwtToken);
        String wxOpenId = jwtConfig.getWxOpenIdByToken(jwtToken);
        String sessionKey = jwtConfig.getSessionKeyByToken(jwtToken);
        if (StringUtils.isEmpty(wxOpenId)) {
            log.warn("user account not exits , please check your token");
            throw new RRException("user account not exits , please check your token");

        }
        if (StringUtils.isEmpty(sessionKey)) {
            log.warn("sessionKey is invalid , please check your token");
            throw new RRException("sessionKey is invalid , please check your token");
        }
        if (!jwtConfig.verifyToken(jwtToken)) {
            log.warn("token is invalid , please check your token");
            throw new RRException("token is invalid , please check your token");
        }

        return wxOpenId;
    }

}
