package com.ps.bingo.localized;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageBundle {
    static public final String ERROR_CODE_KEY_SUFFIX = ".errorCode";
    static public final String MODULE_ID_KEY = "RESOURCE.MODULEID";
    protected final static String BUNDLE_FIELDNAME = "BUNDLE_NAME";
    
    protected MessageBundle() {
    }
    public static void initializeMessages(final Class<?> clazz) throws IllegalArgumentException {
        MessageBundle.initializeMessages(Locale.getDefault(), clazz);
    }

    private static void initializeMessages(final Locale locale, final Class<?> clazz) throws IllegalArgumentException {
        if (!MessageBundle.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("clazz must inherit from MessageBundle.");
        }

        final String bundleName;
        try {
            bundleName = (String) clazz.getField(MessageBundle.BUNDLE_FIELDNAME).get(null);
        } catch (Exception e) {
            throw new IllegalArgumentException("clazz must define BUNDLE_NAME."); 
        }

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, locale);
        if (resourceBundle == null) {
            throw new IllegalArgumentException("Must be able to find a MessageBundle with name: " + bundleName);
        }

        final Field[] fields = clazz.getDeclaredFields();
        final int MOD_EXPECTED = Modifier.PUBLIC | Modifier.STATIC;
        final int MOD_MASK = MOD_EXPECTED | Modifier.FINAL; //no final
        final int numFields = fields.length;

        String moduleID = getModuleID(resourceBundle);
        if(moduleID == null)
        	throw new IllegalArgumentException("clazz must define " + MODULE_ID_KEY);
        
        for (int i = 0; i < numFields; i++) {
            Field field = fields[i];
            if (!BundleMessage.class.isAssignableFrom(field.getType())
                    || (field.getModifiers() & MOD_MASK) != MOD_EXPECTED) {
                continue;
            }
            try {
                if (field.get(null) == null) {
                    String key = field.getName();
                    String value;
                    try {
                        value = resourceBundle.getString(key);
                    } catch (MissingResourceException exception) {
                        value = "Missing message: " + field.getName() + " for locale: " + locale.getDisplayName() + " in bundle: " + bundleName;
                    }

                    int errorCode = maybeGetErrorCode(key, resourceBundle);

                    field.set(null, new BundleMessage(bundleName, key, value, errorCode, moduleID));
                }
            } catch (IllegalAccessException e1) {
                // Field cannot be accessed, we just ignore it.
            }
        }
    }
    static private int maybeGetErrorCode(String key, ResourceBundle resourceBundle) {
        try {
            return Integer.parseInt(resourceBundle.getString(key + ERROR_CODE_KEY_SUFFIX));
        } catch (MissingResourceException ignore) {
            return -1;

        } catch (NumberFormatException ignore) {
            return -1;
        }
    }
    
    static private String getModuleID(ResourceBundle resourceBundle) {
        try {
            return resourceBundle.getString(MODULE_ID_KEY);

        } catch (MissingResourceException ignore) {
            return null;
        }
    }
}
