package com.steven.transaction.springcloud.service;

import com.steven.transaction.core.service.RpcApplicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * SpringCloudRpcApplicationServiceImpl.
 *
 * @author steven
 */
@Service("applicationService")
public class SpringCloudRpcApplicationServiceImpl implements RpcApplicationService {

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public String acquireName() {
        return appName;
    }

}
