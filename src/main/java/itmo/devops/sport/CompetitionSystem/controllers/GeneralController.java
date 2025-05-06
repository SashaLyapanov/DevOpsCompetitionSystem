package itmo.devops.sport.CompetitionSystem.controllers;

import itmo.devops.sport.CompetitionSystem.dto.CompetitionDTO;
import itmo.devops.sport.CompetitionSystem.dto.SportsmanDTO;
import itmo.devops.sport.CompetitionSystem.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class GeneralController {

    private final GeneralService generalService;

    @Autowired
    public GeneralController(GeneralService generalService) {
        this.generalService = generalService;
    }

    /**
     * CRUD - Competition
     */

    @GetMapping("getCompetitions")
    public ResponseEntity<?> getCompetitions() {
        List<CompetitionDTO> response = generalService.getCompetitions();
        System.out.println("Образ обновился2.0!!!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("getCompetition")
    public ResponseEntity<?> getCompetition(@RequestParam String competitionId) {
        CompetitionDTO response = generalService.getCompetition(competitionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("createCompetition")
    public ResponseEntity<?> createCompetition(@RequestBody CompetitionDTO competitionDTO) throws IOException, InterruptedException {
        CompetitionDTO response = generalService.createCompetition(competitionDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("deleteCompetition")
    public ResponseEntity<?> deleteCompetition(@RequestParam String competitionId) {
        System.out.println("Удаление соревнования " + competitionId);
        generalService.deleteCompetition(competitionId);
        System.out.println("Соревнование удалено " + competitionId);
        return ResponseEntity.ok("Competition deleted");
    }

    @PutMapping("editCompetition")
    public ResponseEntity<?> editCompetition(@RequestBody CompetitionDTO competitionDTO) {
        if (competitionDTO.getId() == null) {
            throw new IllegalArgumentException("Competition id cannot be null");
        }
        CompetitionDTO response = generalService.editCompetition(competitionDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * CRUD - Sportsman
     */

    @GetMapping("getSportsmen")
    public ResponseEntity<?> getSportsmen() {
        List<SportsmanDTO> response = generalService.getSportsmen();
        return ResponseEntity.ok(response);
    }

    @GetMapping("getSportsman")
    public ResponseEntity<?> getSportsman(@RequestParam String sportsmanId) {
        SportsmanDTO response = generalService.getSportsman(sportsmanId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("createSportsman")
    public ResponseEntity<?> createSportsman(@RequestBody SportsmanDTO sportsmanDTO) {
        if (sportsmanDTO == null) {
            throw new IllegalArgumentException("Sportsman id cannot be null");
        }
        SportsmanDTO response = generalService.createSportsman(sportsmanDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("deleteSportsman")
    public ResponseEntity<?> deleteSportsman(@RequestParam String sportsmanId) {
        if (sportsmanId == null) {
            throw new IllegalArgumentException("Sportsman id cannot be null");
        }
        generalService.deleteSportsman(sportsmanId);
        return ResponseEntity.ok("Sportsman deleted");
    }

    @PutMapping("editSportsman")
    public ResponseEntity<?> editSportsman(@RequestBody SportsmanDTO sportsmanDTO) {
        if (sportsmanDTO.getId() == null) {
            throw new IllegalArgumentException("Sportsman id cannot be null");
        }

        SportsmanDTO response = generalService.editSportsman(sportsmanDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Регистрация спортсмена на соревнования
     */
    @PostMapping("regInCompetition")
    public ResponseEntity<?> regInCompetition(@RequestParam String competitionId, @RequestParam String sportsmanId) {
        if (competitionId == null || sportsmanId == null) {
            throw new IllegalArgumentException("Competition id and sportsman id cannot be null");
        }

        boolean response = generalService.regInCompetition(competitionId, sportsmanId);

        if (response) {
            return ResponseEntity.ok("Sportsman is registered in competition");
        } else {
            return ResponseEntity.badRequest().body("Sportsman is not registered in competition");
        }
    }



}
