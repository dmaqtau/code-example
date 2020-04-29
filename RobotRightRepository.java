package com.century.robotacc.repository.right;

import com.century.robotacc.model.right.RobotRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RobotRightRepository extends JpaRepository<RobotRight, Long> {
    @Query(value = "SELECT DISTINCT r.robotId " +
            "FROM RobotRight r " +
            "WHERE r.tabnum = :tabnum " +
            "AND r.value = '-1' " +
            "AND r.rightId = :rightTypeId " +
            "AND r.active = true")
    Set<Integer> getRightRobotIds(@Param("tabnum")Integer tabnum, @Param("rightTypeId") Integer rightTypeId);

    @Query(value = "SELECT r FROM RobotRight r WHERE r.tabnum = :tabnum")
    Set<RobotRight> getRights(@Param("tabnum")Integer tabnum);

    @Query(value = "SELECT r FROM RobotRight r " +
            "WHERE r.tabnum = :tabnum " +
            "AND r.rightId = :rightTypeId")
    Set<RobotRight> getRights(@Param("rightTypeId")Integer rightTypeId, @Param("tabnum")Integer tabnum);

    @Query(value = "SELECT DISTINCT s.login, r.robot_id " +
            "FROM robot.robot_right as r " +
            "INNER JOIN public.security_user as s ON r.tabnum = s.tabnum " +
            "WHERE r.value = '-1' " +
            "AND r.right_id = :rightTypeId " +
            "AND r.is_active = true", nativeQuery = true)
    Set<Map<String, Object>> getTotalRightValues(@Param("rightTypeId") Integer rightTypeId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM RobotRight r WHERE r.id IN :rightIds")
    void deleteByIds(@Param("rightIds") List<Long> rightIds);
}
