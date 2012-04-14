package com.github.logview.value.api;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Strings;

public class CommonPartTest {

	public static String getCommonPartInt2(String a, String b) {
		String pre = Strings.commonPrefix(a, b);
		int l = pre.length();
		return pre + Strings.commonSuffix(a.substring(l), b.substring(l));
	}

	public static String getCommonPartInt1(String a, String b) {
		String ret = getCommonPartInt2(a, b);
		for(int i = 1; i < a.length(); i++) {
			String tmp = getCommonPartInt2(a.substring(i), b);
			if(tmp.length() > ret.length()) {
				ret = tmp;
			}
		}
		return ret;
	}

	public static String getCommonPart(String a, String b) {
		String c1 = getCommonPartInt1(a, b);
		String c2 = getCommonPartInt1(b, a);
		if(c1.length() < c2.length()) {
			return c2;
		}
		return c1;
	}

	public static void test(String ex, String a, String b) {
		String c1 = getCommonPart(a, b);
		String c2 = getCommonPart(b, a);
		Assert.assertEquals(c1, c2);
		Assert.assertEquals(ex, c1);
	}

	@Test
	public void test1() {
		test("bc", "abc", "Abc");
		test("ac", "abc", "aBc");
		test("ab", "abc", "abC");
		test("abc def", "abc 123 def", "abc def");
		test("abc abc", "abc abc", "abc 123 abc");
		test("test !", "test 123!", "test 456!");
	}

	@Test
	public void test2() {
		String s = "abcdefg";
		test(s, s, s);
		// -a
		test("bcdefg", s, "bcdefg");
		// -g
		test("abcdef", s, "abcdef");
		// -ag
		test("bcdef", s, "bcdef");
	}
}
