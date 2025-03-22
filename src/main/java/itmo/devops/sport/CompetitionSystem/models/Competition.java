package itmo.devops.sport.CompetitionSystem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "competitions")
public class Competition extends GenericEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "participant_quantity")
    private int participantQuantity;

    @ManyToMany(mappedBy = "competitionList")
    private List<Sportsman> sportsmanList;

    @Override
    public String toString() {
        return "Competition{" +
                "name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", participantQuantity=" + participantQuantity +
                ", description='" + description + '\'' +
                '}';
    }
}
