package app.service.timekeepingService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface TimeKeepingService {

    public String getListTimeKeeping(HttpServletResponse response) throws IOException;
}
