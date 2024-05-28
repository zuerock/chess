package handler;

import spark.Request;
import spark.Response;

public interface Handler {
    public abstract Object handle(Request req, Response res);
}
