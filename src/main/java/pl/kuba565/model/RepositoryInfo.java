package pl.kuba565.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryInfo {
    private String name;
    @JsonProperty("updated_at")
    private Date lastModifiedAt;
}
