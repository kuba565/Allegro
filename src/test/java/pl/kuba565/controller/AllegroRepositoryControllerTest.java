package pl.kuba565.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import pl.kuba565.model.RepositoryInfo;
import pl.kuba565.service.GithubRepositoryGetter;
import pl.kuba565.service.LastModifiedNameGetter;

import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class AllegroRepositoryControllerTest {
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
    public void shouldGetFirstObjectFromList() {
        // given
        GithubRepositoryGetter githubRepositoryGetter = new GithubRepositoryGetter(2000);
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8086));
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/repository"))
                .willReturn(ok(dataMock)
                        .withStatus(200)));

        LastModifiedNameGetter lastModifiedNameGetter = new LastModifiedNameGetter();

        // when
        AllegroRepositoryController allegroRepositoryController = new AllegroRepositoryController(lastModifiedNameGetter,
                githubRepositoryGetter, "http://localhost:8086/repository");
        //then
        ResponseEntity<String> expected = allegroRepositoryController.lastModifiedRepositoryName();
        Assert.assertNotNull(expected);
        Assert.assertEquals(HttpStatus.OK, expected.getStatusCode());
        Assert.assertEquals("data1", expected.getBody());
    }
}