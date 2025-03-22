package itmo.devops.sport.CompetitionSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SportsmanDTO {

    private String id;

    private String surname;

    private String firstName;

    private String patronymic;

    private int age;

    private String email;

    private String phone;

}
