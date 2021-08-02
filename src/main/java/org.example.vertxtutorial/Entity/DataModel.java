package org.example.vertxtutorial.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataModel {
    private String cifNo;
    private String accountNo;
    private String phoneNumber;
    private String notiType;
}
