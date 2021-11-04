package com.ps.bingo.localized;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class BundleMessage implements Serializable {
	private static final long serialVersionUID = 8254826380458026110L;

	private final String messageBundleName;

	private final String messageKey;

	private final String message;

	private final int errorCode;

	private final String moduleID;

	public BundleMessage(final String messageBundleName, final String messageKey, final String message,
			final int errorCode, final String moduleID) {

		this.messageBundleName = messageBundleName;
		this.messageKey = messageKey;
		this.message = message;
		this.errorCode = errorCode;
		this.moduleID = moduleID;
	}

	public String format() {
		return format((Object) null);
	}

	public String format(final Object... arguments) {
		return formatWithoutErrorCode(this.moduleID, this.errorCode, this.message, Locale.getDefault(), arguments);

	}

	public String format(final Locale locale) {
		return format(locale, (Object) null);
	}

	public String format(final Locale locale, final Object... arguments) {
		if (((!Locale.getDefault().equals(locale))) && this.messageBundleName != null && this.messageKey != null) {
			ResourceBundle bundle = ResourceBundle.getBundle(this.messageBundleName, locale);
			if (bundle != null) {
				String pattern;
				try {
					pattern = bundle.getString(this.messageKey);
				} catch (MissingResourceException exception) {
					pattern = "Missing message '" + this.messageKey + "' for locale '" + locale.getDisplayName()
							+ "' in bundle '" + this.messageBundleName + "'.";
				}
				return formatWithoutErrorCode(this.moduleID, this.errorCode, pattern, locale, arguments);
			}
		}
		return formatWithoutErrorCode(this.moduleID, this.errorCode, this.message, locale, arguments);
	}

	public String formatWithErrorCode() {
		return formatWithErrorCode((Object) null);
	}

	public String formatWithErrorCode(final Object... arguments) {
		return formatWithErrorCode(this.moduleID, this.errorCode, this.message, Locale.getDefault(), arguments);

	}

	public String formatWithErrorCode(final Locale locale) {
		return formatWithErrorCode(locale, (Object) null);
	}

	public String formatWithErrorCode(final Locale locale, final Object... arguments) {
		if (((!Locale.getDefault().equals(locale))) && this.messageBundleName != null && this.messageKey != null) {
			ResourceBundle bundle = ResourceBundle.getBundle(this.messageBundleName, locale);
			if (bundle != null) {
				String pattern;
				try {
					pattern = bundle.getString(this.messageKey);
				} catch (MissingResourceException exception) {
					pattern = "Missing message '" + this.messageKey + "' for locale '" + locale.getDisplayName()
							+ "' in bundle '" + this.messageBundleName + "'.";
				}
				return formatWithErrorCode(this.moduleID, this.errorCode, pattern, locale, arguments);
			}
		}
		return formatWithErrorCode(this.moduleID, this.errorCode, this.message, locale, arguments);
	}

	private static String formatWithoutErrorCode(final String moduleID, final int errorCode, final String pattern,
			final Locale locale, final Object... arguments) {
		return formatWithErrorCode(moduleID, -1, pattern, locale, arguments);
	}

	private static String formatWithErrorCode(final String moduleID, final int errorCode, final String pattern,
			final Locale locale, final Object... arguments) {

		String prefix;

		if (moduleID != null && errorCode != -1) {
			prefix = MessageFormat.format("{0}-{1,number,0000}: ", moduleID, errorCode);

		} else {
			prefix = null;
		}

		if (pattern == null) {
			return "Missing message for locale '" + locale.getDisplayName() + "'.";
		}

		if (arguments == null) {
			return prefix == null ? pattern : prefix + pattern;
		}

		final MessageFormat messageFormat;

		if (locale != null) {
			messageFormat = new MessageFormat(pattern, locale);

		} else {
			messageFormat = new MessageFormat(pattern);
		}

		String res = messageFormat.format(arguments);

		return prefix == null ? res : prefix + res;
	}

	public String toString(final Locale locale) {
		return format(locale);
	}

	public String toString() {
		return format();
	}
}
