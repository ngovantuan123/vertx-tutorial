package org.example.vertxtutorial.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor@AllArgsConstructor
public class RegisterRequest {
    private DataModel data;
    private TraceModel trace;
    private String signature;
}
