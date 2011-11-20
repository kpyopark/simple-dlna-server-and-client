package com.lgcns.sol.uuid;

import java.io.Serializable;

/**
 * @deprecated - Use com.lgcns.sol.upnp.common.UPnPUtils 
 *
 */
public class UUID implements Serializable {
	private static final long serialVersionUID = -2632846855777989452L;

	private static final UUIDGen UUIDGEN = new UUIDGen();

	private String _id;

	public UUID() {
		String totlaID = prefix() + UUIDGEN.nextUUID();
		totlaID = totlaID.replaceAll(":", "");
		totlaID = totlaID.replaceAll("-", "");
		_id = totlaID.substring(0, 8) + "-" + totlaID.substring(8, 12) + "-"
				+ totlaID.substring(12, 16) + "-" + totlaID.substring(16, 20)
				+ "-" + totlaID.substring(20, 32);
	}

	protected UUID(String id) {
		if (!id.startsWith(prefix())) {
			throw new IllegalArgumentException("Invalid UUID");
		}

		_id = id;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return (o.getClass() == getClass()) && ((UUID) o)._id.equals(_id);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return _id.hashCode();
	}

	/**
	 * String form of a uuid.
	 * 
	 * @param uuid
	 *            convert the <code>uuid</code> to a string
	 * @return string representation
	 */
	public static String toIdString(UUID uuid) {
		return uuid._id;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return _id;
	}

	protected String prefix() {
		return "";
	}

}
