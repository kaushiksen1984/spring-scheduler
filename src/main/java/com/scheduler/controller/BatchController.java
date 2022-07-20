package com.scheduler.controller;

import com.scheduler.dao.EmployeeDao;
import com.scheduler.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.scheduler.constants.SpringSchedulerConstants.*;

@RestController
@RequestMapping("/loadEmployee")
public class BatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchController.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private EmployeeDao employeeDao;

    @GetMapping
    public BatchStatus load() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters);
        LOGGER.info(LOG_MESSAGE_PREFIX + SEPERATOR + JOB_EXECUTION + jobExecution.getStatus());

        while(jobExecution.isRunning()) {
            LOGGER.info(LOG_MESSAGE_PREFIX + SEPERATOR + JOB_RUNNING);
        }

        List<Employee> employees = employeeDao.findAll();
        LOGGER.info(LOG_MESSAGE_PREFIX + SEPERATOR + LIST_OF_EMPLOYEES + employees);
        return jobExecution.getStatus();
    }
}
