package io.renren.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO 返回值token对象
 */
@Data
@AllArgsConstructor
public class TokenModel implements Serializable {
    /**
     * 微信登陆token
     */
    private String token;

    /**
     * 用户是否绑定
     */
    private Boolean ifBande;
}
