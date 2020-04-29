package com.century.robotacc.service.robot;

import com.century.robotacc.model.robot.Robot;
import com.century.robotacc.model.robot.request.BaseRequest;
import org.springframework.data.jpa.domain.Specifications;

import java.util.List;

/**
 * Created by spolenov on 26.01.2017.
 */
public interface RobotService {
    /**
     * Получить робота по коду
     * @param id - код робота
     * @return Robot - робот
     */
    Robot findOne(int id);

    /**
     * Получить всех роботов (и активных, и неактивных)
     * @return List<Robot> - роботы
     */
    List<Robot> findAll();

    Robot save(Robot robot);

    Integer getNextId();

    List<Integer> getAllRobotsIds();

    List<Robot> findAll(Specifications<Robot> specs);

    /**
     * Получить активного робота по коду
     * @param id - код робота
     * @return Robot - робот
     */
    Robot getActive(int id);

    /**
     * Получить роботов: активных либо нет
     * @param active - признак активности робота
     * @return List<Robot> - роботы
     */
    List<Robot> getRobots(Boolean active);

    /**
     * Получить активных роботов по коду группы
     * @param groupId - код робота
     * @return List<Robot> - роботы
     */
    List<Robot> getActiveByGroupId(int groupId);

    /**
     * Получить роботов по некому запросу
     * @param req - запрос с параметрами фильтрации роботов
     * @return List<Robot> - роботы
     */
    List<Robot> getRobots(BaseRequest req);

    /**
     * Получить роботов по перечню их типов
     * @param typeIds - перечень кодов типов роботов
     * @return List<Robot> - роботы
     */
    List<Robot> getByTypeIds(List<Integer> typeIds);
}
