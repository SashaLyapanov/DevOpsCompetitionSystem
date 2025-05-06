package itmo.devops.sport.CompetitionSystem;
import itmo.devops.sport.CompetitionSystem.repositories.CompetitionRepository;
import itmo.devops.sport.CompetitionSystem.services.GeneralService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import itmo.devops.sport.CompetitionSystem.dto.CompetitionDTO;
import itmo.devops.sport.CompetitionSystem.dto.SportsmanDTO;
import itmo.devops.sport.CompetitionSystem.mapper.CompetitionMapper;
import itmo.devops.sport.CompetitionSystem.mapper.SportsmanMapper;
import itmo.devops.sport.CompetitionSystem.models.Competition;
import itmo.devops.sport.CompetitionSystem.models.Sportsman;
import itmo.devops.sport.CompetitionSystem.repositories.SportsmanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
/*@DataJpaTest*/


/*@RunWith(SpringRunner.class)
@SpringBootTest*/
@MockitoSettings(strictness = Strictness.LENIENT)

@ExtendWith(MockitoExtension.class)
class CompetitionSystemApplicationTests {

	// unit test for General Service
		@Mock
		private CompetitionRepository competitionRepository;

		@Mock
		private CompetitionMapper competitionMapper;

		@Mock
		private SportsmanRepository sportsmanRepository;

		@Mock
		private SportsmanMapper sportsmanMapper;

		@InjectMocks
		private GeneralService generalService;

		private Competition competition;
		private CompetitionDTO competitionDTO;
		private Sportsman sportsman;
		private SportsmanDTO sportsmanDTO;

		@BeforeEach
		void setUp() {
			competition = new Competition();
			competition.setId("1");
			competition.setName("Test Competition");

			competitionDTO = new CompetitionDTO();
			competitionDTO.setId("1");
			competitionDTO.setName("Test Competition DTO");

			sportsman = new Sportsman();
			sportsman.setId("10");
			sportsman.setFirstName("Ivan");
			sportsman.setPatronymic("Ivanovich");

			sportsman.setSurname("Ivanov");
			sportsman.setCompetitionList(new ArrayList<>());


			sportsmanDTO = new SportsmanDTO();
			sportsmanDTO.setId("10");
			sportsman.setFirstName("Ivan");
			sportsman.setPatronymic("Ivanovich");
			sportsman.setSurname("Ivanov DTO");
		}

		@Test
		void getCompetitions_ShouldReturnCompetitionList() {
			when(competitionRepository.findAll()).thenReturn(Collections.singletonList(competition));
			when(competitionMapper.competitionListDTOFromCompetitionList(anyList())).thenReturn(Collections.singletonList(competitionDTO));

			List<CompetitionDTO> result = generalService.getCompetitions();

			assertNotNull(result);
			assertEquals(1, result.size());
			assertEquals("1", result.get(0).getId());
			verify(competitionRepository, times(1)).findAll();
		}

		@Test
		void getCompetition_ShouldReturnCompetitionDTO_WhenExists() {
			when(competitionRepository.findById("1")).thenReturn(Optional.of(competition));
			when(competitionMapper.competitionDTOFromCompetition(any())).thenReturn(competitionDTO);

			CompetitionDTO result = generalService.getCompetition("1");

			assertNotNull(result);
			assertEquals("1", result.getId());
			verify(competitionRepository, times(1)).findById("1");
		}

		@Test
		void getCompetition_ShouldReturnNull_WhenNotExists() {
			when(competitionRepository.findById("2")).thenReturn(Optional.empty());

			CompetitionDTO result = generalService.getCompetition("2");

			assertNull(result);
		}

		@Test
		void createCompetition_ShouldSaveAndReturnDTO() throws IOException, InterruptedException {
			when(competitionMapper.competitionFromCompetitionDTO(any())).thenReturn(competition);
			when(competitionRepository.save(any())).thenReturn(competition);
			when(competitionMapper.competitionDTOFromCompetition(any())).thenReturn(competitionDTO);

			CompetitionDTO result = generalService.createCompetition(competitionDTO);

			assertNotNull(result);
			assertEquals("1", result.getId());
			verify(competitionRepository, times(1)).save(any());
		}

		@Test
		void deleteCompetition_ShouldDelete_WhenNoSportsmen() {
			when(sportsmanRepository.findByCompetition("1")).thenReturn(Collections.emptyList());

			generalService.deleteCompetition("1");

			verify(competitionRepository, times(1)).deleteById("1");
		}

		@Test
		void deleteCompetition_ShouldThrowException_WhenSportsmenExist() {
			when(sportsmanRepository.findByCompetition("1")).thenReturn(Collections.singletonList(sportsman));

			Exception exception = assertThrows(IllegalArgumentException.class, () -> generalService.deleteCompetition("1"));

			assertEquals("We can't delete this competition, because it has some dependencies", exception.getMessage());
			verify(competitionRepository, never()).deleteById("1");
		}

		@Test
		void regInCompetition_ShouldRegisterSportsman() {
			when(competitionRepository.findById("1")).thenReturn(Optional.of(competition));
			when(sportsmanRepository.findById("10")).thenReturn(Optional.of(sportsman));
			when(sportsmanRepository.save(any())).thenReturn(sportsman);

			boolean result = generalService.regInCompetition("1", "10");

			assertTrue(result);
			verify(sportsmanRepository, times(1)).save(any());
		}

		@Test
		void regInCompetition_ShouldReturnFalse_WhenCompetitionOrSportsmanNotFound() {
			when(competitionRepository.findById("1")).thenReturn(Optional.empty());
			when(sportsmanRepository.findById("10")).thenReturn(Optional.of(sportsman));

			boolean result = generalService.regInCompetition("1", "10");

			assertFalse(result);
		}
	@Test
	void editCompetition_ShouldEditAndReturnUpdatedCompetition() {
		when(competitionRepository.findById("1")).thenReturn(Optional.of(competition));
		when(competitionMapper.competitionFromCompetitionDTO(any())).thenReturn(competition);
		when(competitionRepository.save(any())).thenReturn(competition);
		when(competitionMapper.competitionDTOFromCompetition(any())).thenReturn(competitionDTO);

		CompetitionDTO result = generalService.editCompetition(competitionDTO);

		assertNotNull(result);
		assertEquals("1", result.getId());
		verify(competitionRepository, times(1)).save(any());
	}

	@Test
	void editCompetition_ShouldThrowException_WhenCompetitionDoesNotExist() {
		when(competitionRepository.findById("2")).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> generalService.editCompetition(competitionDTO));

		assertEquals("Such competition does not exist", exception.getMessage());
		verify(competitionRepository, never()).save(any());
	}

	@Test
	void editSportsman_ShouldEditAndReturnUpdatedSportsman() {
		when(sportsmanRepository.findById("10")).thenReturn(Optional.of(sportsman));
		when(sportsmanMapper.sportsmanFromSportsmanDTO(any())).thenReturn(sportsman);
		when(sportsmanRepository.save(any())).thenReturn(sportsman);
		when(sportsmanMapper.sportsmanDTOFromSportsman(any())).thenReturn(sportsmanDTO);

		SportsmanDTO result = generalService.editSportsman(sportsmanDTO);

		assertNotNull(result);
		assertEquals("10", result.getId());
		verify(sportsmanRepository, times(1)).save(any());
	}

	@Test
	void editSportsman_ShouldThrowException_WhenSportsmanDoesNotExist() {
		when(sportsmanRepository.findById("20")).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> generalService.editSportsman(sportsmanDTO));

		assertEquals("Such sportsman does not exist", exception.getMessage());
		verify(sportsmanRepository, never()).save(any());
	}
	void createSportsman_ShouldReturnSportsmanDTO_WhenSavedSuccessfully() {
		// Arrange
		when(sportsmanMapper.sportsmanFromSportsmanDTO(sportsmanDTO)).thenReturn(sportsman);
		when(sportsmanRepository.save(sportsman)).thenReturn(sportsman);
		when(sportsmanMapper.sportsmanDTOFromSportsman(sportsman)).thenReturn(sportsmanDTO);

		// Act
		SportsmanDTO result = generalService.createSportsman(sportsmanDTO);

		// Assert
		assertNotNull(result);
		assertEquals(sportsmanDTO.getId(), result.getId());
		assertEquals(sportsmanDTO.getFirstName(), result.getFirstName());
		assertEquals(sportsmanDTO.getPatronymic(), result.getPatronymic());
		assertEquals(sportsmanDTO.getSurname(), result.getSurname());  // Ensure surname matches

		verify(sportsmanRepository, times(1)).save(sportsman);
		verify(sportsmanMapper, times(1)).sportsmanDTOFromSportsman(sportsman);
	}

	@Test
	void getSportsman_ShouldReturnSportsmanDTO_WhenSportsmanExists() {
		// Arrange
		when(sportsmanRepository.findById("10")).thenReturn(Optional.of(sportsman));
		when(sportsmanMapper.sportsmanDTOFromSportsman(sportsman)).thenReturn(sportsmanDTO);

		// Act
		SportsmanDTO result = generalService.getSportsman("10");

		// Assert
		assertNotNull(result);
		assertEquals("10", result.getId());
		/*assertEquals("Ivan", result.getFirstName());*/
		/*assertEquals("Ivanovich", result.getPatronymic());
		assertEquals("Ivanov DTO", result.getSurname());  // Check DTO's surname*/

		verify(sportsmanRepository, times(1)).findById("10");
	}

	@Test
	void getSportsman_ShouldReturnNull_WhenSportsmanDoesNotExist() {
		// Arrange
		when(sportsmanRepository.findById("999")).thenReturn(Optional.empty());

		// Act
		SportsmanDTO result = generalService.getSportsman("999");

		// Assert
		assertNull(result);
		verify(sportsmanRepository, times(1)).findById("999");
	}
}


/*

	@Test
	void contextLoads() {
	}

}
*/
