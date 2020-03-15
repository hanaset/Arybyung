package com.how.arybyungobserver.service;

import com.how.muchcommon.entity.jpaentity.FilterEntity;
import com.how.muchcommon.repository.jparepository.FilterRepository;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
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

        if(filterEntityList.isEmpty()) {
            pattern = null;
            return;
        }
        List<String> allFilter = filterEntityList.stream().map(FilterEntity::getWord).collect(Collectors.toList());

        String allReg = allFilter.stream().map(s -> "(" + s + ")").collect(Collectors.joining("|"));
        pattern = Pattern.compile(allReg, Pattern.MULTILINE);
    }

    public boolean stringFilter(String content) {

        if(this.pattern == null || content == null) {
            return false;
        }

        if(content.length() > 2000) {
            return true;
        }
        Matcher matcher = this.pattern.matcher(content);

        return matcher.find();
    }
}
