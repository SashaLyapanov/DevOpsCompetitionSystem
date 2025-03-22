package itmo.devops.sport.CompetitionSystem.repositories;

import itmo.devops.sport.CompetitionSystem.models.Sportsman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportsmanRepository extends JpaRepository<Sportsman, String> {

    @Query("SELECT s FROM Sportsman s JOIN s.competitionList c WHERE c.id = :competitionId")
    List<Sportsman> findByCompetition(@Param("competitionId") String competitionId);

}
