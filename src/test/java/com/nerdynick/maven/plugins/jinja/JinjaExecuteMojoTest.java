package com.nerdynick.maven.plugins.jinja;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

import com.nerdynick.maven.plugins.template.AbstractTemplateMojoTestCase;

public class JinjaExecuteMojoTest extends AbstractTemplateMojoTestCase<JinjaExecuteMojo> {
	@Override
	protected String getMojoGoal() {
		return "jinja-execute";
	}
	
	@Test
	public void test() throws Exception {
		JinjaExecuteMojo mojo = this.getMojo();
		mojo.execute();
		
		File output = getTestFile("target/test-harness/test.txt");
		List<String> lines = Files.readAllLines(output.toPath());
		
		assertNotNull(lines);
		assertEquals(2, lines.size());
		assertEquals("jinja-maven-plugin com.nerdynick jinja-maven-plugin Maven Plugin to process Jinja templates using maven properties", lines.get(0));
		assertEquals("my-test-prop", lines.get(1));
		
		//Check Scan
		File scanDir = getTestFile("target/test-harness/scan");
		Path scanDirPath = scanDir.toPath();
		
		assertTrue(scanDir.exists());
		assertTrue(scanDir.isDirectory());
		assertEquals(5, scanDir.listFiles().length);
		
		//Check Scan Files
		File temp1 = scanDirPath.resolve("template.txt").toFile();
		File temp2 = scanDirPath.resolve("template_2.txt").toFile();
		
		assertTrue(temp1.exists());
		assertTrue(temp1.isFile());
		assertTrue(temp2.exists());
		assertTrue(temp2.isFile());
		
		//Check Scan Files Multi
		Path multiDir = scanDirPath.resolve("multi");
		temp1 = multiDir.resolve("template.txt").toFile();
		temp2 = multiDir.resolve("template_2.txt").toFile();
		File temp3 = multiDir.resolve("template_3.txt").toFile();
		File temp4 = multiDir.resolve("template_4.txt").toFile();
		
		assertEquals(4, multiDir.toFile().listFiles().length);
		assertTrue(temp1.exists());
		assertTrue(temp1.isFile());
		assertTrue(temp2.exists());
		assertTrue(temp2.isFile());
		assertTrue(temp3.exists());
		assertTrue(temp3.isFile());
		assertTrue(temp4.exists());
		assertTrue(temp4.isFile());
		
		//Check Scan Files with Exclude
		temp1 = scanDirPath.resolve("exclude").resolve("template.txt").toFile();
		temp2 = scanDirPath.resolve("exclude").resolve("template_2.txt").toFile();
		
		assertTrue(temp1.exists());
		assertTrue(temp1.isFile());
		assertTrue(!temp2.exists());
		
		//Check Scan Files with Exclude
		temp1 = scanDirPath.resolve("include").resolve("template.txt").toFile();
		temp2 = scanDirPath.resolve("include").resolve(" template_2.txt").toFile();
		
		assertTrue(!temp1.exists());
		assertTrue(temp2.exists());
		assertTrue(temp2.isFile());
		
	}

}
