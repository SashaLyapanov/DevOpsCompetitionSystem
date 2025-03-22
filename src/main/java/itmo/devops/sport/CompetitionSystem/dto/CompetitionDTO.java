package itmo.devops.sport.CompetitionSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionDTO {

    private String id;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private int participantQuantity;

    private List<SportsmanDTO> sportsmanList;

}
