package com.github.logview.stringpart;

import org.junit.Assert;
import org.junit.Test;

import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.type.BytePart;
import com.github.logview.stringpart.type.ListPart;

public class PartListTest {
	private final PartFactory factory = new PartFactory();

	@Test
	public void testListToBa() {
		ListPart list = new ListPart(factory);
		Assert.assertArrayEquals(new byte[] {
			(byte)PartType.LIST.ordinal(), (byte)0
		}, list.getBytes());
	}

	@Test
	public void testListByteToBa() {
		ListPart list = new ListPart(factory);
		list.add(new BytePart((byte)0x12, factory));
		Assert.assertArrayEquals(new byte[] {
			(byte)PartType.LIST.ordinal(), (byte)1, (byte)PartType.BYTE.ordinal(), (byte)0x12
		}, list.getBytes());
	}

	@Test
	public void testListByteByteToBa() {
		ListPart list = new ListPart(factory);
		list.add(new BytePart((byte)0x12, factory));
		list.add(new BytePart((byte)0x34, factory));
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
