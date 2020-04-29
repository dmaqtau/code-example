package com.century.robotacc.service.robot;

import com.century.robotacc.model.robot.Robot;
import com.century.robotacc.model.robot.request.RobotJournalRequest;
import com.century.robotacc.model.robot.response.RobotResponse;
import com.century.robotacc.model.session.SessionResultEnum;
import com.century.robotacc.service.right.RobotRightService;
import com.century.robotacc.service.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.century.robotacc.model.session.SessionResultEnum.getNonSucceedIds;

/**
 * Created by spolenov on 22.10.2016.
 */
@Slf4j
@Service
public class RobotJournalServiceImpl implements RobotJournalService {
    @Autowired
    RobotService robotService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private RobotRightService rightService;

    private List<RobotResponse> getResponse(List<Robot> robots){
        List<RobotResponse> resultList = new ArrayList<>();
        try{
            resultList = RobotJournalService.getRawResponse(robots);
        }
        catch(Exception e){
            log.error("Error while getting response list from robots", e);
            return resultList;
        }
        return resultList;
    }

    @Override
    public List<RobotResponse> getResponse(RobotJournalRequest request){
        return getResponse(getRobotsWithDisplayLogins(request))
                .stream()
                .filter(getFilterPredicate(request))
                .collect(Collectors.toList());
    }

    @Override
    public List<RobotResponse> getAllRobotsResponse(){
        return getResponse(robotService.findAll());
    }

    @Override
    public List<RobotResponse> getRobots(String active){
        final String nullExpr = "null";
        return getResponse(
                robotService.getRobots(
                    nullExpr.equals(active) ? null: Boolean.parseBoolean(active)
                )
        );
    }

    @Override
    public List<RobotResponse> getRobotsByGroupId(int groupId){
        return getResponse(robotService.getActiveByGroupId(groupId));
    }

    @Override
    public Set<Robot>testRobotFetchPerformance(int iterationsCount){
        List<Robot> allRobots = robotService.findAll();
        if(allRobots.isEmpty()){
            throw new UnsupportedOperationException("No robots to test performance.");
        }
        Set<Robot>result = new HashSet<>();
        int upperIndex = allRobots.size() - 1;

        for(int i = 0; i<iterationsCount; i++){
            int rnd = ThreadLocalRandom.current().nextInt(0, upperIndex + 1);
            log.info("rnd = {}", rnd);
            result.add(robotService.findOne(rnd));
        }
        return result;
    }

    private List<Robot> getRobotsWithDisplayLogins(RobotJournalRequest request){
        List<Robot> result = robotService.getRobots(request);
        result.forEach(r -> r.setDisplayRightsLogins(rightService.getDisplayRightRobotLogins(r.getId())));
        return result;
    }

    private static Predicate<RobotResponse> getFilterPredicate(RobotJournalRequest request){
        return response ->{
            Integer resultId = request.getResultId();
            if(resultId == SessionResultEnum.ALL.getValue()){
                //Фильтр по всем роботам
                return true;
            }
            else{
                Integer lastSessionResultId = response.getLastSessionResultId();
                if(lastSessionResultId == null){
                    return false;
                }
                if(SessionResultEnum.OK.getValue() == request.getResultId()){
                    return request.getResultId().equals(lastSessionResultId);
                }
                else{
                    return getNonSucceedIds().contains(lastSessionResultId);
                }
            }
        };
    }
}
