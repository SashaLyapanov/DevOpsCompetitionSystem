package itmo.devops.sport.CompetitionSystem.mapper;

import itmo.devops.sport.CompetitionSystem.dto.SportsmanDTO;
import itmo.devops.sport.CompetitionSystem.models.Sportsman;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SportsmanMapper {

    Sportsman sportsmanFromSportsmanDTO(SportsmanDTO sportsmanDTO);

    SportsmanDTO sportsmanDTOFromSportsman(Sportsman sportsman);

    List<Sportsman> sportsmanListFromSportsmanDTOList(List<SportsmanDTO> sportsmanDTOList);

    List<SportsmanDTO> sportsmanDTOListFromSportsmanList(List<Sportsman> sportsmanList);

}
