package com.github.logview.stringpart;

import org.junit.Assert;
import org.junit.Test;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.type.BytePart;
import com.github.logview.stringpart.type.ListPart;

public class PartListTest {
	private final PartManager manager = new PartManager();

	@Test
	public void testListToBa() {
		ListPart list = new ListPart(manager);
		Assert.assertArrayEquals(new byte[] {
			(byte)PartType.LIST.ordinal(), (byte)0
		}, list.getBytes());
	}

	@Test
	public void testListByteToBa() {
		ListPart list = new ListPart(manager);
		list.add(new BytePart((byte)0x12, manager));
		Assert.assertArrayEquals(new byte[] {
			(byte)PartType.LIST.ordinal(), (byte)1, (byte)PartType.BYTE.ordinal(), (byte)0x12
		}, list.getBytes());
	}

	@Test
	public void testListByteByteToBa() {
		ListPart list = new ListPart(manager);
		list.add(new BytePart((byte)0x12, manager));
		list.add(new BytePart((byte)0x34, manager));
		Assert.assertArrayEquals(
			new byte[] {
				(byte)PartType.LIST.ordinal(),
				(byte)2,
				(byte)PartType.BYTE.ordinal(),
				(byte)0x12,
				(byte)PartType.BYTE.ordinal(),
				(byte)0x34
			}, list.getBytes());
	}
}
