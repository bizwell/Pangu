package com.joindata.inf.common.util.tools;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by likanghua on 2017/2/28.
 * <p>
 * 根据mac加时间加随机数产生uuid
 */
@Slf4j
public class UUIDUtil2 {
	private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";

	private static final AtomicInteger COUNT = new AtomicInteger(0);
	private static final long TYPE1 = 0x1000L;
	private static final byte VARIANT = (byte) 0x80;
	private static final int SEQUENCE_MASK = 0x3FFF;
	private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
	//private static final long INITIAL_UUID_SEQNO = 0;

	private static final long LEAST;

	private static final long LOW_MASK = 0xffffffffL;
	private static final long MID_MASK = 0xffff00000000L;
	private static final long HIGH_MASK = 0xfff000000000000L;
	private static final int NODE_SIZE = 8;
	private static final int SHIFT_2 = 16;
	private static final int SHIFT_4 = 32;
	private static final int SHIFT_6 = 48;
	private static final int HUNDRED_NANOS_PER_MILLI = 10000;

	static {
		byte[] mac = getLocalMacAddress();
		final Random randomGenerator = new SecureRandom();
		if (mac == null || mac.length == 0) {
			mac = new byte[6];
			randomGenerator.nextBytes(mac);
		}

		final int length = mac.length >= 6 ? 6 : mac.length;
		final int index = mac.length >= 6 ? mac.length - 6 : 0;
		final byte[] node = new byte[NODE_SIZE];
		node[0] = VARIANT;
		node[1] = 0;
		for (int i = 2; i < NODE_SIZE; ++i) {
			node[i] = 0;
		}
		System.arraycopy(mac, index, node, index + 2, length);
		final ByteBuffer buf = ByteBuffer.wrap(node);

		long[] sequences = new long[]{0};
		long rand = randomGenerator.nextLong();
		rand &= SEQUENCE_MASK;
		boolean duplicate;
		do {
			duplicate = false;
			for (final long sequence : sequences) {
				if (sequence == rand) {
					duplicate = true;
					break;
				}
			}
			if (duplicate) {
				rand = (rand + 1) & SEQUENCE_MASK;
			}
		} while (duplicate);

		String assigned = Long.toString(rand);
		System.setProperty(ASSIGNED_SEQUENCES, assigned);

		LEAST = buf.getLong() | rand << SHIFT_6;
	}


	/* This class cannot be instantiated */
	private UUIDUtil2() {
	}

	/**
	 * Generates Type 1 UUID. The time contains the number of 100NS intervals that have occurred
	 * since 00:00:00.00 UTC, 10 October 1582. Each UUID on a particular machine is unique to the 100NS interval
	 * until they rollover around 3400 A.D.
	 * <ol>
	 * <li>Digits 1-12 are the lower 48 bits of the number of 100 ns increments since the start of the UUID
	 * epoch.</li>
	 * <li>Digit 13 is the version (with a value of 1).</li>
	 * <li>Digits 14-16 are a sequence number that is incremented each time a UUID is generated.</li>
	 * <li>Digit 17 is the variant (with a value of binary 10) and 10 bits of the sequence number</li>
	 * <li>Digit 18 is final 16 bits of the sequence number.</li>
	 * <li>Digits 19-32 represent the system the application is running on.</li>
	 * </ol>
	 *
	 * @return universally unique identifiers (UUID)
	 */
	public static String getUUIdString() {
		final long time = ((System.currentTimeMillis() * HUNDRED_NANOS_PER_MILLI) +
				NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) + (COUNT.incrementAndGet() % HUNDRED_NANOS_PER_MILLI);
		final long timeLow = (time & LOW_MASK) << SHIFT_4;
		final long timeMid = (time & MID_MASK) >> SHIFT_2;
		final long timeHi = (time & HIGH_MASK) >> SHIFT_6;
		final long most = timeLow | timeMid | TYPE1 | timeHi;
		return new UUID(most, LEAST).toString().replace("-", "");
	}

	/**
	 * Returns the local network interface's MAC address if possible. The local network interface is defined here as
	 * the {@link NetworkInterface} that is both up and not a loopback interface.
	 *
	 * @return the MAC address of the local network interface or {@code null} if no MAC address could be determined.
	 * @since 2.1
	 */
	private static byte[] getLocalMacAddress() {
		byte[] mac = null;
		try {
			final InetAddress localHost = InetAddress.getLocalHost();
			try {
				final NetworkInterface localInterface = NetworkInterface.getByInetAddress(localHost);
				if (isUpAndNotLoopback(localInterface)) {
					mac = localInterface.getHardwareAddress();
				}
				if (mac == null) {
					final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
					while (networkInterfaces.hasMoreElements() && mac == null) {
						final NetworkInterface nic = networkInterfaces.nextElement();
						if (isUpAndNotLoopback(nic)) {
							mac = nic.getHardwareAddress();
						}
					}
				}
			} catch (final SocketException e) {
			}
			if (mac == null || mac.length == 0) {
				mac = localHost.getAddress();
			}
		} catch (final UnknownHostException ignored) {
			log.error("获取mac地址失败:{}",ignored.getMessage());
		}
		return mac;
	}

	private static boolean isUpAndNotLoopback(final NetworkInterface ni) throws SocketException {
		return ni != null && !ni.isLoopback() && ni.isUp();
	}

}
