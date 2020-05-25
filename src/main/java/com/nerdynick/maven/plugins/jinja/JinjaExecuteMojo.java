package com.nerdynick.maven.plugins.jinja;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.interpret.RenderResult;
import com.nerdynick.maven.plugins.template.AbstractTemplateMojo;
import com.nerdynick.maven.plugins.template.Template;
import com.nerdynick.maven.plugins.template.TemplateConfig;

@Mojo(name="jinja-execute", defaultPhase=LifecyclePhase.GENERATE_SOURCES)
public class JinjaExecuteMojo extends AbstractTemplateMojo {
	private Jinjava jinjava = new Jinjava();
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		final Charset charset = this.getCharset();
		
		final Map<String, Object> context = Maps.newHashMap();
		context.putAll(this.getProjectInfoProps());
		context.putAll(this.getProjectProps());
		
		getLog().info("Jinja Context: "+ context);
		
		final JinjavaConfig config = JinjavaConfig.newBuilder()
			.withCharset(charset)
			.build();
		
		final StringBuilder contentBuilder = new StringBuilder();
		for(TemplateConfig t: templates){
			try {
				final List<Template> temps = t.getTemplates();
				for(Template temp: temps){
					getLog().info("Compiling '"+ temp.input +"' to '"+ temp.output +"'");
					try {
						contentBuilder.setLength(0);
						final BufferedReader reader = Files.newReader(temp.input, charset);
						String sCurrentLine;
				        while ((sCurrentLine = reader.readLine()) != null) {
				            contentBuilder.append(sCurrentLine).append("\n");
				        }
				        
				        final String template = contentBuilder.toString();
				        this.getLog().debug("Rendering Template: "+ temp);
				        final RenderResult renderedResult = jinjava.renderForResult(template, context, config);
				        
				        if(renderedResult.hasErrors()) {
				        	renderedResult.getErrors().forEach(e->{
					        	this.getLog().error("Template Render Error: "+ e);
					        });
				        	throw new MojoFailureException("Failed to render template: "+ temp);
				        }
				        
				        this.getLog().debug("Writing Template Result: "+ temp);
				        final Writer writer = Files.newWriter(temp.output, charset);
				        writer.write(renderedResult.getOutput());
				        writer.close();
				        
				        this.getLog().debug("Done with Template: "+ temp);
					} catch (IOException e) {
						throw new MojoFailureException("Failed to compile template: "+ temp, e);
					}
				}
			} catch (IOException e) {
				throw new MojoFailureException("Failed to process Template", e);
			}
		}

		

		
	}

}
