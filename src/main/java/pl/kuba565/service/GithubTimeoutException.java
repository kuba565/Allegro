package pl.kuba565.service;

public class GithubTimeoutException extends RuntimeException{

    public GithubTimeoutException(Exception e) {
        super(e);
    }
}
