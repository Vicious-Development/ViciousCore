package com.vicious.viciouscore.common.phantom;

/**
 * Indicates that a PhantomMemory instance has disassociated from its associated key. This only happens if the memory has been overwritten.
 */
public class PhantomDisassociationException extends RuntimeException{
    public PhantomDisassociationException(String s) {
        super(s);
    }
}
