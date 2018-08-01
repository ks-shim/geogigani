package dwayne.shim.geogigani.allinone.data.service.repository;

import dwayne.shim.geogigani.allinone.data.service.entity.SessionCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;

public interface SessionCountTableRepository extends JpaRepository<SessionCountEntity, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    SessionCountEntity findOneByDate(String date);

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<SessionCountEntity> findTop10ByOrderByDateDesc();
}
