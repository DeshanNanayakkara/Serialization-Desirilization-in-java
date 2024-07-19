package lk.ijse.gdse.customerManage.util;

import java.util.UUID;

public class Util {
    public static String idGenerator(){
        return UUID.randomUUID().toString();
    }
}
