package org.ssafy.pasila.global.infra.FFmpeg;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.ssafy.pasila.global.common.file.FilenameAwareInputStreamResource;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class FFmpegClient {

    private final RestTemplate restTemplate;

    @Value("${ffmpeg.url}")
    private String url;

    public byte[] convertAudio(MultipartFile file) throws RestClientException, IOException {

        FilenameAwareInputStreamResource fileResource = new FilenameAwareInputStreamResource(
                file.getInputStream(), file.getSize(), "test"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url + "/convert/audio/to/mp3", requestEntity, byte[].class);

    }
}
