package pl.kuba565.service;

import org.springframework.stereotype.Service;
import pl.kuba565.model.RepositoryInfo;

import java.util.List;

@Service
public class LastModifiedNameGetter {
    public String getFirstFromRepositoryInfoList(List<RepositoryInfo> repositoryInfoList) {
        return repositoryInfoList.get(0).getName();
    }
}
