package Entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentEntity {
    private String studentId;
    private String fullName;
    private String className;
    private String birthday;
}
