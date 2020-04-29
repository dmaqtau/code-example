package com.century.robotacc.service.robot;

import com.century.robotacc.model.rest.filter.specification.GeneralSpecifications;
import com.century.robotacc.model.rest.filter.specification.RobotSpecifications;
import com.century.robotacc.model.robot.Robot;
import com.century.robotacc.model.robot.request.BaseRequest;
import com.century.robotacc.repository.robot.RobotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spolenov on 26.01.2017.
 */
@Slf4j
@Service
@SuppressWarnings("unchecked")
public class RobotServiceImpl implements RobotService {
    @Autowired
    RobotRepository robotRepository;

    private GeneralSpecifications specs = new RobotSpecifications();

    @Override
    public Robot findOne(int id) {
        return robotRepository.getById(id);
    }

    @Override
    public List<Robot> findAll() {
        return robotRepository.findAll();
    }

    @Override
    public Robot save(Robot robot) {
        return robotRepository.save(robot);
    }

    @Override
    public Integer getNextId() {
        return robotRepository.getNextId();
    }

    @Override
    public List<Integer> getAllRobotsIds() {
        return robotRepository.getAllRobotsIds();
    }

    @Override
    public List<Robot> findAll(Specifications<Robot> specs) {
        return robotRepository.findAll(specs);
    }

    @Override
    public Robot getActive(int id) {
        return robotRepository.getActiveById(id);
    }

    @Override
    public List<Robot> getRobots(Boolean active) {
        return active == null? robotRepository.findAll():
                robotRepository.findAll(specs.active(active));
    }

    @Override
    public List<Robot> getActiveByGroupId(int groupId) {
        return robotRepository.getActiveByGroupId(groupId);
    }

    @Override
    public List<Robot> getRobots(BaseRequest request){
        List<Robot> result = new ArrayList<>();
        if(request.isDefault()){
            return robotRepository.findAll();
        }
        try{
            result = robotRepository.findAll(specs.getSpecs(request));
        }
        catch(Exception e){
            log.error("Could not fetch robots by request: " + request, e);
        }
        return result;
    }

    @Override
    public List<Robot> getByTypeIds(List<Integer> typeIds) {
        if(typeIds == null || typeIds.isEmpty()){
            return new ArrayList<>();
        }
        return robotRepository.getByTypeIds(typeIds);
    }

}
