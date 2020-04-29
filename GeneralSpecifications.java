package com.century.robotacc.model.rest.filter.specification;

import com.century.robotacc.model.robot.request.BaseRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.Collection;

import static com.century.robotacc.model.rest.filter.specification.GeneralSpecifications.SpecConditionalParam.AND;
import static com.century.robotacc.model.rest.filter.specification.GeneralSpecifications.SpecConditionalParam.OR;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by spolenov on 15.12.2016.
 */
@FunctionalInterface
public interface GeneralSpecifications<T, R extends BaseRequest> {
    String LIST_SUFFIX = "_list";

    default Specification<T> active(final boolean active){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), active);
    }

    default Specification<T> integerSpec(String objectParam, Integer value){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(objectParam), value);
    }

    default Specification<T> integerSpec(String objectParam, String idParam, Integer value ){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(objectParam).get(idParam), value);
    }

    default Specification<T> integerListSpec(String objectParam, Integer value) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.isMember(value, root.get(objectParam));
    }

    default Specifications<T>integerListSpecs(String objectParam, SpecConditionalParam condParam, Collection<Integer> ids){
        Specifications<T> specs = null;
        for(Integer id: ids){
            if(id != 0){
                if(condParam.equals(AND)){
                    specs = integerListSpecAnd(specs, objectParam, id);
                }
                else {
                    specs = integerListSpecOr(specs, objectParam, id);
                }
            }
        }
        return specs;
    }

    default Specifications<T> integerListSpecAnd(Specifications<T> specs, String objectParam, int id){
        if(specs == null){
            return integerListSpecNew(objectParam, id);
        }
        return specs.and(integerListSpec(objectParam, id));
    }

    default Specifications<T> integerListSpecOr(Specifications<T> specs, String objectParam, int id){
        if(specs == null){
            return integerSpecNew(objectParam, id);
        }
        return specs.or(integerSpec(objectParam, id));
    }

    default Specifications<T> integerListSpecNew(String objectParam, int id){
        return where(integerListSpec(objectParam, id));
    }

    default Specifications<T> integerSpecNew(String objectParam, int id){
        return where(integerSpec(objectParam, id));
    }

    default Specifications<T> getSpecsByParam(String param, Collection<Integer> ids){
        String typeParam = param.replace(LIST_SUFFIX, "");

        if(ids == null || ids.isEmpty()){
            return where(trueSpec());
        }

        if(param.contains(LIST_SUFFIX)){
            return where(integerListSpecs(typeParam, AND, ids));
        }
        return where(integerListSpecs(typeParam, OR, ids));
    }

    default Specification<T> trueSpec(){
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and();
    }

    Specifications<T> getSpecs(R request);

    enum SpecConditionalParam{
        OR,
        AND
    }
}
