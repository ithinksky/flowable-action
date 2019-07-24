package com.ithinksky.ch001;

import org.flowable.engine.*;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 功能描述：请假申请
 * 官网文档 https://www.flowable.org/docs/userguide/index.html#getting.started.command.line
 *
 * @author tengpeng.gao
 */
public class HolidayRequest {

    public static void main(String[] args) {

        // 创建流程引擎
        InputStream inputStream = HolidayRequest.class.getClassLoader()
                .getResourceAsStream("ch001/flowable.cfg.xml");
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream
                (inputStream).buildProcessEngine();
        String name = processEngine.getName();
        System.out.println("引擎名称：" + name);

        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("ch001/diagrams/holiday-request.bpmn20.xml")
                .deploy();

        // 通过API查询引擎，了解引擎已知流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());
        
        // 填写变量数据
        Scanner scanner= new Scanner(System.in);

        System.out.println("Who are you?");
        String employee = scanner.nextLine();

        System.out.println("How many holidays do you want to request?");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

        System.out.println("Why do you need them?");
        String description = scanner.nextLine();

        // 启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<>();
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("holidayRequest", variables);

        // 查询任务
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i=0; i<tasks.size(); i++) {
            System.out.println((i+1) + ") " + tasks.get(i).getName());
        }

        // 获取任务描述信息
        System.out.println("Which task would you like to complete?");
        int taskIndex = Integer.valueOf(scanner.nextLine());
        Task task = tasks.get(taskIndex - 1);
        Map<String, Object> processVariables = taskService.getVariables(task.getId());
        System.out.println(processVariables.get("employee") + " wants " +
                processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");

        // 完成任务
        boolean approved = scanner.nextLine().toLowerCase().equals("y");
        variables = new HashMap<>();
        variables.put("approved", approved);
        taskService.complete(task.getId(), variables);


        // 使用历史数据
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .finished()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .list();

        for (HistoricActivityInstance activity : activities) {
            System.out.println(activity.getActivityId() + " took "
                    + activity.getDurationInMillis() + " milliseconds");
        }

    }

    public static class CallExternalSystemDelegate implements JavaDelegate {
        @Override
        public void execute(DelegateExecution execution) {
            System.out.println("Calling the external system for employee "
                    + execution.getVariable("employee"));
        }
    }

    public static class SendRejectionMail implements JavaDelegate {

        @Override
        public void execute(DelegateExecution execution) {
            System.out.println("Send a Rejection Mail to "
                    + execution.getVariable("employee"));
        }

    }
}
