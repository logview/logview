package com.github.logview.stringpart;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.type.BytePart;
import com.github.logview.stringpart.type.IntegerPart;
import com.github.logview.stringpart.type.ListPart;
import com.google.common.io.ByteStreams;

public class PartFactoryTest {
	private final PartManager manager = new PartManager();
	private final PartFactory factory = manager.getFactory();

	@Test
	public void testBaToList() {
		byte[] bytes = new byte[] {
			(byte)PartType.LIST.ordinal(), (byte)0
		};
		Part part = factory.createFrom(ByteStreams.newDataInput(bytes));
		Assert.assertTrue(part instanceof ListPart);
		ListPart list = (ListPart)part;
		Assert.assertEquals(0, list.size());
		Assert.assertArrayEquals(bytes, list.getBytes());
	}

	@Test
	public void testBaByteToList() {
		byte[] bytes = new byte[] {
			(byte)PartType.LIST.ordinal(), (byte)1, (byte)PartType.BYTE.ordinal(), (byte)0x12
		};
		Part part = factory.createFrom(ByteStreams.newDataInput(bytes));
		Assert.assertTrue(part instanceof ListPart);
		ListPart list = (ListPart)part;
		Assert.assertEquals(1, list.size());
		Assert.assertArrayEquals(bytes, list.getBytes());
		Iterator<Part> it = list.getParts().iterator();
		Part child = it.next();
		Assert.assertTrue(child instanceof BytePart);
		Assert.assertEquals(0x12, (byte)((BytePart)child).value());
	}

	@Test
	public void testBaByteIntToList() {
		byte[] bytes = new byte[] {
			(byte)PartType.LIST.ordinal(),
			(byte)2,
			(byte)PartType.BYTE.ordinal(),
			(byte)0x12,
			(byte)PartType.INT.ordinal(),
			(byte)0x12,
			(byte)0x34,
			(byte)0x56,
			(byte)0x78
		};
		Part part = factory.createFrom(ByteStreams.newDataInput(bytes));
		Assert.assertTrue(part instanceof ListPart);
		ListPart list = (ListPart)part;
		Assert.assertEquals(2, list.size());
		Assert.assertArrayEquals(bytes, list.getBytes());
		Iterator<Part> it = list.getParts().iterator();
		Part child1 = it.next();
		Assert.assertTrue(child1 instanceof BytePart);
		Assert.assertEquals(0x12, (byte)((BytePart)child1).value());
		Part child2 = it.next();
		Assert.assertTrue(child2 instanceof IntegerPart);
		Assert.assertEquals(0x12345678, (int)((IntegerPart)child2).value());
	}
}
