package delivery.hooray.telegramadapter.api;

public class HealthApiImpl implements HealthApi {

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<Void> getHealthStatus() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
