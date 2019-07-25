package com.ithinksky.ch005;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 *
 * 文档部署方式：
 * 	addClasspathResource
 * 	addZipInputStream
 *
 * 	addInputStream
 * 	addBpmnModel
 * 	addString
 * 	addBytes
 *
 * 1:
 *  act_re_deployment：流程模型部署对象表。
 *  每部署一次生成一条记录，首先生成这条数据，它的id主键将会被act_re_procdef和act_ge_bytearray作为外键。
 * 2:
 *  act_re_procdef：流程定义表。
 *  一次部署可能采用zip/bar进行部署，里面是有多份流程定义文件xml的，这时候act_re_deployment只有一条部署信息，
 *  但act_re_procdef有多个记录（一个流程定义对应一条），这个表有DEPLOYMENT_ID_外键字段，用它关联act_re_deployment。
 * 3:
 *  act_ge_bytearray：资源文件表。
 *  流程模型资源文件的真正存放地方，它每部署一次就会产生2条记录，
 *      一条是关于bpmn规范的文件内容存放在BYTES字段中，
 *      另一条是图片信息，采用二进制格式存储。
 *  提示：可以部署后解析bpmn文件的内容自动生成流程图，实现流程图的跟踪线路。
 *
 * @author tengpeng.gao
 */
public class DeployingProgrammaticallyTest {



    private ProcessEngine processEngine;

    @Before
    public void init() {
        // 创建流程引擎
        InputStream inputStream = DeployingProgrammaticallyTest.class.getClassLoader()
                .getResourceAsStream("ch005/flowable.cfg.xml");
        processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream
                (inputStream).buildProcessEngine();
        String name = processEngine.getName();
        System.out.println("引擎名称：" + name);

    }

    @Test
    public void testDeploymentByClasspathResource() {
        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("ch005/diagrams/holiday-request.bpmn20.xml")
                .deploy();
        System.out.println("deployment id : " + deployment.getId());
    }

    @Test
    public void testDeploymentByZipInputStream() {
        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();

        String barFileName = "ch005/diagrams.zip";
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(barFileName);
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("deployment id : " + deployment.getId());
    }

}
