package com.nerdynick.maven.plugins.jinja;

import java.util.Properties;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

public class MavenProjectStub extends org.apache.maven.plugin.testing.stubs.MavenProjectStub {

	public MavenProjectStub() {
		super();
		setup();
	}

	public MavenProjectStub(MavenProject project) {
		super(project);
		setup();
	}

	public MavenProjectStub(Model model) {
		super(model);
		setup();
	}

	private void setup() {
		this.setArtifactId("jinja-maven-plugin");
		this.setGroupId("com.nerdynick");
		this.setName("Jinja Maven Plugin");
		this.setDescription("Jinja Maven Plugin Desc");
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		
		props.put("test.prop", "my-test-prop");
		
		return props;
	}
}
