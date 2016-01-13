/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.binariasystems.orion.business.enumerated;

/**
 *
 * @author Alexander
 */
public enum SN2Boolean {
    S(true),N(false);
    private final boolean booleanValue;
    SN2Boolean(boolean booleanValue){
        this.booleanValue = booleanValue;
    }

    /**
     * @return the booleanValue
     */
    public boolean booleanValue() {
        return booleanValue;
    }
    
    public static SN2Boolean fromBoolean(boolean bool){
        return bool ? S : N;
    }
}
