package io.renren.common.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 *
 */
@Data
public class CodeModel implements Serializable {
    /**
     * 微信登陆code
     */
    private String code;

}
