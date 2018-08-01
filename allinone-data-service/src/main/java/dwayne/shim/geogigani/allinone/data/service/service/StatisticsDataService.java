package dwayne.shim.geogigani.allinone.data.service.service;

import dwayne.shim.geogigani.allinone.data.service.entity.SessionCountEntity;
import dwayne.shim.geogigani.allinone.data.service.repository.SessionCountTableRepository;
import dwayne.shim.geogigani.common.data.statistics.SessionCountData;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@Log4j2
@Service
public class StatisticsDataService {

    @Autowired
    private Validator validator;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SessionCountTableRepository sessionCountTableRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementSessionCount(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);

        try {
            SessionCountEntity countEntity = sessionCountTableRepository.findOneByDate(formattedDate);
            if (countEntity == null) countEntity = new SessionCountEntity(formattedDate);
            countEntity.incrementCount();
            sessionCountTableRepository.saveAndFlush(countEntity);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Transactional(readOnly = true)
    public List<SessionCountData> getTop10SessionCountData() {
        try {
            List<SessionCountEntity> entityList = sessionCountTableRepository.findTop10ByOrderByDateDesc();
            if(entityList == null) throw new NullPointerException("entityList is null ...");

            List<SessionCountData> dataList = new ArrayList<>();
            for(SessionCountEntity entity : entityList) {
                SessionCountData data = modelMapper.map(entity, SessionCountData.class);
                dataList.add(data);
            }
            return dataList;
        } catch (Exception e) {
            log.error(e);
            return new ArrayList<>();
        }
    }
}
