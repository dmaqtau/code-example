package com.century.robotacc.service.robot;

import com.century.robotacc.model.robot.Robot;
import com.century.robotacc.model.robot.request.RobotJournalRequest;
import com.century.robotacc.model.robot.response.RobotResponse;
import com.century.robotacc.model.util.MapperUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by spolenov on 22.10.2016.
 */
public interface RobotJournalService {
    List<RobotResponse> getRobots(String active);

    List<RobotResponse> getResponse(RobotJournalRequest request);

    List<RobotResponse> getAllRobotsResponse();

    List<RobotResponse> getRobotsByGroupId(int groupId);

    Set<Robot> testRobotFetchPerformance(int iterationsCount);

    static List<RobotResponse> getRawResponse(List<Robot> robots){
        if(robots == null || robots.isEmpty()){
            return new ArrayList<>();
        }
        return MapperUtils.responseListFrom(robots);
    }
}
