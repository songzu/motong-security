package io.renren.modules.wechat.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import io.renren.common.config.JwtConfig;
import io.renren.common.config.WxMaConfiguration;
import io.renren.common.model.CodeModel;
import io.renren.common.model.TokenModel;
import io.renren.common.utils.JsonUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.dao.CustomerInfoDao;
import io.renren.modules.sys.entity.CustomerInfoEntity;
import io.renren.modules.wechat.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p></p>
 *
 * @author 松竹
 * @version $$ Id: WeChatServiceImpl.java, V 0.1 2020/5/31 12:05 gengen.wang Exp $$
 **/
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private CustomerInfoDao customerInfoDao;

    private String appid = "wxc1accff3513d2cc9";

    @Override
    public R login(String code) {
        try {
            //1.通过code获取session
            final WxMaService wxService = WxMaConfiguration.getMaService(appid);
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            if (session == null) {
                log.warn("通过code[{}]获取sessionId,微信登陆失败,获取用户登陆状态为空!", code);
                return R.error(100, "微信登陆失败");
            }
            log.info("微信登陆,session:[{}],code:[{}]", JsonUtils.toJson(session), code);

            //2.生成微信登陆token
            String token = jwtConfig.createTokenByWxAccount(session.getOpenid(), session.getSessionKey());

            //3.判断用户是否绑定
            //3.1查询
            List<CustomerInfoEntity> customerInfoList = customerInfoDao.queryByWechatId(session.getOpenid());
            if (CollectionUtils.isEmpty(customerInfoList)) {
                return R.ok().put("token", new TokenModel(token, Boolean.FALSE));
            } else if (customerInfoList.size() != 1) {
                return R.error(101, "用户信息大于1条");
            }
            //3.2状态判断
            CustomerInfoEntity customerInfoEntity = customerInfoList.get(0);
            if (customerInfoEntity.getBindeStatus() != 1) {
                return R.ok().put("token", new TokenModel(token, Boolean.FALSE));
            } else {
                return R.ok().put("token", new TokenModel(token, Boolean.TRUE));
            }
        } catch (WxErrorException e) {
            log.error("微信登陆异常,code:[{}],[{}]", code, e);
            return R.error(100, "微信登陆失败");
        } catch (Exception e) {
            log.error("微信登陆失败,code:[{}],[{}]", code, e);
            return R.error(100, "微信登陆失败");
        }
    }

}
