package com.century.robotacc.rest.controllers;

import com.century.robotacc.model.message.Message;
import com.century.robotacc.model.message.MessageContainer;
import com.century.robotacc.model.response.Response;
import com.century.robotacc.service.message.RobotMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.century.robotacc.model.response.Response.errResponse;
import static java.util.Collections.singletonList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by spolenov on 09.02.2017.
 */
@Slf4j
@RestController
@RequestMapping(value = "/message")
public class MessageController extends BaseController{

    @Autowired
    private RobotMessageService robotMessageService;

    @RequestMapping(value = "/save/settings" , method = POST)
    @ResponseBody
    public List<Response> saveSettings(@RequestBody List<MessageContainer> messageSettings){
        try{
            return robotMessageService.save(messageSettings);
        }
            catch(Exception e){
            log.error("Failed to save message settings: robot_id = [{}]",messageSettings.get(0).getRobotId(), e);
            return singletonList(errResponse("Не удалось сохранить сообщение", e.getLocalizedMessage()));
        }
    }

    @RequestMapping(value = "/list/{robotId}" , method = GET)
    @ResponseBody
    public List<MessageContainer> getAllByRobotId(@PathVariable int robotId){
        return robotMessageService.getByRobotId(robotId);
    }

    @RequestMapping(value = "/recipient_logins/{robotId}" , method = GET)
    @ResponseBody
    public Set<String> getRecipientLogins(@PathVariable int robotId){
        return robotMessageService.getRecipientLogins(robotId);
    }

    @RequestMapping(value = "/save/message", method = POST)
    public Response saveMessage(@RequestBody Message message){
        return robotMessageService.saveMessage(message);
    }

    @RequestMapping(value = "/list" , method = POST)
    public List<Message> getMessagesByIds(@RequestBody List<Integer> messageIds){
        return robotMessageService.getMessagesByIds(messageIds);
    }
}
