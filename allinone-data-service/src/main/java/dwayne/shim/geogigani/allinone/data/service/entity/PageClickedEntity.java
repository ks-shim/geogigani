package dwayne.shim.geogigani.allinone.data.service.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "page_clicked_table", indexes = {@Index(name = "content_id", columnList = "content_id", unique = true)})
@Data
public class PageClickedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public PageClickedEntity() {}
    public PageClickedEntity(String contentId) {
        this.contentId = contentId;
    }
    public PageClickedEntity(String contentId, String date) {
        this.contentId = contentId;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "content_id", unique = true, nullable = false, updatable = false)
    private String contentId;

}
