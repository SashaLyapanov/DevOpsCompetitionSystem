package itmo.devops.sport.CompetitionSystem.services;

import itmo.devops.sport.CompetitionSystem.dto.CompetitionDTO;
import itmo.devops.sport.CompetitionSystem.dto.SportsmanDTO;
import itmo.devops.sport.CompetitionSystem.mapper.CompetitionMapper;
import itmo.devops.sport.CompetitionSystem.mapper.SportsmanMapper;
import itmo.devops.sport.CompetitionSystem.models.Competition;
import itmo.devops.sport.CompetitionSystem.models.Sportsman;
import itmo.devops.sport.CompetitionSystem.repositories.CompetitionRepository;
import itmo.devops.sport.CompetitionSystem.repositories.SportsmanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class GeneralService {

    private final CompetitionRepository competitionRepository;

    private final CompetitionMapper competitionMapper;

    private final SportsmanRepository sportsmanRepository;

    private final SportsmanMapper sportsmanMapper;

    @Autowired
    public GeneralService(CompetitionRepository competitionRepository, CompetitionMapper competitionMapper, SportsmanRepository sportsmanRepository, SportsmanMapper sportsmanMapper) {
        this.competitionRepository = competitionRepository;
        this.competitionMapper = competitionMapper;
        this.sportsmanRepository = sportsmanRepository;
        this.sportsmanMapper = sportsmanMapper;
    }

    public List<CompetitionDTO> getCompetitions() {
        List<Competition> competitions = competitionRepository.findAll();
        List<CompetitionDTO> competitionDTOList = competitionMapper.competitionListDTOFromCompetitionList(competitions);

        return competitionDTOList;
    }

    public CompetitionDTO getCompetition(String id) {
        Competition competition = competitionRepository.findById(id).orElse(null);
        return competitionMapper.competitionDTOFromCompetition(competition);
    }

    public List<SportsmanDTO> getSportsmen() {
        List<Sportsman> sportsmen = sportsmanRepository.findAll();
        List<SportsmanDTO> sportsmanDTOList = sportsmanMapper.sportsmanDTOListFromSportsmanList(sportsmen);

        return sportsmanDTOList;
    }

    @Transactional
    public CompetitionDTO createCompetition(CompetitionDTO competitionDTO) throws IOException, InterruptedException {
        if (competitionDTO == null) {
            throw new IllegalArgumentException("Competition DTO cannot be null");
        }

        Competition competition = competitionMapper.competitionFromCompetitionDTO(competitionDTO);
        competition = competitionRepository.save(competition);
        if (competition.getId() != null) {
            HttpClient httpClient = HttpClient.newHttpClient();
            String message = URLEncoder.encode("Зарегистрировано новое соревнование - " + competition.getName() + ". Даты проведения: " + competition.getStartDate() + " - " + competition.getEndDate() + ".", "UTF-8");
            String url = "https://api.telegram.org/bot8068291184:AAFx1-JqwTzVneTntFagyiwA-awSiKhU10E/sendMessage?chat_id=1054444524&text=" + message;
            httpClient.send(HttpRequest.newBuilder(URI.create(url)).build(), HttpResponse.BodyHandlers.discarding());
        }
        return competitionMapper.competitionDTOFromCompetition(competition);
    }

    public void deleteCompetition(String competitionId) {
        if (competitionId == null) {
            throw new IllegalArgumentException("Competition ID cannot be null");
        }
        List<Sportsman> sportsmen = sportsmanRepository.findByCompetition(competitionId);

        if (sportsmen != null && sportsmen.size() > 0) {
            throw new IllegalArgumentException("We can't delete this competition, because it has some dependencies");
        } else {
            competitionRepository.deleteById(competitionId);
        }
    }

    @Transactional
    public CompetitionDTO editCompetition(CompetitionDTO competitionDTO) {
        Competition competition = competitionRepository.findById(competitionDTO.getId()).orElse(null);
        if (competition == null) {
            throw new IllegalArgumentException("Such competition does not exist");
        }

        List<Sportsman> sportsmanList = competition.getSportsmanList();
        competition = competitionMapper.competitionFromCompetitionDTO(competitionDTO);
        competition.setSportsmanList(sportsmanList);

        competition = competitionRepository.save(competition);
        return competitionMapper.competitionDTOFromCompetition(competition);
    }


    public void deleteSportsman(String sportsmanId) {
        sportsmanRepository.deleteById(sportsmanId);
    }

    @Transactional
    public SportsmanDTO editSportsman(SportsmanDTO sportsmanDTO) {
        Sportsman sportsman = sportsmanRepository.findById(sportsmanDTO.getId()).orElse(null);
        if (sportsman == null) {
            throw new IllegalArgumentException("Such sportsman does not exist");
        }
        List<Competition> competitions = sportsman.getCompetitionList();
        sportsman = sportsmanMapper.sportsmanFromSportsmanDTO(sportsmanDTO);
        sportsman.setCompetitionList(competitions);

        sportsman = sportsmanRepository.save(sportsman);
        return sportsmanMapper.sportsmanDTOFromSportsman(sportsman);
    }

    @Transactional
    public boolean regInCompetition(String competitionId, String sportsmanId) {

        Competition competition = competitionRepository.findById(competitionId).orElse(null);
        Sportsman sportsman = sportsmanRepository.findById(sportsmanId).orElse(null);

        if (competition == null || sportsman == null) {
            return false;
        } else {
            List<Competition> competitions = sportsman.getCompetitionList();
            competitions.add(competition);
            sportsman.setCompetitionList(competitions);
            sportsmanRepository.save(sportsman);
            return true;
        }
    }

    @Transactional
    public SportsmanDTO createSportsman(SportsmanDTO sportsmanDTO) {
        Sportsman sportsman = sportsmanMapper.sportsmanFromSportsmanDTO(sportsmanDTO);
        sportsman = sportsmanRepository.save(sportsman);
        return sportsmanMapper.sportsmanDTOFromSportsman(sportsman);
    }

    public SportsmanDTO getSportsman(String sportsmanId) {
        Sportsman sportsman = sportsmanRepository.findById(sportsmanId).orElse(null);
        return sportsmanMapper.sportsmanDTOFromSportsman(sportsman);
    }
}















