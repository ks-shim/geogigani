package dwayne.shim.geogigani.allinone.data.service.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "session_count_table")
@Data
public class SessionCountEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public SessionCountEntity() {}
    public SessionCountEntity(String date) {
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "date", nullable = false, unique = true, updatable = false)
    private String date;

    @Column(name = "count", nullable = false)
    private int count;

    public void incrementCount() {
        count++;
    }
}
