package com.century.robotacc.service.aspect;

import com.century.robotacc.model.robot.Robot;
import com.century.robotacc.service.history.RobotHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class HistoryAspect {
    @Autowired
    private RobotHistoryService robotHistoryService;

    @Around(value = "(execution(* com.century.robotacc.service.robot..*(com.century.robotacc.model.robot.Robot)))")
    public Object time(ProceedingJoinPoint joinPoint) throws Throwable {
        Robot robot = (Robot)joinPoint.getArgs()[0];
        Object ret = joinPoint.proceed(joinPoint.getArgs());
        robotHistoryService.saveHistory(robot);
        return ret;
    }
}

