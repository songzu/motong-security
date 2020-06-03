//package io.renren.modules.wechat.service;
//
///**
// * <p></p>
// *
// * @author 松竹
// * @version $$ Id: WeChatService.java, V 0.1 2020/5/31 12:05 gengen.wang Exp $$
// **/
//public interface WeChatService {
//    /**
//     * @description: 微信登陆认证
//     *
//     * @author: shizhiwei
//     * @date: 2019/8/6 15:18
//     * @param: [code]
//     * @return: com.daojia.qypt.djhrecord.dto.ResultDto
//     */
//    public Object getSessionIdByCode(String code) throws Exception;
//
//    /**
//     * @description: 保存用户公开信息
//     *
//     * @author: shizhiwei
//     * @date: 2019/8/6 15:17
//     * @param: [openId, userInfo]
//     * @return: com.daojia.qypt.djhrecord.dto.ResultDto
//     */
//    public Object saveUserPublicInfo(Long userId, String sessionId, WxUserInfoVo userInfo) throws Exception;
//
//    /**
//     * @description: 通过sessionId获取信息
//     *
//     * @author: shizhiwei
//     * @date: 2019/8/6 16:35
//     * @param: [sessionId]
//     * @return: com.daojia.qypt.djhrecord.dto.ResultDto
//     */
//    public Object getUserBySessionId(String sessionId) throws Exception;
//}
