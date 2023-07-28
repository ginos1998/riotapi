package riot.riotapi.delegators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.services.implementations.ImpSummonerApiService;

import static org.mockito.Mockito.*;


@WebMvcTest(ChampionDelegatorTest.class)
public class SummonerDelegatorTest {

  @Mock
  private ImpSummonerApiService summonerApiServiceMock;

  @InjectMocks
  private SummonerDelegador summonerDelegador;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void getSummonerByName_PositiveTest() {
    // given
    String id = "123456789";
    String accountId = "987654321";
    String puuid = "135798642";
    String name = "pepito";
    SummonerDTO expectedSummoner = new SummonerDTO(id, accountId, puuid, name, 0, 0, 0);

    String expectedName = "pepito";

    // then
    when(summonerApiServiceMock.getSummonerByName(expectedName))
        .thenReturn(expectedSummoner);

    // when
    SummonerDTO receivedSumDTO = summonerDelegador.getSummonerByName(expectedName);

    verify(summonerApiServiceMock, times(1)).getSummonerByName(expectedName);
    Assert.assertNotNull(receivedSumDTO);
    Assert.assertEquals(receivedSumDTO, expectedSummoner);
  }

  @Test
  public void getSummonerByPuuid_PositiveTest() {
    // given
    String id = "123456789";
    String accountId = "987654321";
    String puuid = "135798642";
    String name = "pepito";
    SummonerDTO expectedSummoner = new SummonerDTO(id, accountId, puuid, name, 0, 0, 0);

    String expectedPuuid = "135798642";

    // then
    when(summonerApiServiceMock.getSummonerByPuuid(expectedPuuid))
        .thenReturn(expectedSummoner);

    // when
    SummonerDTO receivedSumDTO = summonerDelegador.getSummonerByPuuid(expectedPuuid);

    verify(summonerApiServiceMock, times(1)).getSummonerByPuuid(expectedPuuid);
    Assert.assertNotNull(receivedSumDTO);
    Assert.assertEquals(receivedSumDTO, expectedSummoner);
  }

  @Test
  public void getSummonerByAccountId_PositiveTest() {
    // given
    String id = "123456789";
    String accountId = "987654321";
    String puuid = "135798642";
    String name = "pepito";
    SummonerDTO expectedSummoner = new SummonerDTO(id, accountId, puuid, name, 0, 0, 0);

    String expectedAccountId = "987654321";

    // then
    when(summonerApiServiceMock.getSummonerByAccountId(expectedAccountId))
        .thenReturn(expectedSummoner);

    // when
    SummonerDTO receivedSumDTO = summonerDelegador.getSummonerByAccountId(expectedAccountId);

    verify(summonerApiServiceMock, times(1)).getSummonerByAccountId(expectedAccountId);
    Assert.assertNotNull(receivedSumDTO);
    Assert.assertEquals(receivedSumDTO, expectedSummoner);
  }

}
