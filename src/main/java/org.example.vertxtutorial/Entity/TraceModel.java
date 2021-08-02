package org.example.vertxtutorial.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraceModel {
    private String requestID;
    private String requestDateTime;
    private String channelID;
    private String clientID;
}
