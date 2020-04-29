package com.century.robotacc.rest.controllers;

import com.century.robotacc.model.response.Response;
import com.century.robotacc.model.schedule.ScheduleContainer;
import com.century.robotacc.model.schedule.ScheduleInterval;
import com.century.robotacc.model.schedule.TimeUnit;
import com.century.robotacc.model.schedule.TimeUnits;
import com.century.robotacc.service.TaskScheduleService;
import com.century.robotacc.service.exception.WrongScheduleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.century.robotacc.model.response.Response.errResponse;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by spolenov on 02.03.2017.
 */
@Slf4j
@RestController
@RequestMapping(value = "/task_schedule")
public class TaskScheduleController extends  BaseController {
    @Autowired
    private TaskScheduleService taskScheduleService;

    @RequestMapping(value = "/{robotId}" , method = GET)
    @ResponseBody
    public List<ScheduleContainer> getSchedules(@PathVariable(value = "robotId") int robotId) throws WrongScheduleException{
        return taskScheduleService.getSchedules(robotId);
    }

    @RequestMapping(value = "/time_units" , method = GET)
    @ResponseBody
    public List<TimeUnit> getTimeUnitList(){
        return TimeUnits.getList();
    }

    @RequestMapping(value = "/intervals" , method = GET)
    @ResponseBody
    public List<Map<String, String>> getIntervalList(){
        return ScheduleInterval.getList();
    }

    @RequestMapping(value = "/save" , method = POST)
    @ResponseBody
    public Response saveSchedules(@RequestBody List<ScheduleContainer> schedules){
        try{
            return taskScheduleService.save(schedules);
        }
        catch(Exception e){
            log.error("Failed to save scheduleContainers", e);
            return errResponse("Не удалось сохранить расписание робота", e.getLocalizedMessage());
        }
    }
}
