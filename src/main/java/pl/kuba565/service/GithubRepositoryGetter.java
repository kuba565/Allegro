package pl.kuba565.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.kuba565.model.RepositoryInfo;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
public class GithubRepositoryGetter {
    private Integer timeout;

    public GithubRepositoryGetter(@Value("${GithubTimeout:2000}") Integer timeout) {
        this.timeout = timeout;
    }

    public List<RepositoryInfo> getRepositoriesFromUrl(String URL) throws GithubTimeoutException {
        RestTemplate restTemplate = new RestTemplateBuilder().setReadTimeout(Duration.ofMillis(timeout)).build();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);

        try {
            ResponseEntity<List<RepositoryInfo>> responseEntity = restTemplate
                    .exchange(URL,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<RepositoryInfo>>() {
                            });

            return responseEntity.getBody();
        } catch (Exception e) {
            throw new GithubTimeoutException(e);
        }
    }
}