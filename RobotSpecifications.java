package com.century.robotacc.model.rest.filter.specification;

import com.century.robotacc.model.robot.Robot;
import com.century.robotacc.model.robot.RobotPriority;
import com.century.robotacc.model.robot.request.BaseRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by spolenov on 15.11.2016.
 */

public class RobotSpecifications implements GeneralSpecifications {
    @Override
    public Specifications<Robot> getSpecs(BaseRequest request){
        Specifications specs = where(trueSpec());

        if(request.getActive() != null){
            specs = specs.and(active(request.getActive()));
        }

        if(request.getOnlyCritical() != null && request.getOnlyCritical()){
            specs = specs.and(onlyCritical());
        }

        if(request.getPlaceId() > 0){
            specs = specs.and(integerSpec("place", "id", request.getPlaceId()));
        }

        if(request.getRespondentId() > 0){
            specs = specs.and(integerSpec("respondent", "id", request.getRespondentId()));
        }

        if(request.getGroupId() > 0){
            specs = specs.and(integerSpec("group", "id", request.getGroupId()));
        }

        specs = specs.and(robotTypes(request.getTypeIds()));
        return specs;
    }

    private Specification<Robot> onlyCritical(){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("priorityId"), RobotPriority.REGULAR.getValue());
    }

    private Specifications<Robot>robotTypes(List<Integer> typeIds){
        return integerListSpecs("types", SpecConditionalParam.AND, typeIds);
    }
}
