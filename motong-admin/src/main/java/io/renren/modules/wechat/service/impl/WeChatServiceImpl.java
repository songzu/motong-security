//package io.renren.modules.wechat.service.impl;
//
//import cn.binarywang.wx.miniapp.api.WxMaService;
//import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
//import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
//import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
//import io.renren.common.config.WxMaConfiguration;
//import io.renren.modules.wechat.service.WeChatService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//
///**
// * <p></p>
// *
// * @author 松竹
// * @version $$ Id: WeChatServiceImpl.java, V 0.1 2020/5/31 12:05 gengen.wang Exp $$
// **/
//@Service
//@Slf4j
//public class WeChatServiceImpl implements WeChatService {
//
//
//    private WxMaService getWxMaService() {
//        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
//        config.setAppid("appId");
//        config.setSecret("appSecret");
//        config.setMsgDataFormat("JSON");
//        WxMaService wxMaService = new WxMaServiceImpl();
//        wxMaService.setWxMaConfig(config);
//        return wxMaService;
//    }
//
//    @Override
//    public Object getSessionIdByCode(String code) {
//        log.info("通过code获取sessionId入参code:{}", code);
//        // 通过code获取用户openId相关信息
//        WxMaJscode2SessionResult session = null;
//        try {
//            session = WxMaConfiguration.getMaService("wxc1accff3513d2cc9").jsCode2SessionInfo(code);
//        } catch (Exception e) {
//            log.error("通过code#{}# 微信登陆失败, 获取sessinoKey失败!", code, e);
//            return null;
//        }
//
//        // 将获取的信息保存，并转化为sessionId返回给前端
//        try {
//            if (session == null) {
//                log.info("通过code#{}#获取sessionId 微信登陆失败, 获取用户登陆状态为空!", code);
//                return null;
//            }
//            // 将session转化为登陆帮助对象
//            UserSessionModel sessionModel = packageSessionModel(session);
//
//            // 封装Vo
//            TUserAuthorizeinfoVo userAuthorizeinfoVo = new TUserAuthorizeinfoVo();
//            String openid = session.getOpenid();
//            TUserAuthorizeinfoEntity userAuthorizeinfoEntity = userAuthorizeinfoDAO.getByOpenId(openid);
//            if (userAuthorizeinfoEntity != null) {
//                // 设置返回userId
//                userAuthorizeinfoVo.setUserId(userAuthorizeinfoEntity.getId());
//                // 设置缓存userId
//                sessionModel.setUserId(userAuthorizeinfoEntity.getId());
//            } else {
//                // 新建用户
//                TUserAuthorizeinfoEntity newUserAuthorizeinfoEntity = packageUserAuthorizeinfoEntity(session);
//                Long insertId = userAuthorizeinfoDAO.insert(newUserAuthorizeinfoEntity);
//                // 设置返回userId
//                userAuthorizeinfoVo.setUserId(insertId);
//                // 设置缓存userId
//                sessionModel.setUserId(insertId);
//            }
//
//            String key = SessionIdUtils.put(sessionModel);
//            userAuthorizeinfoVo.setSessionId(key);
//            logger.info("## 微信登陆成功, 计算本地SessionId完成:{}", key);
//            resultDto.setData(userAuthorizeinfoVo);
//            return resultDto;
//        } catch (Exception e) {
//            logger.error("微信登陆异常",e);
//            return resultDto.setErrorCodeMsg("微信登陆异常");
//        }
//    }
//
//    /** 将session转化为登陆帮助对象 */
////    private UserSessionModel packageSessionModel(WxMaJscode2SessionResult session) throws Exception {
////        UserSessionModel sessionModel = new UserSessionModel();
////        sessionModel.setOpenid(session.getOpenid());
////        sessionModel.setSessionKey(session.getSessionKey());
////        return sessionModel;
////    }
//    /** 封装用户信息 */
////    private TUserAuthorizeinfoEntity packageUserAuthorizeinfoEntity(WxMaJscode2SessionResult session) throws Exception {
////        TUserAuthorizeinfoEntity newUserAuthorizeinfoEntity = new TUserAuthorizeinfoEntity();
////        newUserAuthorizeinfoEntity.setOpenId(session.getOpenid());
////        newUserAuthorizeinfoEntity.setUnionId(session.getUnionid());
////        return newUserAuthorizeinfoEntity;
////    }
//
////    public ResultDto saveUserPublicInfo(Long userId, String sessionId, WxUserInfoVo userInfo) throws Exception {
////        logger.info("保存用户公开信息入参userId:{},sessionId:{},userInfo:{}", userId, sessionId, userInfo);
////        ResultDto resultDto = new ResultDto();
////        try {
////            // 参数校验
////            TUserAuthorizeinfoEntity userAuthorizeinfoEntity = userAuthorizeinfoDAO.getById(userId);
////            if (userAuthorizeinfoEntity == null) {
////                logger.info("通过用户userId:{}查询不到用户,没有登陆或则登陆失败", userId);
////                resultDto.setCode(CodeEnum.NOTLOGIN.getCode());
////                resultDto.setMessage(CodeEnum.NOTLOGIN.getDesc());
////                return resultDto;
////            }
////            // 比较数据库的openId 和缓存中的openId 是否一致
////            String openIdDb= userAuthorizeinfoEntity.getOpenId();
////            UserSessionModel sessionModel = SessionIdUtils.get(sessionId);
////            String openId = "";
////            if (sessionModel != null) {
////                openId = sessionModel.getOpenid();
////            }
////            if (!openIdDb.equals(openId)) {
////                logger.info("通过用户userId:{}查询的用户openIdDb:{},和缓存的openId:{}不匹配", userId, openIdDb, openId);
////                resultDto.setCode(CodeEnum.NOTLOGIN.getCode());
////                resultDto.setMessage(CodeEnum.NOTLOGIN.getDesc());
////                return resultDto;
////            }
////            // 更新信息
////            packageUpdageEntity(userAuthorizeinfoEntity, userInfo);
////            userAuthorizeinfoDAO.update(userAuthorizeinfoEntity);
////            return resultDto;
////        } catch (Exception e) {
////            logger.error("保存公开信息异常",e);
////            return resultDto.setErrorCodeMsg("服务异常");
////        }
////    }
//
//    /** 更新信息 */
////    private void packageUpdageEntity(TUserAuthorizeinfoEntity userAuthorizeinfoEntity, WxUserInfoVo userInfo) throws Exception {
////        String avatarUrl = userInfo.getAvatarUrl();
////        if (StringUtils.isNotEmpty(avatarUrl) && !avatarUrl.contains(WxConstantUrl.WX_TOUXIANG_URL_DOMAIN)) {
////            logger.info("用户头像不包含微信域名,认为头像不合法，不保存到数据库userAuthorizeinfoEntity={}", userAuthorizeinfoEntity);
////            userInfo.setAvatarUrl(userAuthorizeinfoEntity.getAvatarUrl());
////        }
////        userAuthorizeinfoEntity.setAvatarUrl(userInfo.getAvatarUrl());
////        userAuthorizeinfoEntity.setCity(userInfo.getCity());
////        userAuthorizeinfoEntity.setCountry(userInfo.getCountry());
////        userAuthorizeinfoEntity.setGender(userInfo.getGender());
////        userAuthorizeinfoEntity.setLanguage(userInfo.getLanguage());
////        userAuthorizeinfoEntity.setNickName(userInfo.getNickName());
////        userAuthorizeinfoEntity.setProvince(userInfo.getProvince());
////    }
//
//
////    public ResultDto getUserBySessionId(String sessionId) throws Exception {
////        logger.info("通过sessionId获取登陆信息入参sessionId:{}", sessionId);
////        ResultDto resultDto = new ResultDto();
////        try {
////            UserSessionModel sessionModel = SessionIdUtils.get(sessionId);
////            if (sessionModel != null) {
////                TUserAuthorizeinfoVo userAuthorizeinfoVo = new TUserAuthorizeinfoVo();
////                userAuthorizeinfoVo.setUserId(sessionModel.getUserId());
////                userAuthorizeinfoVo.setSessionId(sessionId);
////                resultDto.setData(userAuthorizeinfoVo);
////                return resultDto;
////            }
////            resultDto.setCode(CodeEnum.INVALIDLOGIN.getCode());
////            resultDto.setMessage(CodeEnum.INVALIDLOGIN.getDesc());
////            return resultDto;
////        } catch (Exception e) {
////            logger.error("通过sessionId获取登陆信息异常",e);
////            return resultDto.setErrorCodeMsg("服务异常");
////        }
////    }
//}
