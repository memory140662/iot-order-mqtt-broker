package com.hy.iot.repository;

import com.hy.iot.entity.CheckHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckHistoryRepository extends JpaRepository<CheckHistory, String> {

    List<CheckHistory> findUserCheckHistoriesByUser_Id(String userId);
}
