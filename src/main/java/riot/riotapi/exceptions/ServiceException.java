package riot.riotapi.exceptions;

public class ServiceException extends RuntimeException{
  public ServiceException(String log) {
    super(log);
  }

  public ServiceException(String log, Throwable throwable) {
    super(log, throwable);
  }
}
