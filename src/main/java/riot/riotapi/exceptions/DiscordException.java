package riot.riotapi.exceptions;

public class DiscordException extends RuntimeException{
    public DiscordException(String log) {
        super(log);
    }

    public DiscordException(String log, Throwable thr) {
        super(log, thr);
    }
}
