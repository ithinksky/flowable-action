package com.ithinksky.ch002;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 *
 *
 * @author tengpeng.gao
 */
public class ProcessEngineTest {

    private ProcessEngine processEngine;

    @Before
    public void init() {
        // 创建流程引擎
        InputStream inputStream = ProcessEngineTest.class.getClassLoader()
                .getResourceAsStream("ch002/flowable.cfg.xml");
        processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream
                (inputStream).buildProcessEngine();
        String name = processEngine.getName();
        System.out.println("引擎名称：" + name);

    }

    @Test
    public void testInit() {
        // 初次执行的时候会初始化生成 38 张 流程引擎相关的表
    }

}
