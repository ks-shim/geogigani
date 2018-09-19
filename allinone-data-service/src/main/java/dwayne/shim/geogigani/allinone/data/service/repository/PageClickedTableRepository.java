package dwayne.shim.geogigani.allinone.data.service.repository;

import dwayne.shim.geogigani.allinone.data.service.entity.PageClickedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;

public interface PageClickedTableRepository extends JpaRepository<PageClickedEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PageClickedEntity p where p.contentId = ?1")
    PageClickedEntity findOneByContentId(String contentId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<PageClickedEntity> findTop100ByOrderByDateDesc();
}
