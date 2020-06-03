package io.renren.common.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p></p>
 *
 * @author 松竹
 * @version $$ Id: BindeCustomerModel.java, V 0.1 2020/6/1 23:04 gengen.wang Exp $$
 **/
@Data
public class BindeCustomerModel {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 微信唯一id
     */
    private String weChatOpenId;
    /**
     * 微信昵称
     */
    private String weChatNickName;

}
