package io.renren.modules.sys.dao;

import io.renren.modules.sys.entity.CustomerInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 客户信息表
 * 
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2020-05-31 21:41:20
 */
@Mapper
public interface CustomerInfoDao extends BaseMapper<CustomerInfoEntity> {

    Integer confirm(@Param("ids") List<Long> ids);

    @Select("SELECT * from customer_info WHERE user_name = #{userName} and password = #{password} and status = 1")
    List<CustomerInfoEntity> query(@Param("userName") String userName, @Param("password") String password);

    @Select("SELECT * from customer_info WHERE we_chat_open_id = #{weChatOpenId} and `status` = 1")
    List<CustomerInfoEntity> queryByWechatId(@Param("weChatOpenId") String weChatOpenId);

}
