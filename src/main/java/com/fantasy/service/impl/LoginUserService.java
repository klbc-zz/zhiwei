package com.fantasy.service.impl;

import com.fantasy.entity.Connect;
import com.fantasy.entity.LoginUser;
import com.fantasy.model.dto.UserDTO;
import com.fantasy.util.DBFSqlUtils;
import com.fantasy.util.PasswordConverter;
import com.fantasy.util.TokenUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginUserService {

    private final Map<String, UserDTO> loginUserMap = new ConcurrentHashMap<>();


    public Boolean testConnect(Connect connect) {
        String dbfPath = connect.getDbfPath();
        DBFSqlUtils.executeQuerySqlListResult(dbfPath , "select * from newview",null);
        return true;
    }

    public UserDTO login(LoginUser loginUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setIp(loginUser.getIp());
        userDTO.setDbfPath(loginUser.getDbfPath());
        String peson = loginUser.getPeson();
        String password1 = loginUser.getPassword1();
        if (peson == null) {
           throw new RuntimeException("请输入用户代号");
        }
        if (password1 == null) {
            throw new RuntimeException("请输入密码");
        }
        String password2 = PasswordConverter.encryptDBValue2(password1);
        String dbfPath = loginUser.getDbfPath();

        ArrayList<Object> params = new ArrayList<>();
        params.add(peson);

        List<Map<String, Object>> maps = DBFSqlUtils.executeQuerySqlListResult2(dbfPath, "select * from Newview where peson = ?", params);
        if (maps.isEmpty()) {
            throw new RuntimeException("找不到该账号");
        }
        Map<String, Object> stringObjectMap = maps.get(0);
        if (stringObjectMap == null) {
            return userDTO;
        }
        String CNAME = (String) stringObjectMap.get("CNAME");
        String PASSWORD1 = (String) stringObjectMap.get("PASSWORD");
        String PASSWORD2 = (String) stringObjectMap.get("PASSWORD16");
        String telno  = (String) stringObjectMap.get("TELNO");
        String company  = (String) stringObjectMap.get("COMPANY");
        String partno  = (String) stringObjectMap.get("PARTNO");
        String nextname  = (String) stringObjectMap.get("NEXTNAME");
        loginUser.setCname(CNAME);

        if (password1.equals(PASSWORD1) || password2.equals(PASSWORD2) || (PASSWORD1 == null && password1.isEmpty())){
            userDTO.setPeson(loginUser.getPeson());
            userDTO.setCname(CNAME);
            userDTO.setLogin(true);
            userDTO.setPartno(partno);
            userDTO.setCompany(company);
            userDTO.setTelno(telno);
            userDTO.setNextname(nextname);
            List<Object> partnos = Arrays.asList(partno);
            List<Object> companys = Arrays.asList(company);
            List<Map<String, Object>> mapsAp = DBFSqlUtils.executeQuerySqlListResult(dbfPath, "select * from Accpart where partno = ?", partnos);
            if(!mapsAp.isEmpty()){
                Map<String, Object> mapsAp0 = mapsAp.get(0);
                String partname = (String) mapsAp0.get("PARTNAME");
                userDTO.setPartname(partname);
            }
            List<Map<String, Object>> mapsCp = DBFSqlUtils.executeQuerySqlListResult(dbfPath, "select * from Compart where code = ?", companys);
            if(!mapsCp.isEmpty()){
                Map<String, Object> mapsCp0 = mapsCp.get(0);
                String jname = (String) mapsCp0.get("JNAME");
                userDTO.setJname(jname);
            }
            String token = TokenUtils.encrypt(userDTO.getPeson(),loginUser.getDbfPath());
            loginUserMap.put(token,userDTO);
            userDTO.setToken(token);
            return userDTO;
        }

        return userDTO;
    }

    public UserDTO getLoginUser(String token) {
        if(token == null){
            return null;
        }

        UserDTO userDTO =  loginUserMap.get(token);
        if(userDTO == null){
            String[] ut = TokenUtils.decrypt(token);
            if(ut==null || ut.length != 2){
                return null;
            }
            userDTO = getUserByToken(ut[0],ut[1]);
        }
        return userDTO;
    }
    private UserDTO getUserByToken(String peson,String dbfPath) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(peson);
        List<Map<String, Object>> maps = DBFSqlUtils.executeQuerySqlListResult2(dbfPath, "select * from Newview where peson = ?", params);
        if (maps.isEmpty()) {
            throw new RuntimeException("找不到该账号");
        }
        Map<String, Object> stringObjectMap = maps.get(0);
        if (stringObjectMap == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        String CNAME = (String) stringObjectMap.get("CNAME");
        String PASSWORD1 = (String) stringObjectMap.get("PASSWORD");
        String PASSWORD2 = (String) stringObjectMap.get("PASSWORD16");
        String telno  = (String) stringObjectMap.get("TELNO");
        String company  = (String) stringObjectMap.get("COMPANY");
        String partno  = (String) stringObjectMap.get("PARTNO");
        String nextname  = (String) stringObjectMap.get("NEXTNAME");
        userDTO.setPeson(peson);
        userDTO.setDbfPath(dbfPath);
        userDTO.setCname(CNAME);
        userDTO.setLogin(true);
        userDTO.setPartno(partno);
        userDTO.setCompany(company);
        userDTO.setTelno(telno);
        userDTO.setNextname(nextname);
        List<Object> partnos = Arrays.asList(partno);
        List<Object> companys = Arrays.asList(company);
        List<Map<String, Object>> mapsAp = DBFSqlUtils.executeQuerySqlListResult(dbfPath, "select * from Accpart where partno = ?", partnos);
        if(!mapsAp.isEmpty()){
            Map<String, Object> mapsAp0 = mapsAp.get(0);
            String partname = (String) mapsAp0.get("PARTNAME");
            userDTO.setPartname(partname);
        }
        List<Map<String, Object>> mapsCp = DBFSqlUtils.executeQuerySqlListResult(dbfPath, "select * from Compart where code = ?", companys);
        if(!mapsCp.isEmpty()){
            Map<String, Object> mapsCp0 = mapsCp.get(0);
            String jname = (String) mapsCp0.get("JNAME");
            userDTO.setJname(jname);
        }
        String token = TokenUtils.encrypt(userDTO.getPeson(),dbfPath);
        userDTO.setToken(token);

        loginUserMap.put(token,userDTO);

        return userDTO;
    }
}
