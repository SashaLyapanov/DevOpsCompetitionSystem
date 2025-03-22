package itmo.devops.sport.CompetitionSystem.repositories;

import itmo.devops.sport.CompetitionSystem.models.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, String> {
}
