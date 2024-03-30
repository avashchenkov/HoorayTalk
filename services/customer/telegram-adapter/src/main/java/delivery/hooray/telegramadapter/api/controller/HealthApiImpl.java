package delivery.hooray.telegramadapter.api.controller;

import delivery.hooray.telegramadapter.api.HealthApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthApiImpl implements HealthApi {

    @Override
    public ResponseEntity<Void> getHealthStatus() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
