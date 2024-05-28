package handler;

import spark.*;
import service.ClearService;
import result.Result;
import jsonConverter.*;

public class ClearHandler implements Handler {
    private static ClearHandler instance;

    private ClearHandler() {}

    public static ClearHandler getInstance() {
        if (instance == null) {
            instance = new ClearHandler();
        }
        return instance;
    }
    @Override
    public Object handle(Request req, Response res) {
        JsonConverter converter = JsonConverter.getInstance();
        ClearService service = ClearService.getInstance();
        try {
            Result result = service.clear();
            res.status(200);
            return converter.toJson(result);
        }
        catch (Exception e) {
            res.status(500);
            return converter.toJson(new Result("Error: "+e.toString()));
        }
    }
}
