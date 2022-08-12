package nus.iss.ADBackend.helper;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionsForm {
    private String status;
    private List<String> actions;
}
