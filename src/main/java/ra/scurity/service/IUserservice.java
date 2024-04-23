package ra.scurity.service;

import ra.scurity.model.dto.request.UserLogin;
import ra.scurity.model.dto.request.UserRegister;
import ra.scurity.model.dto.respone.JwtRespone;

public interface IUserservice {
    boolean register(UserRegister userRegister);
    JwtRespone login(UserLogin userLogin) ;
}
