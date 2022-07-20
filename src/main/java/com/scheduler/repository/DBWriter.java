package com.scheduler.repository;

import com.scheduler.dao.EmployeeDao;
import com.scheduler.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.scheduler.constants.SpringSchedulerConstants.*;

@Component
public class DBWriter implements ItemWriter<Employee> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBWriter.class);

    @Autowired
    private EmployeeDao employeeDao;

    public DBWriter(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public void write(List<? extends Employee> employees) throws Exception {
        employeeDao.saveAll(employees);
        LOGGER.info(LOG_MESSAGE_PREFIX + SEPERATOR + LIST_OF_EMPLOYEES + employees);
    }
}
