package Utils;

import java.util.Collection;

public class Checkers {

    public static final String CURRENCY_PROPERTIES = "currency.properties";

    public static  <T> T checkNonNull(T object, String cause) {
        if (object == null) {
            throw new NotFoundException(cause);
        }
        return object;
    }

    public static  <T extends Collection> T checkNonNull(T object, String cause) {
        if (object.size()==0) {
            throw new NotFoundException(cause);
        }
        return object;
    }
    public static boolean checkNonNull(int result, String cause) {
        if (result==0) {
            throw new NotFoundException(cause);
        }
        return true;
    }

}
