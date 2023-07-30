package riot.riotapi.delegators;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import riot.riotapi.dtos.ChampionDTO;
import riot.riotapi.dtos.ChampionDataDTO;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.services.interfaces.IntChampionApiService;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@WebMvcTest(ChampionDelegatorTest.class)
public class ChampionDelegatorTest {

  @Mock
  private IntChampionApiService intChampionApiService;

  @InjectMocks
  private ChampionDelegator championDelegator;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void getChampionByName_PositiveTest() {
    // given
    String champName = "pepito";
    ChampionDataDTO givenDTO = this.createChampionDTO(true);

    // when
    when(intChampionApiService.getChampionByName(champName))
        .thenReturn(givenDTO);

    // then
    ChampionDataDTO receivedDTO = this.championDelegator.getChampionByName(champName);

    assertEquals(receivedDTO, givenDTO);
    verify(intChampionApiService, times(1)).getChampionByName(champName);
  }

  @Test
  public void getAllChampions_PositiveTest() {
    // given
    ChampionDataDTO givenDTO = createChampionDTO(true);
    // when
    when(intChampionApiService.getAllChampions())
        .thenReturn(givenDTO);

    // then
    ChampionDataDTO receivedDTO = this.intChampionApiService.getAllChampions();

    assertEquals(receivedDTO, givenDTO);
    verify(intChampionApiService, times(1)).getAllChampions();
    assertFalse(receivedDTO.getData().isEmpty());
  }

  @Test
  public void importAllChampions_PositiveTest() throws ServiceException {
    // given
    String givenResponse = "OK";

    // when
    when(intChampionApiService.importAllChampions())
        .thenReturn(givenResponse);

    // then
    String receivedResponse = championDelegator.importAllChampions();

    assertEquals(receivedResponse, givenResponse);
    verify(intChampionApiService, times(1)).importAllChampions();
    assertFalse(receivedResponse.isEmpty());
  }

  private ChampionDataDTO createChampionDTO(boolean withData) {
    Map<String, ChampionDTO> map = null;
    ChampionDataDTO dto = new ChampionDataDTO();
    dto.setType("some type");
    dto.setVersion("v13");
    dto.setFormat("random format");

    if (withData) {
      map = new HashMap<>();
      map.put("Illaoi", new ChampionDTO());
    }

    dto.setData(map);

    return dto;
  }
}
