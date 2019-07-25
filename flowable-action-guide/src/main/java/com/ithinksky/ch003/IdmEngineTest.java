package com.ithinksky.ch003;

import org.flowable.idm.engine.IdmEngine;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author tengpeng.gao
 */
public class IdmEngineTest {

    private IdmEngine idmEngine;

    @Before
    public void init() {
        // 创建权限引擎
        InputStream inputStream = IdmEngineTest.class.getClassLoader()
                .getResourceAsStream("ch003/flowable.idm.cfg.xml");
        idmEngine = IdmEngineConfiguration.createIdmEngineConfigurationFromInputStream(inputStream).buildIdmEngine();
        String name = idmEngine.getName();
        System.out.println("引擎名称：" + name);

    }

    @Test
    public void testInit() {
        // 初次执行的时候会初始化生成 9 张 权限引擎相关的表
    }


}
