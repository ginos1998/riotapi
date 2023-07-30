package riot.riotapi.delegators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.entities.Summoner;
import riot.riotapi.services.implementations.ImpSummonerApiService;
import riot.riotapi.services.interfaces.IntSummonerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;


@WebMvcTest(ChampionDelegatorTest.class)
public class SummonerDelegatorTest {

  @Mock
  private ImpSummonerApiService summonerApiServiceMock;
  @Mock
  private IntSummonerService summonerServiceMock;

  @InjectMocks
  private SummonerDelegador summonerDelegador;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * Verifies if, given a 'name' (not null or empty), the app search a summoner in the database
   * and return a not empty list. It means the summoner exists in the database.
   */
  @Test
  public void getSummonerByName_SearchBd_PositiveTest() {
    // given
    String id = "123456789";
    String accountId = "987654321";
    String puuid = "135798642";
    String name = "pepito";
    SummonerDTO expectedSummoner = new SummonerDTO(id, accountId, puuid, name, 0, 0, 0);
    List<SummonerDTO> expectedList = new ArrayList<>(Collections.singletonList(expectedSummoner));
    String expectedName = "pepito";
    ModelMapper mapper = new ModelMapper();

    // then
    when(summonerServiceMock.getSummonerByName(expectedName))
        .thenReturn(expectedList);

    // when
    List<SummonerDTO> receivedList = summonerDelegador.getSummonerBy(expectedName, null, null, true);

    verify(summonerApiServiceMock, times(0)).getSummonerByName(expectedName);
    verify(summonerServiceMock, times(1)).getSummonerByName(expectedName);
    verify(summonerServiceMock, times(0)).saveSummoner(mapper.map(receivedList.get(0), Summoner.class));
    Assert.assertNotNull(receivedList);
    Assert.assertEquals(receivedList, expectedList);

  }

  /**
   * Verifies if, given a 'name' (not null or empty), the app search a summoner first in the database
   * and if the returned list is null or empty, then search the summoner creating a request to the api.
   * Finally, also verifies if the returned list is not null.
   */
  @Test
  public void getSummonerByName_SearchApi_PositiveTest() {
    // given
    String id = "123456789";
    String accountId = "987654321";
    String puuid = "135798642";
    String name = "pepito";
    SummonerDTO expectedSummoner = new SummonerDTO(id, accountId, puuid, name, 0, 0, 0);
    List<SummonerDTO> expectedList = new ArrayList<>(Collections.singletonList(expectedSummoner));
    String expectedName = "pepito ";
    ModelMapper mapper = new ModelMapper();

    // then
    when(summonerServiceMock.getSummonerByName(expectedName))
        .thenReturn(new ArrayList<>());

    when(summonerApiServiceMock.getSummonerByName(expectedName))
        .thenReturn(expectedList);

    // when
    List<SummonerDTO> receivedList = summonerDelegador.getSummonerBy(expectedName, null, null, true);

    verify(summonerApiServiceMock, times(1)).getSummonerByName(expectedName);
    verify(summonerServiceMock, times(1)).getSummonerByName(expectedName);
    verify(summonerServiceMock, times(1)).saveSummoner(mapper.map(receivedList.get(0), Summoner.class));
    Assert.assertNotNull(receivedList);
    Assert.assertEquals(receivedList, expectedList);

  }

  /**
   * Verifies if, given a 'puuid' (not null or empty), the app search a summoner first in the database
   * and if the returned list is null or empty, then search the summoner creating a request to the api.
   * Finally, also verifies if the returned list is not null.
   */
  @Test
  public void getSummonerByPuuid_SearchApi_PositiveTest() {
    // given
    String id = "123456789";
    String accountId = "987654321";
    String puuid = "135798642";
    String name = "pepito";
    SummonerDTO expectedSummoner = new SummonerDTO(id, accountId, puuid, name, 0, 0, 0);

    String expectedPuuid = "135798642";
    List<SummonerDTO> expectedList = new ArrayList<>(Collections.singletonList(expectedSummoner));
    ModelMapper mapper = new ModelMapper();

    // then
    when(summonerServiceMock.getSummonerByPuuid(expectedPuuid))
        .thenReturn(new ArrayList<>());
    when(summonerApiServiceMock.getSummonerByPuuid(expectedPuuid))
        .thenReturn(expectedList);

    // when
    List<SummonerDTO> receivedList = summonerDelegador.getSummonerBy(null, null, expectedPuuid, true);

    verify(summonerServiceMock, times(1)).getSummonerByPuuid(expectedPuuid);
    verify(summonerApiServiceMock, times(1)).getSummonerByPuuid(expectedPuuid);
    verify(summonerServiceMock, times(1)).saveSummoner(mapper.map(receivedList.get(0), Summoner.class));
    Assert.assertNotNull(receivedList);
    Assert.assertEquals(receivedList, expectedList);
  }

  /**
   * Verifies if, given a 'puuid' (not null or empty), the app search a summoner in the database
   * and return a not empty list. It means the summoner exists in the database.
   */
  @Test
  public void getSummonerByPuuid_SearchBd_PositiveTest() {
    // given
    String id = "123456789";
    String accountId = "987654321";
    String puuid = "135798642";
    String name = "pepito";
    SummonerDTO expectedSummoner = new SummonerDTO(id, accountId, puuid, name, 0, 0, 0);

    String expectedPuuid = "135798642";
    List<SummonerDTO> expectedList = new ArrayList<>(Collections.singletonList(expectedSummoner));
    ModelMapper mapper = new ModelMapper();

    // then
    when(summonerServiceMock.getSummonerByPuuid(expectedPuuid))
        .thenReturn(expectedList);

    // when
    List<SummonerDTO> receivedList = summonerDelegador.getSummonerBy(null, null, expectedPuuid, true);

    verify(summonerServiceMock, times(1)).getSummonerByPuuid(expectedPuuid);
    verify(summonerApiServiceMock, times(0)).getSummonerByPuuid(expectedPuuid);
    verify(summonerServiceMock, times(0)).saveSummoner(mapper.map(receivedList.get(0), Summoner.class));
    Assert.assertNotNull(receivedList);
    Assert.assertEquals(receivedList, expectedList);
  }

  /**
   * Verifies if, given a 'accountId' (not null or empty), the app search a summoner in the database
   * and return a not empty list. It means the summoner exists in the database.
   */
  @Test
  public void getSummonerByAccountId_SearchBd_PositiveTest() {
    // given
    String id = "123456789";
    String accountId = "987654321";
    String puuid = "135798642";
    String name = "pepito";
    SummonerDTO expectedSummoner = new SummonerDTO(id, accountId, puuid, name, 0, 0, 0);
    List<SummonerDTO> expectedList = new ArrayList<>(Collections.singletonList(expectedSummoner));
    ModelMapper mapper = new ModelMapper();
    String expectedAccountId = "987654321";

    // then
    when(summonerServiceMock.getSummonerByAccountId(expectedAccountId))
        .thenReturn(expectedList);

    // when
    List<SummonerDTO> receivedList = summonerDelegador.getSummonerBy(null, expectedAccountId, null, true);

    verify(summonerServiceMock, times(1)).getSummonerByAccountId(expectedAccountId);
    verify(summonerApiServiceMock, times(0)).getSummonerByAccountId(expectedAccountId);
    verify(summonerServiceMock, times(0)).saveSummoner(mapper.map(receivedList.get(0), Summoner.class));
    Assert.assertNotNull(receivedList);
    Assert.assertEquals(receivedList, expectedList);
  }

  /**
   * Verifies if, given a 'accountId' (not null or empty), the app search a summoner first in the database
   * and if the returned list is null or empty, then search the summoner creating a request to the api.
   * Finally, also verifies if the returned list is not null.
   */
  @Test
  public void getSummonerByAccountId_SearchApi_PositiveTest() {
    // given
    String id = "123456789";
    String accountId = "987654321";
    String puuid = "135798642";
    String name = "pepito";
    SummonerDTO expectedSummoner = new SummonerDTO(id, accountId, puuid, name, 0, 0, 0);
    List<SummonerDTO> expectedList = new ArrayList<>(Collections.singletonList(expectedSummoner));
    ModelMapper mapper = new ModelMapper();
    String expectedAccountId = "987654321";

    // then
    when(summonerServiceMock.getSummonerByAccountId(expectedAccountId))
        .thenReturn(new ArrayList<>());

    when(summonerApiServiceMock.getSummonerByAccountId(expectedAccountId))
        .thenReturn(expectedList);

    // when
    List<SummonerDTO> receivedList = summonerDelegador.getSummonerBy(null, expectedAccountId, null, true);

    verify(summonerServiceMock, times(1)).getSummonerByAccountId(expectedAccountId);
    verify(summonerApiServiceMock, times(1)).getSummonerByAccountId(expectedAccountId);
    verify(summonerServiceMock, times(1)).saveSummoner(mapper.map(receivedList.get(0), Summoner.class));
    Assert.assertNotNull(receivedList);
    Assert.assertEquals(receivedList, expectedList);
  }

}
