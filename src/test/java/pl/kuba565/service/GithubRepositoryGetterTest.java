package pl.kuba565.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.Assert;
import org.junit.Test;
import pl.kuba565.model.RepositoryInfo;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GithubRepositoryGetterTest {

    static final String dataMock = "[" +
            "  {   " +
            " \"name\": \"data1\"," +
            "    \"updated_at\": \"2019-04-21T07:27:34Z\"" +
            "   }," +
            "  {" +
            "    \"name\": \"data2\"," +
            "    \"updated_at\": \"2019-04-21T07:27:34Z\"" +
            "  }" +
            "]";

    @Test
    public void shouldReturnNullWhenNoData() {
        // given
        GithubRepositoryGetter githubRepositoryGetter = new GithubRepositoryGetter(2000);
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8081));
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/repository"))
                .willReturn(okJson("").withStatus(200)));

        // when

        List<RepositoryInfo> resultList = githubRepositoryGetter.getRepositoriesFromUrl("http://localhost:8081/repository");

        // then
        Assert.assertEquals(resultList, null);
        wireMockServer.resetAll();
        wireMockServer.stop();
    }

    @Test
    public void shouldGetDataFromMockBackend() {
        // given
        GithubRepositoryGetter githubRepositoryGetter = new GithubRepositoryGetter(2000);
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8082));
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/repository"))
                .willReturn(okJson(dataMock)
                        .withStatus(200)));

        // when
        List<RepositoryInfo> resultList = githubRepositoryGetter.getRepositoriesFromUrl("http://localhost:8082/repository");

        // then
        Assert.assertEquals(resultList.size(), 2);
        wireMockServer.resetAll();
        wireMockServer.stop();
    }

    @Test
    public void shouldHandleGithubTimeoutException() {
        // given
        GithubRepositoryGetter githubRepositoryGetter = new GithubRepositoryGetter(200);
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8083));
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/repository"))
                .willReturn(okJson("").withFixedDelay(300)));

        GithubTimeoutException githubTimeoutException = null;

        // when
        try {
            githubRepositoryGetter.getRepositoriesFromUrl("http://localhost:8083/repository");
        } catch (GithubTimeoutException e) {
            githubTimeoutException = e;
        }

        // then
        Assert.assertNotNull(githubTimeoutException);
        wireMockServer.stop();
    }

}