package com.ithinksky.ch004;

import org.flowable.form.engine.FormEngine;
import org.flowable.form.engine.FormEngineConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author tengpeng.gao
 */
public class FormEngineTest {


    private FormEngine formEngine;

    @Before
    public void init() {
        // 创建表单引擎
        InputStream inputStream = FormEngineTest.class.getClassLoader()
                .getResourceAsStream("ch004/flowable.form.cfg.xml");
        formEngine = FormEngineConfiguration.createFormEngineConfigurationFromInputStream(inputStream).buildFormEngine();
        String name = formEngine.getName();
        System.out.println("引擎名称：" + name);

    }

    @Test
    public void testInit() {
        // 初次执行的时候会初始化生成 6 张 表单引擎相关的表
    }


}
