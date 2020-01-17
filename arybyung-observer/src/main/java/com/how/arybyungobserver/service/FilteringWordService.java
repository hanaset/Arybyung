package com.how.arybyungobserver.service;

import com.how.muchcommon.entity.FilterEntity;
import com.how.muchcommon.repository.FilterRepository;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Service
public class FilteringWordService {

    private final FilterRepository filterRepository;
//    private String joonggoReg = "";
//    private String danggnReg = "";
    private String allReg = "";

    public FilteringWordService(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void refreshFilterWord() {
        List<FilterEntity> filterEntityList = filterRepository.findAll();

        List<String> joonggoFilter = filterEntityList.stream().filter(filterEntity -> filterEntity.equals("joonggonara")).map(FilterEntity::getWord).collect(Collectors.toList());
        List<String> danggnFilter = filterEntityList.stream().filter(filterEntity -> filterEntity.equals("danggn")).map(FilterEntity::getWord).collect(Collectors.toList());
        List<String> allFilter = filterEntityList.stream().map(FilterEntity::getWord).collect(Collectors.toList());

//        joonggoReg = joonggoFilter.stream().map(s -> "(" + s + ")").collect(Collectors.joining("|"));
//        danggnReg = danggnFilter.stream().map(s -> "(" + s + ")").collect(Collectors.joining("|"));
        allReg = allFilter.stream().map(s -> "(" + s + ")").collect(Collectors.joining("|"));
        allReg = "(\\S|^)" + allReg + "(\\S|$)";
    }
}
