package itmo.devops.sport.CompetitionSystem.mapper;

import itmo.devops.sport.CompetitionSystem.dto.CompetitionDTO;
import itmo.devops.sport.CompetitionSystem.models.Competition;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompetitionMapper {

    Competition competitionFromCompetitionDTO(CompetitionDTO competitionDTO);

    CompetitionDTO competitionDTOFromCompetition(Competition competition);

    List<Competition> competitionListFromCompetitionDTOList(List<CompetitionDTO> competitionDTOList);

    List<CompetitionDTO> competitionListDTOFromCompetitionList(List<Competition> competitionList);

}
