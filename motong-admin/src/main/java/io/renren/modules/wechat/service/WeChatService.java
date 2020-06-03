package io.renren.modules.wechat.service;

import io.renren.common.model.CodeModel;
import io.renren.common.model.TokenModel;
import io.renren.common.utils.R;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p></p>
 *
 * @author 松竹
 * @version $$ Id: WeChatService.java, V 0.1 2020/5/31 12:05 gengen.wang Exp $$
 **/
public interface WeChatService {


    public R login(String code);

}
