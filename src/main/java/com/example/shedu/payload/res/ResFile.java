package com.example.shedu.payload.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResFile {
    private HttpHeaders headers;
    private Resource resource;
    private String fillName;
}


