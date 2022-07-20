package com.scheduler.batch;

import com.scheduler.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.scheduler.constants.SpringSchedulerConstants.*;

@Component
public class Processor implements ItemProcessor<Employee, Employee> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private static final Map<String, String> departmentNames = new HashMap<>();

    public Processor() {
        departmentNames.put("001", "Technology");
        departmentNames.put("002", "Operations");
        departmentNames.put("003", "Human Resources");
    }

    @Override
    public Employee process(Employee employee) throws Exception {
        String departmentCode = departmentNames.get(employee.getDepartment());
        employee.setDepartment(departmentCode);

        LOGGER.info(LOG_MESSAGE_PREFIX + SEPERATOR + String.format(CONVERTED_MESSAGE, employee.getDepartment(),
                departmentCode));
        return employee;
    }

}
