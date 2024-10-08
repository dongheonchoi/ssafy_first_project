package org.ssafy.pasila.domain.auth.service;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SendMsgService{

    @Value("${coolsms.apiKey}")
    private String apiKey ;

    @Value("${coolsms.apiSecret}")
    private String apiSecret ;

    @Value("${coolsms.fromNumber}")
    private String fromNumber;

    private DefaultMessageService messageService;

    private final RedisTemplate<String, String> redisTemplate;

    public void sendSMS (String phoneNum) {

        int authNum = randomNum();

        messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");

        Message message = new Message();

        int randomNum = randomNum();

        message.setFrom(fromNumber);
        message.setTo(phoneNum);
        message.setText(Integer.toString(authNum));

        this.messageService.sendOne(new SingleMessageSendingRequest(message));

        ValueOperations<String , String > setOperations = redisTemplate.opsForValue();
        setOperations.set("AUTH_PHONE : " + phoneNum, Integer.toString(authNum) , Duration.ofMinutes(3L) );
    }

    public static int randomNum() {
        return (int) (Math.random() * (900000)) + 100000;
    }

}