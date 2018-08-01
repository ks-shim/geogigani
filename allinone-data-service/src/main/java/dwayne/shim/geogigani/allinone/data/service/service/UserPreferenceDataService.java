package dwayne.shim.geogigani.allinone.data.service.service;

import dwayne.shim.geogigani.allinone.data.service.service.data.UserKeywordsData;
import dwayne.shim.geogigani.common.indexing.TravelDataIndexField;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Log4j2
@Service
public class UserPreferenceDataService {

    @Value("${user.keywords.size}")
    private int userKeywordsSize;

    @Value("${user.keywords.ttl}")
    private long userKeywordsTTL;

    @Value("${user.keywords.entry.limit}")
    private long userEntryLimit;

    @Resource
    private StatisticsDataService statisticsDataService;

    private final ConcurrentMap<String, UserKeywordsData> userKeywordsMap;
    public UserPreferenceDataService() {
        userKeywordsMap = new ConcurrentHashMap<>(5000);
    }

    public String getUserKeywords(String userId) {
        UserKeywordsData data = userKeywordsMap.get(userId);
        if(data == null) return null;

        return data.getKeywords();
    }

    private final String[] suffixes = {"시", "도", "군", "구", "면", "동", "읍", "리"};
    private String filterKeywords(String content,
                                  TravelDataIndexField field) {
        if(content.isEmpty()) return content;
        if(TravelDataIndexField.ADDR1 != field) return content;

        StringBuilder sb = new StringBuilder();
        String[] parts = content.split("\\s+");
        for(String part : parts) {
            for(String suffix : suffixes) {
                if (!part.endsWith(suffix)) continue;

                if(sb.length() > 0) sb.append(' ');
                sb.append(part);
            }
        }
        return sb.toString();
    }

    public void addUserKeywords(String userId,
                                Map<String, String> docMap,
                                TravelDataIndexField ... sourcefields) {

        if(userKeywordsMap.size() >= userEntryLimit) {
            log.info("Too many User-Keywords entries exists, so skip this request !!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(TravelDataIndexField field : sourcefields) {
            String value = docMap.get(field.label());
            if(value == null) continue;
            value = filterKeywords(value, field);
            if(value.isEmpty()) continue;

            if(sb.length() > 0) sb.append(' ');
            sb.append(value);
        }

        addUserKeywords(userId, sb.toString());
    }

    private void addUserKeywords(String userId,
                                String keywords) {
        UserKeywordsData data = userKeywordsMap.get(userId);
        if(data == null) {
            // This means 'new session'
            data = new UserKeywordsData(userId);
            userKeywordsMap.putIfAbsent(userId, data);
            statisticsDataService.incrementSessionCount(new Date());
        }

        data.appendKeywords(keywords, userKeywordsSize);
    }

    public void removeOldUserData() {
        log.info("Before removing => User-Keywords entry size : {}", userKeywordsMap.size());
        Iterator<UserKeywordsData> iterator = userKeywordsMap.values().iterator();
        while(iterator.hasNext()) {
            UserKeywordsData data = iterator.next();
            if(!data.notAccessedFor(userKeywordsTTL)) continue;
            iterator.remove();
        }
        log.info("After removing => User-Keywords entry size : {}", userKeywordsMap.size());
    }
}
