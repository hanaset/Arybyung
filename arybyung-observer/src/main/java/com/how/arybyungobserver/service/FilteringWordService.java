package com.how.arybyungobserver.service;

import com.how.muchcommon.entity.FilterEntity;
import com.how.muchcommon.repository.FilterRepository;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@Service
public class FilteringWordService {

    private final FilterRepository filterRepository;
    private Pattern pattern;

    public FilteringWordService(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    @PostConstruct
    public void init() {
        refreshFilterWord();
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void refreshFilterWord() {
        List<FilterEntity> filterEntityList = filterRepository.findAll();
        List<String> allFilter = filterEntityList.stream().map(FilterEntity::getWord).collect(Collectors.toList());

        String allReg = allFilter.stream().map(s -> "(" + s + ")").collect(Collectors.joining("|"));
        pattern = Pattern.compile(allReg, Pattern.MULTILINE);
    }
}
