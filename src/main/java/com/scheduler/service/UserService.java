package com.scheduler.service;

import com.scheduler.dao.UserDao;
import com.scheduler.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.scheduler.constants.SpringSchedulerConstants.*;
import static java.lang.String.format;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    //Schedule a job to add object in database in every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void addToDatabase() {
        Users user = new Users();
        user.setName("user" + new Random().nextInt(374483));
        userDao.save(user);
        LOGGER.info(LOG_MESSAGE_PREFIX + SEPERATOR + format(RESPONSE_RECEIVED_MSG, ADD_OBJECT, new Date().toString()));
    }

    //Schedule a job to fetch objects from database in every 15 seconds
    @Scheduled(cron = "0/15 * * * * *")
    public void retrieveFromDatabase() {
        List<Users> users = userDao.findAll();
        LOGGER.info(LOG_MESSAGE_PREFIX + SEPERATOR + format(RESPONSE_RECEIVED_MSG, RETRIEVE_OBJECT, new Date().toString()));
        LOGGER.info(LOG_MESSAGE_PREFIX + SEPERATOR + LIST_OF_USERS + users);
    }
}
