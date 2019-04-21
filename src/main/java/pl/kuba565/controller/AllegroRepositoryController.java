package pl.kuba565.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kuba565.model.RepositoryInfo;
import pl.kuba565.service.GithubRepositoryGetter;
import pl.kuba565.service.LastModifiedNameGetter;

import java.util.List;

@RestController
@RequestMapping("/")
public class AllegroRepositoryController {
    private final String URL;
    private final LastModifiedNameGetter lastModifiedNameGetter;
    private final GithubRepositoryGetter githubRepositoryGetter;

    AllegroRepositoryController(LastModifiedNameGetter lastModifiedNameGetter, GithubRepositoryGetter githubRepositoryGetter,
                                @Value("${URL:https://api.github.com/orgs/allegro/repos?sort=updated}") String URL) {
        this.URL = URL;
        this.lastModifiedNameGetter = lastModifiedNameGetter;
        this.githubRepositoryGetter = githubRepositoryGetter;
    }

    @GetMapping("repository")
    public ResponseEntity<String> lastModifiedRepositoryName() {
        List<RepositoryInfo> repositoryInfoList = githubRepositoryGetter.getRepositoriesFromUrl(URL);
        String repositoryName = lastModifiedNameGetter.getFirstFromRepositoryInfoList(repositoryInfoList);

        if (repositoryInfoList.size() > 0) {
            return ResponseEntity.ok(repositoryName);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
