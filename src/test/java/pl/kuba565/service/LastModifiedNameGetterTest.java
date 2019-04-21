package pl.kuba565.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import pl.kuba565.model.RepositoryInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class LastModifiedNameGetterTest {
    List<RepositoryInfo> list = List.of(new RepositoryInfo("data1", new Date()));

    @Test
    public void shouldGetLastModifiedName() {
        //given
        LastModifiedNameGetter lastModifiedNameGetter = new LastModifiedNameGetter();

        //when
        String expected = lastModifiedNameGetter.getFirstFromRepositoryInfoList(list);

        //then
        Assert.assertEquals(expected, "data1");
    }
}