package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户信息表
 * 
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2020-05-31 21:41:20
 */
@Data
@TableName("customer_info")
public class CustomerInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 昵称
	 */
	private String nickName;
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
	/**
	 * 参数A
	 */
	private String paramA;
	/**
	 * 参数B
	 */
	private String paramB;
	/**
	 * 生效状态，0-无效，1-有效
	 */
	private Integer validStatus;
	/**
	 * 绑定状态，0-未绑定，1-绑定
	 */
	private Integer bindeStatus;
	/**
	 * 删除状态，0-删除，1-未删除
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date gmtCreate;
	/**
	 * 修改时间
	 */
	private Date gmtUpdate;
	/**
	 * 创建人姓名
	 */
	private String userCreate;
	/**
	 * 创建人id
	 */
	private Long userCreateId;

	/**
	 * 生效状态，0-无效，1-有效
	 */
	@TableField(exist = false)
	private String validStatusName;

	/**
	 * 绑定状态，0-未绑定，1-绑定
	 */
	@TableField(exist = false)
	private String bindeStatusName;

}
