package no.gorandalum.fluentresult;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Exception;
}
