package com.hy.iot.service;

import com.hy.iot.entity.CheckHistory;
import com.hy.iot.repository.CheckHistoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CheckHistoryService {

    @NonNull private CheckHistoryRepository repository;

    public Collection<CheckHistory> getByUserId(String userId) {
        return repository.findUserCheckHistoriesByUser_Id(userId);
    }

}
