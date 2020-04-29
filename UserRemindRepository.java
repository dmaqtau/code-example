package com.century.robotacc.repository;

import com.century.robotacc.model.user_remind.UserRemind;
import com.century.robotacc.model.user_remind.UserRemindAgg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by spolenov on 21.11.2016.
 */
public interface UserRemindRepository extends JpaRepository<UserRemind, Integer> {
    @Query(value = "SELECT agg FROM UserRemindAgg agg WHERE agg.tabnum = :tabnum")
    public <T extends UserRemind> T getByTabnum(@Param(value = "tabnum")Integer tabnum);

    @Query(value = "SELECT agg FROM UserRemindAgg agg WHERE agg.tabnum IN :tabnums")
    public <T extends UserRemind> List<T> getByTabnums(@Param(value = "tabnums")List<Integer> tabnums);

    @Query(value = "SELECT agg FROM UserRemindAgg agg")
    public UserRemindAgg getAll();
}
