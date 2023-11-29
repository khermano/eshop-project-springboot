package cz.muni.fi.orderService.enums;

public enum OrderState {
    RECEIVED, CANCELED, SHIPPED, DONE;

    /**
     * Checks the enum for a string contained within
     * 
     * @param valueString value of type String
     * @return true if the string is contained in the enum 
     */
    public static boolean contains(final String valueString) {
        for (OrderState os : OrderState.values()) {
            if (os.name().equals(valueString)) {
                return true;
            }
        }
        return false;
    }
}
