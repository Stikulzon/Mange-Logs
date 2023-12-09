package zefir.mangelogs;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionManager {
    private String SessionId;
    public String GetSessionId(){
        return SessionId;
    }
    public String CreateSessionId(){
        SessionId = UUID.randomUUID().toString();
        return SessionId;
    }
}
