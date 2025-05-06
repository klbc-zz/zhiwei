package com.fantasy.model.dto;

import com.fantasy.entity.Connect;
import lombok.Data;

@Data
public class UserDTO extends Connect {
    private String peson;
    private String cname;
    private String password;
    private boolean login = false;
    private String partno;
    private String partname;
    private String company;
    private String jname;
    private String telno;
    private String nextname;
    private String token;
    private String ip;
}
