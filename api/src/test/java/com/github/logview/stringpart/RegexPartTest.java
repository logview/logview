package com.github.logview.stringpart;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.github.logview.regex.Config;
import com.github.logview.regex.RegexConfigMatcher;
import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.type.IntegerPart;
import com.github.logview.stringpart.type.RegexPart;

public class RegexPartTest {
	private final PartFactory factory = new PartFactory();

	@Test
	public void testIntPart() throws ParseException, ExecutionException {
		Config config = Config.create("1|name:id type:int||test message '(\\d+)'!", factory);
		RegexConfigMatcher parser = new RegexConfigMatcher(config);
		Part test = parser.match("test message '10'!");
		Assert.assertTrue(test instanceof RegexPart);
		RegexPart regex = new RegexPart(config, factory);
		regex.add(new IntegerPart(10, factory));
		Assert.assertArrayEquals(regex.getBytes(), test.getBytes());
	}
}
